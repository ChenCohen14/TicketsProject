package com.example.ticketsrestapi;

import com.example.ticketsrestapi.entity.Ticket;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class TicketsController {

    @GetMapping("tickets")
    public ResponseEntity<List<Ticket>>  getTickets(@RequestParam (defaultValue = "")
                                                    String title,
                                                    @RequestParam (defaultValue = "")
                                                    String content,
                                                    @RequestParam (defaultValue = "") String email,
                                                    @RequestParam (defaultValue = "")
                                                    Long from,
                                                    @RequestParam (defaultValue = "") Long to){
        JSONParser parser = new org.json.simple.parser.JSONParser();
        JSONArray jsonArray = null;
        List<Ticket> ticketList = new ArrayList<>();
        try {
            Object objArr = parser.parse(new FileReader("data.json"));
            jsonArray = (JSONArray )objArr;
            Gson gson = new Gson();
            Iterator iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JSONObject jsonObject = (JSONObject) iterator.next();
                String id = ((String)jsonObject.get("id")).replaceAll("\n","");
                String ticketTitle = ((String)jsonObject.get("title")).replaceAll("\n","");
                String ticketContent = ((String)jsonObject.get("content")).replaceAll("\n","");
                String userEmail = ((String)jsonObject.get("userEmail")).replaceAll("\n","");
                Long creationTime = (Long)jsonObject.get("creationTime");
                List<String> labels =  (List<String>)jsonObject.get("labels");
                ticketList.add(new Ticket(id,
                        ticketTitle,
                        ticketContent,
                        userEmail,
                        creationTime,
                        labels));
            }

            if(!title.trim().isEmpty()){
                ticketList = ticketList.stream().filter((ticket -> ticket.getTitle().equals(title))).collect(Collectors.toList());

            }
            if(!content.trim().isEmpty()){
                ticketList = ticketList.stream().filter((ticket -> ticket.getContent().equals(content))).collect(Collectors.toList());

            }
            if(!email.trim().isEmpty()){
                ticketList = ticketList.stream().filter((ticket -> ticket.getUserEmail().equals(email))).collect(Collectors.toList());

            }
            if(from != null && from <= 0){
                throw  new RuntimeException("From should be a positive number");
            }
            if(from != null){
                ticketList = ticketList.stream().filter((ticket -> ticket.getCreationTime() >= from)).collect(Collectors.toList());
            }

            if(to != null && to <= 0){
                throw  new RuntimeException("To should be a positive number");
            }

            if(to != null && to < from){
                throw  new RuntimeException("To should be greater than or equal to from");
            }

            if(to != null){
                ticketList = ticketList.stream().filter((ticket -> ticket.getCreationTime() <= to)).collect(Collectors.toList());
            }


            return new ResponseEntity<>(ticketList, HttpStatus.OK);
        } catch(Exception e) {
            //Didn't have time to handle a custom exception for each case,
            //so meanwhile return INTERVAL_SERVER_ERROR
            e.printStackTrace();
            return new ResponseEntity<>(ticketList, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

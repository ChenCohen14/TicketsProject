package com.example.ticketsrestapi.utils;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonParser {

    public static JSONArray jsonArrParser(String fileName) {
        JSONParser parser = new org.json.simple.parser.JSONParser();
        JSONArray jsonArray = null;
        try {
            Object objArr = parser.parse(new FileReader(fileName));
            jsonArray = (JSONArray) objArr;
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

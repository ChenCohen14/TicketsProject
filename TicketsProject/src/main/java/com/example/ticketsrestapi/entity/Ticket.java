package com.example.ticketsrestapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    private String id;
    private String title;
    private String content;
    private String userEmail;
    private Long creationTime;
    private List<String> labels;
}

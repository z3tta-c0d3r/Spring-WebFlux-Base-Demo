package com.example.SpringDemo3.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class User {
    private String name;
    private String surName;
}

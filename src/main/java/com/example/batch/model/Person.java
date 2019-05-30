package com.example.batch.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private LocalDate birthDate;

}

package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Employee {
   private String id;
   private String name;
   private Long phoneNumber;
   private String email;
}



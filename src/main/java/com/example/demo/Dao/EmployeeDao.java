package com.example.demo.Dao;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "#{@environment.getProperty('employee.collection')}")
public class EmployeeDao {
   private  String id;
   private String name;
   private Long phoneNumber;
   private String email;
}

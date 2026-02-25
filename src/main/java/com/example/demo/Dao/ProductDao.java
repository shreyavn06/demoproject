package com.example.demo.Dao;

import  lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("product")
public class ProductDao {
    private String id;
    private String name;
    private Double price;
    private String employeeId;
}

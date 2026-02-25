package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class TestDto {
    private Integer number1;
    private Integer number2;
    private String email;
    private List<String> emails;

}

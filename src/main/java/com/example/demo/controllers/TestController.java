package com.example.demo.controllers;

import com.example.demo.Dto.TestDto;
import com.example.demo.accessor.EmailAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private EmailAccessor emailAccessor;

    @GetMapping("/contact")
    public ResponseEntity<Object> test() {
        return new ResponseEntity<>("A first demo project ", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody TestDto testDto) {
        log.info("value of Number 1 :" + testDto.getNumber1());
        log.info("value of Number 2 :" + testDto.getNumber2());
        return new ResponseEntity<>(testDto.getNumber1() + testDto.getNumber2(), HttpStatus.OK);
    }

    @GetMapping("/about")
    public ResponseEntity<Object> demo() {
        return new ResponseEntity<>("A project test ", HttpStatus.OK);
    }


    @PostMapping("/sendEmail")
    public ResponseEntity<Object> sendEmail(@RequestBody TestDto testDto) {
        try {
            emailAccessor.sendEmail(testDto.getEmail());
            return new ResponseEntity<>("Single email sent successfully", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/sendBulkEmail")
    public ResponseEntity<Object> sendBulkEmail(@RequestBody TestDto testDto) {

            if (testDto.getEmails() == null || testDto.getEmails().isEmpty()) {
                return new ResponseEntity<>("Email list cannot be empty", HttpStatus.BAD_REQUEST);
            }

            emailAccessor.sendBulkEmails(testDto.getEmails());

            return new ResponseEntity<>("Bulk emails sent successfully", HttpStatus.OK);

    }
}
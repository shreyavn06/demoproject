package com.example.demo.controllers;

import com.example.demo.Dao.EmployeeDao;
import com.example.demo.Dao.ProductDao;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.ProductRepository;
import com.example.demo.models.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllEmployees() {
        List<EmployeeDao> response = employeeRepository.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Employee employee) {
        EmployeeDao employeeDao = EmployeeDao.builder()
                .email(employee.getEmail())
                .name(employee.getName())
                .phoneNumber(employee.getPhoneNumber())
                .build();

        EmployeeDao response = employeeRepository.save(employeeDao);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> update(@RequestBody Employee employee) {
        return null;
    }

    @PostMapping("/deleteById")
    public ResponseEntity<Object> deleteById(@RequestBody Employee employee) {

        if (employeeRepository.existsById(employee.getId())) {
            employeeRepository.deleteById(employee.getId());
            return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/findById")
    public ResponseEntity<Object> findById(@RequestBody Employee employee) {

        Optional<EmployeeDao> response = employeeRepository.findById(employee.getId());

        if (response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/with-products/{id}")
    public ResponseEntity<Object> getEmployeeWithProducts(@PathVariable String id) {

        Optional<EmployeeDao> employeeOpt = employeeRepository.findById(id);

        if (employeeOpt.isEmpty()) {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }

        List<ProductDao> products = productRepository.findByEmployeeId(id);

        Map<String, Object> response = new HashMap<>();
        response.put("employee", employeeOpt.get());
        response.put("products", products);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

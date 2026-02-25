package com.example.demo.controllers;

import com.example.demo.Dao.ProductDao;
import com.example.demo.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductDao product) {
        return ResponseEntity.ok(productRepository.save(product));
    }


    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getByEmployee(@PathVariable String employeeId) {
        List<ProductDao> products = productRepository.findByEmployeeId(employeeId);
        return ResponseEntity.ok(products);
    }
}

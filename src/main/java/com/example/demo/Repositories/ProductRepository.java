package com.example.demo.Repositories;

import com.example.demo.Dao.ProductDao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<ProductDao, String> {
    List<ProductDao> findByEmployeeId(String employeeId);
}

package com.example.demo.Repositories;

import com.example.demo.Dao.EmployeeDao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<EmployeeDao,String> {

}

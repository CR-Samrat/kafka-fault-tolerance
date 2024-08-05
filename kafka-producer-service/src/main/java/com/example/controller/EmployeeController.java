package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.ProducerServices;

@RestController
@RequestMapping("/query")
public class EmployeeController {

    @Autowired
    private ProducerServices producerServices;

    @GetMapping("/employees")
    public ResponseEntity<?> getEmployees(){
        return new ResponseEntity<>(this.producerServices.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") int id){
        return new ResponseEntity<>(this.producerServices.getEmployeeById(id), HttpStatus.OK);
    }

}

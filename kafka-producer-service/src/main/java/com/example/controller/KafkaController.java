package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.ProducerServices;

@RestController
@RequestMapping("/transaction")
public class KafkaController {
    
    @Autowired
    private ProducerServices producerServices;

    @PostMapping("/employees/{id}")
    public ResponseEntity<?> sendEmployeeById(@PathVariable("id") int id){
        try {
            this.producerServices.publishEmployeeById(id);

            return new ResponseEntity<>("Employee send successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

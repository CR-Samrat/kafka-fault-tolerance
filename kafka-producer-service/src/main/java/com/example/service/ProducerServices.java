package com.example.service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.example.dto.Employee;

import jakarta.annotation.PostConstruct;

@Service
public class ProducerServices {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    List<String> departments = List.of("Sales","Machine Learning","Testing","Developer","Manager","HR");
    private List<Employee> employees = null;

    @PostConstruct
    public void loadEmployeeData(){
        employees = IntStream.rangeClosed(1, 100)
                            .mapToObj(i -> Employee.builder()
                                                    .id(i)
                                                    .name("Employee "+i)
                                                    .department(departments.get(new Random().nextInt(departments.size())))
                                                    .ipAddress(new Random().nextInt(255)+":"+new Random().nextInt(255)+":"
                                                                +new Random().nextInt(255)+":"+new Random().nextInt(255))
                                                    .build())
                            .toList();
    }

    public List<Employee> getAllEmployees(){
        return employees;
    }

    public Employee getEmployeeById(int id){
        return employees.stream()
                        .filter(emp -> id == emp.getId())
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("Invalid id"));
    }

    public void publishEmployeeById(int id){
        Employee employee = this.getEmployeeById(id);

        CompletableFuture<SendResult<String, Object>> send = kafkaTemplate.send("fault-tolerance-topic","employee-event",employee);

        send.whenComplete((result, error) -> {
            if(error == null){
                System.out.println("Message sent :"+result.getProducerRecord().value().toString());
                System.out.println("Offset : "+result.getRecordMetadata().offset()+"| partition : "+result.getRecordMetadata().partition());
            }else{
                throw new RuntimeException("Error :"+error);
            }
        });
    }
}

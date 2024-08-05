package com.example.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.example.dto.Employee;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsumerService {

    public Boolean validIpAddress(Employee value, int start, int end){
        String[] ips = value.getIpAddress().split(":");
        int prefix = Integer.parseInt(ips[0]);

        return (prefix > start-1 && prefix <end+1);
    }
    
    @RetryableTopic
    @KafkaListener(topics = "fault-tolerance-topic", groupId = "fault-tolerance-group")
    public void consumeMessageFromProducer(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                            @Header(KafkaHeaders.OFFSET) long offset){
        
        try {
            String key = record.key();
            Employee value = (Employee) record.value();

            int valid_ip_start = 100;
            int valid_ip_end = 200;

            log.info("Data received : {}",value);
            log.info("Topic name : {} | Offset : {}", topic, offset);

            if(! validIpAddress(value, valid_ip_start, valid_ip_end)){
                throw new RuntimeException("Invalid ip address : "+value.getIpAddress());
            }
        } catch (Exception e) {
            log.error("Error processing message: ", e);
            throw e;
        }
    }

    @DltHandler
    public void listenDLT(ConsumerRecord<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                                                    @Header(KafkaHeaders.OFFSET) long offset)
    {
        log.info("DLT received : {}, from {}, offset {}",record.value(), topic, offset);
    }
}

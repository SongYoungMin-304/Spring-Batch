package com.project.batch.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class Sender {

    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Autowired
    private JmsTemplate jmsCustomTemplate;

    public void send(String message) {
        log.info("sending message='{}'", message);
        jmsTemplate.convertAndSend("helloworld.q", message);
    }

    public void timesend() {
        log.info("sending message='{}'", "Current Date & Time is :"+LocalDateTime.now());
        jmsTemplate.convertAndSend("time-queue","Current Date & Time is :"+ LocalDateTime.now());
    }
}
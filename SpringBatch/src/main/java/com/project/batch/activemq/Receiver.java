package com.project.batch.activemq;

import org.springframework.jms.annotation.JmsListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Receiver {

    @JmsListener(destination = "helloworld.q",
    		containerFactory = "customConnectionFactory")
    public void receive(String message) {
        log.info("received message='{}'", message);
    }

    @JmsListener(destination = "time-queue",
    		containerFactory = "customConnectionFactory")
    public void timereceive(String message) {
        log.info("received message='{}'", message);
    }

}

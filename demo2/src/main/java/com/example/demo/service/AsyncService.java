package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Async
    public void onAsync(int i) throws Exception{
        System.out.println("Start: "+i);

        int randomCnt = (int) (Math.random() * 3 + 1) * 1000;
        Thread.sleep(randomCnt);

        System.out.println("End: "+i);
        logger.info("onAsync i=" + i);
    }
}

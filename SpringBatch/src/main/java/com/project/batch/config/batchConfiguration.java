package com.project.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.batch.core.util.TimeBasedSequenceIdFactory;

@Configuration
public class batchConfiguration {
	
	 @Bean(name = "timeBasedSequenceIdFactory")
	    public void timeBasedSequenceIdFactory()
	    {
	        new TimeBasedSequenceIdFactory().start();
	    }
}

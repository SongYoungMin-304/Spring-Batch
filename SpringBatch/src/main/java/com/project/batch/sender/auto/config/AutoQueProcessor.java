package com.project.batch.sender.auto.config;

import com.project.batch.domain.AutoQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Slf4j
@StepScope
@Component
public class AutoQueProcessor implements ItemProcessor<AutoQueue, AutoQueue> {
	
	@Override
	public AutoQueue process(AutoQueue item) throws Exception {
		// TODO Auto-generated method stub
		Thread.sleep(100);
		log.info("Check Process"+item.toString());
		return item;
	}

}

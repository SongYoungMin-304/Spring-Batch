package com.project.batch.sender.auto.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import com.project.batch.model.AutoQueDto;
import com.project.batch.sender.DefaultQueProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@StepScope
@Component
public class AutoQueProcessor extends DefaultQueProcessor<AutoQueDto>{
	
	@Override
	public AutoQueDto process(AutoQueDto item) throws Exception {
		// TODO Auto-generated method stub
		Thread.sleep(100);
		log.info("Check Process"+item.toString());
		return item;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

}

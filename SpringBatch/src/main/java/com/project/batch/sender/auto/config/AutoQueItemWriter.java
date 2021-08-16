package com.project.batch.sender.auto.config;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import com.project.batch.model.AutoQueDto;
import com.project.batch.sender.DefaultQueWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@StepScope
@Component
public class AutoQueItemWriter extends DefaultQueWriter<AutoQueDto>{

	@Override
	public void write(List<? extends AutoQueDto> items) throws Exception {
		// TODO Auto-generated method stub
		log.info("¼Û¿µ¹Îwriter");
		
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

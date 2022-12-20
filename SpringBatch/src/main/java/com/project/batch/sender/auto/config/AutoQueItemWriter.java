package com.project.batch.sender.auto.config;

import java.util.List;

import com.project.batch.repository.AutoQueRepository;
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

	private final AutoQueRepository autoQueRepository;

	public AutoQueItemWriter(AutoQueRepository autoQueRepository) {
		this.autoQueRepository = autoQueRepository;
	}

	@Override
	public void write(List<? extends AutoQueDto> items) throws Exception {
		// TODO Auto-generated method stub

		for(AutoQueDto autoQueDto : items){
			log.info("테스트"+autoQueDto.toString());
			autoQueRepository.updateAutoQueue(autoQueDto.getPollKey());
		}

		log.info("check Writer");
		
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

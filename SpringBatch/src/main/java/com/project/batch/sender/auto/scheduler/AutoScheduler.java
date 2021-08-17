package com.project.batch.sender.auto.scheduler;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.batch.core.util.TimeBasedSequenceIdFactory;
import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;
import com.project.batch.sender.auto.scheduler.service.ChnScheduleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "auto.enabled", havingValue = "true")
public class AutoScheduler extends AbstractAutoScheduler<AutoQueSchdDto> {
	
	public AutoScheduler(
			AbstractAutoService abstractAutoService,
			JobLauncher JobLauncher, Job autoQueInterfaceJob
			) {
		super(abstractAutoService, JobLauncher, autoQueInterfaceJob);
	}

	@Scheduled(fixedDelay = 5000)
	@Override
	public void scheduled(){
		
		final String pollKey = TimeBasedSequenceIdFactory.seq();
		//final String pollKey = "1234";
		
		// 임시 처리
		List<AutoQueSchdDto> list = this.doScheduleList(pollKey);
		
		if (list == null)
			return;
		
		for(AutoQueSchdDto dto : list) {
			this.execute(dto, pollKey);
		}
		
	}

}

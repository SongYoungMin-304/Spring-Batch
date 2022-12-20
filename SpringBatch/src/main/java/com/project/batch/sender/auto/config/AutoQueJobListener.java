package com.project.batch.sender.auto.config;


import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoQueJobListener implements JobExecutionListener{
	
	private final AbstractAutoService abstractAutoService;
    private String scheduleId;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		scheduleId = jobExecution.getJobParameters().getString("schedule.id");
		abstractAutoService.setRunning(scheduleId, false);
	}


}

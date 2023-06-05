package com.project.batch.sender.stepExample.config;

import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StepJobListener implements JobExecutionListener {

    private final AbstractAutoService abstractAutoService;
    private String scheduleId;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        scheduleId = jobExecution.getJobParameters().getString("schedule.id");
        abstractAutoService.setRunning(scheduleId, true);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        scheduleId = jobExecution.getJobParameters().getString("schedule.id");
        abstractAutoService.setRunning(scheduleId, false);
    }
}

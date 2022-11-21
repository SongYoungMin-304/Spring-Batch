package com.project.batch.scheduler;

import com.project.batch.core.util.TimeBasedSequenceIdFactory;
import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.sender.auto.scheduler.AutoScheduler;
import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@RequiredArgsConstructor
public abstract class AutoBaseCronJob extends QuartzJobBean implements InterruptableJob {


    protected final Job job;
    protected final JobExplorer jobExplorer;
    protected final JobLauncher jobLauncher;
    protected final AbstractAutoService autoBaseScheduleService;
    protected final AutoScheduler autoScheduler;


    @Override
    public void interrupt() throws UnableToInterruptJobException {

    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        autoScheduler.scheduled();

        log.info("일단테스트");
    }
}

package com.project.batch.sender.auto.scheduler;

import com.project.batch.scheduler.AutoBaseCronJob;
import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.UnableToInterruptJobException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;


/**
 * AutoQuartzCrobJob.java
 * QuartzCrob JOB 등록
 * @author sym
 *
 */
@Slf4j
@Component
public class AutoQuartzCrobJob extends AutoBaseCronJob {

    public AutoQuartzCrobJob(Job autoQueInterfaceJob,
                               JobExplorer jobExplorer,
                               JobLauncher jobLauncher,
                               AbstractAutoService abstractAutoService){
        super(autoQueInterfaceJob, jobExplorer, jobLauncher, abstractAutoService);
    }

}

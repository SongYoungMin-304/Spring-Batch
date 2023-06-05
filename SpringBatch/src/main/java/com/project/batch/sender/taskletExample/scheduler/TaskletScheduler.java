package com.project.batch.sender.taskletExample.scheduler;

import com.project.batch.core.contstants.JobParamConstrants;
import com.project.batch.core.scheduler.Scheduler;
import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@ConditionalOnProperty(name ="tasklet.enabled", havingValue = "true")
public class TaskletScheduler implements Scheduler {

    protected final JobLauncher jobLauncher;
    protected final Job jobName;
    protected final String jobCheckId = "TaskletJob";

    protected AbstractAutoService abstractAutoService;

    public TaskletScheduler(
            JobLauncher jobLauncher,
            Job taskletInterfaceJob,
            AbstractAutoService abstractAutoService
    ){
        this.jobLauncher = jobLauncher;
        this.jobName = taskletInterfaceJob;
        this.abstractAutoService = abstractAutoService;
    }

    @Scheduled(cron ="0/5 * * * * ?")
    @Override
    public void scheduled() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        log.info("check song");

        try {
            if(!abstractAutoService.isRunning(jobCheckId)){
                log.info("Job launch ~");
                jobLauncher.run(jobName, this.makeJobParameters(jobCheckId));
            }else{
                log.info("already running {}", jobCheckId);
            }

        } catch (Exception e){
            log.error("job launch failed ", e);
        }
    }

    public JobParameters makeJobParameters(String scheduleId) {
        return new JobParametersBuilder()
                .addString(JobParamConstrants.RUN_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()))
                .addString(JobParamConstrants.SCHEDULE_ID, scheduleId)
                .toJobParameters();
    }
}

package com.project.batch.scheduler;

import com.project.batch.core.contstants.JobParamConstrants;
import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseCronJob extends QuartzJobBean implements InterruptableJob {

    protected boolean isInterrupted = false;
    protected JobKey jobKey = null;

    protected final Job job;
    protected final JobExplorer jobExplorer;
    protected final JobLauncher jobLauncher;
    protected final AbstractAutoService autoBaseScheduleService;


    protected ArrayBlockingQueue<Map> taskQueue = new ArrayBlockingQueue<Map>(1000);

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        log.debug(" {} -- INTERRUPTING --", jobKey);
        isInterrupted = true;
    }

    public JobParametersBuilder makeJobParameters() {

        final JobParametersBuilder builder = new JobParametersBuilder()
                .addString(JobParamConstrants.RUN_TIME, LocalDateTime.now().toString());

        return builder;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        jobKey = context.getJobDetail().getKey();

        log.debug("executeInternal invoked, jobName:{}, jobKey: {}, time:{}",
                job.getName(),
                jobKey, LocalDateTime.now().toString());

        if (isInterrupted) {
            log.warn("jobKey: {} is Interrupted.", jobKey);
            return;
        }
        try {
            final JobParameters jobParameters = this.makeJobParameters().toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e){

        }
       /* if (taskQueue.isEmpty()) {
            List<Map<String, Object>> list = autoBaseScheduleService.getScheduleList();

            list.forEach(map -> {
                if (map == null) {
                    return;
                }

                final String jobScheduleId = map.get("JOB_SCHEDULE_ID").toString();



                try {
                    log.info("job launch [jobKey:{}, scheduleId:{}]", jobKey, jobScheduleId);
                    this.autoBaseScheduleService.registSchedule(jobScheduleId, map);

                } catch (Exception e) {
                            this.autoBaseScheduleService.unRegistSchedule(jobScheduleId);
                            log.error("auto job JobExecutionAlreadyRunningException", e);
                }
            });*/
    }
}

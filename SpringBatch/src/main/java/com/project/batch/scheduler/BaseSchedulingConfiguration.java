package com.project.batch.sender.auto.scheduler;

import com.project.batch.scheduler.AutoBaseCronJob;
import com.project.batch.sender.auto.config.AutoQueJobConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BaseSchedulingConfiguration {

    private final SchedulerFactoryBean schedulerFactory;

    @PostConstruct
    public void start() {
        log.info("JobController set..");
        try{
            registAutoScheduler();
        }catch (Exception e) {
            log.error("SchedulerException", e);
        }
    }

    // *  *   *   *   *   *     *
    //초  분  시  일  월  요일  년도(생략가능)
    public TriggerBuilder cronJobTriggerBuilder(String triggerName,
                                                String triggerGroup,
                                                String scheduleExp) {

        return TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
                ;
    }

    public JobDetail buildJobDetail(Class job, String name, String desc, Map params) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);
        return JobBuilder
                .newJob(job)
                .withIdentity(name)
                .withDescription(desc)
                .usingJobData(jobDataMap)
                .build();
    }


    private void registAutoScheduler() throws SchedulerException {

        JobDetail schedulAutoJobDetail = buildJobDetail(
                AutoQuartzCrobJob.class,
                AutoQueJobConfiguration.JOB_NAME, //name
                AutoQueJobConfiguration.JOB_DESC, //desc
                new HashMap());

        Trigger autoSendEmailJobTrigger = cronJobTriggerBuilder("scheduleAutoTrigger",
                "scheduleTrigger",
                "3/5 * * * * ?").build();


        if (!schedulerFactory.getScheduler().checkExists(schedulAutoJobDetail.getKey())) {
                schedulerFactory.getScheduler().scheduleJob(
                        schedulAutoJobDetail,
                        autoSendEmailJobTrigger
                );
        }
    }
}

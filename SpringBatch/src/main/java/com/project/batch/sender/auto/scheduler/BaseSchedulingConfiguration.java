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


/**
 * BaseSchedulingConfiguration.java
 * Quartz-trigger Bean 등록 처리
 * @author sym
 *
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BaseSchedulingConfiguration {

    private final SchedulerFactoryBean schedulerFactory;

    /**
     * start
     * Bean 이 올라갈때 실행
     * @throws Excpetion
     *
     */
    @PostConstruct
    public void start() {
        log.info("JobController set..");
        try{
            registAutoScheduler();
        }catch (SchedulerException e) {
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

        // scheduleAutoJobDetail 이라는 Job을 세팅
        JobDetail schedulAutoJobDetail = buildJobDetail(
                AutoQuartzCrobJob.class,
                AutoQueJobConfiguration.JOB_NAME, //name
                AutoQueJobConfiguration.JOB_DESC, //desc
                new HashMap());

        // Trigger 관련 세팅 진행
        Trigger autoSendEmailJobTrigger = cronJobTriggerBuilder("scheduleAutoTrigger",
                "scheduleTrigger",
                "3/5 * * * * ?").build();


        JobDetail scheduleCreateEmailJobDetail = buildJobDetail(
                AutoQuartzCrobJob.class,
                "Create Auto schedule info", //name
                "테스트 스케줄 생성", //desc
                new HashMap()); //param

        Trigger scheduleCreateEmailJobTrigger = cronJobTriggerBuilder("scheduleCreateEmailTrigger",
                "scheduleCreate",
                "3/5 * * * * ?").build();

   /*     if (!ChannelType.EMAIL.getCode().equals(TMSSystem.getProperty("channel.type"))) {
            log.info("unScheduleJob email");
            schedulerFactory.getScheduler().unscheduleJob(scheduleSendEmailJobTrigger.getKey());
            schedulerFactory.getScheduler().unscheduleJob(scheduleCreateEmailJobTrigger.getKey());

        } else {*/
            // Job과 트리거를 설정,
            if (!schedulerFactory.getScheduler().checkExists(schedulAutoJobDetail.getKey())) {
                schedulerFactory.getScheduler().scheduleJob(
                        schedulAutoJobDetail,
                        autoSendEmailJobTrigger
                );
            }

            if (!schedulerFactory.getScheduler().checkExists(scheduleCreateEmailJobDetail.getKey())) {
                schedulerFactory.getScheduler().scheduleJob(
                        scheduleCreateEmailJobDetail,
                        scheduleCreateEmailJobTrigger
                );
            }
        //}
    }
}

package com.project.batch.configuration;

import com.project.batch.SpringBatchApplicationTests;
import com.project.batch.config.InitDb;
import com.project.batch.core.contstants.JobParamConstrants;
import com.project.batch.domain.StepQueue;
import com.project.batch.repository.step.StepRepository;
import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;
import com.project.batch.sender.stepExample.config.StepItemProcessor;
import com.project.batch.sender.stepExample.config.StepItemWriter;
import com.project.batch.sender.stepExample.config.StepJobConfiguration;
import com.project.batch.sender.stepExample.config.StepJobListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBatchTest    // a
@SpringBootTest(classes = {InitDb.class, SpringBatchApplicationTests.class, StepJobConfiguration.class, StepItemProcessor.class, StepItemWriter.class, StepJobListener.class, AbstractAutoService.class})
public class test {

    @Autowired
    protected StepRepository stepRepository;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils; // launchJob, laucnStep()과 같은 스프링 배치 테스트에 필요한 유틸성 메서드 지원

    @Autowired
    private InitDb initDb;

    @Test
    public void 테스트2() throws Exception{

        //initDb.init();

        log.info("sssssssssssssssssssssssssssss");

        List<StepQueue> fetch = stepRepository.getStepQueue().fetch();

        log.info("sssssssssssssssssssssssssssss"+fetch.size());

        for (StepQueue stepQueue : fetch) {
            log.info("songsongsongsong");
            log.info(stepQueue.toString());
        }

        // when
        try{
            JobExecution jobExecution = jobLauncherTestUtils.launchJob(makeJobParameters("StepJob"));
        }catch (Exception e){
            log.error("songyoungmin"+e);
        }

        for (StepQueue stepQueue : fetch) {
            log.info("kangkangkang");
            log.info(stepQueue.toString());
        }

    }

    public JobParameters makeJobParameters(String scheduleId) {
        return new JobParametersBuilder()
                .addString(JobParamConstrants.RUN_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()))
                .addString(JobParamConstrants.SCHEDULE_ID, scheduleId)
                .toJobParameters();
    }
}

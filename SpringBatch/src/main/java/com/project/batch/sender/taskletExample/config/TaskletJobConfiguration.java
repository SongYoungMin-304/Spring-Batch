package com.project.batch.sender.taskletExample.config;

import com.project.batch.repository.step.StepRepository;
import com.project.batch.sender.stepExample.config.StepJobListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class TaskletJobConfiguration {

    public static final String JOB_NAME = "taskletInterfaceJob";
    public static final String JOB_DESC = "tasklet 잡 처리";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private int chunkSize = 100;

    private final EntityManagerFactory emf;
    private final StepRepository stepRepository;


    @Bean(name= JOB_NAME)
    public Job taslketInterfaceJob(Step taskletStep,
                                StepJobListener stepJobListener){
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(stepJobListener)
                .start(taskletStep)
                .build();
    }

    @Bean(name="taskletStep")
    public Step taskletStep(SimpleTasklet simpleTasklet){
        return stepBuilderFactory.get("taskletStep")
                .tasklet(simpleTasklet)
                .build();
    }
}

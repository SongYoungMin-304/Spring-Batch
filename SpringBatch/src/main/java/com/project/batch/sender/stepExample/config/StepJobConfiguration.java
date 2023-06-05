package com.project.batch.sender.stepExample.config;


import com.project.batch.domain.StepQueue;
import com.project.batch.repository.step.StepRepository;
import com.project.batch.sender.reader.QuerydslPagingItemReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class StepJobConfiguration {

    public static final String JOB_NAME = "stepInterfaceJob";
    public static final String JOB_DESC = "STEP 잡 처리";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private int chunkSize = 100;

    private final EntityManagerFactory emf;
    private final StepRepository stepRepository;


    @Bean(name= JOB_NAME)
    public Job stepInterfaceJob(Step stepStep,
                                StepJobListener stepJobListener){
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(stepJobListener)
                .start(stepStep)
                .build();
    }

    @Bean(name ="stepStep")
    public Step stepStep(
            @Value("${step.throttle.limit}") int throttleLimit,
            StepItemProcessor stepItemProcessor,
            StepItemWriter stepItemWriter) throws Exception{
        return stepBuilderFactory.get("stepStep")
                .<StepQueue, StepQueue>chunk(chunkSize)
                .reader(stepReader(null, null, null))
                .processor(stepItemProcessor)
                .writer(stepItemWriter)
                .taskExecutor(taskStepExecutor())
                .throttleLimit(1)
                .build();
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<StepQueue> stepReader(
            @Value("#{jobParameters['min.seq']}") Long minSeq,
            @Value("#{jobParameters['max.seq']}") Long maxSeq,
            @Value("#{jobParameters['poll.key']}") String pollKey
    ){
        return new QuerydslPagingItemReader<>(emf, chunkSize,
                stepRepository.getStepQueue());
    }

    @Bean
    public TaskExecutor taskStepExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(8);
        executor.setThreadNamePrefix("async-thread-");
        return executor;
    }

}

package com.project.batch.sender.auto.config;

import com.project.batch.domain.AutoQueue;
import com.project.batch.repository.AutoQueueRepository;
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

//import com.project.batch.sender.QuerydslPagingItemReader;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class AutoQueJobConfiguration {
	public static final String JOB_NAME = "autoQueInterfaceJob";
    public static final String JOB_DESC = "테스트 잡 처리 ";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory emf;
    private final AutoQueueRepository autoQueueRepository;

    private int chunkSize = 100;

	@Bean(name= JOB_NAME)
    public Job onlineXmsQueInterfaceJob(Step autoQueSendStep,
                                    AutoQueJobListener autoQueJobListener) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(autoQueJobListener)
                .start(autoQueSendStep)
                .build();
    }
	@Bean(name="autoQueSendStep")
    public Step autoSendStep(
            @Value("${auto.throttle.limit}00") int throttleLimit,
            AutoQueProcessor autoQueProcessor,
            AutoQueItemWriter autoQueItemWriter) throws Exception {

        return stepBuilderFactory.get("autoQueSendStep")
                .<AutoQueue, AutoQueue>chunk(chunkSize)
                .reader(queueReader(null,null,null))
                .processor(autoQueProcessor)
                .writer(autoQueItemWriter)
                .taskExecutor(taskExecutor())
                .throttleLimit(1)
                //.taskExecutor(dbIntegrationBatchThreadPool)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(8);
        executor.setThreadNamePrefix("async-thread-");
        return executor;
    }
    /*
    * query dsl reader
    *
    */
    @Bean
    @StepScope
    public QuerydslPagingItemReader<AutoQueue> queueReader(
            @Value("#{jobParameters['min.seq']}") Long minSeq,
            @Value("#{jobParameters['max.seq']}") Long maxSeq,
            @Value("#{jobParameters['poll.key']}") String pollKey
    ){
        return new QuerydslPagingItemReader<>(emf, chunkSize,
                autoQueueRepository.getAutoQueue(minSeq, maxSeq, pollKey, "01"));
    }

}

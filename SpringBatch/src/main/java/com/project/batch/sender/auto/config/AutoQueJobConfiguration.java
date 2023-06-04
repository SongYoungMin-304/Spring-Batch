package com.project.batch.sender.auto.config;

import com.project.batch.domain.AutoQueue;
import com.project.batch.repository.AutoQueueRepository;
import com.project.batch.sender.QuerydslPagingItemReader;
import com.project.batch.sender.tasklet.SimpleTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

//import com.project.batch.sender.QuerydslPagingItemReader;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class AutoQueJobConfiguration {
	public static final String JOB_NAME = "autoQueInterfaceJob";
    public static final String JOB_DESC = "테스트 잡 처리 ";

	//@Value("${tms.integration.db.auto.chunk.size:100}")
	private int chunkSize = 5;
	
	private final DataSource dataSource;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    //private final TaskExecutor dbIntegrationBatchThreadPool;

    private final EntityManagerFactory emf;

    private final AutoQueueRepository autoQueueRepository;
	
	@Bean(name= JOB_NAME)
    public Job onlineXmsQueInterfaceJob(Step autoQueSendStep, Step taskletStep, Step simpleTaskletStep,
                                    AutoQueJobListener autoQueJobListener) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(autoQueJobListener)
                //.start(taskletStep)
                .start(autoQueSendStep)
                //.next(simpleTaskletStep)
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
	
	/*@Bean(name = "autoQueItemReader")
    @StepScope
    public JdbcPagingItemReader<AutoQueDto> autoQueItemReader(
    		 @Value("#{jobParameters['min.seq']}") String minSeq,
             @Value("#{jobParameters['max.seq']}") String maxSeq,
             @Value("#{jobParameters['poll.key']}") String pollKey
    ) throws Exception {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("minSeq", minSeq);
        parameterValues.put("maxSeq", maxSeq);
        parameterValues.put("pollKey", pollKey);

        return new JdbcPagingItemReaderBuilder<AutoQueDto>()
                .pageSize(1)
                .fetchSize(1)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(AutoQueDto.class))
                .queryProvider(autoQueCreateQueryProvider())
                .parameterValues(parameterValues)
                .name("autoQueItemReader")
                .build();
    }
	
	@Bean
    @StepScope
    public PagingQueryProvider autoQueCreateQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT " +
        		"Q.QUEUE_ID queueId," +
                "Q.POLL_KEY pollKey," +
                "Q.FLAG flag" +
                "");
        queryProvider.setFromClause("FROM AUTO_QUEUE Q " +
                "");
        queryProvider.setWhereClause("WHERE Q.FLAG = 'I' " +
                " AND Q.POLL_KEY = :pollKey" +
        		" AND Q.QUEUE_ID BETWEEN :minSeq AND :maxSeq");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("QUEUE_ID", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }*/

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(8);
        executor.setThreadNamePrefix("async-thread-");
        return executor;
    }



    @Bean(name="taskletStep")
    public Step taskletStep(){
        return stepBuilderFactory.get("taskletStep")
                .tasklet(tasklet())
                .build();
    }

    @Bean(name="simpleTaskletStep")
    public Step simpleTaskletStep(SimpleTasklet simpleTasklet){
        return stepBuilderFactory.get("simpleTaskletStep")
                .tasklet(simpleTasklet)
                .build();
    }

    public Tasklet tasklet(){
        return ((contribution, chunkContext) -> {
            List<String> items = getItems();
            log.info("items : "+ items.toString());
            return RepeatStatus.FINISHED;
        });
    }

    public Tasklet tasklet2(){
        Tasklet tasklet = new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                return RepeatStatus.FINISHED;
            }
        };

        Tasklet tasklet2 = new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                return null;
            }
        };

        Tasklet tasklet3 =  (contributeion, chunkContext) ->{
            log.info("으악");
            return RepeatStatus.FINISHED;
        };

        return tasklet;
    }

    private List<String> getItems(){
        List<String> items = new ArrayList<>();

        for(int i = 0; i < 100; i++){
            items.add(i + "test!");
        }

        return items;
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

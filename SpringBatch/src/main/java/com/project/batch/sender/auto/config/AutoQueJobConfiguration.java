package com.project.batch.sender.auto.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.project.batch.model.AutoQueDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
	
	@Bean(name= JOB_NAME)
    public Job onlineXmsQueInterfaceJob(Step autoQueSendStep,
                                    AutoQueJobListener autoQueJobListener) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(autoQueJobListener)
                .flow(autoQueSendStep)
                .end()
                .build();
    }
	@Bean(name="autoQueSendStep")
    public Step autoSendStep(
            @Value("${auto.throttle.limit}00") int throttleLimit,
            AutoQueProcessor autoQueProcessor,
            AutoQueItemWriter autoQueItemWriter) throws Exception {

        return stepBuilderFactory.get("autoQueSendStep")
                .<AutoQueDto, AutoQueDto>chunk(chunkSize)
                .reader(autoQueItemReader(null,null,null))
                .processor(autoQueProcessor)
                .writer(autoQueItemWriter)
                .taskExecutor(taskExecutor())
                .throttleLimit(1)
                //.taskExecutor(dbIntegrationBatchThreadPool)
                .build();
    }
	
	@Bean(name = "autoQueItemReader")
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
                .fetchSize(chunkSize)
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
    }

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setThreadNamePrefix("async-thread-");
        return executor;
    }

}

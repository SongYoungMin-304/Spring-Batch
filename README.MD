#Spring batch

## 2023.04.02 스프링 배치 최적화

일괄 처리를 도와주는 프레임 워크

Spring Batch 는 JOB 으로 이루어져 있고 JOB 안에 STEP, 그리고 STEP은 READER. PROCESSOR, WRITER 으로 구성 되어있음

![https://blog.kakaocdn.net/dn/cqe6GD/btr6oH82sAi/GbFeeubWkKInZKFlmDBeS1/img.png](https://blog.kakaocdn.net/dn/cqe6GD/btr6oH82sAi/GbFeeubWkKInZKFlmDBeS1/img.png)

**JobRepository**

: 다양한 배치 수행과 관련된 수치 데이터와 잡의 상태를 유지 및 관리

**Job**

: Job은 배치 처리 과정을 하나의 단위로 만들어 표현한 객체이고 여러 step 인스턴스를 포함하는 컨테이너 이다.

**JobLauncher**

: Job을 실행하는 역할을 담당한다. Job.execute을 호출하는 역할이다.

**Step**

: 스프링 배치에서 가장 일반적으로 상태를 보여주는 단위이다. 각 Step은 잡을 구성하는 독립된 작업의 단위

STEP 은 Tasklet, Chunk 기반으로 2가지가 존재

- **Tasklet** : Step 중지될 때까지 execute 메소드가 계속 반복해서 수행하고 수행할 때마다 독립적인 트랜잭션이 얻어짐, 초기화, 저장 프로시저 실행, 알림 전송과 같은 잡에서 일반적으로 사용
- **Chunk** : 한 번에 하나씩 데이터(row)를 읽어 Chunk 라는 덩어리를 만든 뒤, Chunk 단위로 트랜잭션을 다루는 것

Chunk 단위로 트랜잭션을 수행하기 때문에 실패한 경우에 해당 Chunk 만큼만 롤백

```java
    JobLauncher jobLauncher;
    Job jobName;

    jobName = autoQueInterfaceJob;

    JobLauncher.run(jobName, this.makeJobParameters(pollKey, scheduleInfo));
    // jobName 이랑 파라미터 세팅해서 실행

	public JobParameters makeJobParameters(String pollKey, T queDto) {
		return new JobParametersBuilder()
				.addString(JobParamConstrants.RUN_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()))
				.addString(JobParamConstrants.SCHEDULE_ID, queDto.getScheduledId())
				.addLong(JobParamConstrants.MIN_SEQ, queDto.getMinSeq())
				.addLong(JobParamConstrants.MAX_SEQ, queDto.getMaxSeq())
				.addString(JobParamConstrants.POLL_KEY, pollKey)
				.toJobParameters();
	}
```

**JOB 구성**

```java
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

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class AutoQueJobConfiguration {
	public static final String JOB_NAME = "autoQueInterfaceJob";
    public static final String JOB_DESC = "테스트 잡 처리 ";

	@Value("${tms.integration.db.auto.chunk.size:100}")
	private int chunkSize;

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
            @Value("${auto.throttle.limit}") int throttleLimit,
            AutoQueProcessor autoQueProcessor,
            AutoQueItemWriter autoQueItemWriter) throws Exception {

        return stepBuilderFactory.get("autoQueSendStep")
                .<AutoQueDto, AutoQueDto>chunk(chunkSize)
                .reader(autoQueItemReader(null,null,null))
                .processor(autoQueProcessor)
                .writer(autoQueItemWriter)
                .throttleLimit(throttleLimit)
                //.taskExecutor(dbIntegrationBatchThreadPool)
                .build();
    }
}
```

1) @Bean(name = JOB_NAME) : 해당 부분으로 JOB 을 BEAN으로 등록한다.

- flow : autoQueSendStep

2) @Bean(name = "autoQueSendStep") : job에서 실행하는 STEP을 BEAN으로 등록한다.

- reader : autoItemReader

- processor : autoQueProcessor

- writer : autoQueItemWriter

- throttleLimit(throttleLimit)

**Reader**

```java
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
                .pageSize(chunkSize)
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
        sortKeys.put("queueId", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }
```

1) PagingReader 로 세팅

2) pageSize : 100으로 세팅, fetchSize : 100으로 세팅

(pageSize => 페이징 사이즈 100,  fetchSize : cursor Reader 등에서 사용할 몇개를 가져올지에 대한 부분)

- cursor와 paging 의 차이

: cursor Reader는 데이터를 한번에 가져오고 paging은 말 그대로 페이징해서 가져오는 방식임, paging 의 경우에는 thread-safe 가 된다면 멀티 스레드 등의 처리도 가능하다.

**PROCESSOR**

```java
package com.project.batch.sender.auto.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import com.project.batch.model.AutoQueDto;
import com.project.batch.sender.DefaultQueProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@StepScope
@Component
public class AutoQueProcessor extends DefaultQueProcessor<AutoQueDto>{

	@Override
	public AutoQueDto process(AutoQueDto item) throws Exception {
		// TODO Auto-generated method stub
		log.info("Check Process");
		return item;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

}
```

```java
package com.project.batch.sender;

import com.project.batch.model.AutoQueDto;

public abstract class DefaultQueProcessor<T extends AutoQueDto>
    extends AbstractQueProcessor<T>{

}
```

```java
package com.project.batch.sender;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

import com.project.batch.model.AutoQueDto;

public abstract class AbstractQueProcessor <T extends AutoQueDto> implements
ItemProcessor<T, T>, StepExecutionListener{

}
```

: 별다른 처리는 하지 않았지만 가공 등이 필요한 경우 여기서 진행하면 된다.

**WRITER**

```java
package com.project.batch.sender.auto.config;

import java.util.List;

import com.project.batch.repository.AutoQueRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import com.project.batch.model.AutoQueDto;
import com.project.batch.sender.DefaultQueWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@StepScope
@Component
public class AutoQueItemWriter extends DefaultQueWriter<AutoQueDto>{

	private final AutoQueRepository autoQueRepository;

	public AutoQueItemWriter(AutoQueRepository autoQueRepository) {
		this.autoQueRepository = autoQueRepository;
	}

	@Override
	public void write(List<? extends AutoQueDto> items) throws Exception {
		// TODO Auto-generated method stub

		for(AutoQueDto autoQueDto : items){
			log.info("테스트"+autoQueDto.toString());
			autoQueRepository.updateAutoQueue(autoQueDto.getPollKey());
		}

		log.info("check Writer");

	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

}
```

```java
package com.project.batch.sender;

import com.project.batch.model.AutoQueDto;

public abstract class DefaultQueWriter<T extends AutoQueDto>
extends AbstractQueWriter<T> {

}
```

```java
package com.project.batch.sender;

import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import com.project.batch.model.AutoQueDto;

public abstract class AbstractQueWriter <T extends AutoQueDto>
implements ItemWriter<T>, StepExecutionListener{

}
```

: 가져온 데이터를 CHUNK 단위로 업데이트 하도록 처리하였음

- > 여기서 CHUNK는 아래에 옵션에서 세팅한 100이 된다. (즉 WRITER의 chunkSize 는 100 이 된다는 뜻)

```xml
.<AutoQueDto, AutoQueDto>chunk(chunkSize)
```

- Spring batch 를 사용할 때 중요한 고려할 점

1. Reader 의 방식 선택

- cursor Reader : Reader에 정의되어 있는 쿼리를 한번에 실행을 한다.

(해당 경우에 reader의 데이터량이 많으면 OutofMemory 가 발생할 수 있다.)

- paging Reader : Reader에 정의되어 있는 쿼리에 페이징 (offset limit)등을 처리해서 데이터를 나눠서 가져올수 있다.

(해당 경우에 페이징의 갯수 만큼 데이터를 가져오기 때문에 OutofMemory 가 발생할 가능성을 줄일 수 있다.)

(중요! : 말그대로 페이징을 하는 것이기 때문에 정렬 조건이 필요하다)

(또한 페이징이라는 것을 통해서 쿼리를 여러번 가져오는 것이므로 멀티 스레드 사용이 가능하다.)

(몇 건씩 가져와서 처리 할 것인지 확인 필요)

2. chunk size의 결정

- Reader Processor Writer 에서는 아래처럼 list를 가지고 처리는 하는데 해당 리스트에 몇건이 뭉쳐서 처리 할지를 결정해야 한다.( ex) 10000건 실행 chunk-size 가 100건 이면 100번 실행 됨)

```
public void write(List<? extends AutoQueDto> items) throws Exception {
```

3. Thread Executor 과 throttleLime 의 결정

- 해당 batch 작업을 실행한 Thread Executor 를 결정해주고 해당 스레드가 동시에 몇개를 돌개 할지 처리 할 수 가 있따.

```
return stepBuilderFactory.get("autoQueSendStep")
        .<AutoQueDto, AutoQueDto>chunk(chunkSize)
        .reader(autoQueItemReader(null,null,null))
        .processor(autoQueProcessor)
        .writer(autoQueItemWriter)
        .taskExecutor(taskExecutor())
        .throttleLimit(8)
        //.taskExecutor(dbIntegrationBatchThreadPool)
        .build();
```

1) 여기서 보면 taskExecutor() 을 taskExecutor로 사용하고 있다.

```java
@Bean
public TaskExecutor taskExecutor(){
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(8);
    executor.setMaxPoolSize(8);
    executor.setThreadNamePrefix("async-thread-");
    return executor;
}
```

- > corePoolSize 와 maxPoolSize 가 8로 세팅되어있는 것을 알 수 있다. 즉 8개의 스레드를 통해서 돌리겠다는 뜻이다.

2) throttleLimt 사용

- > 해당 부분이 이해가 안됬는데 이 내용은 **동시에** 사용할 수 있는 Thread의 갯수를 정해준다는 것이다.

즉 실행할 executor를 정의해주고 해당 executor에서 동시에 몇개를 사용할 수 있는 지에 대한 부분을 정의해 준 것이다.

- 자세한 테스트를 위한 Processor 에 Thread.sleep(3000) 을 적용하고 테스트를 해보았다.

| throttleLimt \ executor | 1개 | 8개 |
| --- | --- | --- |
| 1개 | 5분3초 | 5분 |
| 8개 | 5분 | 45초 |

해당 데이터를 통해서 알 수 있는 거는 아무리 스레드가 많은 executor을 사용하더라도 throttleLimit를 제한 해버리면 번갈아 가면서 스레드를 사용하기 때문에  속도가 빨리지지 않는 다는 것이다.

즉 Spring Batch 를 적절하게 사용 하기 위해서는

1. Reader의 판단

- 페이징 리더로 할지 커서 리더로 할지

2. step에 대한 스레드를 멀티로 구성할지 단일로 구성할지 이다.

- 멀티스레드로 구성하기 위해서는 각각의 스레드가 Thread-safe해야 하고 paging reader를 사용해야 한다.(최근에 나온 cursor 리더는 리더만 한번에 하고 process writer 은 멀티 스레드로 돌리는 synchronize 방식을 지원하는 것으로 알고 있다.

3. chunk size를 통해서 list에 담을 데이터 사이즈 조절

- 대량 업데이트 시에는 batchUpdate 등을 사용하는 게 좋은 데 해당 경우에 처리할 갯수를 정해줌으로써 속도에 이점을 가질 수 있다.

/*
package com.project.batch.config;

import com.project.batch.config.annotation.AopAnnotation;
import com.project.batch.core.lock.LockConfiguration;
import com.project.batch.core.lock.StorageLockProvider;
import io.micrometer.core.instrument.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

@Aspect
@Component
@Slf4j
public class LoggingAOP {

	@Value("${distribute.min.mstime}")
	protected static int MIN_AT_TIME;
	@Value("${distribute.max.mstime}")
	protected static int MAX_UNTIL_TIME;
	
	*/
/*@Around("execution(* com.project.batch.sender.auto.scheduler.AutoScheduler.scheduled(..))")
	public void logging(final ProceedingJoinPoint joinPoint) throws Throwable {
		//log.info("aop Process"+joinPoint.getSignature());
		joinPoint.proceed();
	}*//*


	@Autowired
	private StorageLockProvider storageLockProvider;

	protected String getClassAndMethodName(Method method) {
		return new StringBuilder().append(method.getDeclaringClass().getSimpleName())
				.append(".")
				.append(method.getName())
				.toString();
	}

	@Around("execution(@com.project.batch.config.annotation.AopAnnotation * *(..))")
	public Object logging(final ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("aop Process"+joinPoint.getSignature());

		Object proceed = null;

		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		AopAnnotation aopAnnotation = method.getAnnotation(AopAnnotation.class);

		String distributedLockName = aopAnnotation.name();

		// 어떤 스케줄러가 lock 할건지 정의
		String lockedBy = aopAnnotation.lockedBy();
		lockedBy = StringUtils.isEmpty(lockedBy) ? getClassAndMethodName(method) : lockedBy;

		long waitingTime  = aopAnnotation.waiting();
		long deadLineTime = aopAnnotation.deadLine();
		boolean alone = aopAnnotation.alone();


		//lockProvider.lock();

		try{
			LockConfiguration lockConfiguration = new LockConfiguration(distributedLockName
					, Instant.now()
					, Duration.ofMillis((waitingTime==0) ? MIN_AT_TIME:waitingTime)
					, Duration.ofMillis((deadLineTime==0) ? MAX_UNTIL_TIME*10:deadLineTime)
					, lockedBy
					, (alone==true)?"1":"0");

			log.info("TL={}",distributedLockName);

			boolean lockYn = storageLockProvider.lock(lockConfiguration);

			if(lockYn==false){
				log.info("Lock Fail {} is already locked, This method {} is skip",method.getName());
			}else{
				proceed = joinPoint.proceed();
				storageLockProvider.unlock(lockConfiguration);
			}

		} catch (Exception e){
			log.error("Exception occur"+ e.toString());
		}

		return proceed;
	}
}
*/

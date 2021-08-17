package com.project.batch.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAOP {
	
	/*@Around("execution(* com.project.batch.sender.auto.scheduler.AutoScheduler.scheduled(..))")
	public void logging(final ProceedingJoinPoint joinPoint) throws Throwable {
		//log.info("aop Process"+joinPoint.getSignature());
		joinPoint.proceed();
	}*/

	@Around("execution(@com.project.batch.config.annotation.AopAnnotation * *(..))")
	public void logging(final ProceedingJoinPoint joinPoint) throws Throwable {
		//log.info("aop Process"+joinPoint.getSignature());
		joinPoint.proceed();
	}
}

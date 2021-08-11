package com.project.batch.sender.auto.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "auto.enabled", havingValue = "true")
public class AutoScheduler extends AbstractAutoScheduler{

	@Scheduled(fixedDelay = 5000)
	@Override
	public void scheduled(){
		log.info("¼Û¿µ¹Î");
	}

}

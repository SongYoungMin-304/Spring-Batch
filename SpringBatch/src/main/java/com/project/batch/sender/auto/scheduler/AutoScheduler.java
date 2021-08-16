package com.project.batch.sender.auto.scheduler;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.sender.auto.scheduler.service.ChnScheduleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "auto.enabled", havingValue = "true")
public class AutoScheduler extends AbstractAutoScheduler<AutoQueSchdDto> {
	
	public AutoScheduler(ChnScheduleService<AutoQueSchdDto> abstractAutoService) {
		super(abstractAutoService);
	}

	@Scheduled(fixedDelay = 5000)
	@Override
	public void scheduled(){
		
		//final String pollKey = TimeBasedSequenceIdFactory.seq();
		
		// 임시 처리
		List<AutoQueSchdDto> list = this.doScheduleList("1234");
		
		for(AutoQueSchdDto dto : list) {
			log.info("송영민 송아지");
		}
		
	}

}

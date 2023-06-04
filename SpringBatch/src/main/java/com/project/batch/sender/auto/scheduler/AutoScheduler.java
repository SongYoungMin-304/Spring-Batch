package com.project.batch.sender.auto.scheduler;

/*
 * scheduler 대신 quartzTrigger을 사용하기 위한 조치
 *
 */

import com.project.batch.core.util.TimeBasedSequenceIdFactory;
import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;
import com.project.batch.sender.auto.scheduler.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
//@ConditionalOnProperty(name = "auto.enabled", havingValue = "true")
public class AutoScheduler extends AbstractAutoScheduler<AutoQueSchdDto> {

	protected static final long LIMIT_TIME = 1000 * 60; // 1분

	public AutoScheduler(
			AbstractAutoService abstractAutoService,
			JobLauncher JobLauncher,
			Job autoQueInterfaceJob,
			TemplateService templateService
			) {
		super(abstractAutoService, JobLauncher, autoQueInterfaceJob, templateService);
	}

	//@Scheduled(fixedDelay = 5000, cron = "1 * * * * *")
	//@AopAnnotation(name = "auto-send", alone = true, deadLine = LIMIT_TIME)
	@Scheduled(cron = "0/5 * * * * ?")
	@Override
	public void scheduled(){
		
		final String pollKey = TimeBasedSequenceIdFactory.seq();
		//final String pollKey = "1234";

		List<AutoQueSchdDto> list = this.doScheduleList(pollKey);

		if (list == null)
			return;
		
		for(AutoQueSchdDto dto : list) {
			this.execute(dto, pollKey);
		}
		
	}

}

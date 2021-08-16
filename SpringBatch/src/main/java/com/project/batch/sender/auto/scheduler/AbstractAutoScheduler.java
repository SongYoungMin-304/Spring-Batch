package com.project.batch.sender.auto.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import com.project.batch.core.contstants.JobParamConstrants;
import com.project.batch.core.scheduler.Scheduler;
import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.sender.auto.scheduler.service.ChnScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAutoScheduler<T extends AutoQueSchdDto> implements Scheduler {

	protected final ChnScheduleService<T> abstractAutoService;
	protected final JobLauncher JobLauncher;
	protected final Job jobName;

	public List<T> doScheduleList(String pollKey) {
		// int cnt = this.batchQueService.updatePreBatchSchd(serverId, channelType);

		log.info("¼Û¿µ¹Î11111"+pollKey);
		int updateSucc = abstractAutoService.updatePollKey(pollKey);

		if (updateSucc == 0) {
			log.info("There is no Scheduler");
			return null;
		}
		
		return this.abstractAutoService.getScheduleList(pollKey);
	}
	
	public void execute(T scheduleInfo, String pollKey) {

		try {
			JobLauncher.run(jobName, this.makeJobParameters(pollKey, scheduleInfo));
		} catch (Exception e) {
			log.error("job launch failed ", e);
			this.abstractAutoService.setRunning(scheduleInfo.getScheduledId(), false);
		}
	} // end for loop
	
	public JobParameters makeJobParameters(String pollKey, T queDto) {
		return new JobParametersBuilder()
				.addString(JobParamConstrants.RUN_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()))
				.addString(JobParamConstrants.SCHEDULE_ID, queDto.getScheduledId())
				.addLong(JobParamConstrants.MIN_SEQ, queDto.getMinSeq())
				.addLong(JobParamConstrants.MAX_SEQ, queDto.getMaxSeq())
				.addString(JobParamConstrants.POLL_KEY, pollKey)
				.toJobParameters();
	}

}

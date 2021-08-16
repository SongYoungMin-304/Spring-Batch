package com.project.batch.sender.auto.scheduler;

import java.util.List;

import com.project.batch.core.scheduler.Scheduler;
import com.project.batch.domain.AutoQueue;
import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;
import com.project.batch.sender.auto.scheduler.service.ChnScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAutoScheduler<T extends AutoQueSchdDto> implements Scheduler {

	protected final ChnScheduleService<T> abstractAutoService;

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

}

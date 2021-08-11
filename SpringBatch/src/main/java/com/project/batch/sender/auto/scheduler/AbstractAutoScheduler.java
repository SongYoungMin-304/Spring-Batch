package com.project.batch.sender.auto.scheduler;

import java.util.List;

import com.project.batch.core.scheduler.Scheduler;
import com.project.batch.sender.auto.scheduler.service.AbstractAutoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAutoScheduler implements Scheduler {

	AbstractAutoService abstractAutoService;

	public void doScheduleList(String pollKey) {
		// int cnt = this.batchQueService.updatePreBatchSchd(serverId, channelType);

		int updateSucc = abstractAutoService.updatePollKey(pollKey);

		//return this.abstractAutoService.getScheduleList(pollKey);
	}

}

package com.project.batch.sender.auto.scheduler.service;

import java.util.List;

public interface ChnScheduleService<T> {
	List<T> getScheduleList();

	List<T> getScheduleList(String pollKey);

	List<String> getTemplateMsgId();

	boolean isRunning(String scheduleId);

	void setRunning(String scheduleId, boolean isRun);

	int updatePollKey(String pollKey, String templateMsgId);
}

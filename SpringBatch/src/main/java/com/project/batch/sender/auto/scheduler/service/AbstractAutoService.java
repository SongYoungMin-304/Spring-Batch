package com.project.batch.sender.auto.scheduler.service;

import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.repository.auto.AutoQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbstractAutoService implements ChnScheduleService<AutoQueSchdDto>{
	
	private final AutoQueueRepository autoQueueRepository;

	private Map<String, Boolean> scheduleHashMap = new ConcurrentHashMap<>();
	
	private final String dateTimeFormat = "yyyyMMddHHmmss";
	
	@Value("${max.msg.expire.days}")
    private int maxMsgExpiredDays;
	
	public long updatePollKey(String pollKey, String templateMsgId) {
	   	 return autoQueueRepository.updatePreAutoQueue(pollKey, templateMsgId);
	}

	@Override
	public List<AutoQueSchdDto> getScheduleList() {
		return null;
	}

	public List<AutoQueSchdDto> getScheduleList(String pollKey){
		return autoQueueRepository.findBySchdByPollKey(pollKey);
	}

	@Override
	public List<String> getTemplateMsgId() {
		return autoQueueRepository.findList();
	}

	@Override
	public boolean isRunning(String scheduleId) {
		Boolean isRun = this.scheduleHashMap.get(scheduleId);
		if(isRun == null){
			return false;
		}else{
			return isRun;
		}
	}

	@Override
	public void setRunning(String scheduleId, boolean isRun) {
		// TODO Auto-generated method stub
		if (isRun) {
			this.scheduleHashMap.put(scheduleId, true);
		} else {
			this.scheduleHashMap.remove(scheduleId);
		}
	}
}

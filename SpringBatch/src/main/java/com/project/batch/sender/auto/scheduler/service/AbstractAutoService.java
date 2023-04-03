package com.project.batch.sender.auto.scheduler.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.batch.domain.TemplateInfo;
import com.project.batch.model.AutoQueSchdDto;
import com.project.batch.repository.AutoQueRepository;
import com.project.batch.repository.TemplateMsgInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbstractAutoService implements ChnScheduleService<AutoQueSchdDto>{
	
	private final AutoQueRepository autoQueRepository;

	private Map<String, Boolean> scheduleHashMap = new ConcurrentHashMap<>();
	
	private final String dateTimeFormat = "yyyyMMddHHmmss";
	
	@Value("${max.msg.expire.days}")
    private int maxMsgExpiredDays;
	
	public int updatePollKey(String pollKey, String templateMsgId) {
	   	 String nowDateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
	     String twoHourAgoDateStr = LocalDateTime.now().minusDays(maxMsgExpiredDays)
	         .format(DateTimeFormatter.ofPattern(dateTimeFormat));
	     return autoQueRepository.updatePreAutoQueue(pollKey, templateMsgId);
	}

	@Override
	public List<AutoQueSchdDto> getScheduleList() {
		return null;
	}

	public List<AutoQueSchdDto> getScheduleList(String pollKey){
		return autoQueRepository.findBySchdByPollKey(pollKey);
	}

	@Override
	public List<String> getTemplateMsgId() {
		// TODO Auto-generated method stub
		return autoQueRepository.findlist();
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

package com.project.batch.sender.auto.scheduler.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
	
	
	
	private final String dateTimeFormat = "yyyyMMddHHmmss";
	
	@Value("${max.msg.expire.days}")
    private int maxMsgExpiredDays;
	
	public int updatePollKey(String pollKey) {
	   	 String nowDateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
	     String twoHourAgoDateStr = LocalDateTime.now().minusDays(maxMsgExpiredDays)
	         .format(DateTimeFormatter.ofPattern(dateTimeFormat));
	     return autoQueRepository.updatePreAutoQueue(pollKey);
	}
	
	public List<AutoQueSchdDto> getScheduleList(String pollKey){
		return autoQueRepository.findBySchdByPollKey(pollKey);
	}

	@Override
	public List<AutoQueSchdDto> getScheduleList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AutoQueSchdDto getScheduleInfo(String scheduleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRunning(String scheduleId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRunning(String scheduleId, boolean isRun) {
		// TODO Auto-generated method stub
		
	}
}

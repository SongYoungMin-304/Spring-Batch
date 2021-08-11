package com.project.batch.sender.auto.scheduler.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.batch.repository.AutoQueRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbstractAutoService {
	
	private final AutoQueRepository autoQueRepository;
	
	private final String dateTimeFormat = "yyyyMMddHHmmss";
	
	@Value("${max.msg.expire.days}")
    private int maxMsgExpiredDays;
	
	public int updatePollKey(String pollKey) {
	   	 String nowDateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
	     String twoHourAgoDateStr = LocalDateTime.now().minusDays(maxMsgExpiredDays)
	         .format(DateTimeFormatter.ofPattern(dateTimeFormat));
	     return autoQueRepository.updatePreAutoQueue(pollKey, 1000);
	}

}

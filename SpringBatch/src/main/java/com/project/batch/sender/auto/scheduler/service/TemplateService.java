package com.project.batch.sender.auto.scheduler.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.project.batch.domain.TemplateInfo;
import com.project.batch.repository.TemplateMsgInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TemplateService {
	
	private final TemplateMsgInfoRepository templateMsgInfoRepository;
	
	@Cacheable(cacheNames = "getRecordCompleteCache")
	public TemplateInfo getTemplateInfo(long templateMsgId) {
		//log.info("ĳ�� Ȯ��"+templateMsgId);
		return templateMsgInfoRepository.findByTemplateMsgId(templateMsgId);
	}


}

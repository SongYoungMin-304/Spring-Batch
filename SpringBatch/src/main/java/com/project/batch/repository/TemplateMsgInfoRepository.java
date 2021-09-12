package com.project.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.batch.domain.TemplateInfo;

public interface TemplateMsgInfoRepository extends JpaRepository<TemplateInfo, Long>{
	
	TemplateInfo findByTemplateMsgId(long templateMsgId);

}

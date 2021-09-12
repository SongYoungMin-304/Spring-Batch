package com.project.batch.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
@Table(name = "TEMPLATE_INFO")
public class TemplateInfo {

	 @Id
	 private long templateMsgId;
	 
	 private String templateName;
	
}

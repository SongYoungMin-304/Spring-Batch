package com.project.batch.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
@Table(name = "AUTO_QUEUE")
public class AutoQueue implements Serializable{
	
	 @Id
	 private long queueId;
	 
	 private String pollKey;
	 private String flag;
	 private String queueName;

}

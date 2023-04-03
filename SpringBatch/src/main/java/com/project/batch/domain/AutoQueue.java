package com.project.batch.domain;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
@Table(name = "AUTO_QUEUE")
public class AutoQueue implements Serializable{
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private long queueId;
	 
	 private String pollKey;
	 private String flag;
	 private String queueName;
	 private String templateMsgId;
	 

}

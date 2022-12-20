package com.project.batch.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class AutoQueDto {
	private long queueId;
	private String pollKey;
	private String flag;
	private String queueName;
}

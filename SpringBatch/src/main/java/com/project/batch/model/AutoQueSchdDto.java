package com.project.batch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AutoQueSchdDto {
	
	private static final String DELIM  = "|";
	
	private Long minSeq;
	private Long maxSeq;
	private String pollKey;
	private String templateMsgId;
	
	public AutoQueSchdDto(Long minSeq, Long maxSeq, String pollKey, String templateMsgId) {
		this.minSeq = minSeq;
		this.maxSeq = maxSeq;
		this.pollKey = pollKey;
		this.templateMsgId = templateMsgId;
	}

	public AutoQueSchdDto(String templateMsgId){
		this.templateMsgId = templateMsgId;
	}

	public String getScheduledId() {
		StringBuilder sb = new StringBuilder();
        sb.append(templateMsgId);
        return sb.toString();
	}
}

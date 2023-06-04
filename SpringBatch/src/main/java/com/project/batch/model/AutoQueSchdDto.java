package com.project.batch.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AutoQueSchdDto {
	
	private static final String DELIM  = "|";
	
	private long minSeq;
	private long maxSeq;
	private String pollKey;
	private String templateMsgId;

	@QueryProjection
	public AutoQueSchdDto(long minSeq, long maxSeq, String pollKey, String templateMsgId) {
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

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
	
	public AutoQueSchdDto(Long minSeq, Long maxSeq, String pollKey) {
		this.minSeq = minSeq;
		this.maxSeq = maxSeq;
		this.pollKey = pollKey;
	}

	public String getScheduledId() {
		StringBuilder sb = new StringBuilder();
        sb.append(pollKey);
        return sb.toString();
	}
}

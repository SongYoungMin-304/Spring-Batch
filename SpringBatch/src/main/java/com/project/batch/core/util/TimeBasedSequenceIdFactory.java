package com.project.batch.core.util;


import java.util.Locale;

/**
 * 
 * @author pioneer
 *
 */
public class TimeBasedSequenceIdFactory extends SequenceIdFactory{

	private long lastAllocatedId = 0;
	
	@Override
	public String createSequence() {
		long id = System.currentTimeMillis();
		if (id <= lastAllocatedId) {
			id = lastAllocatedId + 1;
		}

		lastAllocatedId = id;
		return PRE_KEY+Long.toString(id, 36).toUpperCase(Locale.ENGLISH);
	}
}

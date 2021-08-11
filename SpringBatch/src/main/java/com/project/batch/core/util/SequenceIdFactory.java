package com.project.batch.core.util;


import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * SequenceIdFactory is a very simple sequence number producer, which
 * assigns numeric identifiers based on the current time(mec) time 
 * or sequentially increase number, amending
 * it as necessary to make it unique.
 * 
 * !Note : After engine process stop, Rebooting Engine may return duplicate Key 
 * (UUidSequenceIdFactory return primary sequence but slow performance( X3 ) )   
 * 
 * Inheritance Class : @Link{SequenceIncreaseIdFactory},
 * 					   @Link{TimeBasedSequenceIdFactory}, 
 * 					   @Link{UuidSequenceIdFactory}
 * @author pioneer
 * @since  2015.03
 */

public abstract class SequenceIdFactory extends Thread {
	protected final String PRE_KEY="01-";
	
	private final static int CAPACITY = 100000;
	
	private static BlockingQueue<String> queue = new ArrayBlockingQueue(CAPACITY);
	
	@Override
	public void run(){
		while(true){
			try {
				queue.put(createSequence());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String seq(String addKey){
		try {
			return addKey + queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return addKey+UUID.randomUUID();
		}
	}
	
	public static String seq(){
		return seq("");
	}

	public static int bufferSize(){
		return queue.size();
	}
 
	abstract public String createSequence();

}
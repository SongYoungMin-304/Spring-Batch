package com.project.batch.core.util;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceIncreaseIdFactory extends SequenceIdFactory implements Runnable{
	
	private AtomicInteger id = new AtomicInteger(10000000);
	
	public void run(){
		super.run();
		while(true){
			try {
				Thread.sleep(1000*60*60*24);
				if(id.decrementAndGet() > Integer.MAX_VALUE-100000000){
					id.set(10000000);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@Override
	public String createSequence() {
			return ""+id.getAndIncrement();
	}
}

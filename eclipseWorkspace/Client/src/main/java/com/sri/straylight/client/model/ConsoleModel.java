package com.sri.straylight.client.model;

public class ConsoleModel {
	
	/** The start time_. */
	private long startTime_ = 0;
	
	public ConsoleModel() {
		startTime_ = System.currentTimeMillis();
	}

	public Long getElepasedTime() {
		Long elapsedTimeMillis =  System.currentTimeMillis()-startTime_;
		return elapsedTimeMillis;
	}
	
	

}

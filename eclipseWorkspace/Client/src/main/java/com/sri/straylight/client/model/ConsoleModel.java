package com.sri.straylight.client.model;

public class ConsoleModel extends BaseModel{
	
	/** The start time_. */
	private long startTime_ = 0;
	
	public ConsoleModel(String title) {
		super(title);
		startTime_ = System.currentTimeMillis();
	}

	public Long getElepasedTime() {
		Long elapsedTimeMillis =  System.currentTimeMillis()-startTime_;
		return elapsedTimeMillis;
	}


}

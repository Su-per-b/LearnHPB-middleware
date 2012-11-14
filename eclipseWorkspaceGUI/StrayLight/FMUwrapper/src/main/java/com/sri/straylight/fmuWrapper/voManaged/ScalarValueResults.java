package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;

public class ScalarValueResults {
	

	public ScalarValueCollection input;
	public ScalarValueCollection output;
	private double time_;
	
	
	public ScalarValueResults(ScalarValueResultsStruct struct) {
		time_ = struct.time;
		input = new ScalarValueCollection(struct.input);
		output = new ScalarValueCollection(struct.output);
	}
	
	
	
	public String toString() {
		String str = "time: " + Double.toString(time_) + " " +  output.toString();
		return str;
	}
	


	public double getTime() {
		return time_;
	}


	
}

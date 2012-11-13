package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;

public class ScalarValueResults {
	

	public ScalarValueCollection input;
	public ScalarValueCollection output;
	private double time_;
	
	
	public ScalarValueResults(ScalarValueResultsStruct st1) {
		time_ = st1.time;
		input = new ScalarValueCollection(st1.input);
		output = new ScalarValueCollection(st1.output);
	}
	
	
	
	public String toString() {
		String str = "time: " + Double.toString(time_) + " " +  output.toString();
		return str;
	}
	


	public double getTime() {
		return time_;
	}


	
}

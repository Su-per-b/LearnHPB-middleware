package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.sri.straylight.fmuWrapper.voNative.ResultOfStepStruct;
import com.sun.jna.Pointer;

public class ResultOfStep {
	
	private double time_;
	private double[] input_;
	private double[] output_;
	
	
	
	public ResultOfStep() {
		
	}
	
	
	public ResultOfStep(ResultOfStepStruct resultOfStepStruct) {
		
		time_ = resultOfStepStruct.time;

		Pointer p = resultOfStepStruct.input.getPointer();
		input_ = p.getDoubleArray(0, resultOfStepStruct.inputLength);
		
		Pointer p2 = resultOfStepStruct.output.getPointer();
		output_ = p2.getDoubleArray(0, resultOfStepStruct.outputLength);
	}
	
	public Vector<String> getOutputList() {
		return convertToString(output_);
	}
	
	public Vector<String> getInputList() {
		
		return convertToString(input_);
	}
	
	public double getTime() {
		
		return time_;
	}
	
	
	private Vector<String> convertToString(double[] ary) {
		
		String[] strArray = new String[ary.length];
		int len = ary.length;
		
		for (int i = 0; i < len; i++) {
			strArray[i] = Double.toString(ary[i]);
		}
		
		List<String> list = Arrays.asList(strArray);
        Vector<String> vector = new Vector<String>(list);
        
        return vector;
	}
	
	public String inputToString() {
		
		Vector<String> strList = getInputList();
		String timeStr = "time: " + Double.toString(time_);
		strList.insertElementAt(timeStr,0);
		
		return strList.toString();
	}
	
	public String toString() {
		
		Vector<String> strList = getOutputList();
		String timeStr = "time: " + Double.toString(time_);
		strList.insertElementAt(timeStr,0);
		
		return strList.toString();
	}
	
}

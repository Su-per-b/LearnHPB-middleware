package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;
import com.sun.jna.ptr.DoubleByReference;

public class ResultOfStepStruct extends Structure {

	public double time;
	
	//public DoubleArrayStruct  testStruct;
	
	public DoubleByReference  input;
	public int inputLength;

	public DoubleByReference  output;
	public int outputLength;

}

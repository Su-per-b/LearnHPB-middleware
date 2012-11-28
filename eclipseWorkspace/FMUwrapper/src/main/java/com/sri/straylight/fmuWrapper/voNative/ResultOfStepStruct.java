package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;
import com.sun.jna.ptr.DoubleByReference;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultOfStepStruct.
 */
public class ResultOfStepStruct extends Structure {

	/** The time. */
	public double time;
	
	//public DoubleArrayStruct  testStruct;
	
	/** The input. */
	public DoubleByReference  input;
	
	/** The input length. */
	public int inputLength;

	/** The output. */
	public DoubleByReference  output;
	
	/** The output length. */
	public int outputLength;

}

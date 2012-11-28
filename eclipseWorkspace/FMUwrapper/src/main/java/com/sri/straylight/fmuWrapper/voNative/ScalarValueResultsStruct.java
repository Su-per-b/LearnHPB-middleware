package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class ScalarValueResultsStruct extends Structure {
	
	public static class ByReference extends ScalarValueResultsStruct implements Structure.ByReference { }
	
	public ScalarValueCollectionStruct.ByReference  input;
	public ScalarValueCollectionStruct.ByReference  output;
	public double time;
	
}

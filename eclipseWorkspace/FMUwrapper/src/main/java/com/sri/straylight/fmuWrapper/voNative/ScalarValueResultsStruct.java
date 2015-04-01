package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class ScalarValueResultsStruct extends Structure {
	
	public static class ByReference extends ScalarValueResultsStruct implements Structure.ByReference { }
	
	public ScalarValueCollectionStruct.ByReference  input;
	public ScalarValueCollectionStruct.ByReference  output;
	public double time;
	
	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "input", "output", "time"});
	}
	
	
	
}

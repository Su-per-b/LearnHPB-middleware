package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class Element  extends Structure  {
	
	public static class ByReference extends Element implements Structure.ByReference { }
	int type;          // element type 
    String attributes; // null or n attribute value strings
    int n;             // size of attributes, even number
    
    
}

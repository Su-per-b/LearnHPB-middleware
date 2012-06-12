package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class Element2  extends Structure {
	
	public static class ByReference extends Element2 implements Structure.ByReference { }

	public int n;             // size of attributes, even number
    
    
}

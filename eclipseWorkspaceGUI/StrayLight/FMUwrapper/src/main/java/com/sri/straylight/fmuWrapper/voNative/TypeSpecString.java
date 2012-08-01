
package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class TypeSpecString  extends Structure {
	
	public static class ByReference extends TypeSpecString implements Structure.ByReference { }
	
	public String start;
	public String declaredType;
	public int startValueStatus;

}

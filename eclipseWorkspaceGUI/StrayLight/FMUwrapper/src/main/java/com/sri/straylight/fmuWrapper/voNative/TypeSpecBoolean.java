package com.sri.straylight.fmuWrapper.voNative;


import com.sun.jna.Structure;

public class TypeSpecBoolean extends Structure {
	
	public static class ByReference extends TypeSpecBoolean implements Structure.ByReference { }
	public byte start;
	public byte fixed;

	public int startValueStatus;
	public int fixedValueStatus;

	public String declaredType;
	
}

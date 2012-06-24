package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class TypeSpec  extends Structure {
	
	public static class ByReference extends TypeSpec implements Structure.ByReference { }
	public int type;          // element type 
	public int n;      // size of attributes, even number
	//public String[] attributes;
	
	
	public Elm getTypeEnum() {

		Elm p = Elm.elm_Annotation;
		Elm theEnum  = p.getForValue (type);
		
		return Elm.elm_Annotation;
	}
	
	
}

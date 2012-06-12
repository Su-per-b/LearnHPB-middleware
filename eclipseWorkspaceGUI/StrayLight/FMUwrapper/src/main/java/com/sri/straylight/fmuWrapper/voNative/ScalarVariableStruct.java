package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;

public class ScalarVariableStruct extends Structure {

	public String name;
	public int idx;
	public int causality;
	public String description;
	public int type;
	public Element2.ByReference element;
	//public ScalarValueStruct.ByReference scalarValueStruct;
	
	public Enu getCausalityEnum() {

		Enu p = Enu.enu_constant;
		Enu theEnum  = p.getForValue (causality);
		
		return theEnum;
	}
	
	public Elm getTypeEnum() {

		Elm p = Elm.elm_Annotation;
		Elm theEnum  = p.getForValue (type);
		
		return Elm.elm_Annotation;
	}
	
}

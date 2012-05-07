package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;

public class ScalarVariableStruct extends Structure {

	public String name;
	public int idx;
	public int causality;
	public int type;
	
	public Enu getCausalityEnum() {

		Enu p = Enu.enu_constant;
		Enu theEnum  = p.getForValue (causality);
		
		return theEnum;
	}
}

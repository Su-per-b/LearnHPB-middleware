package com.sri.straylight.fmuWrapper;


import com.sun.jna.Structure;

public class ScalarVariableMeta extends Structure {

	public String name;
	public int idx;
	public int causality;
	
	public Enu getCausalityEnum() {

		Enu p = Enu.enu_constant;
		Enu theEnum  = p.getForValue (causality);
		
		return theEnum;
	}
	
}

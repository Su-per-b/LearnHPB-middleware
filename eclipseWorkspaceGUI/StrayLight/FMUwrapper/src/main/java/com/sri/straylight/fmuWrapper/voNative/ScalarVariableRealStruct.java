package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

public class ScalarVariableRealStruct extends Structure {

	public String name;
	public int idx;
	public int causality;
	public String description;
	public TypeSpecReal.ByReference typeSpecReal;
	public int valueReference;
	
	public Enu getCausalityEnum() {

		Enu p = Enu.enu_constant;
		Enu theEnum  = p.getForValue (causality);
		
		return theEnum;
	}
	
//	public long getValueReference() {
//
//			long vr = (long) valueReference.getValue();
//			return vr;
//	}
//	
}

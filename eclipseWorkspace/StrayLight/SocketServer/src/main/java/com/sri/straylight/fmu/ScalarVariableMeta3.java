package com.sri.straylight.fmu;

import com.sun.jna.Native;
import com.sun.jna.Structure;

public class ScalarVariableMeta3 extends Structure {

	public String name;
	public int idx;

	
	public int causality; // = Enu.enu_constant;
	
	 // public int buf_length;
	 // public byte[] buffer;
	  
	protected int getNativeSize(Class nativeType, Object value) {
		
		if (nativeType == Enu.class) {
			return 4;
		} else {
			
			int size = super.getNativeSize (nativeType, value);
		    return size;
			
		}
		

	
	}
	
	protected int getNativeAlignment(Class type, Object value, boolean isFirstElement) {
		
		if (type == Enu.class) {
			return 4;
		} else {
			int size = super.getNativeAlignment (type, value, isFirstElement);
		    return size;
		}
		
	}
	
	
	

	  public ScalarVariableMeta3() {
		  
		   // buffer = new byte[4];
		   // buf_length = buffer.length;
		   // causality = Enu.enu_constant;
		  
		    //allocateMemory();
		  }
	
}

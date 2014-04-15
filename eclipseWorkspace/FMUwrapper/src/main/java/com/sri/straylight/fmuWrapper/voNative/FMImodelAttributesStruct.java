package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class FMImodelAttributesStruct extends Structure {
	
	/** The real value. */
	public AttributeStruct.ByReference attributeStructAry;
	
	/** The real size. */
	public int attributeStructSize;
	
	
	public AttributeStruct[] toArray() {
		
		if (attributeStructSize > 0) {
			AttributeStruct[] ary = (AttributeStruct[]) attributeStructAry.toArray(attributeStructSize);
			return ary;
		} else {
			return null;
		}
		

	}
	
	
}

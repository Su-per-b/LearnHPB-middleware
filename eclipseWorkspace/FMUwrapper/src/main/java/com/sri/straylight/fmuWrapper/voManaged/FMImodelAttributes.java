package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Vector;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.AttributeStruct;
import com.sri.straylight.fmuWrapper.voNative.FMImodelAttributesStruct;

public class FMImodelAttributes implements JsonSerializable {

	private Vector<AttributeStruct> attributeStructs_;
	
	public FMImodelAttributes(FMImodelAttributesStruct  fmiModelAttributesStruct) {

		init(fmiModelAttributesStruct);
		
	}

	private void init(FMImodelAttributesStruct fmiModelAttributesStruct) {

		
		AttributeStruct[] ary = fmiModelAttributesStruct.toArray();	
	
		attributeStructs_ = new Vector<AttributeStruct>();
		
		int len = ary.length;
				
		for (int i = 0; i < len; i++) {
			
			AttributeStruct attributeStruct = ary[i];
			attributeStructs_.add(attributeStruct);

		}

	}
	
	public Vector<AttributeStruct> toVector() {
		
		return attributeStructs_;
	}
	
	
	@Override
	public String toJsonString() {
		return JsonController.getInstance().toJsonString(this);
	}
	
	
}

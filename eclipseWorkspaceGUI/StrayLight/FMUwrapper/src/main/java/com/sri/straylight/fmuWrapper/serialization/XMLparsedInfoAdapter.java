
package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;


/**
 * The Class ResultEventAdapter.
 */
public class XMLparsedInfoAdapter extends AdapterBase<XMLparsedInfo> {

	
	public XMLparsedInfoAdapter() {
		super();
	}
	
	
	@Override
	public JsonElement serialize(XMLparsedInfo src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		serializeOneField_("scalarVariablesAll_", src.getScalarVariablesAll());
		
		return jsonObject_;
	}
	

	@Override
	public XMLparsedInfo deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

		destObject_ = new XMLparsedInfo();
		super.deserialize(jsonElement, typeOfT, context);
		
		
        JsonElement jsonElement1 = jsonObject_.get("scalarVariablesAll_");
        
        ScalarVariablesAll sVarAll = deserializationContext_.deserialize
        		(jsonElement1, ScalarVariablesAll.class);
        
        destObject_.setScalarVariablesAll(sVarAll);
        
		
		return destObject_;

	}
	
	

}

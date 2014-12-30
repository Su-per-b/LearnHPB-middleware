
package com.sri.straylight.fmuWrapper.serialization;



import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarVariableRealAdapter extends AdapterBase<ScalarVariableReal> {

	final protected String[][] fieldNamesEx_ = { 
			{ "name_", "n" },
			{ "idx_", "i" },
			{ "causality_", "c" },
			{ "variability_", "vb" },
			{ "description_", "d" },
			{ "valueReference_", "vr" }
		};
	
	
	public ScalarVariableRealAdapter() {
		super(ScalarVariableReal.class);
		super.init(fieldNamesEx_);
	}
	

	
	
	@Override
	public JsonElement serialize(ScalarVariableReal src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		
		TypeSpecReal typeSpecReal =  src.getTypeSpecReal();
		JsonElement element = serializationContext_.serialize(typeSpecReal, TypeSpecReal.class);
		jsonObject_.add("typeSpecReal", element);

		return jsonObject_;
	}
	

	@Override
	public ScalarVariableReal deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

    	super.deserialize(jsonElement, typeOfT, context);
    	
        JsonElement jsonElementInput = jsonObject_.get("typeSpecReal");
        TypeSpecReal typeSpecReal = context.deserialize(jsonElementInput, TypeSpecReal.class);
        destObject_.setTypeSpecReal(typeSpecReal);
        
		return destObject_;

	}
	
	
	
	
	

	
	
	

}

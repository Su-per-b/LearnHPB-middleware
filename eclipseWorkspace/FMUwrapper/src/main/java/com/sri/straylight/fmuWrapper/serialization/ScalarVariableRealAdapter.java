
package com.sri.straylight.fmuWrapper.serialization;



import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarVariableRealAdapter extends AdapterBase<ScalarVariableReal> {

	//final protected String[] fieldNames_ = { "name_" };
	
	
	public ScalarVariableRealAdapter() {
		super();
		super.setFieldNames(fieldNames_);
	}
	
	
	@Override
	public JsonElement serialize(ScalarVariableReal src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		
		TypeSpecReal typeSpecReal =  src.getTypeSpecReal();
		JsonElement element = serializationContext_.serialize(typeSpecReal, TypeSpecReal.class);
		jsonObject_.add("typeSpecReal_", element);
		
		JsonPrimitive primitive1 = new JsonPrimitive(src.getName());
		jsonObject_.add("name_", primitive1);
		
		JsonPrimitive primitive2 = new JsonPrimitive(src.getIdx());
		jsonObject_.add("idx_", primitive2);
		
		JsonPrimitive primitive3 = new JsonPrimitive(src.getCausalityAsInt());
		jsonObject_.add("causality_", primitive3);
		
		JsonPrimitive primitive4 = new JsonPrimitive(src.getVariabilityAsInt());
		jsonObject_.add("variability_", primitive4);
		
		JsonPrimitive primitive5 = new JsonPrimitive(src.getDescription());
		jsonObject_.add("description_", primitive5);
		
		JsonPrimitive primitive6 = new JsonPrimitive(src.getVariabilityAsInt());
		jsonObject_.add("valueReference_", primitive6);
		
		String unit = src.getUnit();
		JsonPrimitive primitive7 = new JsonPrimitive(unit);
		jsonObject_.add("unit_", primitive7);
		
		
		return jsonObject_;
	}
	

	@Override
	public ScalarVariableReal deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

		destObject_ = new ScalarVariableReal();
    	super.deserialize(jsonElement, typeOfT, context);
    	
        JsonElement jsonElementInput = jsonObject_.get("typeSpecReal_");
        TypeSpecReal typeSpecReal = context.deserialize(jsonElementInput, TypeSpecReal.class);
        destObject_.setTypeSpecReal(typeSpecReal);
        
        String name = jsonObject_.get("name_").getAsString();
        destObject_.setName(name);
        
        int idx = jsonObject_.get("idx_").getAsInt();
        destObject_.setIdx(idx);
        
        int causality = jsonObject_.get("causality_").getAsInt();
        destObject_.setCausality(causality);
        
        
        int variability = jsonObject_.get("variability_").getAsInt();
        destObject_.setVariability(variability);
        
        
        String description = jsonObject_.get("description_").getAsString();
        destObject_.setDescription(description);
        
        int valueReference = jsonObject_.get("valueReference_").getAsInt();
        destObject_.setValueReference(valueReference);
        
        
        

        
		return destObject_;

	}
	
	
	
	
	
	

}

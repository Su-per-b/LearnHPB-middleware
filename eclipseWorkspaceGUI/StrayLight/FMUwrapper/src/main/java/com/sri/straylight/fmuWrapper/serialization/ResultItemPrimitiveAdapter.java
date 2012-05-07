package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValue;



public class ResultItemPrimitiveAdapter implements 
JsonSerializer<ScalarValue>, JsonDeserializer<ScalarValue> {

	@Override
    public JsonElement serialize(
    		ScalarValue src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        
        result.add("idx", new JsonPrimitive(src.idx));
        result.add("string", new JsonPrimitive(src.string));
        
        return result;
    }
    
    
    
    @Override
    public ScalarValue deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ScalarValue ri = new ScalarValue();
        
        ri.idx = jsonObject.get("idx").getAsInt();
        ri.string = jsonObject.get("string").getAsString();
        	
        return ri;

    }
    
}

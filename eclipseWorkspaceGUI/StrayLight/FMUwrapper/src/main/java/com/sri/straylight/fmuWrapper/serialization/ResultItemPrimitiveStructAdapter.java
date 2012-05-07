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
import com.sri.straylight.fmuWrapper.voNative.ScalarValueStruct;


public class ResultItemPrimitiveStructAdapter implements 
JsonSerializer<ScalarValueStruct>, JsonDeserializer<ScalarValueStruct> {

	@Override
    public JsonElement serialize(
    		ScalarValueStruct src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        
        result.add("idx", new JsonPrimitive(src.idx));
        result.add("string", new JsonPrimitive(src.string));
        
        return result;
    }
    
    
    
    @Override
    public ScalarValueStruct deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ScalarValueStruct struct = new ScalarValueStruct();
        
        struct.idx = jsonObject.get("idx").getAsInt();
        struct.string = jsonObject.get("string").getAsString();
        	
        return struct;

    }
    
}

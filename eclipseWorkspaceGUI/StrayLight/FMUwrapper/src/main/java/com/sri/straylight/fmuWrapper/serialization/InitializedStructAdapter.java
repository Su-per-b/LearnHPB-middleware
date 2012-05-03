package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sri.straylight.fmuWrapper.InitializedStruct;



public class InitializedStructAdapter implements 
JsonSerializer<InitializedStruct>, JsonDeserializer<InitializedStruct> {

	@Override
    public JsonElement serialize(
    		InitializedStruct src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));

        JsonArray jsAry = new JsonArray();

        for (int j = 0; j < src.columnNames.length; j++) {
        		
        	String columnName = src.columnNames[j];
        	
        	JsonElement elem = new JsonPrimitive(columnName);
        	jsAry.add(elem);
        	
		}
        
        result.add("columnNames", jsAry);
        
        
        return result;
    }
    
    
    
    @Override
    public InitializedStruct deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
    	InitializedStruct struct = new InitializedStruct();
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        JsonArray jsAry = jsonObject.get("columnNames").getAsJsonArray();
        int len = jsAry.size();
        
        String[] columnNames = new String[len];
        		
        for(int i=0; i < len; i++) {
        	JsonElement elem = jsAry.get(i);
        	columnNames[i] = elem.getAsString();
        }
        
        
        struct.columnNames = columnNames;
        
        	
        return struct;

    }
    
}
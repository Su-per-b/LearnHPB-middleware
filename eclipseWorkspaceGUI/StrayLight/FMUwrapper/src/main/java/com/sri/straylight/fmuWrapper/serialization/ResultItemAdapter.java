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
import com.sri.straylight.fmuWrapper.ResultItem;
import com.sri.straylight.fmuWrapper.ResultItemPrimitive;



public class ResultItemAdapter implements 
JsonSerializer<ResultItem>, JsonDeserializer<ResultItem> {

	@Override
    public JsonElement serialize(
    		ResultItem src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        
        result.add("time", new JsonPrimitive(src.time));
        result.add("string", new JsonPrimitive(src.string));
        result.add("primitiveCount", new JsonPrimitive(src.primitiveCount));
        

        JsonArray jsAry = new JsonArray();

        
        for (int j = 0; j < src.primitiveAry.length; j++) {
        		
        	ResultItemPrimitive primitive = src.primitiveAry[j];
        	JsonElement elem = context.serialize(primitive);
        	jsAry.add(elem);
        	
		}
        
        result.add("primitiveAry", jsAry);

        
        return result;
    }
    
    
    
    @Override
    public ResultItem deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ResultItem resultItem = new ResultItem();
        
        resultItem.time = jsonObject.get("time").getAsDouble();
        resultItem.string = jsonObject.get("string").getAsString();
        resultItem.primitiveCount = jsonObject.get("primitiveCount").getAsInt();
        
        JsonArray jsAry = jsonObject.get("primitiveAry").getAsJsonArray();
        
        int len = jsAry.size();
        
        ResultItemPrimitive[] primitiveAry = new ResultItemPrimitive[len];
        		
        for(int i=0; i < len; i++) {

        	JsonElement elem = jsAry.get(i);
        	ResultItemPrimitive primitive = context.deserialize(elem, ResultItemPrimitive.class);
        	primitiveAry[i] = primitive;
        }
        
        resultItem.primitiveAry = primitiveAry;
        
        	
        return resultItem;

    }
    
}

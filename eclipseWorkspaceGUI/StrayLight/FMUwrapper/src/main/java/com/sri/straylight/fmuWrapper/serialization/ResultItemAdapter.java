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
import com.sri.straylight.fmuWrapper.ResultItemPrimitiveStruct;


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
        		
        	ResultItemPrimitiveStruct struct = src.primitiveAry[j];
        	JsonElement elem = context.serialize(struct);
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
        
        ResultItemPrimitiveStruct[] primitiveAry = new ResultItemPrimitiveStruct[len];
        		
        for(int i=0; i < len; i++) {

        	JsonElement elem = jsAry.get(i);
        	ResultItemPrimitiveStruct struct = context.deserialize(elem, ResultItemPrimitiveStruct.class);
        	primitiveAry[i] = struct;;
        }
        
        resultItem.primitiveAry = primitiveAry;
        
        	
        return resultItem;

    }
    
}

package com.sri.straylight.fmuWrapper.serialization;



import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sri.straylight.fmuWrapper.voManaged.ResultOfStep;



// TODO: Auto-generated Javadoc
/**
 * The Class ResultItemAdapter.
 */
public class ResultItemAdapter implements 
JsonSerializer<ResultOfStep>, JsonDeserializer<ResultOfStep> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
    public JsonElement serialize(
    		ResultOfStep src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
//        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
//        
//        result.add("time", new JsonPrimitive(src.time));
//        result.add("string", new JsonPrimitive(src.string));
//        result.add("primitiveCount", new JsonPrimitive(src.scalarValueCount));
//        
//
//        JsonArray jsAry = new JsonArray();
//
//        
//        for (int j = 0; j < src.scalarValueAry.length; j++) {
//        		
//        	ScalarValue primitive = src.scalarValueAry[j];
//        	JsonElement elem = context.serialize(primitive);
//        	jsAry.add(elem);
//        	
//		}
//        
//        result.add("primitiveAry", jsAry);

        
        return result;
    }
    
    
    
    /* (non-Javadoc)
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    @Override
    public ResultOfStep deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ResultOfStep resultItem = new ResultOfStep();
        
//        resultItem.time = jsonObject.get("time").getAsDouble();
//        resultItem.string = jsonObject.get("string").getAsString();
//        resultItem.scalarValueCount = jsonObject.get("primitiveCount").getAsInt();
//        
//        JsonArray jsAry = jsonObject.get("primitiveAry").getAsJsonArray();
//        
//        int len = jsAry.size();
//        
//        ScalarValue[] primitiveAry = new ScalarValue[len];
//        		
//        for(int i=0; i < len; i++) {
//
//        	JsonElement elem = jsAry.get(i);
//        	ScalarValue primitive = context.deserialize(elem, ScalarValue.class);
//        	primitiveAry[i] = primitive;
//        }
//        
//        resultItem.scalarValueAry = primitiveAry;
        
        	
        return resultItem;

    }
    
}

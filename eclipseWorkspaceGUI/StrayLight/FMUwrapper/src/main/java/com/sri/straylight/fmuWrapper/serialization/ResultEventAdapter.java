
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
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.ResultOfStep;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultEventAdapter.
 */
public class ResultEventAdapter implements 
JsonSerializer<ResultEvent>, JsonDeserializer<ResultEvent> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
    public JsonElement serialize(
    		ResultEvent src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
    	
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        
        //result.add("resultString", new JsonPrimitive(src.resultString));
        
        
        ScalarValueResults scalarValueResults = src.getScalarValueResults();
        
        result.add(
        		"resultItem", 
        		context.serialize(scalarValueResults, scalarValueResults.getClass())
        		);
        
        
        return result;
    }
	
	
    
    
    
    /* (non-Javadoc)
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    @Override
    public ResultEvent deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement element = jsonObject.get("resultItem");
        
        ScalarValueResults scalarValueResults = context.deserialize(element,ScalarValueResults.class);
        
        ResultEvent resultEvent = new ResultEvent(this, scalarValueResults);
        
        
        return resultEvent;

    }
    
}

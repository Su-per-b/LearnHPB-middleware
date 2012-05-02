
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
import com.sri.straylight.fmuWrapper.ResultItem;
import com.sri.straylight.fmuWrapper.event.ResultEvent;

public class ResultEventAdapter implements 
JsonSerializer<ResultEvent>, JsonDeserializer<ResultEvent> {

	@Override
    public JsonElement serialize(
    		ResultEvent src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
    	
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        
        result.add("resultString", new JsonPrimitive(src.resultString));
        
        result.add(
        		"resultItem", 
        		context.serialize(src.resultItem, src.resultItem.getClass())
        		);
        
        
        return result;
    }
    
    
    
    @Override
    public ResultEvent deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        JsonElement element = jsonObject.get("resultItem");
        ResultEvent resultEvent = new ResultEvent(this);
        
        resultEvent.resultString = jsonObject.get("resultString").getAsString();
        
        resultEvent.resultItem = context.deserialize(element,ResultItem.class);
        
        return resultEvent;

    }
    
}

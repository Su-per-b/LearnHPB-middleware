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
import com.sri.straylight.fmuWrapper.State;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;

public class FMUstateEventAdapter implements 
JsonSerializer<FMUstateEvent>, JsonDeserializer<FMUstateEvent> {

	@Override
    public JsonElement serialize(
    		FMUstateEvent src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
    	
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        
       // result.add("fmuState", new JsonPrimitive(src.resultString));
        
        result.add(
        		"fmuState", 
        		context.serialize(src.fmuState, src.fmuState.getClass())
        		);
        
        
        return result;
    }
    
    
    
    @Override
    public FMUstateEvent deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        JsonElement element = jsonObject.get("fmuState");
        
        FMUstateEvent event = new FMUstateEvent(this);
        

        event.fmuState = context.deserialize(element,State.class);
        
        return event;

    }
    
}

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
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.voManaged.Initialized;

public class InitializedEventAdapter implements 
JsonSerializer<InitializedEvent>, JsonDeserializer<InitializedEvent> {

	@Override
    public JsonElement serialize(
    		InitializedEvent src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
    	
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        
        result.add(
        		"initializedStruct", 
        		context.serialize(src.initializedStruct, src.initializedStruct.getClass())
        		);

        return result;
    }
    
    
    
    @Override
    public InitializedEvent deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement element = jsonObject.get("initializedStruct");

        InitializedEvent event = new InitializedEvent(this);
        event.initializedStruct = context.deserialize(element,Initialized.class);
        
        return event;

    }
    
}

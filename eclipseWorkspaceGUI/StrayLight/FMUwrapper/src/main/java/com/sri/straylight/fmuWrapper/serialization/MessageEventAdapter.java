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
import com.sri.straylight.fmuWrapper.MessageStruct;
import com.sri.straylight.fmuWrapper.event.MessageEvent;

public class MessageEventAdapter implements 
JsonSerializer<MessageEvent>, JsonDeserializer<MessageEvent> {

	@Override
    public JsonElement serialize(
    		MessageEvent src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
    	
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add(
        		"messageStruct", 
        		context.serialize(src.messageStruct, src.messageStruct.getClass())
        		);

        return result;
    }
    
    
    
    @Override
    public MessageEvent deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        JsonElement element = jsonObject.get("messageStruct");

        MessageEvent messageEvent = new MessageEvent(this);
        messageEvent.messageStruct = context.deserialize(element,MessageStruct.class);
        
        return messageEvent;

    }
    
}

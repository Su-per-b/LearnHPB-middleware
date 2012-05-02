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


public class MessageStructAdapter implements 
JsonSerializer<MessageStruct>, JsonDeserializer<MessageStruct> {

	@Override
    public JsonElement serialize(
    		MessageStruct src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("msgText", new JsonPrimitive(src.msgText));
        result.add("messageType", new JsonPrimitive(src.messageType));
        
        return result;
    }
    
    
    
    @Override
    public MessageStruct deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        MessageStruct messageStruct = new MessageStruct();
        messageStruct.msgText = jsonObject.get("msgText").getAsString();
        messageStruct.messageType = jsonObject.get("messageType").getAsInt();
        	
        return messageStruct;

    }
    
}

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
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageEventAdapter.
 */
public class MessageEventAdapter implements 
JsonSerializer<MessageEvent>, JsonDeserializer<MessageEvent> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
    public JsonElement serialize(
    		MessageEvent src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
    	
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add(
        		"messageStruct", 
        		context.serialize(src.getPayload(), src.getPayload().getClass())
        		);

        return result;
    }
    
    
    
    /* (non-Javadoc)
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    @Override
    public MessageEvent deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        JsonElement element = jsonObject.get("messageStruct");

        
        MessageStruct messageStruct = context.deserialize(element,MessageStruct.class);
        MessageEvent messageEvent = new MessageEvent(this, messageStruct);
       
        return messageEvent;

    }
    
}

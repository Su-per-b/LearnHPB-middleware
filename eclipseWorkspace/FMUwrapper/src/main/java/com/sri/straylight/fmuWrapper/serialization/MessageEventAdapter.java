package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;


/**
 * The Class MessageEventAdapter.
 */
public class MessageEventAdapter 
	extends AdapterEventBase<MessageEvent, MessageStruct>
{

	
	public MessageEventAdapter() {
		super(MessageEvent.class, MessageStruct.class);
	}

    @Override
    public MessageEvent deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {

    	MessageStruct messageStruct = deserializePayload_(jsonElement, typeOfT, context);	
    	MessageEvent event = new MessageEvent(this, messageStruct);
    	
        return event;
    }
    
}

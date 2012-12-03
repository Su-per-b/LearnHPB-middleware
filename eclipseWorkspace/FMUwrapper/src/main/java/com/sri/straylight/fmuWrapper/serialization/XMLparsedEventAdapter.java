package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;


/**
 * The Class MessageEventAdapter.
 */
public class XMLparsedEventAdapter 
	extends AdapterEventBase<XMLparsedEvent, XMLparsedInfo>
{


    @Override
    public XMLparsedEvent deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {

    	super.deserializeHelper_(jsonElement, typeOfT, context, XMLparsedInfo.class);
    	
    	XMLparsedEvent event = new XMLparsedEvent(this, payload_);
    	
        return event;

    }
    
}

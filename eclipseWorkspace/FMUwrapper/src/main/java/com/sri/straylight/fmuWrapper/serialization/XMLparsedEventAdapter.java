package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


/**
 * The Class MessageEventAdapter.
 */
public class XMLparsedEventAdapter 
	extends AdapterEventBase<XMLparsedEvent, XMLparsedInfo>
{

	
	public XMLparsedEventAdapter() {
		super(XMLparsedEvent.class, XMLparsedInfo.class);
	}
	
	
    @Override
    public XMLparsedEvent deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
    	XMLparsedInfo payload = deserializePayload_(jsonElement, typeOfT, context);	
    	XMLparsedEvent event = new XMLparsedEvent(this, payload);
    	
        return event;

    }
    
}

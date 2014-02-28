
package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.SessionControlEvent;
import com.sri.straylight.fmuWrapper.voManaged.SessionControl;



public class SessionControlEventAdapter 
	extends AdapterEventBase<SessionControlEvent, SessionControl>
{


    
    
    @Override
    public SessionControlEvent deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
    	super.deserializeHelper_(jsonElement, typeOfT, context, SessionControl.class);
    	
    	SessionControlEvent event = new SessionControlEvent(this, payload_);
        
        return event;

    }
    


    
}


package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.event.SessionControlEvent;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.SessionControl;



public class SessionControlEventAdapter 
	extends AdapterEventBase<SessionControlEvent, SessionControl>
{


	public SessionControlEventAdapter() {
		super(SessionControlEvent.class, SessionControl.class);
	}
	
    
    @Override
    public SessionControlEvent deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {

    	SessionControl payload = deserializePayload_(jsonElement, typeOfT, context);	
    	SessionControlEvent event = new SessionControlEvent(this, payload);
    	
        return event;

    }
    


    
}

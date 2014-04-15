
package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.SessionControlClientRequest;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlModel;



public class SessionControlEventAdapter 
	extends AdapterEventBase<SessionControlClientRequest, SessionControlModel>
{


	public SessionControlEventAdapter() {
		super(SessionControlClientRequest.class, SessionControlModel.class);
	}
	
    
    @Override
    public SessionControlClientRequest deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {

    	SessionControlModel payload = deserializePayload_(jsonElement, typeOfT, context);	
    	SessionControlClientRequest event = new SessionControlClientRequest(this, payload);
    	
        return event;

    }
    


    
}

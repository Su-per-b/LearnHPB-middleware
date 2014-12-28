package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.InitialStateRequest;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;


/**
 * The Class MessageEventAdapter.
 */
public class InitialStateRequestAdapter 
	extends AdapterEventBase<InitialStateRequest, ScalarValueCollection>
{

	
	public InitialStateRequestAdapter() {
		super(InitialStateRequest.class, ScalarValueCollection.class);
	}
	
	
    @Override
    public InitialStateRequest deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
    	ScalarValueCollection payload = deserializePayload_(jsonElement, typeOfT, context);	
    	InitialStateRequest event = new InitialStateRequest(this, payload);
    	
        return event;

    }
    
}

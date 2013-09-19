package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;



/**
 * The Class MessageEventAdapter.
 */
public class ScalarValueChangeRequestAdapter 
	extends AdapterEventBase<ScalarValueChangeRequest, ScalarValueCollection>
{


    @Override
    public ScalarValueChangeRequest deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {

    	
    	
    	super.deserializeHelper_(jsonElement, typeOfT, context, ScalarValueCollection.class);
    	ScalarValueChangeRequest event = new ScalarValueChangeRequest(this, payload_);
    	
        return event;

    }
    
}
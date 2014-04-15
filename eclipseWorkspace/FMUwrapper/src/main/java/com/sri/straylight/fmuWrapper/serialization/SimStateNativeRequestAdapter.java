

package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


public class SimStateNativeRequestAdapter 
	extends AdapterEventBase<SimStateNativeRequest, SimStateNative> {

	
	public SimStateNativeRequestAdapter() {
		super(SimStateNativeRequest.class, SimStateNative.class);
	}
	
	

    @Override
    public SimStateNativeRequest deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
    	
    	SimStateNative payload = deserializePayload_(jsonElement, typeOfT, context);	
    	SimStateNativeRequest event = new SimStateNativeRequest(this, payload);
    	
    	
        return event;

    }
    


    
}


package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


public class SimStateNativeNotifyAdapter 
	extends AdapterEventBase<SimStateNativeNotify, SimStateNative> {

	public SimStateNativeNotifyAdapter() {
		super(SimStateNativeNotify.class, SimStateNative.class);
	}
	
	
    @Override
    public SimStateNativeNotify deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
    	SimStateNative payload = deserializePayload_(jsonElement, typeOfT, context);	
    	SimStateNativeNotify event = new SimStateNativeNotify(this, payload);
    	
    	return event;

    }
    


    
}


package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


public class SimStateNativeNotifyAdapter 
	extends AdapterEventBase<SimStateNativeNotify, SimStateNative> {


    @Override
    public SimStateNativeNotify deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
    	super.deserializeHelper_(jsonElement, typeOfT, context);
    	SimStateNativeNotify event = new SimStateNativeNotify(this, payload_);
        
        return event;

    }
    


    
}

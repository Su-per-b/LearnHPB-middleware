package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.SimStateWrapperNotify;
import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;


public class SimStateWrapperNotifyAdapter 
		extends AdapterEventBase<SimStateWrapperNotify, SimStateWrapper>  {


    @Override
    public SimStateWrapperNotify deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
    	super.deserializeHelper_(jsonElement, typeOfT, context);
    	SimStateWrapperNotify event = new SimStateWrapperNotify(this, payload_);
        
        return event;

    }

    
}

package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;


/**
 * The Class MessageEventAdapter.
 */
public class ConfigChangeNotifyAdapter 
	extends AdapterEventBase<ConfigChangeNotify, ConfigStruct>

{


    @Override
    public ConfigChangeNotify deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {

    	super.deserializeHelper_(jsonElement, typeOfT, context, ConfigStruct.class);
    	
    	ConfigChangeNotify event = new ConfigChangeNotify(this, payload_);
    	
        return event;

    }
    
}

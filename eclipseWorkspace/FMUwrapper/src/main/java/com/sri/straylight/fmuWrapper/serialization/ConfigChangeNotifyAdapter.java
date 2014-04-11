package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;


/**
 * The Class MessageEventAdapter.
 */
public class ConfigChangeNotifyAdapter 
	extends AdapterEventBase<ConfigChangeNotify, ConfigStruct>

{
	
	public ConfigChangeNotifyAdapter() {
		super(ConfigChangeNotify.class, ConfigStruct.class);
	}
	
    @Override
    public ConfigChangeNotify deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {

    	
    	ConfigStruct payload = deserializePayload_(jsonElement, typeOfT, context);	
    	ConfigChangeNotify event = new ConfigChangeNotify(this, payload);
    
    	
        return event;

    }
    
}

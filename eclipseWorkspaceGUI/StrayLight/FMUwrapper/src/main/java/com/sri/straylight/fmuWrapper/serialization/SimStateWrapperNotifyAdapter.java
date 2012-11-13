package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sri.straylight.fmuWrapper.event.SimStateWrapperNotify;
import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultEventAdapter.
 */
public class SimStateWrapperNotifyAdapter extends AdapterBase
	implements JsonSerializer<SimStateWrapperNotify>, JsonDeserializer<SimStateWrapperNotify> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
    public JsonElement serialize(
    		SimStateWrapperNotify src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
		JsonObject jsonObject = serializeHelper_(src);

		
		JsonElement elem = context.serialize(src.getPayload());
		jsonObject.add("payload_", elem);
		
		
        return jsonObject;
    }
	
	

    
    /* (non-Javadoc)
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    @Override
    public SimStateWrapperNotify deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException 
     {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement element = jsonObject.get("payload_");
        
        SimStateWrapper payload = context.deserialize(element,SimStateWrapper.class);
        SimStateWrapperNotify newObject = new SimStateWrapperNotify(this,payload);
        
        return newObject;
    }
    

    
}

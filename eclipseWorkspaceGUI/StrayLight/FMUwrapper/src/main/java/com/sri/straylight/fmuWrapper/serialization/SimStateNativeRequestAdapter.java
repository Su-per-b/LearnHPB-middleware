

package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateWrapperNotify;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValue;
import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


public class SimStateNativeRequestAdapter extends AdapterBase
	implements JsonSerializer<SimStateNativeRequest>, JsonDeserializer<SimStateNativeRequest> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
    public JsonElement serialize(
    		SimStateNativeRequest src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
		
		 JsonObject jsonObject = serializeHelper_(src);
		
		 SimStateNative simStateNative = src.getPayload();
		 int intPayload = simStateNative.getIntValue();
		 
		 jsonObject.add("intPayload", new JsonPrimitive(intPayload));
		 

        return jsonObject;
    }
	
	

    
    /* (non-Javadoc)
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    @Override
    public SimStateNativeRequest deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException 
     {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        int intPayload  = jsonObject.get("intPayload").getAsInt();
        
        SimStateNative instance = SimStateNative.simStateNative_0_uninitialized;
        SimStateNative payload = instance.getForValue(intPayload);
        
        SimStateNativeRequest newObject = new SimStateNativeRequest(this, payload);
        

        return newObject;
    }
    

    
}

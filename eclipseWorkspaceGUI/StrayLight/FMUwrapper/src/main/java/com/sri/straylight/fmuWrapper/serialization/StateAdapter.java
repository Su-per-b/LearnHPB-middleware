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
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Class StateAdapter.
 */
public class StateAdapter implements 
JsonSerializer<SimStateNative>, JsonDeserializer<SimStateNative> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
    public JsonElement serialize(
    		SimStateNative src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
    	
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        
        int asInt = src.getIntValue();
        result.add("asInt", new JsonPrimitive(asInt));


        return result;
    }
    
    
    
    /* (non-Javadoc)
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    @Override
    public SimStateNative deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int asInt = jsonObject.get("asInt").getAsInt();
        
        SimStateNative state = SimStateNative.simStateNative_0_uninitialized;
        state = state.getForValue(asInt);
        

        return state;

    }
    
}

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
import com.sri.straylight.fmuWrapper.voManaged.ScalarValue;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultEventAdapter.
 */
public class ScalarValueAdapter extends AdapterBase
	implements JsonSerializer<ScalarValue>, JsonDeserializer<ScalarValue> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
    public JsonElement serialize(
    		ScalarValue src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
		JsonObject jsonObject = serializeHelper_(src);

		jsonObject.add("idx", new JsonPrimitive(src.getIdx()));
		//jsonObject.add("string", new JsonPrimitive(src.string));
        
        return jsonObject;
    }
	
	

    
    /* (non-Javadoc)
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
 */
    @Override
    public ScalarValue deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException 
     {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ScalarValue<Double> scalarValue = new ScalarValue<Double>(1,1.0);

        int idx = jsonObject.get("idx").getAsInt();
        
        
        return scalarValue;

    }
        
}

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
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;



/**
 * The Class InitializedStructAdapter.
 */
public class InitializedStructAdapter implements 
JsonSerializer<XMLparsedInfo>, JsonDeserializer<XMLparsedInfo> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
    public JsonElement serialize(
    		XMLparsedInfo src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));

        /*
        JsonArray jsAry = new JsonArray();

        for (int j = 0; j < src.outputVarNames.length; j++) {
        		
        	String columnName = src.outputVarNames[j];
        	
        	JsonElement elem = new JsonPrimitive(columnName);
        	jsAry.add(elem);
        	
		}
        
        result.add("columnNames", jsAry);
        */
        
        return result;
    }
    
    
    
    /* (non-Javadoc)
     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
     */
    @Override
    public XMLparsedInfo deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
    	XMLparsedInfo struct = new XMLparsedInfo();
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
       // JsonArray jsAry = jsonObject.get("columnNames").getAsJsonArray();

        
        /*
        String[] columnNames = new String[len];
        		
        for(int i=0; i < len; i++) {
        	JsonElement elem = jsAry.get(i);
        	columnNames[i] = elem.getAsString();
        }
        
        
        struct.outputVarNames = columnNames;
        
        	*/
        
        return struct;

    }
    
}

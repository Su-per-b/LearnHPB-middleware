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
import com.sri.straylight.fmuWrapper.MessageStruct;
import com.sri.straylight.fmuWrapper.State;
import com.sri.straylight.fmuWrapper.event.MessageEvent;

public class StateAdapter implements 
JsonSerializer<State>, JsonDeserializer<State> {

	@Override
    public JsonElement serialize(
    		State src, 
    		Type typeOfSrc, 
    		JsonSerializationContext context) {
        
    	JsonObject result = new JsonObject();
    	
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        
        int asInt = src.getIntValue();
        result.add("asInt", new JsonPrimitive(asInt));


        return result;
    }
    
    
    
    @Override
    public State deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int asInt = jsonObject.get("asInt").getAsInt();
        
        State state = State.fmuState_cleanedup;
        state = state.getForValue(asInt);
        

        return state;

    }
    
}

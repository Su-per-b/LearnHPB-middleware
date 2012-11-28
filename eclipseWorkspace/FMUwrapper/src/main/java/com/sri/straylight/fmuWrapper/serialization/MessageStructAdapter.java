package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;



/**
 * The Class MessageStructAdapter.
 */
public class MessageStructAdapter 
	extends AdapterBase<MessageStruct> {

	final protected String[] fieldNames_ = {"msgText", "messageType"};

	public MessageStructAdapter() {
		super();
		super.setFieldNames(fieldNames_);
	}
	
	
    @Override
    public MessageStruct deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
        destObject_ = new MessageStruct();
        super.deserialize(jsonElement, typeOfT, context);

        return destObject_;

    }
    
}

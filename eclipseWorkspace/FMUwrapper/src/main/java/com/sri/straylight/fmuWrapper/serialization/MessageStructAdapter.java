package com.sri.straylight.fmuWrapper.serialization;

import com.sri.straylight.fmuWrapper.voNative.MessageStruct;



/**
 * The Class MessageStructAdapter.
 */
public class MessageStructAdapter 
	extends AdapterBase<MessageStruct> {

	final protected String[] fieldNames_ = {"msgText", "messageType"};

	public MessageStructAdapter() {
		
		super(MessageStruct.class);
		super.init(fieldNames_);
		
	}
	
	

    
}

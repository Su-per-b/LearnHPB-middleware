
package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sri.straylight.fmuWrapper.MessageStruct;



public class MessageEvent extends EventObject {


	public MessageStruct messageStruct;
	
    //here's the constructor
    public MessageEvent(Object source) {
        super(source);
        
    }
     
}



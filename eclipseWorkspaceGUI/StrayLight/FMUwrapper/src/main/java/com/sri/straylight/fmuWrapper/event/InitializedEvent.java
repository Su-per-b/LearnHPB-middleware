package com.sri.straylight.fmuWrapper.event;


import java.util.EventObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sri.straylight.fmuWrapper.InitializedStruct;
import com.sri.straylight.fmuWrapper.MessageStruct;



public class InitializedEvent extends EventObject {


	public InitializedStruct initializedStruct;
	
    //here's the constructor
    public InitializedEvent(Object source) {
        super(source);
        
    }
     
}



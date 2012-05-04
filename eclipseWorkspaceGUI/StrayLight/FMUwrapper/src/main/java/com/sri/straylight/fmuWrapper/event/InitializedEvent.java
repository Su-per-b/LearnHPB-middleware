package com.sri.straylight.fmuWrapper.event;


import java.util.EventObject;

import com.sri.straylight.fmuWrapper.InitializedStruct;



public class InitializedEvent extends EventObject {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public InitializedStruct initializedStruct;
	
    //here's the constructor
    public InitializedEvent(Object source) {
        super(source);
        
    }
     
}



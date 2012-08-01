package com.sri.straylight.fmuWrapper.event;


import java.util.EventObject;



public class InitializedEvent extends EventObject {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//public Initialized initializedStruct;
	
	
	
    //here's the constructor
    public InitializedEvent(Object source) {
        super(source);
    }
    

    

}



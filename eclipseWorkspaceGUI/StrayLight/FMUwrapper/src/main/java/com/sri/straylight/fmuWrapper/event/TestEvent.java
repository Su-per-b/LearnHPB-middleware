package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voManaged.ResultOfStep;



public class TestEvent extends EventObject {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    //here's the constructor
    public TestEvent(Object source) {
        super(source);
    }
    
    

}



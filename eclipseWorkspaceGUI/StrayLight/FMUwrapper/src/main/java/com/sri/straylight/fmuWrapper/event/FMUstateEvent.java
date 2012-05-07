package com.sri.straylight.fmuWrapper.event;



import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voNative.State;





public class FMUstateEvent extends EventObject {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public State fmuState;
	
    //here's the constructor
    public FMUstateEvent(Object source) {
        super(source);
    }
    
    

}



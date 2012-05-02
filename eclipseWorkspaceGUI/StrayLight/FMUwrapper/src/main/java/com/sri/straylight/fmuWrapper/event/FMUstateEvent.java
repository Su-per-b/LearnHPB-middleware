package com.sri.straylight.fmuWrapper.event;



import java.util.EventObject;

import com.sri.straylight.fmuWrapper.State;



public class FMUstateEvent extends EventObject {


	public State fmuState;
	
    //here's the constructor
    public FMUstateEvent(Object source) {
        super(source);
    }
    
    

}



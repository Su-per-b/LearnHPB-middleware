package com.sri.straylight.fmuWrapper;



import java.util.EventObject;



public class FMUstateEvent extends EventObject {


	public State fmuState;
	
    //here's the constructor
    public FMUstateEvent(Object source) {
        super(source);
    }
    
    

}



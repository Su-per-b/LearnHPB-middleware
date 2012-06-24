package com.sri.straylight.fmuWrapper.event;


import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;


import com.sri.straylight.fmuWrapper.FMUcontroller;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;



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



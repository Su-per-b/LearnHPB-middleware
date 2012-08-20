package com.sri.straylight.client.event;

import java.util.EventObject;



public class DataModelUpdateNotify extends EventObject  {


	private static final long serialVersionUID = 1L;
	
	
    //here's the constructor
    public DataModelUpdateNotify(Object source) {
        super(source);
    }
    


}

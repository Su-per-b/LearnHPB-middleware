
package com.sri.straylight.fmuWrapper;

import java.util.EventObject;



public class MessageEvent extends EventObject {


	public MessageStruct messageStruct;
	
    //here's the constructor
    public MessageEvent(Object source) {
        super(source);
    }
    
    

}



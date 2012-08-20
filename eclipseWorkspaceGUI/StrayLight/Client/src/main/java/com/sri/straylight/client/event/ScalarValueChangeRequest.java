package com.sri.straylight.client.event;

import java.util.EventObject;
import java.util.Vector;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

public class ScalarValueChangeRequest extends EventObject {
	
	
	
	private static final long serialVersionUID = 1L;
	
	private Vector<ScalarValueRealStruct> payload_;
    //here's the constructor
    public ScalarValueChangeRequest(Object source, Vector<ScalarValueRealStruct> scalarValueList) {
        super(source);
        
        payload_ = scalarValueList;
        
    }
    
    public Vector<ScalarValueRealStruct> getPayload() {
    	
    	return payload_;
    }
    
    
    
}

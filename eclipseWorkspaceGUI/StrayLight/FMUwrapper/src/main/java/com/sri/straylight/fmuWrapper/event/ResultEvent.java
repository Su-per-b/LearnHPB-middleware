package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.ResultItem;



public class ResultEvent extends EventObject {

	
	public ResultItem resultItem;
	public String resultString ="";
	
    //here's the constructor
    public ResultEvent(Object source) {
        super(source);
    }
    
    

}



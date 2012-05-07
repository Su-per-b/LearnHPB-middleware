package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voManaged.Result;



public class ResultEvent extends EventObject {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Result resultItem;
	public String resultString ="";
	
    //here's the constructor
    public ResultEvent(Object source) {
        super(source);
    }
    
    

}



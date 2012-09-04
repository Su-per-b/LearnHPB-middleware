package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voManaged.ResultOfStep;



// TODO: Auto-generated Javadoc
/**
 * The Class ResultEvent.
 */
public class ResultEvent extends EventObject {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The result of step. */
	public ResultOfStep resultOfStep;
	//public String resultString ="";
	
    //here's the constructor
    /**
	 * Instantiates a new result event.
	 *
	 * @param source the source
	 */
	public ResultEvent(Object source) {
        super(source);
    }
    
    

}



package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;



// TODO: Auto-generated Javadoc
/**
 * The Class ResultEvent.
 */
public class ResultEvent extends EventObject {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The result of step. */
	private ScalarValueResults scalarValueResults_;
	
    public ScalarValueResults getScalarValueResults() {
		return scalarValueResults_;
	}

    /**
	 * Instantiates a new result event.
	 *
	 * @param source the source
     * @param scalarValueResults 
	 */
	public ResultEvent(Object source) {
        super(source);
    }
	
    /**
	 * Instantiates a new result event.
	 *
	 * @param source the source
     * @param scalarValueResults 
	 */
	public ResultEvent(Object source, ScalarValueResults scalarValueResults) {
        super(source);
        
        scalarValueResults_ = scalarValueResults;
    }
    
    

}



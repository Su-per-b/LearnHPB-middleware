package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;



// TODO: Auto-generated Javadoc
/**
 * The Class ResultEvent.
 */
public class ResultEvent extends BaseEvent<ScalarValueResults> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResultEvent(Object source, ScalarValueResults payload) {
        super(source, payload);
    }
}



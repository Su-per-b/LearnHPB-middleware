package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;



// TODO: Auto-generated Javadoc
/**
 * The Class ResultEvent.
 */
public class ResultEvent extends BaseEvent<ScalarValueResults> {

	public ResultEvent(Object source, ScalarValueResults payload) {
        super(source, payload);
    }
}



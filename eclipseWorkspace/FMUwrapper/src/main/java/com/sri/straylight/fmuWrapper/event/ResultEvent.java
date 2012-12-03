package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;

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
	


	
    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        ResultEvent typedObj = (ResultEvent) obj;
        
        return new EqualsBuilder().
                append(this.payload_, typedObj.getPayload()).
                isEquals();

    }
}



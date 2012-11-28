package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(this.payload_).
            toHashCode();
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



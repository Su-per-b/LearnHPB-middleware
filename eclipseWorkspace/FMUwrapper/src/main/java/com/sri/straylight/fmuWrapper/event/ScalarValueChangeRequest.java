package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;
import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarValueChangeRequest.
 */
public class ScalarValueChangeRequest extends BaseEvent<ScalarValueCollection> {
	
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	


    public ScalarValueChangeRequest(Object source, ScalarValueCollection scalarValueList) {
        super(source);
        
        payload_ = scalarValueList;
        
    }
    
    
    
    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        ScalarValueChangeRequest typedObj = (ScalarValueChangeRequest) obj;
        
        return new EqualsBuilder().
                append(this.payload_, typedObj.getPayload()).
                isEquals();

    }
    
    
}

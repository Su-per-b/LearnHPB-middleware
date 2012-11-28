package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class SimStateNativeRequest extends BaseEvent<SimStateNative> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimStateNativeRequest(Object source, SimStateNative newState) {
        super(source, newState);
    }
	

    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;
        
        SimStateNativeRequest event;
        if (obj instanceof BaseEvent) {
        	 event = (SimStateNativeRequest) obj;
        	 
             return new EqualsBuilder().
                     append(this.payload_, event.getPayload()).
                     isEquals();
        } else {
        	return false;
        }
        

    }
    
}

package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;


public class SimStateWrapperNotify extends BaseEvent<SimStateWrapper> {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimStateWrapperNotify(Object source, SimStateWrapper newState) {
        super(source,newState);
    }
	
	
    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;
        
        SimStateWrapperNotify event;
        if (obj instanceof BaseEvent) {
        	 event = (SimStateWrapperNotify) obj;
        	 
             return new EqualsBuilder().
                     append(this.payload_, event.getPayload()).
                     isEquals();
        } else {
        	return false;
        }
        

    }
    
    
}

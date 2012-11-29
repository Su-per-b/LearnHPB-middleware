package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.bushe.swing.event.EventBus;

import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class SimStateNativeNotify extends BaseEvent<SimStateNative> {
	
	private static final long serialVersionUID = 1L;

	public SimStateNativeNotify(Object source, SimStateNative newState) {
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
        
        SimStateNativeNotify  event = (SimStateNativeNotify) obj;
        	 
         return new EqualsBuilder().
                 append(this.payload_, event.getPayload()).
                 isEquals();

        

    }


	public static void fire(Object source, SimStateNative simStateNative) {
		SimStateNativeNotify event = new SimStateNativeNotify(source,simStateNative);
		EventBus.publish(event);
	}



}

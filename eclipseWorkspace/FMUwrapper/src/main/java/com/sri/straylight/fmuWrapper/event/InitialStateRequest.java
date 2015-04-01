package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.voManaged.InitialState;



/**
 * The Class InitialStateRequest.
 */
public class InitialStateRequest extends SerializableEvent<InitialState> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitialStateRequest(Object source, InitialState payload) {
        super(source, payload, InitialStateRequest.class);
    }
	


	
    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        InitialStateRequest typedObj = (InitialStateRequest) obj;
        
        return new EqualsBuilder().
                append(this.payload_, typedObj.getPayload()).
                isEquals();

    }
}



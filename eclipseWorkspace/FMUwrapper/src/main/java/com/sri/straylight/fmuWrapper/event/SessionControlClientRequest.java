package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.voManaged.SessionControlModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigChangeNotify.
 */
public class SessionControlClientRequest extends SerializableEvent<SessionControlModel> {
	

	private static final long serialVersionUID = 1L;

	public SessionControlClientRequest(Object source, SessionControlModel payload) {
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

        SessionControlClientRequest typedObj = (SessionControlClientRequest) obj;
        
        return new EqualsBuilder().
                append(this.payload_, typedObj.getPayload()).
                isEquals();

    }
    
}

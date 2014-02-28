package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.voManaged.SessionControl;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigChangeNotify.
 */
public class SessionControlEvent extends BaseEvent<SessionControl> {
	

	private static final long serialVersionUID = 1L;

	public SessionControlEvent(Object source, SessionControl payload) {
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

        SessionControlEvent typedObj = (SessionControlEvent) obj;
        
        return new EqualsBuilder().
                append(this.payload_, typedObj.getPayload()).
                isEquals();

    }
    
}

package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;

// TODO: Auto-generated Javadoc
/**
 * An event with an Object payload.
 * <p>
 * Instantiate an event, put an optional payload into it fire it with the API of a controller.
 *
 * @param <PAYLOAD> the generic type
 * @author Christian Bauer
 */
public class BaseEvent<T> extends EventObject implements JsonSerializable {

    /** The payload. */
    private T payload_;
    

    private static final long serialVersionUID = 1L;
    

    
    public BaseEvent( Object source ) {
        super( source );
    }
    

    public BaseEvent(Object source, T payload) {
        super( source );
        this.payload_ = payload;
    }


	/**
     * Gets the payload.
     *
     * @return the payload
     */
    public T getPayload() {
        return payload_;
    }
    

    /**
     * Sets the payload.
     *
     * @param payload the new payload
     */
    public void setPayload(T payload) {
    	payload_ = payload;
    }

    
    public String toJson() {
    	return JsonController.getInstance().toJson(this);
    }

    
    
    
}

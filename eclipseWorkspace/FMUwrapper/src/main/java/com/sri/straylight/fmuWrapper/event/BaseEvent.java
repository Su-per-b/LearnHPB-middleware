package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bushe.swing.event.EventBus;

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
public class BaseEvent<P> extends EventObject implements JsonSerializable {

    /** The payload. */
    protected P payload_;
    
    protected static final long serialVersionUID = 1L;
    

    public BaseEvent( Object source ) {
        super( source );
    }
    

    public BaseEvent(Object source, P payload) {
        super( source );
        this.payload_ = payload;
    }


	/**
     * Gets the payload.
     *
     * @return the payload
     */
    public P getPayload() {
        return payload_;
    }
    

    /**
     * Sets the payload.
     *
     * @param payload the new payload
     */
    public void setPayload(P payload) {
    	payload_ = payload;
    }

    
    public String toJson() {
    	return JsonController.getInstance().toJson(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers

            append(this.payload_).
            toHashCode();
    }
    
	public void fire() {
		EventBus.publish(this);
	}
    
}

package com.sri.straylight.fmuWrapper.event;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bushe.swing.event.EventBus;

import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;

// TODO: Auto-generated Javadoc
/**
 * An event with an Object payload.
 * <p>
 * Instantiate an event, put an optional payload into it fire it with the API of a controller.
 *
 * @param <PAYLOAD> the generic type
 * @author Christian Bauer
 */
public class SerializableEvent<PAYLOAD extends Iserializable> extends BaseEvent<PAYLOAD> implements Iserializable {


    protected PAYLOAD payload_;
    
    protected static final long serialVersionUID = 1L;
    
    private Class<?> clazz_;
    
    
    /** The fired in controllers. */
    Set<AbstractController> firedInControllers = new HashSet<AbstractController>();
    
    public SerializableEvent( Object source ) {
        super( source );
    }
    

    public SerializableEvent(Object source, PAYLOAD payload) {
        this( source );
        this.payload_ = payload;
    }


	public SerializableEvent(Object source, PAYLOAD payload, Class<?> clazz) {
		this( source, payload);
		
		clazz_ = clazz;
	}
	
    public Class<?> getClazz() {
        return clazz_;
    }

	/**
     * Gets the payload.
     *
     * @return the payload
     */
    public PAYLOAD getPayload() {
        return payload_;
    }
    

    /**
     * Sets the payload.
     *
     * @param payload the new payload
     */
    public void setPayload(PAYLOAD payload) {
    	payload_ = payload;
    }

    
    public String toJsonString() {
    	return JsonController.getInstance().toJsonString(this);
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
    

    /**
     * Adds the fired in controller.
     *
     * @param seenController the seen controller
     */
	public void addFiredInController(AbstractController seenController) {
        firedInControllers.add(seenController);
    }

    /**
     * Already fired.
     *
     * @param controller the controller
     * @return true, if successful
     */
	public boolean alreadyFired(AbstractController controller) {
        return firedInControllers.contains(controller);
    }



	
}

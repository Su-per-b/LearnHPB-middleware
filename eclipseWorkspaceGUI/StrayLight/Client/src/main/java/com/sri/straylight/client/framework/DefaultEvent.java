package com.sri.straylight.client.framework;

import java.util.HashSet;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * An event with an Object payload.
 * <p>
 * Instantiate an event, put an optional payload into it fire it with the API of a controller.
 *
 * @param <PAYLOAD> the generic type
 * @author Christian Bauer
 */
public class DefaultEvent<PAYLOAD> {

    /** The payload. */
    PAYLOAD payload;
    
    /** The fired in controllers. */
    Set<AbstractController> firedInControllers = new HashSet<AbstractController>();

    /**
     * Instantiates a new default event.
     */
    public DefaultEvent() {}

    /**
     * Instantiates a new default event.
     *
     * @param payload the payload
     */
    public DefaultEvent(PAYLOAD payload) {
        this.payload = payload;
    }

    /**
     * Gets the payload.
     *
     * @return the payload
     */
    public PAYLOAD getPayload() {
        return payload;
    }

    /**
     * Sets the payload.
     *
     * @param payload the new payload
     */
    public void setPayload(PAYLOAD payload) {
        this.payload = payload;
    }

    /**
     * Adds the fired in controller.
     *
     * @param seenController the seen controller
     */
    void addFiredInController(AbstractController seenController) {
        firedInControllers.add(seenController);
    }

    /**
     * Already fired.
     *
     * @param controller the controller
     * @return true, if successful
     */
    boolean alreadyFired(AbstractController controller) {
        return firedInControllers.contains(controller);
    }
}

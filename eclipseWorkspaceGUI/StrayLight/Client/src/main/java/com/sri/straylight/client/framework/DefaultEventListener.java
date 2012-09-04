package com.sri.straylight.client.framework;

// TODO: Auto-generated Javadoc
/**
 * A listener for a particular event, needs parameterization for typesafe usage of the payload.
 *
 * @param <PAYLOAD> the generic type
 * @author Christian Bauer
 */
public interface DefaultEventListener<PAYLOAD> {
    
    /**
     * Handle event.
     *
     * @param event the event
     */
    public void handleEvent(DefaultEvent<PAYLOAD> event);
}

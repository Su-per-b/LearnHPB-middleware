package com.sri.straylight.fmuWrapper.event;




/**
 * A listener for a particular event, needs parameterization for typesafe usage of the payload.
 *
 * @param <PAYLOAD> the generic type
 * @author Christian Bauer
 * @param <PAYLOAD>
 */
public interface StraylightEventListener<EVENT extends BaseEvent<PAYLOAD>, PAYLOAD> {
    
    /**
     * Handle event.
     *
     * @param event the event
     */
    public void handleEvent(EVENT event);


	
}

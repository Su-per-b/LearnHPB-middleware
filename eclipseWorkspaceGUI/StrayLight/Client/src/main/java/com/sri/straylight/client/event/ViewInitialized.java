package com.sri.straylight.client.event;

import java.awt.Container;
import java.util.EventObject;

import com.sri.straylight.client.view.BaseView;




// TODO: Auto-generated Javadoc
/**
 * The Class SimStateRequest.
 */
public class ViewInitialized extends BaseEvent<BaseView> {

    /**
     * Instantiates a new sim state request.
     *
     * @param source the source
     * @param newState the new state
     */
    public ViewInitialized(Object source, BaseView payload) {
        super(source, payload);
    }
    
}

package com.sri.straylight.client.event;

import com.sri.straylight.client.view.BaseView;


/**
 * The Class SimStateRequest.
 */
public class ViewInitialized extends BaseEventForClient<BaseView> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

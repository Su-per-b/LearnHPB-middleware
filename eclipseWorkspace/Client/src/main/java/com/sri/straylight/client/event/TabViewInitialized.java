package com.sri.straylight.client.event;

import com.sri.straylight.client.view.BaseView;


/**
 * The Class SimStateRequest.
 */
public class TabViewInitialized extends BaseEventForClient<BaseView> {

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
    public TabViewInitialized(Object source, BaseView payload) {
        super(source, payload);
    }
    
}

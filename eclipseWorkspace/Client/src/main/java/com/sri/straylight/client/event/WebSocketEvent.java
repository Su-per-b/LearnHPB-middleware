package com.sri.straylight.client.event;

import com.sri.straylight.client.view.BaseView;


/**
 * The Class SimStateRequest.
 */
public class WebSocketEvent extends BaseEventForClient<WebSocketEventStruct> {

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
    public WebSocketEvent(Object source, WebSocketEventStruct payload) {
        super(source, payload);
    }
    
    
    
    public WebSocketEvent(
    		Object source, 
    		String eventTitle, 
    		String eventDetail, 
    		WebSocketEventType webSocketEventType) {
    	
    	super(source);

    	WebSocketEventStruct payload = new WebSocketEventStruct();
        payload.eventTitle = eventTitle;
        payload.eventDetail = eventDetail;
        payload.setMessageTypeEnum(webSocketEventType);
    	
        setPayload( payload );
    }
    
    
}

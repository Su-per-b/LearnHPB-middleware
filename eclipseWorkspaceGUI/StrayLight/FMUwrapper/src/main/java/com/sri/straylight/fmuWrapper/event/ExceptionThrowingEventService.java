package com.sri.straylight.fmuWrapper.event;

import org.bushe.swing.event.EventSubscriber;
import org.bushe.swing.event.SwingEventService;

public class ExceptionThrowingEventService extends SwingEventService {


	/** {@inheritDoc} */										
	@Override						
	protected void handleException(Object event, Throwable e, StackTraceElement[] callingStack,						
			EventSubscriber eventSubscriber) {		
		
		super.handleException(event, e, callingStack, eventSubscriber);					
		throwRuntimeException(e);					
	}						

	/** {@inheritDoc} */						
	@Override						
	protected void handleException(String action, Object event, String topic, Object eventObj, Throwable e,						
			StackTraceElement[] callingStack, String sourceString) {					
		super.handleException(action, event, topic, eventObj, e, callingStack, sourceString);					
		throwRuntimeException(e);					
	}						

	/**						
	 * Throw a runtime exception.						
	 * @param e    the exception to throw						
	 */						
	private void throwRuntimeException(Throwable e) {						
		if (e instanceof RuntimeException) {					
			throw (RuntimeException) e;				
		} else {					
			throw new RuntimeException(e);				
		}					
	}						



}

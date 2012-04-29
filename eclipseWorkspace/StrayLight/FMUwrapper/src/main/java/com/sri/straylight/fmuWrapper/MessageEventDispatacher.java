package com.sri.straylight.fmuWrapper;
import java.util.*;

public class MessageEventDispatacher {

	
	final Collection<MessageEventListener> listeners = new ArrayList<MessageEventListener>();
	
	
	  public synchronized void 
	  addListener(MessageEventListener l) {
	    listeners.add(l);
	  }

	  
	  public synchronized void 
	  removeListener(MessageEventListener l) {
	    listeners.remove(l);
	  }

	  
	  public void fireEvent(MessageEvent e) {
		  
	    for(Iterator<MessageEventListener> i = copyListeners(); i.hasNext();) {
	    	MessageEventListener l = (MessageEventListener) i.next();
	        l.onMessageEvent(e);
	      }
	    
	  }

	  
	  private synchronized Iterator<MessageEventListener> copyListeners() {
	    return new ArrayList<MessageEventListener>(listeners).iterator();
	  }    
	
}

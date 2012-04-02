package com.sri.straylight.socketserver;
import java.util.*;

public class ResultEventDispatacher {

	
	final Collection listeners = new ArrayList();
	
	
	  public synchronized void 
	  addListener(ResultEventListener l) {
	    listeners.add(l);
	  }

	  
	  public synchronized void 
	  removeListener(ResultEventListener l) {
	    listeners.remove(l);
	  }

	  
	  public void fireEvent(ResultEvent e) {
	    for(Iterator i = copyListeners(); i.hasNext();) {
	    	ResultEventListener l = (ResultEventListener) i.next();
	        l.eventUpdate(e);
	      }
	  }

	  
	  private synchronized Iterator copyListeners() {
	    return new ArrayList(listeners).iterator();
	  }    
	
}

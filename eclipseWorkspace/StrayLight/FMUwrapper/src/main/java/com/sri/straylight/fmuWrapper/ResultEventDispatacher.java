package com.sri.straylight.fmuWrapper;
import java.util.*;

public class ResultEventDispatacher {

	
	final Collection<EventListener> listeners = new ArrayList<EventListener>();
	
	
	  public synchronized void 
	  addListener(ResultEventListener l) {
	    listeners.add(l);
	  }

	  
	  public synchronized void 
	  removeListener(ResultEventListener l) {
	    listeners.remove(l);
	  }

	  
	  public void fireEvent(ResultEvent e) {
	    for(Iterator<EventListener> i = copyListeners(); i.hasNext();) {
	    	ResultEventListener l = (ResultEventListener) i.next();
	        l.onResultEvent(e);
	      }
	  }

	  
	  private synchronized Iterator<EventListener> copyListeners() {
	    return new ArrayList<EventListener>(listeners).iterator();
	  }    
	
}

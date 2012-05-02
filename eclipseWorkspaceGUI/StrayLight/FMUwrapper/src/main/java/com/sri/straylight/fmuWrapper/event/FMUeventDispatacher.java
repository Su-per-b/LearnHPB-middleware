package com.sri.straylight.fmuWrapper.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;




public class FMUeventDispatacher {

	
	final Collection<FMUeventListener> listeners = new ArrayList<FMUeventListener>();
	
	
	  public synchronized void 
	  addListener(FMUeventListener l) {
	    listeners.add(l);
	  }

	  
	  public synchronized void 
	  removeListener(FMUeventListener l) {
	    listeners.remove(l);
	  }

	  
	  public void fireMessageEvent(MessageEvent e) {
		for(Iterator<FMUeventListener> i = copyListeners(); i.hasNext();) {
			FMUeventListener l = (FMUeventListener) i.next();
		    l.onMessageEvent(e);
		  } 
	  }

	  public void fireResultEvent(ResultEvent e) {
	    for(Iterator<FMUeventListener> i = copyListeners(); i.hasNext();) {
	    	FMUeventListener l = (FMUeventListener) i.next();
	        l.onResultEvent(e);
	      } 
	  }
	  
	  public void fireStateEvent(FMUstateEvent e) {
	    for(Iterator<FMUeventListener> i = copyListeners(); i.hasNext();) {
	    	FMUeventListener l = (FMUeventListener) i.next();
	        l.onFMUstateEvent(e);
	      } 
	  }
	  
	  private synchronized Iterator<FMUeventListener> copyListeners() {
	    return new ArrayList<FMUeventListener>(listeners).iterator();
	  }


	public void fireInitializedEvent(InitializedEvent e) {
	    for(Iterator<FMUeventListener> i = copyListeners(); i.hasNext();) {
	    	FMUeventListener l = (FMUeventListener) i.next();
	        l.onInitializedEvent(e);
	      } 
		
	}    
	
}

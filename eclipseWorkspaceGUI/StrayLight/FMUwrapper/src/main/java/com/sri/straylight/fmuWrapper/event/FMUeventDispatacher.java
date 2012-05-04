package com.sri.straylight.fmuWrapper.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
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

	public void fireEvent(EventObject e) {

		Class c = e.getClass();
		if (c == MessageEvent.class) {
			fireEvent((MessageEvent) e);
		} else if(c == ResultEvent.class) {
			fireEvent((ResultEvent) e);
		} else if(c == FMUstateEvent.class) {
			fireEvent((FMUstateEvent) e);
		} else if(c == InitializedEvent.class) {
			fireEvent((InitializedEvent) e);
		}


	}

	public void fireEvent(MessageEvent e) {
		for(Iterator<FMUeventListener> i = copyListeners(); i.hasNext();) {
			FMUeventListener l = (FMUeventListener) i.next();
			l.onMessageEvent(e);
		} 
	}

	public void fireEvent(ResultEvent e) {
		for(Iterator<FMUeventListener> i = copyListeners(); i.hasNext();) {
			FMUeventListener l = (FMUeventListener) i.next();
			l.onResultEvent(e);
		} 
	}

	public void fireEvent(FMUstateEvent e) {
		for(Iterator<FMUeventListener> i = copyListeners(); i.hasNext();) {
			FMUeventListener l = (FMUeventListener) i.next();
			l.onFMUstateEvent(e);
		} 
	}


	public void fireEvent(InitializedEvent e) {
		for(Iterator<FMUeventListener> i = copyListeners(); i.hasNext();) {
			FMUeventListener l = (FMUeventListener) i.next();
			l.onInitializedEvent(e);
		} 

	}

	private synchronized Iterator<FMUeventListener> copyListeners() {
		return new ArrayList<FMUeventListener>(listeners).iterator();
	}

}

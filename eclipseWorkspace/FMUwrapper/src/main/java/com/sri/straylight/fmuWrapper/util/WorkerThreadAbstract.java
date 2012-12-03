package com.sri.straylight.fmuWrapper.util;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

/**
 * Base worker for calling the FmuController
 */
public abstract class WorkerThreadAbstract extends SwingWorker<Void, Void> {
	
	private Object syncObect_;
	
	public void setSyncObject(Object syncObect) {
		syncObect_ = syncObect;
	}
	
	@Override
	public Void doInBackground() {
		// this synchronized block forces the actions to complete before
		// another thread
		// (which is synced to the same object) can be interleaved
		
		if (syncObect_ != null) {
			synchronized (syncObect_) {
				doIt_();
			}
		} else {
			doIt_();
		}
		

		return null;
	}


	@Override
	public void done() {

		// Calling the get() method inside the done helps get back any
		// exceptions that were thrown during the doInBackground
		try {
			super.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		doneIt_();

	}
	
	
	protected void setName_(String name) {
		Thread.currentThread().setName(name);
	}
	
	//for subclass to override
	protected abstract void doIt_();
	protected abstract void doneIt_();
}
package com.sri.straylight.client.controller;


import java.io.IOException;
import java.util.EventObject;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.FMUcontroller;
import com.sri.straylight.fmuWrapper.event.ConfigChangeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


// TODO: Auto-generated Javadoc
/**
 * The Class FmuConnectLocal.
 */
public class FmuConnectLocal implements  IFmuConnect {

	/** The fmu_. */
	private FMUcontroller fmu_;
	
	/** The task xm lconnect_. */
	private TaskConnect taskConnect_;
	
	/** The task xm lparse_. */
	private TaskXMLparse taskXMLparse_;
	
	
	/** The task run_. */
	private TaskRun taskRun_;
	
	/** The task request state change_. */
	private TaskRequestStateChange taskRequestStateChange_;
	
	/** The task change input_. */
	private TaskChangeInput taskChangeInput_;
	
	/** The task change scalar values_. */
	private TaskChangeScalarValues taskChangeScalarValues_;
	
	/** The change input sync_. */
	private Object changeInputSync_ = new Object();
	
	/** The request state change sync_. */
	private Object requestStateChangeSync_ = new Object();
	
	/**
	 * Instantiates a new fmu connect local.
	 */
	public FmuConnectLocal() {
		AnnotationProcessor.process(this);
	}
	

	public void changeInput(int idx, double value) {
		
		synchronized(changeInputSync_) { 
			taskChangeInput_ = new TaskChangeInput();
			taskChangeInput_.setChange(idx, value);
			taskChangeInput_.execute();
		}
	}


	public void changeScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {
		
		synchronized(changeInputSync_) { 
			taskChangeScalarValues_ = new TaskChangeScalarValues();
			taskChangeScalarValues_.setScalarValues(scalarValueList);
			taskChangeScalarValues_.execute();
		}
	}
	

	public void connect() {
		taskConnect_ = new TaskConnect();
		taskConnect_.execute();
	}


	public void xmlParse() {
		taskXMLparse_ = new TaskXMLparse();
		taskXMLparse_.execute();
	}

	public void run() {
		taskRun_ = new TaskRun();
		taskRun_.execute();
	}
	
	
	
	public void requestStateChange(SimStateNative newState) {

		synchronized(requestStateChangeSync_) { 
			taskRequestStateChange_ = new TaskRequestStateChange();
			taskRequestStateChange_.setState(newState); 
			taskRequestStateChange_.execute();
		}
	}
	

	public void setConfig(ConfigStruct configStruct) {
		fmu_.setConfig(configStruct);
	}


	
	//event handlers
	@EventSubscriber(eventClass=ConfigChangeRequest.class)
	public void onConfigChangeRequest(ConfigChangeRequest event) {
	
		ConfigStruct configStruct = event.getPayload();
	
		fmu_.setConfig(configStruct);
		
		//fmu_.inputChange(event.idx, event.value);
	}
	

	@EventSubscriber(eventClass=SimStateNativeNotify.class)
	public void onSimStateNotify(SimStateNativeNotify event) {

		SimStateClientNotify event2 = new SimStateClientNotify(this, event.getPayload());
		EventBus.publish(event2);

	}
	
	



	/**
	 * The Class TaskXMLparse.
	 */
	private class TaskXMLparse extends SwingWorker<Void, EventObject>   
	{
		 
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			
            // TODO enable button, change text here
			try {
				super.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
            synchronized(MainController.instance) {
            	taskXMLparse_ = null;

            }
          }
		


		

		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground()
		{
			fmu_.xmlParse();
			return null;
		}






	}

	/**
	 * The Class TaskConnect.
	 */
	private class TaskConnect extends SwingWorker<Void, Void>   
	{

		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground()
		{
			fmu_ = new FMUcontroller();
			
			try
			{
				fmu_.connect();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			
			return null;
		}
		
		
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
            synchronized(MainController.instance) {
            	taskConnect_ = null;
            }
          }
	}


	/**
	 * The Class TaskRun.
	 */
	private class TaskRun extends SwingWorker<Void, EventObject>
	{

		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground()
		{
			fmu_.run();
			return null;

		}
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			
            // TODO enable button, change text here
			try {
				super.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
            synchronized(MainController.instance) {
            	taskRun_ = null;
            }
          }

	}
	
	
	/**
	 * The Class TaskChangeScalarValues.
	 */
	private class TaskChangeScalarValues extends SwingWorker<Void, EventObject>
	{
		

		/** The scalar value list_. */
		private Vector<ScalarValueRealStruct> scalarValueList_;
		
		
		/**
		 * Sets the scalar values.
		 *
		 * @param scalarValueList the new scalar values
		 */
		public void setScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {
		
			scalarValueList_ = scalarValueList;
		}
		
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground()
		{
			fmu_.setScalarValues(scalarValueList_);
			return null;
			
		}
		
		
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			
			// TODO enable button, change text here
			try {
				super.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			synchronized(MainController.instance) {
				taskChangeInput_ = null;
            	//System.out.println("execute done");
			}
		}
		
	}
	
	
	/**
	 * The Class TaskChangeInput.
	 */
	private class TaskChangeInput extends SwingWorker<Void, EventObject>
	{
		
		/** The idx_. */
		private int idx_;
		
		/** The value_. */
		private double value_;
		
		/**
		 * Sets the change.
		 *
		 * @param idx the idx
		 * @param value the value
		 */
		public void setChange(int idx, double value) {
			idx_ = idx;
			value_ = value;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground()
		{
			fmu_.setScalarValueReal(idx_, value_);
			return null;
			
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			
			// TODO enable button, change text here
			try {
				super.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			synchronized(MainController.instance) {
				taskChangeInput_ = null;
            	//System.out.println("execute done");
			}
		}
		
	}

	/**
	 * The Class TaskRequestStateChange.
	 */
	private class TaskRequestStateChange extends SwingWorker<Void, EventObject>
	{

		/** The new state_. */
		private SimStateNative newState_;
		
		/**
		 * Sets the state.
		 *
		 * @param newState the new state
		 */
		public void setState(SimStateNative newState) {
			newState_ = newState;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground()
		{
			fmu_.requestStateChange(newState_);
			return null;

		}
		
		/* (non-Javadoc)
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			
            // TODO enable button, change text here
			try {
				super.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
            synchronized(MainController.instance) {
            	taskRequestStateChange_ = null;
            }
          }

	}
	
	
}

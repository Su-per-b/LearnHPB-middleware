package com.sri.straylight.socketserver;


import java.awt.EventQueue;
import java.io.IOException;
import java.util.EventObject;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.FMUcontroller;
import com.sri.straylight.fmuWrapper.event.ConfigChangeRequest;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.socketserver.controller.MainController;


// TODO: Auto-generated Javadoc
/**
 * The Class FmuConnectLocal.
 */
public class FmuConnectLocal    {

	private FMUcontroller fmu_;
	private TaskRun taskRun_;
	private TaskRequestStateChange taskRequestStateChange_;
	private TaskChangeInput taskChangeInput_;
	private TaskChangeScalarValues taskChangeScalarValues_;
	
	private Object changeInputSync_ = new Object();
	private Object requestStateChangeSync_ = new Object();
	
	
	/**
	 * Instantiates a new fmu connect local.
	 */
	public FmuConnectLocal() {
		AnnotationProcessor.process(this);
	}
	
	public FMUcontroller getFmu() {
		
		return fmu_;
	}
	

	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#changeInput(int, double)
	 */
	public void changeInput(int idx, double value) {
		
		synchronized(changeInputSync_) { 
			taskChangeInput_ = new TaskChangeInput();
			taskChangeInput_.setChange(idx, value);
			taskChangeInput_.execute();
		}
	}

	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#changeScalarValues(java.util.Vector)
	 */
	public void changeScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {
		
		synchronized(changeInputSync_) { 
			taskChangeScalarValues_ = new TaskChangeScalarValues();
			taskChangeScalarValues_.setScalarValues(scalarValueList);
			taskChangeScalarValues_.execute();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#connect()
	 */
	public void connect() {
		
		
		EventQueue.invokeLater(new Runnable() { 
			  @Override
			  public void run() {
					fmu_ = new FMUcontroller();
					
					try
					{
						fmu_.connect();
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
			  }
			});
		
	}

	

	
	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#xmlParse()
	 */
	public void xmlParse() {
		
		EventQueue.invokeLater(new Runnable() { 
			  @Override
			  public void run() {
					fmu_.xmlParse();
			  }
			});
	}



	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#run()
	 */
	public void run() {
		taskRun_ = new TaskRun();
		taskRun_.execute();
	}
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#requestStateChange(com.sri.straylight.fmuWrapper.voNative.SimStateNative)
	 */
	public void requestStateChange(SimStateNative newState) {

		synchronized(requestStateChangeSync_) { 
			taskRequestStateChange_ = new TaskRequestStateChange();
			taskRequestStateChange_.setState(newState); 
			taskRequestStateChange_.execute();
		}
	}
	

	/**
	 * Resume.
	 */
	public void resume() {
		taskRequestStateChange_ = new TaskRequestStateChange();
		taskRequestStateChange_.setState(SimStateNative.simStateNative_7_resume_requested); 
		taskRequestStateChange_.execute();
	}

	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#setConfig(com.sri.straylight.fmuWrapper.voNative.ConfigStruct)
	 */
	public void setConfig(ConfigStruct configStruct) {
		fmu_.setConfig(configStruct);
	}


	
	/**
 * On config change request.
 *
 * @param event the event
 */
@EventSubscriber(eventClass=ConfigChangeRequest.class)
	public void onConfigChangeRequest(ConfigChangeRequest event) {
		fmu_.setConfig(event.getPayload());
		//fmu_.inputChange(event.idx, event.value);
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

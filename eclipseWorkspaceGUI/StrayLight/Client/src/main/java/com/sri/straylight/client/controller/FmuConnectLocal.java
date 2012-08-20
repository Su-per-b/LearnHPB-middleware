package com.sri.straylight.client.controller;


import java.util.EventObject;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.model.SimStateClient;
import com.sri.straylight.fmuWrapper.FMUcontroller;
import com.sri.straylight.fmuWrapper.event.ConfigChangeRequest;
import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


public class FmuConnectLocal implements  IFmuConnect {

	private FMUcontroller fmu_;
	private TaskConnect taskXMLconnect_;
	private TaskXMLparse taskXMLparse_;
	private TaskInit taskInit_;
	private TaskRun taskRun_;
	private TaskRequestStateChange taskRequestStateChange_;
	private TaskChangeInput taskChangeInput_;
	
	private TaskChangeScalarValues taskChangeScalarValues_;
	
	private Object changeInputSync_ = new Object();
	private Object requestStateChangeSync_ = new Object();
	
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
		taskXMLconnect_ = new TaskConnect();
		taskXMLconnect_.execute();
	}


	public void xmlParse() {
		taskXMLparse_ = new TaskXMLparse();
		taskXMLparse_.execute();
	}

	public void init() {
		taskInit_ = new TaskInit();
		taskInit_.execute();
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
	

	public void resume() {
		taskRequestStateChange_ = new TaskRequestStateChange();
		taskRequestStateChange_.setState(SimStateNative.simStateNative_7_resume_requested); 
		taskRequestStateChange_.execute();
	}

	public void setConfig(ConfigStruct configStruct) {
		fmu_.setConfig(configStruct);
	}

//
//	public String[] getOutputVariableNames() {
//
//		ScalarVariableRealStruct[] svAry = fmu_.getScalarVariableOutputAry();
//		int len = svAry.length;
//		String[] strAry = new String [len];
//
//		for (int i = 0; i < len; i++) {
//			strAry[i] = svAry[i].name;
//		}
//
//		return strAry;
//
//	}
//	
	
	@EventSubscriber(eventClass=ConfigChangeRequest.class)
	public void onConfigChangeRequest(ConfigChangeRequest event) {
		fmu_.setConfig(event.payload);
		//fmu_.inputChange(event.idx, event.value);
	}
	


	
	
	@EventSubscriber(eventClass=com.sri.straylight.fmuWrapper.event.SimStateServerNotify.class)
	public void onSimStateNotify(com.sri.straylight.fmuWrapper.event.SimStateServerNotify event) {

		SimStateServer serverState = event.getPayload();
		SimStateClient clientState;

		switch (serverState) {
		case simStateServer_1_connect_completed:
			clientState = SimStateClient.level_1_connect_completed;
			break;
		case simStateServer_2_xmlParse_completed:
			clientState = SimStateClient.level_2_xmlParse_completed;
			break;
		case simStateServer_3_ready:
			clientState = SimStateClient.level_3_ready;
			break;
		case simStateServer_4_run_completed:
			clientState = SimStateClient.level_4_run_completed;
			break;
		case simStateServer_4_run_started:
			clientState = SimStateClient.level_4_run_started;
			break;
		case simStateServer_6_reset_completed:
			clientState = SimStateClient.level_6_reset_completed;
			break;
		default:
			clientState = SimStateClient.level_e_error;
			break;
		}

		
		com.sri.straylight.client.event.SimStateNotify event2 = 
				new com.sri.straylight.client.event.SimStateNotify(this, clientState);


		EventBus.publish(event2);

	}
	
	



	private class TaskXMLparse extends SwingWorker<Void, EventObject>   
	{
		 
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
		


		

		@Override
		public Void doInBackground()
		{
			fmu_.xmlParse();
			return null;
		}






	}

	private class TaskConnect extends SwingWorker<Void, EventObject>   
	{

		@Override
		public Void doInBackground()
		{
			fmu_ = new FMUcontroller();
			fmu_.connect();
			return null;
		}
		
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
            	taskXMLconnect_ = null;
            }
          }
	}
	
	private class TaskInit extends SwingWorker<Void, EventObject>   
	{

		@Override
		public Void doInBackground()
		{
			fmu_.init();
			return null;

		}
		
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
            	taskInit_ = null;
            }
          }
	}


	private class TaskRun extends SwingWorker<Void, EventObject>
	{

		@Override
		public Void doInBackground()
		{
			fmu_.run();
			return null;

		}
		
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
	
	
	private class TaskChangeScalarValues extends SwingWorker<Void, EventObject>
	{
		

		private Vector<ScalarValueRealStruct> scalarValueList_;
		
		
		public void setScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {
		
			scalarValueList_ = scalarValueList;
		}
		
		
		@Override
		public Void doInBackground()
		{
			fmu_.setScalarValues(scalarValueList_);
			return null;
			
		}
		
		
		
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
	
	
	private class TaskChangeInput extends SwingWorker<Void, EventObject>
	{
		
		private int idx_;
		private double value_;
		
		public void setChange(int idx, double value) {
			idx_ = idx;
			value_ = value;
		}
		
		@Override
		public Void doInBackground()
		{
			fmu_.setScalarValueReal(idx_, value_);
			return null;
			
		}
		
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

	private class TaskRequestStateChange extends SwingWorker<Void, EventObject>
	{

		private SimStateNative newState_;
		
		public void setState(SimStateNative newState) {
			newState_ = newState;
		}
		
		@Override
		public Void doInBackground()
		{
			fmu_.requestStateChange(newState_);
			return null;

		}
		
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

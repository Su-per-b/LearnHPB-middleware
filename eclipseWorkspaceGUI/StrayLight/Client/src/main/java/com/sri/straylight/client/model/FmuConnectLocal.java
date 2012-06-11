package com.sri.straylight.client.model;


import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingWorker;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.fmuWrapper.FMU;
import com.sri.straylight.fmuWrapper.event.FMUeventDispatacher;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;


public class FmuConnectLocal implements  IFmuConnect {

	private FMU fmu_;
	public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
	public static String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
	public static String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";


	private FMUeventDispatacher fmuEventDispatacher_;
	private TaskInit taskInit;

	public FmuConnectLocal() {

	}

	public void init(FMUeventListener l) {

		fmuEventDispatacher_ = new FMUeventDispatacher();
		fmuEventDispatacher_.addListener(l);

		taskInit = new TaskInit();

		taskInit.execute();
	}
	
	public void init() {


		taskInit = new TaskInit();

		taskInit.execute();
	}
	
	public void run() {
		TaskRun taskRun = new TaskRun();
		taskRun.execute();
	}







	public String[] getOutputVariableNames() {

		ArrayList<String> strList = new ArrayList<String>();

		ArrayList<ScalarVariableStruct> svnList = fmu_.getScalarVariableOutputList();
		Iterator<ScalarVariableStruct> itr = svnList.iterator();

		while (itr.hasNext()) {
			ScalarVariableStruct svm = itr.next();
			strList.add(svm.name);
		}


		String[] strAry = strList.toArray(new String[strList.size()]);
		return strAry;


	}

	private class TaskInit extends SwingWorker<Void, EventObject> implements FMUeventListener 
	{


		
		public void onResultEvent(ResultEvent event) {
			publish(event);
			//System.out.printf("TaskInit.onMessageEvent Thread:%s \n", Thread.currentThread().getName());
		}

		public void onMessageEvent(MessageEvent event) {
			publish(event);
			//System.out.printf("TaskInit.onMessageEvent Thread:%s \n", Thread.currentThread().getName());
		}

		public void onFMUstateEvent(FMUstateEvent event) {
			publish(event);
			System.out.printf("TaskInit.onMessageEvent Thread:%s \n", Thread.currentThread().getName());
		}

		public void onInitializedEvent(InitializedEvent event) {
			publish(event);
			//System.out.printf("TaskInit.onMessageEvent Thread:%s \n", Thread.currentThread().getName());
		}
		


		@Override
		protected void process(List<EventObject> eventList) {
			//System.out.printf("FmuConnectLocal.onMessageEvent Thread:%s \n", Thread.currentThread().getName());

			for (Iterator<EventObject> iterator = eventList.iterator(); iterator
					.hasNext();) {
				EventObject event = (EventObject) iterator.next();
				
				
				if (fmuEventDispatacher_ == null) {
					EventBus.publish(event);
				} else {
					fmuEventDispatacher_.fireEvent(event);
				}
				
				
				
			}
		}


		@Override
		public Void doInBackground()
		{
			fmu_ = new FMU(testFmuFile, nativeLibFolder);
			fmu_.fmuEventDispatacher.addListener(this);

			fmu_.init_1();
			fmu_.init_2(unzipFolder);
			fmu_.init_3();

			return null;

		}
	}

	private class TaskRun extends SwingWorker<Void, Void>
	{
		public Void doInBackground()
		{
			fmu_.run();
			return null;

		}
	}


}

package com.sri.straylight.SwingGUI;


import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.SwingWorker;

import com.sri.straylight.fmuWrapper.FMU;
import com.sri.straylight.fmuWrapper.ScalarVariableMeta;
import com.sri.straylight.fmuWrapper.event.FMUeventDispatacher;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;


public class FmuConnectLocal implements FMUeventListener, IFmuConnect {
	
    private FMU fmu_;
    public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
    public static String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
    public static String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";
    
    
	private FMUeventDispatacher fmuEventDispatacher_;
	
    public FmuConnectLocal() {
    	fmuEventDispatacher_ = new FMUeventDispatacher();
    }
    
    public void init() {
		fmu_ = new FMU(testFmuFile, nativeLibFolder);
		fmu_.fmuEventDispatacher.addListener(this);

		TaskInit taskInit = new TaskInit();
		taskInit.execute();
    }
    
    
    public void run() {
		TaskRun taskRun = new TaskRun();
		taskRun.execute();
    }

    public void onResultEvent(ResultEvent event) {
    	fmuEventDispatacher_.fireResultEvent(event);
    }
   
    public void onMessageEvent(MessageEvent event) {
    	fmuEventDispatacher_.fireMessageEvent(event);
    }
    
    public void onFMUstateEvent(FMUstateEvent event) {
    	fmuEventDispatacher_.fireStateEvent(event);
    }
    
    public void onInitializedEvent(InitializedEvent event) {
    	fmuEventDispatacher_.fireInitializedEvent(event);
    }
    
    
    public void addListener(FMUeventListener l) {
    	fmuEventDispatacher_.addListener(l);
    }
    
    
    public String[] getOutputVariableNames() {
    	 
    	ArrayList<String> strList = new ArrayList<String>();
    	
    	ArrayList<ScalarVariableMeta> svnList = fmu_.getScalarVariableOutputList();
        Iterator<ScalarVariableMeta> itr = svnList.iterator();
        
        while (itr.hasNext()) {
        	ScalarVariableMeta svm = itr.next();
        	strList.add(svm.name);
        }
        
        
        String[] strAry = strList.toArray(new String[strList.size()]);
        return strAry;
    	
    	
    }
    
    private class TaskInit extends SwingWorker<Void, Void>
    {
        public Void doInBackground()
        {
            	
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
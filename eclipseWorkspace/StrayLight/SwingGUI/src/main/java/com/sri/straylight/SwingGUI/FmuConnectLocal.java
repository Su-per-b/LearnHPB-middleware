package com.sri.straylight.SwingGUI;


import javax.swing.SwingWorker;

import com.sri.straylight.fmuWrapper.*;

public class FmuConnectLocal implements ResultEventListener, MessageEventListener  {
	
    private FMU fmu_;
    public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
    public static String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
    public static String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";
    
    
    public ResultEventDispatacher resultEventDispatacher;
	public MessageEventDispatacher messageEventDispatacher;
	
    public FmuConnectLocal() {
        
    	resultEventDispatacher = new ResultEventDispatacher();
    	messageEventDispatacher = new MessageEventDispatacher();
    }
    
    public void run() {
        
    	
		fmu_ = new FMU(testFmuFile, nativeLibFolder);
		fmu_.resultEventDispatacher.addListener(this);
		fmu_.messageEventDispatacher.addListener(this);
		
		
        Task task = new Task();
        task.execute();
    }
    
    
    public void onResultEvent(ResultEvent event) {
    	resultEventDispatacher.fireEvent(event);
    }
   
    public void onMessageEvent(MessageEvent event) {
    	messageEventDispatacher.fireEvent(event);
    }
    
    
    private class Task extends SwingWorker<Void, Void>
    {
        public Void doInBackground()
        {
            	
    		fmu_.init(unzipFolder);
    		fmu_.run();

            return null;
            
        }
    }
    
}

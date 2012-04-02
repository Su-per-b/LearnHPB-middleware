package com.sri.straylight.socketserver;


import com.sri.straylight.common.Unzip;
import java.io.*;

public class FMU implements ResultEventListener{

	private String fmuFilePath_;
	private String fmuFileName_;
	
	private String unzipfolder_;
	private String dllPath_;

	
	private ResultEventDispatacher disp;
	
		
	public FMU(String fmuFileName) {
		
		fmuFilePath_ = fmuFileName;
		disp = new ResultEventDispatacher();
    	
    	disp.addListener(this);
    	
	}
	
	public void unzip() {
		
		String name = new File(fmuFilePath_).getName();
		name = name.substring(0, name.length()-4); 
		
		//dllPath_ = unzipfolder_ + File.separator   + 
			//	"binaries" + File.separator + "win32" + 
			//	File.separator + name + ".dll";
 				
		unzipfolder_ = Main.config.unzipFolderBase + File.separator + name;
		Unzip.unzip(fmuFilePath_, unzipfolder_);
		
	}
	
	
	public void eventUpdate(ResultEvent re) {
		
    	System.out.println("eventUpdate - " + re.resultString);
	}

	
	public void init() {
    	JNAfmuWrapper.INSTANCE.initAll();
	}
	
	public void run() {

    	while(JNAfmuWrapper.INSTANCE.isSimulationComplete() == 0) {
    		
        	String str = JNAfmuWrapper.INSTANCE.getStringXy();

        	ResultEvent re = new ResultEvent(this);
        	re.resultString = str;
        	
        	disp.fireEvent(re);

    	}
    	
    	JNAfmuWrapper.INSTANCE.end();
		
	}
	
	

	
	
	
}

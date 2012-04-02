

package com.sri.straylight.socketserver;


import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;



public interface JNAfmuWrapper extends Library {
	
	String result = System.setProperty("jna.library.path", "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug");
	JNAfmuWrapper  INSTANCE = (JNAfmuWrapper ) Native.loadLibrary("FMUwrapper", JNAfmuWrapper.class);
	
	void testFMU();
	
	int testFMU2();
	
	void initAll();
	
	//void init2();
	
	String getStringXy();
	
	int isSimulationComplete();
	
	void end();
	
	
}
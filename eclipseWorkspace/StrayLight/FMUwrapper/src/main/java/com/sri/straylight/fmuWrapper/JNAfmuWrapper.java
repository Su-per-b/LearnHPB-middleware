

package com.sri.straylight.fmuWrapper;


import com.sun.jna.Callback;
import com.sun.jna.Library;



public interface JNAfmuWrapper extends Library {
	
	
	int doOneStep();
	
	void end();
	
	ResultItemStruct getResultStruct();
	
	int getVariableCount();
	
	ScalarVariableMeta getSVmetaData();
	
	void init(String unzipPath);
	
	int isSimulationComplete();
	
	
	public int registerMessageCallback (MessageCallbackInterface messageCallback);
	
	public int registerResultCallback (ResultCallbackInterface resultCallback);
	
	public int run(); 
	
	
	

	

	
	public interface MessageCallbackInterface extends Callback {
		public boolean messageCallback(MessageStruct messageStruct);
	    
	 }
	
	public interface ResultCallbackInterface extends Callback {
		public boolean resultCallback(ResultItemStruct resultItemStruct);
	    
	 }
	
}






package com.sri.straylight.fmuWrapper;


import com.sun.jna.Callback;
import com.sun.jna.Library;



public interface JNAfmuWrapper extends Library {
	
	
	int doOneStep();
	
	void end();
	
	ResultItemStruct getResultStruct();
	
	int getVariableCount();
	
	ScalarVariableMeta getSVmetaData();
	
	void init(String unzipFolder);
	
	
	void init_1(
			MessageCallbackInterface messageCallback,
			ResultCallbackInterface resultCallback,
			StateChangeCallbackInterface stateChangeCallback
			);
	
	void init_2(String unzipFolder);
	
	void init_3();
	
	int isSimulationComplete();
	
	public int run(); 
	
	
	
	public interface MessageCallbackInterface extends Callback {
		public boolean messageCallback(MessageStruct messageStruct);
	 }
	
	public interface ResultCallbackInterface extends Callback {
		public boolean resultCallback(ResultItemStruct resultItemStruct);
	 }
	
	public interface StateChangeCallbackInterface extends Callback {
		public boolean stateChangeCallback(State fmuState);
	 }
}






package com.sri.straylight.fmuWrapper;



import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ResultStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;
import com.sri.straylight.fmuWrapper.voNative.State;
import com.sun.jna.Callback;
import com.sun.jna.Library;



public interface JNAfmuWrapper extends Library {
	
	
	int doOneStep();
	
	void end();
	
	ResultStruct getResultStruct();
	
	int getVariableCount();
	
	int forceCleanup();
	
	ScalarVariableStruct getSVmetaData();
	
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
		public boolean resultCallback(ResultStruct resultItemStruct);
	 }
	
	public interface StateChangeCallbackInterface extends Callback {
		public boolean stateChangeCallback(State fmuState);
	 }

	void deleteMessageStruct(MessageStruct messageStruct);
}




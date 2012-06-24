

package com.sri.straylight.fmuWrapper;



import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ResultOfStepStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.fmiStatus;
import com.sun.jna.Callback;
import com.sun.jna.Library;



public interface JNAfmuWrapper extends Library {
	
	
	public int doOneStep();
	
	//public void cleanup();
	
	public ResultOfStepStruct getResultStruct();
	
	public int forceCleanup();
	
	public ScalarVariableRealStruct getScalarVariableInputStructs();
	public ScalarVariableRealStruct getScalarVariableOutputStructs();
	public ScalarVariableStruct getScalarVariableInternalStructs();
	
	public int getInputVariableCount();
	public int getOutputVariableCount();
	public int getInternalVariableCount();
	
	
	public void connect (
			MessageCallbackInterface messageCallback,
			ResultCallbackInterface resultCallback,
			StateChangeCallbackInterface stateChangeCallback
			);
	
	public void xmlParse(String unzipFolder);
	
	public void init();
	
	public int isSimulationComplete();
	
	public int run(); 
	
	public void requestStateChange(SimStateNative newState); 
	
	public void setConfig(ConfigStruct configStruct);
	
	public ConfigStruct getConfig();
	
	
	public interface MessageCallbackInterface extends Callback {
		public boolean messageCallback(MessageStruct messageStruct);
	 }
	
	public interface ResultCallbackInterface extends Callback {
		public boolean resultCallback(ResultOfStepStruct resultOfStepStruct);
	 }
	
	public interface StateChangeCallbackInterface extends Callback {
		public boolean stateChangeCallback(SimStateNative fmuState);
	 }

	//public void deleteMessageStruct(MessageStruct messageStruct);



	public fmiStatus changeInput(int idx, double value);


}




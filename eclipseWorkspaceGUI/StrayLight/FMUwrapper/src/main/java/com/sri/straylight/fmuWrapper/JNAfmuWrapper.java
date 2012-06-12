

package com.sri.straylight.fmuWrapper;



import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MetaDataStruct;
import com.sri.straylight.fmuWrapper.voNative.ResultStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;
import com.sri.straylight.fmuWrapper.voNative.State;
import com.sun.jna.Callback;
import com.sun.jna.Library;



public interface JNAfmuWrapper extends Library {
	
	
	public int doOneStep();
	
	public void end();
	
	public ResultStruct getResultStruct();
	
	public int getVariableCount();
	
	public int forceCleanup();
	
	public ScalarVariableStruct getScalarVariableStructs();

	
	public void init(String unzipFolder);
	
	
	public void initCallbacks(
			MessageCallbackInterface messageCallback,
			ResultCallbackInterface resultCallback,
			StateChangeCallbackInterface stateChangeCallback
			);
	
	public void initXML(String unzipFolder);
	
	public void initSimulation();
	
	public int isSimulationComplete();
	
	public int run(); 
	
	public void setMetaData(MetaDataStruct metaDataStruct);
	
	public MetaDataStruct getMetaData();
	
	
	public interface MessageCallbackInterface extends Callback {
		public boolean messageCallback(MessageStruct messageStruct);
	 }
	
	public interface ResultCallbackInterface extends Callback {
		public boolean resultCallback(ResultStruct resultItemStruct);
	 }
	
	public interface StateChangeCallbackInterface extends Callback {
		public boolean stateChangeCallback(State fmuState);
	 }

	public void deleteMessageStruct(MessageStruct messageStruct);


}




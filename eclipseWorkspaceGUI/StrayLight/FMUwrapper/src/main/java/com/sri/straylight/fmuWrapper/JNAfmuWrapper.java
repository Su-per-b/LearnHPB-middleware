

package com.sri.straylight.fmuWrapper;


import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ResultOfStepStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.fmiStatus;
import com.sun.jna.Callback;
import com.sun.jna.Library;



// TODO: Auto-generated Javadoc
/**
 * The Interface JNAfmuWrapper.
 */
public interface JNAfmuWrapper extends Library {
	
	
	/**
	 * Do one step.
	 *
	 * @return the int
	 */
	public int doOneStep();
	
	//public void cleanup();
	
	/**
	 * Gets the result struct.
	 *
	 * @return the result struct
	 */
	public ResultOfStepStruct getResultStruct();
	
	/**
	 * Force cleanup.
	 *
	 * @return the int
	 */
	public int forceCleanup();
	

	/**
	 * Gets the all scalar variables.
	 *
	 * @return the all scalar variables
	 */
	public ScalarVariablesAllStruct  getAllScalarVariables();
	
	/**
	 * Sets the scalar values.
	 *
	 * @param scalarValueAry the scalar value ary
	 * @param length the length
	 */
	public void setScalarValues(ScalarValueRealStruct[] scalarValueAry, int length);
	
	/**
	 * Connect.
	 *
	 * @param messageCallback the message callback
	 * @param resultCallback the result callback
	 * @param stateChangeCallback the state change callback
	 */
	public void connect (
			MessageCallbackInterface messageCallback,
			ResultCallbackInterface resultCallback,
			StateChangeCallbackInterface stateChangeCallback
			);
	
	/**
	 * Xml parse.
	 *
	 * @param unzipFolder the unzip folder
	 */
	public void xmlParse(String unzipFolder);
	
	/**
	 * Inits the.
	 *
	 * @return the int
	 */
	public int init();
	
	/**
	 * Checks if is simulation complete.
	 *
	 * @return the int
	 */
	public int isSimulationComplete();
	
	/**
	 * Run.
	 *
	 * @return the int
	 */
	public int run(); 
	
	/**
	 * Request state change.
	 *
	 * @param newState the new state
	 */
	public void requestStateChange(SimStateNative newState); 
	
	/**
	 * Sets the config.
	 *
	 * @param configStruct the config struct
	 * @return the int
	 */
	public int setConfig(ConfigStruct configStruct);
	
	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	public ConfigStruct getConfig();
	
	
	/**
	 * The Interface MessageCallbackInterface.
	 */
	public interface MessageCallbackInterface extends Callback {
		
		/**
		 * Message callback.
		 *
		 * @param messageStruct the message struct
		 * @return true, if successful
		 */
		public boolean messageCallback(MessageStruct messageStruct);
	 }
	
	/**
	 * The Interface ResultCallbackInterface.
	 */
	public interface ResultCallbackInterface extends Callback {
		
		/**
		 * Result callback.
		 *
		 * @param resultOfStepStruct the result of step struct
		 * @return true, if successful
		 */
		public boolean resultCallback(ScalarValueResultsStruct scalarValueResultsStruct);
	 }
	
	/**
	 * The Interface StateChangeCallbackInterface.
	 */
	public interface StateChangeCallbackInterface extends Callback {
		
		/**
		 * State change callback.
		 *
		 * @param fmuState the fmu state
		 * @return true, if successful
		 */
		public boolean stateChangeCallback(SimStateNative fmuState);
	 }

	//public void deleteMessageStruct(MessageStruct messageStruct);



	/**
	 * Sets the scalar value real.
	 *
	 * @param idx the idx
	 * @param value the value
	 * @return the fmi status
	 */
	public fmiStatus setScalarValueReal(int idx, double value);


	
	public ScalarValueResultsStruct  getTest();
	
	
	
	
}




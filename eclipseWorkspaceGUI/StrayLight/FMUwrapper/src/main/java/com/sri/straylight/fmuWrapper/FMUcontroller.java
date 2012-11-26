package com.sri.straylight.fmuWrapper;




import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateWrapperNotify;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.EnumTypeMapper;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sun.jna.Library;
import com.sun.jna.Native;



// TODO: Auto-generated Javadoc
/**
 * The Class FMUcontroller.
 */
public class FMUcontroller  {

	
	/** The jna fmu wrapper_. */
	private JNAfmuWrapper jnaFMUWrapper_;
	
	/** The config struct_. */
	private ConfigStruct configStruct_;
	
	/** The fmu wrapper config_. */
	private FMUwrapperConfig fmuWrapperConfig_;
	
	
	/** The sim state native_. */
	private SimStateNative simStateNative_;

	private SimStateWrapper simStateWrapper_;
	
	/**
	 * Gets the fmu state.
	 *
	 * @return the fmu state
	 */
	public SimStateNative getSimStateNative() {
		return simStateNative_;
	}
	
	public SimStateWrapper getSimStateWrapper() {
		return simStateWrapper_;
	}




	
	/**
	 * Instantiates a new fMu controller.
	 *
	 * @param fmuWrapperConfig the fmu wrapper config
	 */
	public FMUcontroller(FMUwrapperConfig fmuWrapperConfig) {
		
		simStateNative_ = SimStateNative.simStateNative_0_uninitialized;
		simStateWrapper_ = SimStateWrapper.simStateServer_0_uninitialized;
		
		fmuWrapperConfig_ = fmuWrapperConfig;
		AnnotationProcessor.process(this);
	}
	
	
	/**
	 * Instantiates a new FMUcontroller.
	 */
	public FMUcontroller() {
		fmuWrapperConfig_ = FMUwrapperConfig.load();
		AnnotationProcessor.process(this);
	}
	
	
	
	
	/**
	 * Notify state change_.
	 *
	 * @param state_arg the state_arg
	 */
	private void notifyStateChange_(SimStateWrapper state_arg)
	{
		EventBus.publish(
				new SimStateWrapperNotify(this, state_arg)
				);	
		
	}
	
	
	
	/** The message callback func_. */
	private JNAfmuWrapper.MessageCallbackInterface messageCallbackFunc_ = 
			new JNAfmuWrapper.MessageCallbackInterface() {

		public boolean messageCallback(MessageStruct messageStruct) {


			MessageEvent event = new MessageEvent(this, messageStruct);
			
			EventBus.publish(event);

			
			return true;                  
		}
	};


	/** The result callback func_. */
	private JNAfmuWrapper.ResultCallbackInterface resultCallbackFunc_ = 
			new JNAfmuWrapper.ResultCallbackInterface() {

		public boolean resultCallback(ScalarValueResultsStruct scalarValueResultsStruct) {
			
			//ScalarValueResultsStruct res = jnaFMUWrapper_.getTest();
			ScalarValueResults scalarValueResults = new ScalarValueResults(scalarValueResultsStruct);
			
			ResultEvent event = new ResultEvent(this, scalarValueResults);
			//ResultEvent event = new ResultEvent(this);
			
			
			try {
				EventBus.publish(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
			return true;                  
		}
	};


	/** The state change callback func_. */
	private JNAfmuWrapper.StateChangeCallbackInterface stateChangeCallbackFunc_ = 

			new JNAfmuWrapper.StateChangeCallbackInterface() {
		public boolean stateChangeCallback(SimStateNative fmuState) {
			
			onSimStateNative (fmuState);
			return true;                  
		}		
	};

	/**
	 * On sim state native.
	 *
	 * @param simStateNative the sim state native
	 * TODO:(raj) get rid of all these switch blocks of code
	 * and replace with a public / private state notification system
	 */

	public void onSimStateNative(SimStateNative simStateNative)
	{

		switch (simStateNative) {
		
			case simStateNative_2_xmlParse_completed:
				simStateWrapper_ = SimStateWrapper.simStateServer_2_xmlParse_completed;
				notifyStateChange_(SimStateWrapper.simStateServer_2_xmlParse_completed);
				break;
			case simStateNative_3_ready:  
				simStateWrapper_ = SimStateWrapper.simStateServer_3_ready;
				notifyStateChange_(SimStateWrapper.simStateServer_3_ready);
				break;
			case simStateNative_4_run_started:  
				simStateWrapper_ = SimStateWrapper.simStateServer_4_run_started;
				notifyStateChange_(SimStateWrapper.simStateServer_4_run_started);
	        	break;
			case simStateNative_4_run_completed:  
				simStateWrapper_ = SimStateWrapper.simStateServer_4_run_completed;
				notifyStateChange_(SimStateWrapper.simStateServer_4_run_completed);
				break;
			case simStateNative_5_step_completed:  
				//notifyStateChange_(SimStateServer.simStateServer_5_step_completed);
				break;
			case simStateNative_7_terminate_completed: 
				simStateWrapper_ = SimStateWrapper.simStateServer_7_terminate_completed;
				notifyStateChange_(SimStateWrapper.simStateServer_7_terminate_completed);
				break;
			case simStateNative_7_reset_completed:  
				simStateWrapper_ = SimStateWrapper.simStateServer_7_reset_completed;
				notifyStateChange_(SimStateWrapper.simStateServer_7_reset_completed); 
				break;
			case simStateNative_e_error:
				notifyStateChange_(SimStateWrapper.simStateServer_e_error);
				break;
			default:
				break;
		}
		
		simStateNative_ = simStateNative;
		
	}



	/**
	 * Gets the meta data.
	 *
	 * @return the meta data
	 */
	public ConfigStruct getMetaData() {
		return configStruct_;
	}
	
	


	/**
	 * Xml parse.
	 */
	public void xmlParse() {
		
		jnaFMUWrapper_.xmlParse(fmuWrapperConfig_.fmuFolderAbsolutePath);
		
		ScalarVariablesAllStruct scalarVariablesAllStruct = jnaFMUWrapper_.getAllScalarVariables();
		ScalarVariablesAll scalarVariablesAll = new ScalarVariablesAll(scalarVariablesAllStruct);
		
		configStruct_ = jnaFMUWrapper_.getConfig();	
		XMLparsedInfo xmlParsed = new XMLparsedInfo(scalarVariablesAll);
		
		XMLparsedEvent event = new XMLparsedEvent(this, xmlParsed, configStruct_);
		EventBus.publish(event);

	}

	



	/**
	 * Check config.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void checkConfig()  throws IOException {
		
		//check to see that DLL exists
		boolean exists = (new File(fmuWrapperConfig_.nativeLibFolderAbsolutePath + "\\FMUwrapper.dll")).exists();
		
		if (!exists) {
			throw new IOException ("FMUwrapper.dll not found in: " + fmuWrapperConfig_.nativeLibFolderAbsolutePath);
		}

	}


	/**
	 * Connect.
	 *
	 * @throws Exception the exception
	 */
	public void connect() throws IOException {
		
		checkConfig();
		
		//System.setProperty("java.library.path", "fmuWrapperConfig_.nativeLibFolderAbsolutePath");
		System.setProperty("jna.library.path", fmuWrapperConfig_.nativeLibFolderAbsolutePath);

		Map<String, Object> options = new HashMap<String, Object>();
		EnumTypeMapper mp = new EnumTypeMapper();

		options.put(Library.OPTION_TYPE_MAPPER, mp);
		jnaFMUWrapper_ = (JNAfmuWrapper ) Native.loadLibrary("FMUwrapper", JNAfmuWrapper.class, options);
		
		jnaFMUWrapper_.connect(
				messageCallbackFunc_, 
				resultCallbackFunc_,
				stateChangeCallbackFunc_
				);
		
		notifyStateChange_(SimStateWrapper.simStateServer_1_connect_completed);
	}


	/**
	 * Run.
	 */
	public void run() {
		jnaFMUWrapper_.run();
	}


	/**
	 * Sets the config.
	 *
	 * @param configStruct the new config
	 */
	public void setConfig(ConfigStruct configStruct) {

		configStruct_ = configStruct;
		int result = jnaFMUWrapper_.setConfig(configStruct_);
		
		if (result == 0) {
			
			EventBus.publish(new ConfigChangeNotify(this,configStruct_));
		}

	}


	
	/**
	 * Request state change.
	 *
	 * @param newState the new state
	 */
	public void requestStateChange(SimStateNative newState) {
		jnaFMUWrapper_.requestStateChange(newState);
	}



	/**
	 * Sets the scalar value real.
	 *
	 * @param idx the idx
	 * @param value the value
	 */
	public void setScalarValueReal(int idx, double value) {
		jnaFMUWrapper_.setScalarValueReal(idx, value);
	}

	/**
	 * Sets the scalar values.
	 *
	 * @param scalarValueList the new scalar values
	 */
	public void setScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {

		int len = scalarValueList.size();
		ScalarValueRealStruct[] ary4 = (ScalarValueRealStruct[]) new ScalarValueRealStruct().toArray(len);
		
		for (int i = 0; i < len; i++) {
			ScalarValueRealStruct struct = scalarValueList.get(i);
			ary4[i].idx = struct.idx;
			ary4[i].value = struct.value;
		}
		
		
		jnaFMUWrapper_.setScalarValues(ary4, len);
		
	}
	


}

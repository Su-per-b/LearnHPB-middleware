package com.sri.straylight.fmuWrapper;




import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateServerNotify;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.voManaged.ResultOfStep;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValue;
import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.EnumTypeMapper;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ResultOfStepStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.fmiStatus;
import com.sun.jna.Library;
import com.sun.jna.Native;



public class FMUcontroller  {

	
	private JNAfmuWrapper jnaFMUWrapper_;
	private ConfigStruct configStruct_;
	private FMUwrapperConfig fmuWrapperConfig_;
	
	private ScalarVariableRealStruct[] scalarVariableInputAry_;
	private ScalarVariableRealStruct[] scalarVariableOutputAry_;
	private ScalarVariableStruct[] scalarVariableInternalAry_;
		
	public ScalarVariableRealStruct[] getScalarVariableInputAry() {
		return scalarVariableInputAry_;
	}
	
	public ScalarVariableRealStruct[] getScalarVariableOutputAry() {
		return scalarVariableOutputAry_;
	}
	
	public ScalarVariableStruct[] getScalarVariableInternalAry() {
		return scalarVariableInternalAry_;
	}
	
	
	private SimStateNative simStateNative_;



	public SimStateNative getFmuState() {
		return simStateNative_;
	}


	private boolean cleanupWhenPossible_ = false;



	
	public FMUcontroller(FMUwrapperConfig fmuWrapperConfig) {
		fmuWrapperConfig_ = fmuWrapperConfig;
	}
	
	public FMUcontroller() {
		fmuWrapperConfig_ = FMUwrapperConfig.load();
	}
	
	
	
	
	private void notifyStateChange_(SimStateServer state_arg)
	{
		EventBus.publish(
				new SimStateServerNotify(this, state_arg)
				);	
		
	}
	
	
	
	private JNAfmuWrapper.MessageCallbackInterface messageCallbackFunc_ = 
			new JNAfmuWrapper.MessageCallbackInterface() {

		public boolean messageCallback(MessageStruct messageStruct) {


			MessageEvent event = new MessageEvent(this);
			event.messageStruct = messageStruct;

			EventBus.publish(event);

			
			return true;                  
		}
	};


	private JNAfmuWrapper.ResultCallbackInterface resultCallbackFunc_ = 
			new JNAfmuWrapper.ResultCallbackInterface() {

		public boolean resultCallback(ResultOfStepStruct resultOfStepStruct) {
			
			
			ResultOfStep resultOfStep = new ResultOfStep (resultOfStepStruct);

			ResultEvent event = new ResultEvent(this);
			event.resultOfStep = resultOfStep;
			
			EventBus.publish(event);
			return true;                  
		}
	};


	private JNAfmuWrapper.StateChangeCallbackInterface stateChangeCallbackFunc_ = 

			new JNAfmuWrapper.StateChangeCallbackInterface() {
		public boolean stateChangeCallback(SimStateNative fmuState) {
			
			onSimStateNative (fmuState);
			return true;                  
		}		
	};

	public void onSimStateNative(SimStateNative simStateNative)
	{


		if (simStateNative == SimStateNative.simStateNative_4_run_started) {
			notifyStateChange_(SimStateServer.simStateServer_4_run_started);
		} else if (simStateNative == SimStateNative.simStateNative_4_run_completed) {
			notifyStateChange_(SimStateServer.simStateServer_4_run_completed);
		} else if (simStateNative == SimStateNative.simStateNative_5_stop_completed) {
			notifyStateChange_(SimStateServer.simStateServer_5_stop_completed);
		}
		
		
		simStateNative_ = simStateNative;
		
	}



	public ConfigStruct getMetaData() {
		return configStruct_;
	}
	
	


	public void xmlParse() {
		
		//unzipfolder_ = unzippedFolder;
		jnaFMUWrapper_.xmlParse(fmuWrapperConfig_.unzipFolder);

		int intputVariableCount = jnaFMUWrapper_.getInputVariableCount();
		
		ScalarVariableRealStruct struct = jnaFMUWrapper_.getScalarVariableInputStructs();
		scalarVariableInputAry_ = (ScalarVariableRealStruct[]) struct.toArray(intputVariableCount);
		
		
		int outputVariableCount = jnaFMUWrapper_.getOutputVariableCount();
		scalarVariableOutputAry_ = (ScalarVariableRealStruct[]) 
				jnaFMUWrapper_.getScalarVariableOutputStructs().toArray(outputVariableCount);
		

		int internalVariableCount = jnaFMUWrapper_.getInternalVariableCount();
		scalarVariableInternalAry_ = (ScalarVariableStruct[]) 
				jnaFMUWrapper_.getScalarVariableInternalStructs().toArray(internalVariableCount);
		
		
		
		configStruct_ = jnaFMUWrapper_.getConfig();
				
		XMLparsed xmlParsedStruct = new XMLparsed();
		
		xmlParsedStruct.inputVars = scalarVariableInputAry_;
		xmlParsedStruct.outputVars = scalarVariableOutputAry_;
		xmlParsedStruct.internalVars = scalarVariableInternalAry_;

		XMLparsedEvent event = new XMLparsedEvent(this, xmlParsedStruct, configStruct_);
		EventBus.publish(event);
		
		notifyStateChange_(SimStateServer.simStateServer_2_xmlParse_completed);
	}
	
	

	public void init() {
		jnaFMUWrapper_.init();
		notifyStateChange_(SimStateServer.simStateServer_3_init_completed);
	}





	public void connect() {

		System.setProperty("jna.library.path", fmuWrapperConfig_.nativeLibFolder);

		Map<String, Object> options = new HashMap<String, Object>();
		EnumTypeMapper mp = new EnumTypeMapper();

		options.put(Library.OPTION_TYPE_MAPPER, mp);
		jnaFMUWrapper_ = (JNAfmuWrapper ) Native.loadLibrary("FMUwrapper", JNAfmuWrapper.class, options);
		
		jnaFMUWrapper_.connect(
				messageCallbackFunc_, 
				resultCallbackFunc_,
				stateChangeCallbackFunc_
				);
		
		notifyStateChange_(SimStateServer.simStateServer_1_connect_completed);
	}



	public void unzip() {
//
//		String name = new File(fmuFilePath_).getName();
//		name = name.substring(0, name.length()-4); 
//
//		unzipfolder_ = Main.config.unzipFolderBase + File.separator + name;
//		Unzip.unzip(fmuFilePath_, unzipfolder_);
	}





	public void run() {
		jnaFMUWrapper_.run();
	}

//	public void forceCleanup() {
//
//		if (simulationStateNative_ == SimStateNative.fmuState_level_5_initializedFMU ||
//				simulationStateNative_ == SimStateNative.fmuState_error
//				) {
//
//			jnaFMUWrapper_.forceCleanup();
//			simulationStateNative_ = SimStateNative.fmuState_cleanedup;
//			
//			//SimulationStateEvent event = new SimulationStateEvent(this);
//			//event.simulationState = SimulationState.cleanedup;
//			//fmuEventDispatacher.fireEvent(event);
//			
//			notifyStateChange_(SimStateServer.level_4_run_cleanedup);
//			
//
//		} else if (simulationStateNative_ == SimStateNative.fmuState_level_1_xmlParsed ||
//				simulationStateNative_ == SimStateNative.fmuState_level_2_dllLoaded ||
//				simulationStateNative_ == SimStateNative.fmuState_level_3_instantiatedSlaves ||
//				simulationStateNative_ == SimStateNative.fmuState_level_4_initializedSlaves 
//				)
//		{
//			cleanupWhenPossible_ = true;
//
//		}
//
//	}



	public void setConfig(ConfigStruct configStruct) {

		configStruct_ = configStruct;
		jnaFMUWrapper_.setConfig(configStruct_);
		
	}


	public void requestStateChange(SimStateNative newState) {
		jnaFMUWrapper_.requestStateChange(newState);
	}



	public void changeInput(int idx, double value) {
		fmiStatus status = jnaFMUWrapper_.changeInput(idx, value);
	}

	public void doOneStep() {
		jnaFMUWrapper_.doOneStep();
		
	}

	

	


}

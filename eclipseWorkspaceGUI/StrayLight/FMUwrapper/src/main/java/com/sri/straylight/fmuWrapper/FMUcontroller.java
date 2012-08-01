package com.sri.straylight.fmuWrapper;




import java.util.HashMap;
import java.util.Map;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateServerNotify;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.voManaged.ResultOfStep;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.EnumTypeMapper;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ResultOfStepStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.fmiStatus;
import com.sun.jna.Library;
import com.sun.jna.Native;



public class FMUcontroller  {

	
	private JNAfmuWrapper jnaFMUWrapper_;
	private ConfigStruct configStruct_;
	private FMUwrapperConfig fmuWrapperConfig_;
	
	
	private SimStateNative simStateNative_;



	public SimStateNative getFmuState() {
		return simStateNative_;
	}


	private boolean cleanupWhenPossible_ = false;



	
	public FMUcontroller(FMUwrapperConfig fmuWrapperConfig) {
		fmuWrapperConfig_ = fmuWrapperConfig;
		AnnotationProcessor.process(this);
	}
	
	public FMUcontroller() {
		fmuWrapperConfig_ = FMUwrapperConfig.load();
		AnnotationProcessor.process(this);
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
		}else if (simStateNative == SimStateNative.simStateNative_e_error) {
			notifyStateChange_(SimStateServer.simStateServer_e_error);
		} else if (simStateNative == SimStateNative.simStateNative_3_ready) {
			notifyStateChange_(SimStateServer.simStateServer_3_ready); 
		} else if (simStateNative == SimStateNative.simStateNative_2_xmlParse_completed) {
			onXMLparseCompleted();
		} else if (simStateNative == SimStateNative.simStateNative_7_reset_completed) {
			onXMLparseCompleted();
			notifyStateChange_(SimStateServer.simStateServer_6_reset_completed); 
		}
		
		
		
		simStateNative_ = simStateNative;
		
	}



	public ConfigStruct getMetaData() {
		return configStruct_;
	}
	
	
	private void onXMLparseCompleted() {
		
		return;

	}


	public void xmlParse() {
		
		jnaFMUWrapper_.xmlParse(fmuWrapperConfig_.fmuFolderAbsolutePath);
		
		ScalarVariablesAllStruct scalarVariablesAllStruct = jnaFMUWrapper_.getAllScalarVariables();
		ScalarVariablesAll scalarVariablesAll = new ScalarVariablesAll(scalarVariablesAllStruct);
		
		configStruct_ = jnaFMUWrapper_.getConfig();	
		XMLparsed xmlParsed = new XMLparsed(scalarVariablesAll);
		
		XMLparsedEvent event = new XMLparsedEvent(this, xmlParsed, configStruct_);
		EventBus.publish(event);
		
		notifyStateChange_(SimStateServer.simStateServer_2_xmlParse_completed);

	}

	

	public void init() {
		jnaFMUWrapper_.init();
		//notifyStateChange_(SimStateServer.simStateServer_3_init_completed);
	}





	public void connect() {

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




	public void setConfig(ConfigStruct configStruct) {

		configStruct_ = configStruct;
		int result = jnaFMUWrapper_.setConfig(configStruct_);
		
		if (result == 0) {
			
			EventBus.publish(new ConfigChangeNotify(configStruct_));
		}
		
		
	}


	public void requestStateChange(SimStateNative newState) {
		jnaFMUWrapper_.requestStateChange(newState);
	}



	public void setScalarValueReal(int idx, double value) {
		fmiStatus status = jnaFMUWrapper_.setScalarValueReal(idx, value);
	}


	

	


}

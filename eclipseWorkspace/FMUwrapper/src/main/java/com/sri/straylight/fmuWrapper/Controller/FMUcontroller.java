package com.sri.straylight.fmuWrapper.Controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.sri.straylight.fmuWrapper.JNAfmuWrapper;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.MessageCallbackInterface;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.ResultCallbackInterface;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.EnumTypeMapper;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * The Class FMUcontroller.
 */
public class FMUcontroller extends AbstractController {

	private JNAfmuWrapper jnaFMUWrapper_;
	private ConfigStruct configStruct_;
	protected FMUwrapperConfig fmuWrapperConfig_;
	private SimStateNative simStateNative_;

	private HashMap<SimStateNative, Boolean> fireEventsForStates_ = new HashMap<SimStateNative, Boolean>();

	private MsgCallBack messageCallbackFunc_;
	private RsltCallback resultCallbackFunc_;
	
	
	private class MsgCallBack implements MessageCallbackInterface {
		
		private FMUcontroller fmuController_;
		
		public MsgCallBack(FMUcontroller fmuController) {
			fmuController_ = fmuController;
		}
		
		
		public boolean messageCallback(MessageStruct messageStruct) {
			
			MessageEvent event = new MessageEvent(this, messageStruct);
			fmuController_.fireEvent(event);
			
			return true;
		}
		
	}
		
	private class RsltCallback implements ResultCallbackInterface {
		
		private FMUcontroller fmuController_;
		
		public RsltCallback(FMUcontroller fmuController) {
			fmuController_ = fmuController;
		}
		
		
		public boolean resultCallback(
				ScalarValueResultsStruct scalarValueResultsStruct) {

			ScalarValueResults scalarValueResults = new ScalarValueResults(
					scalarValueResultsStruct);
			ResultEvent event = new ResultEvent(this, scalarValueResults);
			
			fmuController_.fireEvent(event);

			return true;
		}
		
	}
		
	
	
	// constructor
	public FMUcontroller(AbstractController parent) {
		super(parent);
		fmuWrapperConfig_ = FMUwrapperConfig.load();
		init();
	}

	public FMUcontroller(AbstractController parent,
			FMUwrapperConfig fmuWrapperConfig) {
		super(parent);
		fmuWrapperConfig_ = fmuWrapperConfig;
		init();
	}

	public FMUcontroller() {
		super(null);
		fmuWrapperConfig_ = FMUwrapperConfig.load();
		init();
	}

	public SimStateNative getSimStateNative() {
		return simStateNative_;
	}

	protected void init() {
		
		messageCallbackFunc_ = new MsgCallBack(this);
		resultCallbackFunc_ = new RsltCallback(this);
		
		
		AnnotationProcessor.process(this);

		simStateNative_ = SimStateNative.simStateNative_0_uninitialized;

		fireEventsForStates_.put(
				SimStateNative.simStateNative_1_connect_completed, true);
		fireEventsForStates_.put(
				SimStateNative.simStateNative_2_xmlParse_completed, true);

		fireEventsForStates_.put(
				SimStateNative.simStateNative_3_init_initializedSlaves, true);
		fireEventsForStates_.put(SimStateNative.simStateNative_3_ready, true);

		fireEventsForStates_.put(SimStateNative.simStateNative_4_run_completed,
				true);
		fireEventsForStates_.put(SimStateNative.simStateNative_4_run_started,
				true);

		fireEventsForStates_.put(SimStateNative.simStateNative_5_step_started,
				true);
		fireEventsForStates_.put(
				SimStateNative.simStateNative_5_step_completed, true);

		fireEventsForStates_.put(
				SimStateNative.simStateNative_7_terminate_completed, true);
		fireEventsForStates_.put(SimStateNative.simStateNative_e_error, true);

	}

	private void notifyStateChange_(SimStateNative simStateNative) {
		SimStateNativeNotify e = new SimStateNativeNotify(this, simStateNative);
		this.fireEvent(e);
	}


		
		

	/** The state change callback func_. */
	private JNAfmuWrapper.StateChangeCallbackInterface stateChangeCallbackFunc_ =

	new JNAfmuWrapper.StateChangeCallbackInterface() {
		public boolean stateChangeCallback(SimStateNative simStateNative) {

			simStateNative_ = simStateNative;

			if (fireEventsForStates_.containsKey(simStateNative)) {
				notifyStateChange_(simStateNative);
			}

			return true;
		}
	};

	/**
	 * Gets the meta data.
	 * 
	 * @return the meta data
	 */
	public ConfigStruct getConfigStruct() {
		return configStruct_;
	}

	/**
	 * Xml parse.
	 */
	public void xmlParse() {

		jnaFMUWrapper_.xmlParse(fmuWrapperConfig_.fmuFolderAbsolutePath);

		ScalarVariablesAllStruct scalarVariablesAllStruct = jnaFMUWrapper_
				.getAllScalarVariables();
		ScalarVariablesAll scalarVariablesAll = new ScalarVariablesAll(
				scalarVariablesAllStruct);

		
		XMLparsedInfo xmlParsed = new XMLparsedInfo(scalarVariablesAll);
		
		XMLparsedEvent event = new XMLparsedEvent(this, xmlParsed);
		fireEvent(event);
		

		configStruct_ = jnaFMUWrapper_.getConfig();
		
//		DefaultExperimentStruct.ByReference defaultExperimentStruct 
//			= new DefaultExperimentStruct.ByReference();
//		
//		defaultExperimentStruct.startTime = configStruct_.defaultExperimentStruct.startTime;
//		defaultExperimentStruct.stopTime = configStruct_.defaultExperimentStruct.stopTime;
//		defaultExperimentStruct.tolerance = configStruct_.defaultExperimentStruct.tolerance;
//		
//		ConfigStruct configStruct = new ConfigStruct();
//		configStruct.defaultExperimentStruct = defaultExperimentStruct;
//		configStruct.stepDelta = configStruct_.stepDelta;
//		
//				
//		
//		String json = event.toJson();
		
		ConfigChangeNotify event2 = new ConfigChangeNotify(this, configStruct_);
//		Object eventDeserialized = JsonController.getInstance().fromJson(json);
		
		
		fireEvent(event2);

		
	}

	/**
	 * Check config.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void checkConfig() throws IOException {

		// check to see that DLL exists
		boolean exists = (new File(
				fmuWrapperConfig_.nativeLibFolderAbsolutePath
						+ "\\FMUwrapper.dll")).exists();

		if (!exists) {
			throw new IOException("FMUwrapper.dll not found in: "
					+ fmuWrapperConfig_.nativeLibFolderAbsolutePath);
		}

	}

	/**
	 * Connect.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void connect() throws IOException {

		checkConfig();

		// System.setProperty("java.library.path",
		// "fmuWrapperConfig_.nativeLibFolderAbsolutePath");
		System.setProperty("jna.library.path",
				fmuWrapperConfig_.nativeLibFolderAbsolutePath);

		Map<String, Object> options = new HashMap<String, Object>();
		EnumTypeMapper mp = new EnumTypeMapper();

		options.put(Library.OPTION_TYPE_MAPPER, mp);
		jnaFMUWrapper_ = (JNAfmuWrapper) Native.loadLibrary("FMUwrapper",
				JNAfmuWrapper.class, options);

		jnaFMUWrapper_.connect(messageCallbackFunc_, resultCallbackFunc_,
				stateChangeCallbackFunc_);

		notifyStateChange_(SimStateNative.simStateNative_1_connect_completed);
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
	 * @param configStruct
	 *            the new config
	 */
	public void setConfig(ConfigStruct configStruct) {

		configStruct_ = configStruct;
		int result = jnaFMUWrapper_.setConfig(configStruct_);

		if (result == 0) {

			ConfigChangeNotify event = new ConfigChangeNotify(this, configStruct_);
			fireEvent(event);
		}

	}
	

	/**
	 * Request state change.
	 * 
	 * @param newState
	 *            the new state
	 */
	public void requestStateChange(SimStateNative newState) {
		jnaFMUWrapper_.requestStateChange(newState);
	}

	/**
	 * Sets the scalar value real.
	 * 
	 * @param idx
	 *            the idx
	 * @param value
	 *            the value
	 */
	public void setScalarValueReal(int idx, double value) {
		jnaFMUWrapper_.setScalarValueReal(idx, value);
	}

	/**
	 * Sets the scalar values.
	 * 
	 * @param scalarValueList
	 *            the new scalar values
	 */
	public void setScalarValues(Vector<ScalarValueRealStruct> scalarValueList) {

		int len = scalarValueList.size();
		ScalarValueRealStruct[] ary4 = (ScalarValueRealStruct[]) new ScalarValueRealStruct()
				.toArray(len);

		for (int i = 0; i < len; i++) {
			ScalarValueRealStruct struct = scalarValueList.get(i);
			ary4[i].idx = struct.idx;
			ary4[i].value = struct.value;
		}

		jnaFMUWrapper_.setScalarValues(ary4, len);

	}

}

package com.sri.straylight.fmuWrapper;




import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.sri.straylight.fmuWrapper.event.FMUeventDispatacher;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.Result;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValue;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.EnumTypeMapper;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ResultStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;
import com.sri.straylight.fmuWrapper.voNative.State;
import com.sun.jna.Library;
import com.sun.jna.Native;



public class FMU  {

	private String fmuFilePath_;
	private String nativeLibFolder_;

	private String unzipfolder_;
	private int variableCount_;
	private JNAfmuWrapper jnaFMUWrapper_;

	private ArrayList<ScalarVariableStruct> scalarVariableInputList_;
	public ArrayList<ScalarVariableStruct> getScalarVariableInputList() {
		return scalarVariableInputList_;
	}

	private ArrayList<ScalarVariableStruct> scalarVariableOutputList_;
	public ArrayList<ScalarVariableStruct> getScalarVariableOutputList() {
		return scalarVariableOutputList_;
	}

	private ArrayList<ScalarVariableStruct> scalarVariableInternalList_;
	public ArrayList<ScalarVariableStruct> getScalarVariableInternalList() {
		return scalarVariableInternalList_;
	}


	private HashMap<Integer, ScalarVariableStruct> scalarVariableAllList_;
	private ScalarVariableStruct[] svMetaArray_;
	private State fmuState_ = State.fmuState_level_0_uninitialized;

	public State getFmuState() {
		return fmuState_;
	}

	public FMUeventDispatacher fmuEventDispatacher;

	private boolean cleanupWhenPossible_ = false;



	private JNAfmuWrapper.MessageCallbackInterface messageCallbackFunc_ = 
			new JNAfmuWrapper.MessageCallbackInterface() {

		public boolean messageCallback(MessageStruct messageStruct) {


			MessageEvent event = new MessageEvent(this);
			event.messageStruct = messageStruct;

			// messageEventDispatacher.fireEvent(event);
			fmuEventDispatacher.fireEvent(event);
			
			
			jnaFMUWrapper_.deleteMessageStruct (messageStruct);
			
			return true;                  
		}
	};


	private JNAfmuWrapper.ResultCallbackInterface resultCallbackFunc_ = 
			new JNAfmuWrapper.ResultCallbackInterface() {

		public boolean resultCallback(ResultStruct resultItemStruct) {
			fireResultEvent(resultItemStruct);
			return true;                  
		}
	};


	private JNAfmuWrapper.StateChangeCallbackInterface stateChangeCallbackFunc_ = 

			new JNAfmuWrapper.StateChangeCallbackInterface() {
		public boolean stateChangeCallback(State fmuState) {
			//  fmuState_ = fmuState;
			stateChange (fmuState);
			return true;                  
		}		
	};

	public void stateChange(State fmuState)
	{
		if (fmuState == State.fmuState_level_5_initializedFMU &&
				cleanupWhenPossible_) 
		{
			cleanupWhenPossible_ = false;
			jnaFMUWrapper_.forceCleanup();
			fmuState_ = State.fmuState_cleanedup;
		}


		FMUstateEvent event = new FMUstateEvent(this);
		event.fmuState = fmuState;
		fmuEventDispatacher.fireEvent(event);


		if (fmuState == State.fmuState_level_5_initializedFMU) {
			InitializedEvent event2  = InitializedEvent.factoryMake(this); 
			fmuEventDispatacher.fireEvent(event2);
		}

		if (fmuState == State.fmuState_completedSimulation) {
			jnaFMUWrapper_.end();
		}
	}




	public int getVariableCount()
	{
		return variableCount_;
	}




	public void fireResultEvent(ResultStruct resultItemStruct)
	{
		ResultEvent event = new ResultEvent(this);
		Result resultItem = new Result (resultItemStruct);

		event.resultItem = resultItem;

		String str = "Result Update:  \n" + 
				"     time: " + Double.toString(resultItemStruct.time) + " \n";

		int len = resultItem.scalarValueCount;

		for (int i = 0; i < len; i++) {

			ScalarValue primitive = resultItem.scalarValueAry[i];
			ScalarVariableStruct svm = (ScalarVariableStruct) scalarVariableAllList_.get(new Integer (primitive.idx));  

			str += "      " +  svm.name + " : " +  primitive.string + "  \n";
		}

		str +=  " \n";


		event.resultString = str;
		fmuEventDispatacher.fireEvent(event);
	}



	public FMU(String fmuFilePath, String nativeLibFolder) {
		super();

		nativeLibFolder_ = nativeLibFolder;
		fmuFilePath_ = fmuFilePath;
		fmuEventDispatacher = new FMUeventDispatacher();

		loadLibrary();

		//initialize lists
		scalarVariableInputList_ =  new ArrayList<ScalarVariableStruct>();
		scalarVariableOutputList_ =  new ArrayList<ScalarVariableStruct>();
		scalarVariableInternalList_=  new ArrayList<ScalarVariableStruct>();
		scalarVariableAllList_ = new HashMap<Integer, ScalarVariableStruct>();

	}

	public void init_1() {

		jnaFMUWrapper_.init_1(
				messageCallbackFunc_, 
				resultCallbackFunc_,
				stateChangeCallbackFunc_
				);
	}

	public void init_2(String unzippedFolder) {
		jnaFMUWrapper_.init_2(unzippedFolder);

		variableCount_ = jnaFMUWrapper_.getVariableCount();  

		ScalarVariableStruct svMetaArchtype = jnaFMUWrapper_.getSVmetaData();
		svMetaArray_ = (ScalarVariableStruct[]) svMetaArchtype.toArray(variableCount_);


		for (int i = 0; i < variableCount_; i++) {

			ScalarVariableStruct svm = svMetaArray_[i];
			Enu causality = svm.getCausalityEnum();
			scalarVariableAllList_.put(new Integer(svm.idx), svm);

			switch (causality) {
			case enu_input: 
				scalarVariableInputList_.add(svm);
				break;
			case enu_output: 
				scalarVariableOutputList_.add(svm);
				break;
			case enu_internal:
				scalarVariableInternalList_.add(svm);
				break;
			}

		}
	}

	public void init_3() {
		jnaFMUWrapper_.init_3();
	}





	public void loadLibrary() {

		System.setProperty("jna.library.path", nativeLibFolder_);

		Map<String, Object> options = new HashMap<String, Object>();
		EnumTypeMapper mp = new EnumTypeMapper();

		options.put(Library.OPTION_TYPE_MAPPER, mp);
		jnaFMUWrapper_ = (JNAfmuWrapper ) Native.loadLibrary("FMUwrapper", JNAfmuWrapper.class, options);

	}



	public void unzip() {

		String name = new File(fmuFilePath_).getName();
		name = name.substring(0, name.length()-4); 

		unzipfolder_ = Main.config.unzipFolderBase + File.separator + name;
		Unzip.unzip(fmuFilePath_, unzipfolder_);
	}





	public void run() {
		jnaFMUWrapper_.run();
	}

	public void forceCleanup() {

		if (fmuState_ == State.fmuState_level_5_initializedFMU ||
				fmuState_ == State.fmuState_error
				) {

			jnaFMUWrapper_.forceCleanup();
			fmuState_ = State.fmuState_cleanedup;
			FMUstateEvent event = new FMUstateEvent(this);
			event.fmuState = fmuState_;
			fmuEventDispatacher.fireEvent(event);

		} else if (fmuState_ == State.fmuState_level_1_xmlParsed ||
				fmuState_ == State.fmuState_level_2_dllLoaded ||
				fmuState_ == State.fmuState_level_3_instantiatedSlaves ||
				fmuState_ == State.fmuState_level_4_initializedSlaves 
				)
		{
			cleanupWhenPossible_ = true;

		}

	}







}

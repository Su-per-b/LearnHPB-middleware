package com.sri.straylight.fmuWrapper;




import com.sri.straylight.fmuWrapper.event.FMUeventDispatacher;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class FMU  {

	private String fmuFilePath_;
	private String nativeLibFolder_;
	
	private String unzipfolder_;
	private int variableCount_;
	private JNAfmuWrapper jnaFMUWrapper_;
	
	private ArrayList<ScalarVariableMeta> scalarVariableInputList_;
	private ArrayList<ScalarVariableMeta> scalarVariableOutputList_;
	private HashMap<Integer, ScalarVariableMeta> variableListAll_;
	
	private ScalarVariableMeta[] svMetaArray_;
	private State fmuState_;
	
	public FMUeventDispatacher fmuEventDispatacher;

	private boolean cleanupWhenPossible_ = false;
	 
	private JNAfmuWrapper.MessageCallbackInterface messageCallbackFunc_ = 
			new JNAfmuWrapper.MessageCallbackInterface() {
		
		      public boolean messageCallback(MessageStruct messageStruct) {

			    	  
		    	  MessageEvent event = new MessageEvent(this);
		    	  event.messageStruct = messageStruct;

		    	 // messageEventDispatacher.fireEvent(event);
		    	  fmuEventDispatacher.fireMessageEvent(event);
			         return true;                  
			       }
			};
		 
	 
	private JNAfmuWrapper.ResultCallbackInterface resultCallbackFunc_ = 
			new JNAfmuWrapper.ResultCallbackInterface() {
		
		      public boolean resultCallback(ResultItemStruct resultItemStruct) {
		    	  	 fireResultEvent(resultItemStruct);
			         return true;                  
			      }
			};
					
	 
	private JNAfmuWrapper.StateChangeCallbackInterface stateChangeCallbackFunc_ = 
			new JNAfmuWrapper.StateChangeCallbackInterface() {
		
		      public boolean stateChangeCallback(State fmuState) {
		    	  fmuState_ = fmuState;
		    	  
		    	  if (fmuState == State.fmuState_level_5_initializedFMU &&
		    			  cleanupWhenPossible_) 
		    	  {
	    			  cleanupWhenPossible_ = false;
	    			  jnaFMUWrapper_.forceCleanup();
	    			  fmuState_ = State.fmuState_cleanedup;
		    	  }
		    	  
		    	  
		    	  
		    	  FMUstateEvent event = new FMUstateEvent(this);
		    	  event.fmuState = fmuState;
		    	  fmuEventDispatacher.fireStateEvent(event);
		    	  
		    	  
		    	  if (fmuState == State.fmuState_level_5_initializedFMU) {
		    		  
	    		    	ArrayList<String> strList = new ArrayList<String>();
	    		    	
	    		    	ArrayList<ScalarVariableMeta> svnList = getScalarVariableOutputList();
	    		        Iterator<ScalarVariableMeta> itr = svnList.iterator();
	    		        
	    		        while (itr.hasNext()) {
	    		        	ScalarVariableMeta svm = itr.next();
	    		        	strList.add(svm.name);
	    		        }
	    		        
	    		        
	    		        InitializedStruct struct = new InitializedStruct();
	    		        struct.columnNames = strList.toArray(new String[strList.size()]);
	    		        
	    		        InitializedEvent event2  = new InitializedEvent(this);
	    		        event2.initializedStruct = struct;

		    		  fmuEventDispatacher.fireInitializedEvent(event2);

		    		  
		    	  }
		    	  
		    	  if (fmuState == State.fmuState_completedSimulation) {
		    		  jnaFMUWrapper_.end();
		    	  }
		    	  
			      return true;                  
			  }		
			};
			
					
					
	public int getVariableCount()
	{
	     return variableCount_;
	}
	
	public ArrayList<ScalarVariableMeta> getScalarVariableOutputList() {
		return scalarVariableOutputList_;
	}
	
	
	   
	public void fireResultEvent(ResultItemStruct resultItemStruct)
	{
  		ResultEvent event = new ResultEvent(this);
  		ResultItem resultItem = new ResultItem (resultItemStruct);
  		
  		event.resultItem = resultItem;
  		
		String str = "Result Update:  \n" + 
				"     time: " + Double.toString(resultItemStruct.time) + " \n";
				
		int len = resultItem.primitiveCount;

		for (int i = 0; i < len; i++) {
			
			ResultItemPrimitive primitive = resultItem.primitiveAry[i];
			ScalarVariableMeta svm = (ScalarVariableMeta) variableListAll_.get(new Integer (primitive.idx));  
			
			str += "      " +  svm.name + " : " +  primitive.string + "  \n";
		}

		str +=  " \n";


		event.resultString = str;
    	fmuEventDispatacher.fireResultEvent(event);
	}

	
		
	public FMU(String fmuFilePath, String nativeLibFolder) {
		
		nativeLibFolder_ = nativeLibFolder;
		fmuFilePath_ = fmuFilePath;
		fmuEventDispatacher = new FMUeventDispatacher();
		
		loadLibrary();
    	
    	//initialize lists
    	scalarVariableInputList_ =  new ArrayList<ScalarVariableMeta>();
    	scalarVariableOutputList_ =  new ArrayList<ScalarVariableMeta>();
    	variableListAll_ = new HashMap<Integer, ScalarVariableMeta>();

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

		ScalarVariableMeta svMetaArchtype = jnaFMUWrapper_.getSVmetaData();
		svMetaArray_ = (ScalarVariableMeta[]) svMetaArchtype.toArray(variableCount_);

		
    	for (int i = 0; i < variableCount_; i++) {

    		ScalarVariableMeta svm = svMetaArray_[i];
    		Enu causality = svm.getCausalityEnum();
    		variableListAll_.put(new Integer(svm.idx), svm);
    		
    		switch (causality) {
    			case enu_input: 
    				scalarVariableInputList_.add(svm);
    				break;
    			case enu_output: 
    				scalarVariableOutputList_.add(svm);
    				break;
    			default:
    				//variableListOther_.add(svm);
    				
    		}
    		
		}
	}
	
	public void init_3() {
		jnaFMUWrapper_.init_3();
	}
	
	
	
	
	
	public void loadLibrary() {
		
    	System.setProperty("jna.library.path", nativeLibFolder_);
    	
		Map<String, Object> options = new HashMap<String, Object>();
		HlTypeMapper mp = new HlTypeMapper();
		
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
	    	  fmuEventDispatacher.fireStateEvent(event);
			  
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

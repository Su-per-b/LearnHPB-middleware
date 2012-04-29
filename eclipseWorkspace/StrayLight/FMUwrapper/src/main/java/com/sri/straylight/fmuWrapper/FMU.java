package com.sri.straylight.fmuWrapper;


import com.sri.straylight.common.Unzip;
import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class FMU  {

	private String fmuFilePath_;
	private String nativeLibFolder_;
	
	private String unzipfolder_;
	private int variableCount_;
	private JNAfmuWrapper jnaFMUWrapper_;
	public ArrayList<ScalarVariableMeta> variableListOther_;
	public ArrayList<ScalarVariableMeta> variableListInputs_;
	public ArrayList<ScalarVariableMeta> variableListOutputs_;
	
	private HashMap<Integer, ScalarVariableMeta> variableListAll_;
	
	private ScalarVariableMeta[] svMetaArray_;
	
	public ResultEventDispatacher resultEventDispatacher;
	public MessageEventDispatacher messageEventDispatacher;
	

	 
	private JNAfmuWrapper.MessageCallbackInterface messageCallbackFunc_ = 
			new JNAfmuWrapper.MessageCallbackInterface() {
		
		      public boolean messageCallback(MessageStruct messageStruct) {

			    	  
		    	  MessageEvent event = new MessageEvent(this);
		    	  event.messageStruct = messageStruct;

		    	  messageEventDispatacher.fireEvent(event);
			    	
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
					
	 
	
	public int getVariableCount()
	{
	     return variableCount_;
	}
	
	   
	public void fireResultEvent(ResultItemStruct resultItemStruct)
	{
  		ResultEvent event = new ResultEvent(this);
  		event.resultItemStruct = resultItemStruct;
  		
		String str = "Result Update:  \n" + 
				"     time: " + Double.toString(resultItemStruct.time) + " \n";
				
		int len = resultItemStruct.primitiveCount;
		
		ResultItemPrimitiveStruct[] ary = resultItemStruct.getPrimitives();
		
		for (int i = 0; i < len; i++) {
			ScalarVariableMeta svm = (ScalarVariableMeta) variableListAll_.get(new Integer (i));  
			str += "      " +  svm.name + " : " +  ary[i].string + "  \n";
		}

		str +=  " \n";
		//System.out.println(result);
	
		

		event.resultString = str;
    	resultEventDispatacher.fireEvent(event);
	}

	
		
	public FMU(String fmuFilePath, String nativeLibFolder) {
		
		nativeLibFolder_ = nativeLibFolder;
		fmuFilePath_ = fmuFilePath;
		resultEventDispatacher = new ResultEventDispatacher();
		messageEventDispatacher = new MessageEventDispatacher();
		
		loadLibrary();
    	
    	//initialize lists
    	variableListOther_ =  new ArrayList<ScalarVariableMeta>();
    	variableListInputs_ =  new ArrayList<ScalarVariableMeta>();
    	variableListOutputs_ =  new ArrayList<ScalarVariableMeta>();
    	variableListAll_ = new HashMap<Integer, ScalarVariableMeta>();

	}
	

	
	public void init(String unzippedFolder) {
		
		jnaFMUWrapper_.init(unzippedFolder);
    	variableCount_ = jnaFMUWrapper_.getVariableCount();  

		ScalarVariableMeta svMetaArchtype = jnaFMUWrapper_.getSVmetaData();
		svMetaArray_ = (ScalarVariableMeta[]) svMetaArchtype.toArray(variableCount_);

		
    	for (int i = 0; i < variableCount_; i++) {

    		ScalarVariableMeta svm = svMetaArray_[i];
    		Enu causality = svm.getCausalityEnum();
    		variableListAll_.put(new Integer(svm.idx), svm);
    		
    		switch (causality) {
    			case enu_input: 
    				variableListInputs_.add(svm);
    				break;
    			case enu_output: 
    				variableListOutputs_.add(svm);
    				break;
    			default:
    				variableListOther_.add(svm);
    				
    		}
    		
		}
    	
	}
	
	
	
	
	public void loadLibrary() {
		
    	System.setProperty("jna.library.path", nativeLibFolder_);
    	
		Map<String, Object> options = new HashMap<String, Object>();
		HlTypeMapper mp = new HlTypeMapper();
		
		options.put(Library.OPTION_TYPE_MAPPER, mp);
		jnaFMUWrapper_ = (JNAfmuWrapper ) Native.loadLibrary("FMUwrapper", JNAfmuWrapper.class, options);
		
		jnaFMUWrapper_.registerMessageCallback(messageCallbackFunc_);
		jnaFMUWrapper_.registerResultCallback(resultCallbackFunc_);
		
	}
	


	public void unzip() {
		
		String name = new File(fmuFilePath_).getName();
		name = name.substring(0, name.length()-4); 
				
		unzipfolder_ = Main.config.unzipFolderBase + File.separator + name;
		Unzip.unzip(fmuFilePath_, unzipfolder_);
	}
	
	

	public ArrayList<ScalarVariableMeta> getInputs() {
		return variableListInputs_;
	}
	
	public ArrayList<ScalarVariableMeta> getOutputs() {
		return variableListOutputs_;
	}
	

	
	public void run() {
		
		jnaFMUWrapper_.run();
		//runManagedLoop();
	}
	
	public void runManagedLoop() {
		

		
    	while(jnaFMUWrapper_.isSimulationComplete() == 0) {

    		jnaFMUWrapper_.doOneStep();
    		ResultItemStruct riStruct = jnaFMUWrapper_.getResultStruct();

    		fireResultEvent(riStruct);
    	}
    	
    	jnaFMUWrapper_.end();
		
	}
	
	

	
	
	
}

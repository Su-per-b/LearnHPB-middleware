package com.sri.straylight.fmuWrapper;


import com.sri.straylight.common.Unzip;
import com.sun.jna.Callback;
import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
import java.util.ArrayList;
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
	
   public int getVariableCount()
   {
     return variableCount_;
   }
	   
	public ResultEventDispatacher disp;
	
		
	public FMU(String fmuFilePath, String nativeLibFolder) {
		
		nativeLibFolder_ = nativeLibFolder;
		fmuFilePath_ = fmuFilePath;
		disp = new ResultEventDispatacher();
		
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
		
		
		JNAfmuWrapper.MyCallback fnc = new JNAfmuWrapper.MyCallback() {
			
		      public boolean callback(String msg) {
		          //System.out.println(msg);
		    	  
		  		ResultEvent re = new ResultEvent(this);
		    	re.resultString = msg;
		    	re.resultType = ResultType.resultType_debug_message;
		    	
		    	disp.fireEvent(re);
		    	
		           return true;                  
		       }
		 };
		 
		
		jnaFMUWrapper_.registerCallback(fnc);
		
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
	
	private void fireEvent(ResultItemStruct resultItemStruct) {
		
		String result = "Result Update:  <br />\n" + 
				"     time: " + Double.toString(resultItemStruct.time) + "  <br />\n";
				
		int len = resultItemStruct.primitiveCount;
		
		ResultItemPrimitiveStruct[] ary = resultItemStruct.getPrimitives();
		
		for (int i = 0; i < len; i++) {
			ScalarVariableMeta svm = (ScalarVariableMeta) variableListAll_.get(new Integer (i));  
			result += "      " +  svm.name + " : " +  ary[i].string + " <br /> \n";
		}

		result +=  "<br /> \n";
		//System.out.println(result);
	
		
		ResultEvent re = new ResultEvent(this);
    	re.resultString = result;
    	re.resultItemStruct = resultItemStruct;
    	
    	disp.fireEvent(re);
	}
	
	
	
	public void run() {
		
		ResultItemStruct r = jnaFMUWrapper_.testResultItemStruct();
		ResultItemPrimitiveStruct[] ary = r.getPrimitives();
		
    	while(jnaFMUWrapper_.isSimulationComplete() == 0) {

    		int result = jnaFMUWrapper_.doOneStep();
    		ResultItemStruct riStruct = jnaFMUWrapper_.getResultStruct();

        	fireEvent(riStruct);
        	
    	}
    	
    	jnaFMUWrapper_.end();
		
	}
	
	

	
	
	
}

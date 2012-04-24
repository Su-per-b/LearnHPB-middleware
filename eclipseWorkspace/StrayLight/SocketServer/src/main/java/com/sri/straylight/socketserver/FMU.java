package com.sri.straylight.socketserver;


import com.sri.straylight.common.Unzip;
import com.sri.straylight.fmu.*;
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



public class FMU implements ResultEventListener{

	private String fmuFilePath_;
	private String fmuFileName_;
	
	private String unzipfolder_;
	private String dllPath_;
	private int variableCount_;
	private JNAfmuWrapper jnaFMUWrapper_;
	private ArrayList<ScalarVariableMeta> variableListOther_;
	private ArrayList<ScalarVariableMeta> variableListInputs_;
	private ArrayList<ScalarVariableMeta> variableListOutputs_;
	private ScalarVariableMeta[] svMetaArray_;
	
   public int getVariableCount()
   {
     return variableCount_;
   }
	   
	private ResultEventDispatacher disp;
	
		
	public FMU(String fmuFileName) {
		
		fmuFilePath_ = fmuFileName;
		
		loadLibrary();
		
		//add event listener
		disp = new ResultEventDispatacher();
    	disp.addListener(this);
    	
    	//initialize lists
    	variableListOther_ =  new ArrayList<ScalarVariableMeta>();
    	variableListInputs_ =  new ArrayList<ScalarVariableMeta>();
    	variableListOutputs_ =  new ArrayList<ScalarVariableMeta>();
    	
	}
	

	
	
	public void init(String unzippedFolder) {
		jnaFMUWrapper_.initAll(unzippedFolder);

    	variableCount_ = jnaFMUWrapper_.getVariableCount();  

		ScalarVariableMeta svMetaArchtype = jnaFMUWrapper_.getSVmetaData();
		svMetaArray_ = (ScalarVariableMeta[]) svMetaArchtype.toArray(variableCount_);

		
    	for (int i = 0; i < variableCount_; i++) {

    		ScalarVariableMeta sv = svMetaArray_[i];
    		Enu causality = sv.getCausalityEnum();
    		
    		switch (causality) {
    			case enu_input: 
    				variableListInputs_.add(sv);
    				break;
    			case enu_output: 
    				variableListOutputs_.add(sv);
    				break;
    			default:
    				variableListOther_.add(sv);
    				
    		}
    		
		}
    	
	}
	
	
	public void loadLibrary() {
		
		Map<String, Object> options = new HashMap<String, Object>();
		HlTypeMapper mp = new HlTypeMapper();
		
		options.put(Library.OPTION_TYPE_MAPPER, mp);
		jnaFMUWrapper_ = (JNAfmuWrapper ) Native.loadLibrary("FMUwrapper", JNAfmuWrapper.class, options);
		
	}
	
	

	
	
	public void test() {
		
		
		
		
		int sizeOfInt = 4;
		Memory ptr2 = new Memory(variableCount_ * sizeOfInt);
		//jnaFMUWrapper_.getDataList4(ptr2);
		
		
		int[] ary = ptr2.getIntArray(0, variableCount_);
		
		
		ScalarVariableMeta[] vars = new ScalarVariableMeta[variableCount_];
		
		//ScalarVariableMeta svMeta = jnaFMUWrapper_.getDataList6();
		
		//ScalarVariableMeta[] svMetaAry = (ScalarVariableMeta[]) svMeta.toArray(variableCount_);
		

		
	}
	
	public void unzip() {
		
		String name = new File(fmuFilePath_).getName();
		name = name.substring(0, name.length()-4); 
				
		unzipfolder_ = Main.config.unzipFolderBase + File.separator + name;
		Unzip.unzip(fmuFilePath_, unzipfolder_);
		
	}
	
	
	public void eventUpdate(ResultEvent re) {
    	System.out.println("eventUpdate - " + re.resultString);
	}

	

	
	
	public ArrayList<ScalarVariableMeta> getInputs() {
		
		return variableListInputs_;
	
	}
	
	public ArrayList<ScalarVariableMeta> getOutputs() {
		
		return variableListOutputs_;
	
	}
	
	
	
	public void run() {
		
		ResultItemStruct r = jnaFMUWrapper_.testResultItemStruct();
		ResultItemPrimitiveStruct[] ary = r.getPrimitives();
		
    	while(jnaFMUWrapper_.isSimulationComplete() == 0) {

    		String str = jnaFMUWrapper_.getResultFromOneStep();
    		
    		ResultItemStruct riStruct;
    		riStruct = jnaFMUWrapper_.getResultStruct();
    		
    		ResultItemPrimitiveStruct[] ary2 = riStruct.getPrimitives();

        	ResultEvent re = new ResultEvent(this);
        	re.resultString = str;
        	
        	disp.fireEvent(re);

    	}
    	
    	jnaFMUWrapper_.end();
		
	}
	
	

	
	
	
}

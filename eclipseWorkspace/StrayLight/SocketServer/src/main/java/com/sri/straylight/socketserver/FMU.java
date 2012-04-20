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
	private ArrayList<FMUvariable> variableList_;
	private ArrayList<FMUvariable> variableListInputs_;
	private ArrayList<FMUvariable> variableListOutputs_;
	
	
   public int getVariableCount()
   {
     return variableCount_;
   }
	   
	private ResultEventDispatacher disp;
	
		
	public FMU(String fmuFileName) {
		
		Map<String, Object> options = new HashMap<String, Object>();
		HlTypeMapper mp = new HlTypeMapper();
		options.put(Library.OPTION_TYPE_MAPPER, mp);
		jnaFMUWrapper_ = (JNAfmuWrapper ) Native.loadLibrary("FMUwrapper", JNAfmuWrapper.class, options);
		
		
		fmuFilePath_ = fmuFileName;
		disp = new ResultEventDispatacher();
    	
    	disp.addListener(this);
    	
    	variableList_ =  new ArrayList<FMUvariable>();
    	variableListInputs_ =  new ArrayList<FMUvariable>();
    	variableListOutputs_ =  new ArrayList<FMUvariable>();

	}
	
	
	public void test() {
		
	//	jnaFMUWrapper_.getaDataList2();
		
		int count = jnaFMUWrapper_.getVariableCount();
		
		//int[] list2 = new int[size];
		
		
		Pointer ptr  = jnaFMUWrapper_.getaDataList3();
		
		
		//ByteBuffer foo = ByteBuffer.allocateDirect(1000000);
		int sizeOfInt = 4;
		Memory ptr2 = new Memory(count * sizeOfInt);
		
		
		jnaFMUWrapper_.getaDataList4(ptr2);
		
		
		
		int[] ary = ptr2.getIntArray(0, count);
		
		//PointerTo
	//	int val = list2[4];
		
		
		//int[] list = jnaFMUWrapper_.getaDataList2();
		
		//System.out.println("list2[4]" +list2[4]);
		
		
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

	
	public void init(String unzippedFolder) {
		jnaFMUWrapper_.initAll(unzippedFolder);
    	variableCount_ = jnaFMUWrapper_.getVariableCount();
    	
    	
    	
    	for (int i = 0; i < variableCount_; i++) {
    		FMUvariable vr = new FMUvariable();
    		
    		vr.name = jnaFMUWrapper_.getVariableName(i);
    		vr.description = jnaFMUWrapper_.getVariableDescription(i);
    		vr.causality = jnaFMUWrapper_.getVariableCausality(i); 
    		vr.type = jnaFMUWrapper_.getVariableType(i);
    		
    		
    		
    		variableList_.add(vr);
    		
    		if (vr.causality == Enu.enu_input) {
    			variableListInputs_.add(vr);
    		}
    		
    		if (vr.causality == Enu.enu_output) {
    			variableListOutputs_.add(vr);
    		}
    		
    		
    		
		}

	}
	
	
	public ArrayList<FMUvariable> getInputs() {
		
		return variableListInputs_;
	
	}
	
	public ArrayList<FMUvariable> getOutputs() {
		
		return variableListOutputs_;
	
	}
	
	
	
	public void run() {

    	while(jnaFMUWrapper_.isSimulationComplete() == 0) {
    	//	
        	//String str = jnaFMUWrapper_.getVariableName(1);

    		String str = jnaFMUWrapper_.getResultFromOneStep();
    		
    		
    		
        	ResultEvent re = new ResultEvent(this);
        	re.resultString = str;
        	
        	disp.fireEvent(re);

    	}
    	
    	jnaFMUWrapper_.end();
		
	}
	
	

	
	
	
}

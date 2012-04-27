

package com.sri.straylight.fmuWrapper;


import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Callback;
import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;


public interface JNAfmuWrapper extends Library {
	

	void end();
	
	String getResultFromOneStep();
	
	Enu getVariableCausality(int idx);
	
	int getVariableCount();
	
	ScalarVariableMeta getSVmetaData();
	
	void initAll(String unzipPath);
	
	int isSimulationComplete();
	
	void testFMU(String unzipPath);
	
	ResultItemStruct getResultStruct();
	
	ResultItemPrimitiveStruct testPrimitive();
	
	ResultItemPrimitiveStruct testPrimitiveArray();
	
	ResultItemStruct testResultItemStruct();
	

	
	public interface MyCallback extends Callback {
		
		public boolean callback(String msg);
	    
	 }
	
	
	public int registerCallback (MyCallback callback);
	
	
}




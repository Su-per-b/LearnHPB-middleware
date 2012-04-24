

package com.sri.straylight.socketserver;


import java.util.HashMap;
import java.util.Map;

import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sri.straylight.fmu.*;


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
	
}
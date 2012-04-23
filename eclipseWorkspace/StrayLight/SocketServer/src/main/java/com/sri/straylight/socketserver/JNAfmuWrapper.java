

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
	
	int[] getDataList2();
	
	Pointer getDataList3();
	
	void getDataList4(Memory buf);
	
	ScalarVariableMeta getSVmetaData();
	
	String getVariableDescription(int idx);
	
	String getVariableName(int idx);
	
	Elm getVariableType(int idx);
	
	void initAll(String unzipPath);
	
	int isSimulationComplete();
	
	void testFMU(String unzipPath);
	
	
	ScalarVariableMeta test_a();
	
	
	void test_b(ScalarVariableMeta3 sv);
	
	//static String test1();
	
	//EnumEnu getVariableCausality(int idx);
	
	
}
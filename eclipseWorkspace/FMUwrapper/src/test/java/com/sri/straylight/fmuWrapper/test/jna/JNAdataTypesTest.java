package com.sri.straylight.fmuWrapper.test.jna;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.JNAfmuWrapper;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.MessageCallbackInterface;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.ResultCallbackInterface;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.StateChangeCallbackInterface;
import com.sri.straylight.fmuWrapper.test.base.JNAdataTypes;
import com.sri.straylight.fmuWrapper.voManaged.FMImodelAttributes;
import com.sri.straylight.fmuWrapper.voNative.AttributeStruct;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.EnumTypeMapper;
import com.sri.straylight.fmuWrapper.voNative.FMImodelAttributesStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class JNAdataTypesTest {
	
	final private String pathToNativeLibs_ = "E:\\LHPB\\LearnHPB-middleware\\visualStudioWorkspace\\bin\\Debug";
	final private String pathToFMUfolder_ = "E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test";
	

	
	
	@Test
	public void T00_DataTypes() {
		
		System.setProperty("jna.library.path", pathToNativeLibs_ );
		
		Map<String, Object> options = new HashMap<String, Object>();
		EnumTypeMapper mp = new EnumTypeMapper();
		
		options.put(Library.OPTION_TYPE_MAPPER, mp);
		
		JNAdataTypes jnaDataTypes = (JNAdataTypes) Native.loadLibrary("FMUwrapper",JNAdataTypes.class, options);
		assertNotNull(jnaDataTypes);
		
		
		
		ScalarVariablesAllStruct scalarVariablesAllStruct_0 = jnaDataTypes.test_GetScalarVariablesAllStruct();
		assertNotNull(scalarVariablesAllStruct_0);
		
		ScalarVariablesAllStruct scalarVariablesAllStruct_1 = jnaDataTypes.test_GetScalarVariablesAllStruct2();
		assertNotNull(scalarVariablesAllStruct_1);
		
		ScalarVariableCollectionStruct scalarVariableCollectionStruct = jnaDataTypes.test_GetScalarVariableCollectionStruct();
		assertNotNull(scalarVariableCollectionStruct);
		
		ScalarValueRealStruct scalarValueRealStruct = jnaDataTypes.test_GetScalarValueRealStruct();
		assertNotNull(scalarValueRealStruct);
	
		ScalarVariableRealStruct scalarVariableRealStruct = jnaDataTypes.test_GetScalarVariableRealStruct();
		assertNotNull(scalarVariableRealStruct);

		
		return;
	
	}
	
	
	
	
	@Test
	public void T01_ScalarVariableRealStruct() {
		
		System.setProperty("jna.library.path", pathToNativeLibs_ );
		
		Map<String, Object> options = new HashMap<String, Object>();
		EnumTypeMapper mp = new EnumTypeMapper();
		
		options.put(Library.OPTION_TYPE_MAPPER, mp);
		
		JNAdataTypes jnaDataTypes = (JNAdataTypes) Native.loadLibrary("FMUwrapper",JNAdataTypes.class, options);
		assertNotNull(jnaDataTypes);
		
	
		ScalarVariableRealStruct scalarVariableRealStruct = jnaDataTypes.test_GetScalarVariableRealStruct();
		assertNotNull(scalarVariableRealStruct);

		
		Assert.assertEquals("scalarVar name", scalarVariableRealStruct.name);
		Assert.assertEquals(1, scalarVariableRealStruct.idx);
		
		Assert.assertEquals(Enu.enu_input, scalarVariableRealStruct.causality);
		Assert.assertEquals(Enu.enu_continuous, scalarVariableRealStruct.variability);

		Assert.assertEquals("The Description", scalarVariableRealStruct.description);
		Assert.assertEquals(125420, scalarVariableRealStruct.valueReference);

		TypeSpecReal.ByReference typeSpecReal_1 = scalarVariableRealStruct.typeSpecReal;
		
		Assert.assertEquals(20.25, typeSpecReal_1.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_1.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_1.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_1.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_1.unit, 0.0);

		Assert.assertEquals(1, typeSpecReal_1.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.unitValueStatus, 0.0);
		
		
		return;
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private class ResultCallback implements ResultCallbackInterface {

		public boolean resultCallback(
				ScalarValueResultsStruct scalarValueResultsStruct) {



			return true;
		}
	}
	
	
	
	private class StateChangeCallback implements StateChangeCallbackInterface {

		public boolean stateChangeCallback(SimStateNative simStateNative) {



			return true;
		}
	}
	
	
	
	private class MessageCallback implements MessageCallbackInterface {

		public boolean messageCallback(MessageStruct messageStruct) {



			return true;
		}

	}
	
	
	
	
}

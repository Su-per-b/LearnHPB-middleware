package com.sri.straylight.fmuWrapper.test.jna;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.JNAfmuWrapper.MessageCallbackInterface;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.ResultCallbackInterface;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.StateChangeCallbackInterface;
import com.sri.straylight.fmuWrapper.test.base.JNAdataTypes;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.EnumTypeMapper;
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

public class JNAdataTypesT {
	
	final private String pathToNativeLibs_ = "E:\\LHPB\\LearnHPB-middleware\\visualStudioWorkspace\\bin\\Debug";
	final private String pathToFMUfolder_ = "E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test";
	

	
	
	private JNAdataTypes jnaDataTypes_;
	
	

	//runs before each test
	@Before
	public void beforeEachTest() {
		
		System.setProperty("jna.library.path", pathToNativeLibs_ );
		
		Map<String, Object> options = new HashMap<String, Object>();
		EnumTypeMapper mp = new EnumTypeMapper();
		
		options.put(Library.OPTION_TYPE_MAPPER, mp);
		
		jnaDataTypes_ = (JNAdataTypes) Native.loadLibrary("FMUwrapper",JNAdataTypes.class, options);
		assertNotNull(jnaDataTypes_);
		
		
	}
	
	//runs after each test
	@After
	public void afterEachTest() {
		
		

		
	}
	
	
	
	@Test
	public void T00_DataTypes() {
		

		ScalarVariablesAllStruct scalarVariablesAllStruct_0 = jnaDataTypes_.test_GetScalarVariablesAllStruct();
		assertNotNull(scalarVariablesAllStruct_0);
		
		
		
		ScalarVariableCollectionStruct scalarVariableCollectionStruct = jnaDataTypes_.test_GetScalarVariableCollectionStruct();
		assertNotNull(scalarVariableCollectionStruct);
		
		ScalarValueRealStruct scalarValueRealStruct = jnaDataTypes_.test_GetScalarValueRealStruct();
		assertNotNull(scalarValueRealStruct);
	
		TypeSpecReal typeSpecReal = jnaDataTypes_.test_GetTypeSpecReal();
		assertNotNull(typeSpecReal);
		
		ScalarVariableRealStruct scalarVariableRealStruct = jnaDataTypes_.test_GetScalarVariableRealStruct();
		assertNotNull(scalarVariableRealStruct);

		
		return;
	
	}
	
	@Test
	public void T01_TypeSpecReal() {
		
		TypeSpecReal typeSpecReal_0 = jnaDataTypes_.test_GetTypeSpecReal();
		assertNotNull(typeSpecReal_0);
		
		Assert.assertEquals(20.25, typeSpecReal_0.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_0.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_0.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_0.unit);

		Assert.assertEquals(1, typeSpecReal_0.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.unitValueStatus, 0.0);
		
		return;
	
	}
	
	
	@Test
	public void T02_ScalarVariableRealStruct() {
		

		ScalarVariableRealStruct scalarVariableReal_0 = jnaDataTypes_.test_GetScalarVariableRealStruct();
		assertNotNull(scalarVariableReal_0);
		
		
		int causality = Enu.enu_input.getIntValue();
		Assert.assertEquals(causality, scalarVariableReal_0.causality);
		Assert.assertEquals("The Description", scalarVariableReal_0.description);
		Assert.assertEquals(1, scalarVariableReal_0.idx);
		Assert.assertEquals("scalarVar name", scalarVariableReal_0.name);
		
		int variability = Enu.enu_continuous.getIntValue();
		Assert.assertEquals(variability, scalarVariableReal_0.variability);
		
		Assert.assertEquals(125420, scalarVariableReal_0.valueReference);
		TypeSpecReal.ByReference typeSpecReal_0 = scalarVariableReal_0.typeSpecReal;
		
		Assert.assertEquals(20.25, typeSpecReal_0.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_0.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_0.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_0.unit);

		Assert.assertEquals(1, typeSpecReal_0.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.unitValueStatus, 0.0);
		
		
		return;
	
	}
	
	
	
	@Test
	public void T03_ScalarVariableCollectionStruct() {
		
		
		ScalarVariableCollectionStruct svCollectionStruct_0 = jnaDataTypes_.test_GetScalarVariableCollectionStruct();
		assertNotNull(svCollectionStruct_0);
		
		//ScalarVariableRealStruct realValueAry = svCollectionStruct_0.realValue;
		ScalarVariableRealStruct[] realVariableAry = svCollectionStruct_0.getRealAsArray(0);
		
		int realSize = svCollectionStruct_0.realSize;
		
		Assert.assertEquals(2, realSize);
		
		ScalarVariableRealStruct  scalarVariableReal_0 = realVariableAry[0];
		ScalarVariableRealStruct  scalarVariableReal_1 = realVariableAry[1];
		
		assertNotNull(scalarVariableReal_0);
		assertNotNull(scalarVariableReal_1);
		
		int causality = Enu.enu_input.getIntValue();
		Assert.assertEquals(causality, scalarVariableReal_0.causality);
		Assert.assertEquals("The Description", scalarVariableReal_0.description);
		Assert.assertEquals(1, scalarVariableReal_0.idx);
		Assert.assertEquals("scalarVar name", scalarVariableReal_0.name);
		
		int variability = Enu.enu_continuous.getIntValue();
		Assert.assertEquals(variability, scalarVariableReal_0.variability);
		
		Assert.assertEquals(125420, scalarVariableReal_0.valueReference);
		TypeSpecReal.ByReference typeSpecReal_0 = scalarVariableReal_0.typeSpecReal;
		
		Assert.assertEquals(20.25, typeSpecReal_0.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_0.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_0.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_0.unit);
		
		Assert.assertEquals(1, typeSpecReal_0.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.unitValueStatus, 0.0);
		
		
		int causality2 = Enu.enu_input.getIntValue();
		Assert.assertEquals(causality2, scalarVariableReal_1.causality);
		Assert.assertEquals("The Description", scalarVariableReal_1.description);
		Assert.assertEquals(2, scalarVariableReal_1.idx);
		Assert.assertEquals("scalarVar name", scalarVariableReal_1.name);
		
		int variability2 = Enu.enu_continuous.getIntValue();
		Assert.assertEquals(variability2, scalarVariableReal_1.variability);
		
		Assert.assertEquals(125420, scalarVariableReal_1.valueReference);
		TypeSpecReal.ByReference typeSpecReal_1 = scalarVariableReal_1.typeSpecReal;
		
		Assert.assertEquals(20.25, typeSpecReal_1.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_1.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_1.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_1.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_1.unit);
		
		Assert.assertEquals(1, typeSpecReal_1.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.unitValueStatus, 0.0);
		
		
		
		
		return;
		
	}
	
	
	
	@Test
	public void T04_ScalarVariableCollection() {
		
		
		ScalarVariableCollectionStruct.ByReference svCollectionStruct_0 = jnaDataTypes_.test_GetScalarVariableCollectionStruct();
		assertNotNull(svCollectionStruct_0);
		
		
		ScalarVariableCollection svCollection_0 = new ScalarVariableCollection(svCollectionStruct_0);
		

		Vector<ScalarVariableReal> realList = svCollection_0.getRealVarList();
		
		ScalarVariableReal  scalarVariableReal_0 = realList.get(0);
		ScalarVariableReal  scalarVariableReal_1 = realList.get(1);
		
		assertNotNull(scalarVariableReal_0);
		assertNotNull(scalarVariableReal_1);
		
		Assert.assertEquals("C1", scalarVariableReal_0.getUnit());
		
		Assert.assertEquals( Enu.enu_input, scalarVariableReal_0.getCausalityAsEnum());
		Assert.assertEquals( Enu.enu_input.getIntValue(), scalarVariableReal_0.getCausalityAsInt());
		Assert.assertEquals( "input", scalarVariableReal_0.getCausalityAsString());
		
		Assert.assertEquals("The Description", scalarVariableReal_0.getDescription());
		Assert.assertEquals(1, scalarVariableReal_0.getIdx());
		Assert.assertEquals("scalarVar name", scalarVariableReal_0.getName());
		
		Assert.assertEquals( Enu.enu_continuous, scalarVariableReal_0.getVariabilityAsEnum());
		Assert.assertEquals( Enu.enu_continuous.getIntValue(), scalarVariableReal_0.getVariabilityAsInt());
		Assert.assertEquals( "continuous", scalarVariableReal_0.getVariabilityAsString());
		
		Assert.assertEquals(125420, scalarVariableReal_0.getValueReference());
			
		
		TypeSpecReal typeSpecReal_0 = scalarVariableReal_0.getTypeSpecReal();
		
		Assert.assertEquals(20.25, typeSpecReal_0.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_0.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_0.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_0.unit);
		
		Assert.assertEquals(1, typeSpecReal_0.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.unitValueStatus, 0.0);
		
		Assert.assertEquals("C1", scalarVariableReal_1.getUnit());
		
		Assert.assertEquals( Enu.enu_input, scalarVariableReal_1.getCausalityAsEnum());
		Assert.assertEquals( Enu.enu_input.getIntValue(), scalarVariableReal_1.getCausalityAsInt());
		Assert.assertEquals( "input", scalarVariableReal_1.getCausalityAsString());
		
		Assert.assertEquals("The Description", scalarVariableReal_1.getDescription());
		Assert.assertEquals(2, scalarVariableReal_1.getIdx());
		Assert.assertEquals("scalarVar name", scalarVariableReal_1.getName());
		
		Assert.assertEquals( Enu.enu_continuous, scalarVariableReal_1.getVariabilityAsEnum());
		Assert.assertEquals( Enu.enu_continuous.getIntValue(), scalarVariableReal_1.getVariabilityAsInt());
		Assert.assertEquals( "continuous", scalarVariableReal_1.getVariabilityAsString());
		
		Assert.assertEquals(125420, scalarVariableReal_1.getValueReference());
			
		
		TypeSpecReal typeSpecReal_1 = scalarVariableReal_1.getTypeSpecReal();
		
		Assert.assertEquals(20.25, typeSpecReal_1.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_1.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_1.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_1.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_1.unit);
		
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

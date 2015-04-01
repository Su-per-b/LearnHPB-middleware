package com.sri.straylight.fmuWrapper.test.jna;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.sri.straylight.fmuWrapper.JNAfmuWrapper;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.MessageCallbackInterface;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.ResultCallbackInterface;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.StateChangeCallbackInterface;
import com.sri.straylight.fmuWrapper.voManaged.FMImodelAttributes;
import com.sri.straylight.fmuWrapper.voNative.AttributeStruct;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.EnumTypeMapper;
import com.sri.straylight.fmuWrapper.voNative.FMImodelAttributesStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sun.jna.Library;
import com.sun.jna.Native;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JNAfunctionsT {
	
	final private String pathToNativeLibs_ = "E:\\LHPB\\LearnHPB-middleware\\visualStudioWorkspace\\bin\\Debug";
	final private String pathToFMUfolder_ = "E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test";


	//final private String pathToFMUfolder_ = "E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit3_summer";
	
	
	
	
	private JNAfmuWrapper jnaFMUWrapper_;
	
	

	//runs before each test
	@Before
	public void beforeEachTest() {
		
		
		Assert.assertNull(jnaFMUWrapper_);
		System.setProperty("jna.library.path", pathToNativeLibs_ );
		
		EnumTypeMapper mp = new EnumTypeMapper();
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(Library.OPTION_TYPE_MAPPER, mp);
		
		String dllName = "FMUwrapper";
		jnaFMUWrapper_ = (JNAfmuWrapper) Native.loadLibrary(dllName, JNAfmuWrapper.class, options);
		assertNotNull(jnaFMUWrapper_);
		
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		
	}
	
	//runs after each test
	@After
	public void afterEachTest() {
		
		
		int result_0 = jnaFMUWrapper_.forceCleanup();
		Assert.assertEquals(0, result_0);
		
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);	
		
	}
	
	
	
	private void assertSimState_(SimStateNative simStateNativeExpected) {
		
		
		SimStateNative simStateNativeActual = jnaFMUWrapper_.getSimStateNative();
		Assert.assertEquals(simStateNativeExpected, simStateNativeActual);	
		
	}
	
	
	
	
	
	@Test
	public void T01_ForceCleanup() {
		
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);			
		
	}
	

	
	@Test
	public void T02_Connect() {
		
		
		MessageCallback messageCallbackHandler = new MessageCallback();
		ResultCallback resultCallbackHandler = new ResultCallback();
		StateChangeCallback stateChangeCallbackHandler = new StateChangeCallback();
		
		jnaFMUWrapper_.connect(
			messageCallbackHandler,
			resultCallbackHandler,
			stateChangeCallbackHandler
		);
		
		assertSimState_(SimStateNative.simStateNative_1_connect_completed);		
		
	}
	
	

	
	@Test
	public void T03_XmlParse() {
		
		T02_Connect();

		jnaFMUWrapper_.xmlParse(pathToFMUfolder_);
		
		assertSimState_(SimStateNative.simStateNative_2_xmlParse_completed);		
		
		return;
	}
	
	
	
	@Test
	public void T04_Config() {
		
		T03_XmlParse();
		
		ConfigStruct configStruct = jnaFMUWrapper_.getConfig();
		DefaultExperimentStruct  defaultExperimentStruct = configStruct.defaultExperimentStruct;
		
		Assert.assertEquals(28000.0, defaultExperimentStruct.startTime, 0.0);
		Assert.assertEquals(86400.0, defaultExperimentStruct.stopTime, 0.0);
		Assert.assertEquals(9.9999999999999995e-007, defaultExperimentStruct.tolerance, 0.0);
		
		Assert.assertEquals(300.0, configStruct.stepDelta, 0.0);
		
		
		return;
	}
	
	
	


	@Test
	public void T05_Attributes() {
		
		T03_XmlParse();
		
		FMImodelAttributesStruct fmiModelAttributesStruct = jnaFMUWrapper_.getFMImodelAttributesStruct();
		assertNotNull(fmiModelAttributesStruct);
		
		FMImodelAttributes fmiModelAttributes = new FMImodelAttributes(fmiModelAttributesStruct);
		assertNotNull(fmiModelAttributes);

		Vector<AttributeStruct>  vec = fmiModelAttributes.toVector();
		
		AttributeStruct attributeStruct_0 = vec.get(0);
		Assert.assertEquals("fmiVersion", attributeStruct_0.name);
		Assert.assertEquals("1.0", attributeStruct_0.value);
		
		
		AttributeStruct attributeStruct_1 = vec.get(1);
		Assert.assertEquals("modelName", attributeStruct_1.name);
		Assert.assertEquals("LearnGB_v4_2.VAVReheat.ClosedLoop", attributeStruct_1.value);

		AttributeStruct attributeStruct_2 = vec.get(2);
		Assert.assertEquals("modelIdentifier", attributeStruct_2.name);
		Assert.assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop", attributeStruct_2.value);
		
		AttributeStruct attributeStruct_3 = vec.get(3);
		Assert.assertEquals("guid", attributeStruct_3.name);
		Assert.assertEquals("{a682377c-3cd5-4bb9-9844-b0e426b1eb15}", attributeStruct_3.value);
		
		AttributeStruct attributeStruct_4 = vec.get(4);
		Assert.assertEquals("description", attributeStruct_4.name);
		Assert.assertEquals("Variable air volume flow system with terminal reheat and five thermal zones", attributeStruct_4.value);
		
		AttributeStruct attributeStruct_5 = vec.get(5);
		Assert.assertEquals("version", attributeStruct_5.name);
		Assert.assertEquals("5", attributeStruct_5.value);
		
		AttributeStruct attributeStruct_6 = vec.get(6);
		Assert.assertEquals("generationTool", attributeStruct_6.name);
		Assert.assertEquals("Dymola Version 2013 (32-bit), 2012-03-28", attributeStruct_6.value);

		AttributeStruct attributeStruct_7 = vec.get(7);
		Assert.assertEquals("generationDateAndTime", attributeStruct_7.name);
		Assert.assertEquals("2012-09-07T23:42:12Z", attributeStruct_7.value);
		
		AttributeStruct attributeStruct_8 = vec.get(8);
		Assert.assertEquals("variableNamingConvention", attributeStruct_8.name);
		Assert.assertEquals("structured", attributeStruct_8.value);
		
		AttributeStruct attributeStruct_9 = vec.get(9);
		Assert.assertEquals("numberOfContinuousStates", attributeStruct_9.name);
		Assert.assertEquals("358", attributeStruct_9.value);
		

		assertSimState_(SimStateNative.simStateNative_2_xmlParse_completed);
		
		return;
	}
	
	
	
	
//	@Test
//	public void T06_UnitDefinitions() {
//		T03_XmlParse();
//		
//		BaseUnitStruct   baseUnitStructAry = jnaFMUWrapper_.getUnitDefinitions();
//		DisplayUnitDefinitionStruct
//		
//		
//	}
//	
//
//	@Test
//	public void T07_ScalarVariables() {
//		
//	
//
//		
//		
//	}
	
	
	
	
	
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

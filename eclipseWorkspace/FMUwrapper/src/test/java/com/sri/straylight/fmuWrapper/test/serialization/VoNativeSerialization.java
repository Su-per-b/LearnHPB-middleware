package com.sri.straylight.fmuWrapper.test.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;

public class VoNativeSerialization {
	
	
	/** The for serialization. */
	private  JsonController gsonController_ = JsonController.getInstance();
    
	
	public final static String STR_defaultExperimentStruct_0 = "{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10}";
	
	public final static String STR_defaultExperimentStruct_1 = "{\"t\":\"DefaultExperimentStruct\",\"startTime\":0,\"stopTime\":100,\"tolerance\":1.1}";
	
	public final static String STR_configStruct_0 = "{\"t\":\"ConfigStruct\",\"stepDelta\":1,\"defaultExperimentStruct\":{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10}}";
	
	public final static String STR_messageStruct_0 = "{\"t\":\"MessageStruct\",\"msgText\":\"This is the message text\",\"messageType\":0}";
	
	public final static String STR_simStateNative_0 = "{\"t\":\"SimStateNative\",\"intValue\":0}";
	
	public final static String STR_typeSpecReal_0 = "{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"K\"}";


	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void defaultExperimentStruct_serialize() {

		
		DefaultExperimentStruct.ByReference defaultExperimentStruct_0
			= new DefaultExperimentStruct.ByReference();
		
		defaultExperimentStruct_0.startTime = 123.03;
		defaultExperimentStruct_0.stopTime = 145.03;
		defaultExperimentStruct_0.tolerance = 10.0;
		
		String jsonString_0 = defaultExperimentStruct_0.toJsonString();
		
		assertEquals(
				STR_defaultExperimentStruct_0, 
				jsonString_0);
		
		
		DefaultExperimentStruct.ByReference defaultExperimentStruct_1
		= new DefaultExperimentStruct.ByReference();
	
		defaultExperimentStruct_1.startTime = 0.0;
		defaultExperimentStruct_1.stopTime = 100.0;
		defaultExperimentStruct_1.tolerance = 1.1;
	
		String jsonString_1 = defaultExperimentStruct_1.toJsonString();
		
		assertEquals(
				STR_defaultExperimentStruct_1, 
				jsonString_1
		);
		
		

	}
	
	
	
	@Test
	public void defaultExperimentStruct_deserialize() {
		

		Object deserializedObject_0 = gsonController_.fromJson(STR_defaultExperimentStruct_0);
		
		//assert
		assertEquals(DefaultExperimentStruct.ByReference.class, deserializedObject_0.getClass());

		DefaultExperimentStruct.ByReference defaultExperimentStruct_0 = (DefaultExperimentStruct.ByReference) deserializedObject_0;
		
		assertEquals(123.03, defaultExperimentStruct_0.startTime, 0.0);
		assertEquals(145.03, defaultExperimentStruct_0.stopTime, 0.0);
		assertEquals(10.0, defaultExperimentStruct_0.tolerance, 0.0);
		
		
		
		Object deserializedObject_1 = gsonController_.fromJson(STR_defaultExperimentStruct_1);
		
		//assert
		assertEquals(DefaultExperimentStruct.ByReference.class, deserializedObject_1.getClass());

		DefaultExperimentStruct.ByReference defaultExperimentStruct_1 = (DefaultExperimentStruct.ByReference) deserializedObject_1;
		
		assertEquals(0.0, defaultExperimentStruct_1.startTime, 0.0);
		assertEquals(100.0, defaultExperimentStruct_1.stopTime, 0.0);
		assertEquals(1.1, defaultExperimentStruct_1.tolerance, 0.0);
		

	}
	
	
	
	@Test
	public void configStruct_serialize() {

		
		DefaultExperimentStruct.ByReference defaultExperimentStruct 
		= new DefaultExperimentStruct.ByReference();

		defaultExperimentStruct.startTime = 123.03;
		defaultExperimentStruct.stopTime = 145.03;
		defaultExperimentStruct.tolerance = 10.0;
		
		ConfigStruct configStruct = new ConfigStruct();
		configStruct.stepDelta = 1.0;
		configStruct.defaultExperimentStruct = defaultExperimentStruct;
		
		
		String jsonString_0 = configStruct.toJsonString();
		
		assertEquals(
				STR_configStruct_0,
				jsonString_0
		);
		
	}
	
	@Test
	public void configStruct_deserialize() {

		
		Object deserializedObject_0 = gsonController_.fromJson(STR_configStruct_0);
		
		assertEquals(ConfigStruct.class, deserializedObject_0.getClass());
		
		ConfigStruct configStruct_0 = (ConfigStruct) deserializedObject_0;
		
		
		assertEquals(1.0, configStruct_0.stepDelta, 0.0);
		
		assertEquals(123.03, configStruct_0.defaultExperimentStruct.startTime, 0.0);
		assertEquals(145.03, configStruct_0.defaultExperimentStruct.stopTime, 0.0);
		assertEquals(10.0, configStruct_0.defaultExperimentStruct.tolerance, 0.0);
		
	}
	
	
	@Test
	public void messageStruct_serialize() {

    	MessageStruct messageStruct = new MessageStruct();
    	messageStruct.msgText = "This is the message text";
    	
    	messageStruct.setMessageTypeEnum(MessageType.messageType_debug);
    	String jsonString_0 = messageStruct.toJsonString();
    	
    	
		assertEquals(
				STR_messageStruct_0,
				jsonString_0
		);
		
    	

		return;
		
	}
	
	
	@Test
	public void messageStruct_deserialize() {
		
		Object deserializedObject = gsonController_.fromJson(STR_messageStruct_0);
		
		assertEquals(MessageStruct.class, deserializedObject.getClass());
		
		MessageStruct messageStruct_0 = (MessageStruct) deserializedObject;
		
		assertEquals(0, messageStruct_0.messageType);
		assertEquals("This is the message text", messageStruct_0.msgText);
		
		
	}
	
	
	@Test
	public void simStateNative_serialize() {

		//make SimStateNative 1
		SimStateNative simStateNative_0 = SimStateNative.simStateNative_0_uninitialized;
		
		//serialize / deserialize 
		String jsonString_0 = simStateNative_0.toJsonString();
		
		assertEquals(
				STR_simStateNative_0, 
				jsonString_0
		);

	}
	
	
	@Test
	public void simStateNative_deserialize() {

		Object deserializedObject = gsonController_.fromJson(STR_simStateNative_0);
		
		assertEquals(SimStateNative.class, deserializedObject.getClass());
		SimStateNative simStateNative_0 = (SimStateNative) deserializedObject;
			
		assertEquals(0, simStateNative_0.getIntValue());
		assertEquals("simStateNative_0_uninitialized", simStateNative_0.name());
		

	}
	
	
	@Test
	public void typeSpecReal_serialize() {

		//make
		TypeSpecReal typeSpecReal_0 = new TypeSpecReal();
		typeSpecReal_0.start = 20.25;
		typeSpecReal_0.nominal = 21.25;
		typeSpecReal_0.min = 22.25;
		typeSpecReal_0.max = 23.25;
		typeSpecReal_0.unit = "K";
		
		typeSpecReal_0.startValueStatus = 1;
		typeSpecReal_0.nominalValueStatus = 1;
		typeSpecReal_0.minValueStatus = 1;
		typeSpecReal_0.maxValueStatus = 1;
		typeSpecReal_0.unitValueStatus = 1;
		

		//serialize / deserialize 
		String jsonString_0 = typeSpecReal_0.toJsonString();
		
		assertEquals(
				STR_typeSpecReal_0,
				jsonString_0);

	}
	
	
	@Test
	public void typeSpecReal_deserialize() {
		
		Object deserializedObject = gsonController_.fromJson(STR_typeSpecReal_0);
		
		assertEquals(TypeSpecReal.class, deserializedObject.getClass());
		TypeSpecReal typeSpecReal_0 = (TypeSpecReal) deserializedObject;
		
		assertEquals(20.25, typeSpecReal_0.start, 0.0);
		assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		assertEquals(22.25, typeSpecReal_0.min, 0.0);
		assertEquals(23.25, typeSpecReal_0.max, 0.0);
		assertEquals("K", typeSpecReal_0.unit);
		
	}
	

	
}

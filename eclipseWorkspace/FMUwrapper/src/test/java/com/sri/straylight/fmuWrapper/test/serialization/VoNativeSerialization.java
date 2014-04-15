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

		
		DefaultExperimentStruct.ByReference theStruct 
			= new DefaultExperimentStruct.ByReference();
		
		theStruct.startTime = 123.03;
		theStruct.stopTime = 145.03;
		theStruct.tolerance = 10.0;
		
		String json = theStruct.toJsonString();
		
		assertEquals(
				"{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10.0}", 
				json);
		

	}
	
	
	
	@Test
	public void defaultExperimentStruct_deserialize() {
		
		String jsonString = "{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10.0}";
		Object deserializedObject = gsonController_.fromJson(jsonString);
		
		//assert
		assertEquals(DefaultExperimentStruct.ByReference.class, deserializedObject.getClass());

		DefaultExperimentStruct.ByReference defaultExperimentStruct = (DefaultExperimentStruct.ByReference) deserializedObject;
		
		assertEquals(123.03, defaultExperimentStruct.startTime, 0.0);
		assertEquals(145.03, defaultExperimentStruct.stopTime, 0.0);
		assertEquals(10.0, defaultExperimentStruct.tolerance, 0.0);

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
		
		
		String json = configStruct.toJsonString();
		
		assertEquals(
				"{\"t\":\"ConfigStruct\",\"stepDelta\":1.0,\"defaultExperimentStruct\":{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10.0}}", 
				json);
		
	}
	
	@Test
	public void configStruct_deserialize() {

		
		String jsonString = "{\"t\":\"ConfigStruct\",\"stepDelta\":1.0,\"defaultExperimentStruct\":{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10.0}}";
		Object deserializedObject = gsonController_.fromJson(jsonString);
		
		assertEquals(ConfigStruct.class, deserializedObject.getClass());
		
		ConfigStruct configStruct = (ConfigStruct) deserializedObject;
		
		
		assertEquals(1.0, configStruct.stepDelta, 0.0);
		
		assertEquals(123.03, configStruct.defaultExperimentStruct.startTime, 0.0);
		assertEquals(145.03, configStruct.defaultExperimentStruct.stopTime, 0.0);
		assertEquals(10.0, configStruct.defaultExperimentStruct.tolerance, 0.0);
		
	}
	
	
	@Test
	public void messageStruct_serialize() {

    	MessageStruct messageStruct = new MessageStruct();
    	messageStruct.msgText = "testMessageStruct";
    	
    	messageStruct.setMessageTypeEnum(MessageType.messageType_debug);
    	String json = messageStruct.toJsonString();
    	
		assertEquals(
				"{\"t\":\"MessageStruct\",\"msgText\":\"testMessageStruct\",\"messageType\":0}", 
				json);
    	

		return;
		
	}
	
	
	@Test
	public void messageStruct_deserialize() {
		
		String jsonString = "{\"t\":\"MessageStruct\",\"msgText\":\"testMessageStruct\",\"messageType\":0}";
		Object deserializedObject = gsonController_.fromJson(jsonString);
		
		assertEquals(MessageStruct.class, deserializedObject.getClass());
		
		MessageStruct messageStruct = (MessageStruct) deserializedObject;
		
		assertEquals(0, messageStruct.messageType, 0.0);
		assertEquals("testMessageStruct", messageStruct.msgText);
		
		
	}
	
	
	@Test
	public void simStateNative_serialize() {

		//make SimStateNative 1
		SimStateNative simStateNative1 = SimStateNative.simStateNative_0_uninitialized;
		
		//serialize / deserialize 
		String json = simStateNative1.toJsonString();
		
		assertEquals(
				"{\"t\":\"SimStateNative\",\"intValue\":0}", 
				json);

	}
	
	
	@Test
	public void simStateNative_deserialize() {

		String jsonString = "{\"t\":\"SimStateNative\",\"intValue\":0}";
		Object deserializedObject = gsonController_.fromJson(jsonString);
		
		assertEquals(SimStateNative.class, deserializedObject.getClass());
		SimStateNative simStateNative = (SimStateNative) deserializedObject;
			
		assertEquals(0, simStateNative.getIntValue());
		assertEquals("simStateNative_0_uninitialized", simStateNative.name());
		

	}
	
	
	@Test
	public void typeSpecReal_serialize() {

		//make
		TypeSpecReal typeSpecReal = new TypeSpecReal();
		typeSpecReal.start = 20.25;
		typeSpecReal.nominal = 21.25;
		typeSpecReal.min = 22.25;
		typeSpecReal.max = 23.25;
		typeSpecReal.unit = "K";
		
		typeSpecReal.startValueStatus = 1;
		typeSpecReal.nominalValueStatus = 1;
		typeSpecReal.minValueStatus = 1;
		typeSpecReal.maxValueStatus = 1;
		typeSpecReal.unitValueStatus = 1;
		

		//serialize / deserialize 
		String json = typeSpecReal.toJsonString();
		
		assertEquals(
				"{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"K\"}",
				json);

	}
	
	
	@Test
	public void typeSpecReal_deserialize() {
		
		String jsonString = "{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"K\",\"startValueStatus\":1,\"nominalValueStatus\":1,\"minValueStatus\":1,\"maxValueStatus\":1,\"unitValueStatus\":1}";
		Object deserializedObject = gsonController_.fromJson(jsonString);
		
		assertEquals(TypeSpecReal.class, deserializedObject.getClass());
		TypeSpecReal typeSpecReal = (TypeSpecReal) deserializedObject;
		
		assertEquals(20.25, typeSpecReal.start, 0.0);
		assertEquals(21.25, typeSpecReal.nominal, 0.0);
		assertEquals(22.25, typeSpecReal.min, 0.0);
		assertEquals(23.25, typeSpecReal.max, 0.0);
		assertEquals("K", typeSpecReal.unit);
		

		
	}
	

	
}

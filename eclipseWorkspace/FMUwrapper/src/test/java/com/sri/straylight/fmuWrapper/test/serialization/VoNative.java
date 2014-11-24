package com.sri.straylight.fmuWrapper.test.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.test.base.OrderedRunner;
import com.sri.straylight.fmuWrapper.test.main.CONSTANTS;
import com.sri.straylight.fmuWrapper.test.main.Util;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;

@RunWith(OrderedRunner.class)
public class VoNative {
	
	
	
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
	public void T01_defaultExperimentStruct_serialize() {

		DefaultExperimentStruct.ByReference defaultExperimentStruct_0
			= new DefaultExperimentStruct.ByReference();
		
		defaultExperimentStruct_0.startTime = 123.03;
		defaultExperimentStruct_0.stopTime = 145.03;
		defaultExperimentStruct_0.tolerance = 10.0;
		
	    Util.serializeOk(
	      defaultExperimentStruct_0,
	      CONSTANTS.STR_defaultExperimentStruct_0
	    );
	    	
		
		DefaultExperimentStruct.ByReference defaultExperimentStruct_1
		= new DefaultExperimentStruct.ByReference();
	
		defaultExperimentStruct_1.startTime = 0.0;
		defaultExperimentStruct_1.stopTime = 100.0;
		defaultExperimentStruct_1.tolerance = 1.1;
	
		
		Util.serializeOk(
			defaultExperimentStruct_1,
			CONSTANTS.STR_defaultExperimentStruct_1
		);
	    
	}
	
	
	
	@Test
	public void T02_defaultExperimentStruct_deserialize() {
		

		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_defaultExperimentStruct_0,
	      DefaultExperimentStruct.ByReference.class
	    );
	    
		DefaultExperimentStruct.ByReference defaultExperimentStruct_0 = (DefaultExperimentStruct.ByReference) deserializedObject_0;
		
		assertEquals(123.03, defaultExperimentStruct_0.startTime, 0.0);
		assertEquals(145.03, defaultExperimentStruct_0.stopTime, 0.0);
		assertEquals(10.0, defaultExperimentStruct_0.tolerance, 0.0);
		
		
		Iserializable deserializedObject_1 = Util.deserializeOk(
	      CONSTANTS.STR_defaultExperimentStruct_1,
	      DefaultExperimentStruct.ByReference.class
	    );
		
		DefaultExperimentStruct.ByReference defaultExperimentStruct_1 = (DefaultExperimentStruct.ByReference) deserializedObject_1;
		
		assertEquals(0.0, defaultExperimentStruct_1.startTime, 0.0);
		assertEquals(100.0, defaultExperimentStruct_1.stopTime, 0.0);
		assertEquals(1.1, defaultExperimentStruct_1.tolerance, 0.0);
		

	}
	
	
	
	@Test
	public void T03_configStruct_serialize() {

		
		DefaultExperimentStruct.ByReference defaultExperimentStruct 
		= new DefaultExperimentStruct.ByReference();

		defaultExperimentStruct.startTime = 123.03;
		defaultExperimentStruct.stopTime = 145.03;
		defaultExperimentStruct.tolerance = 10.0;
		
		ConfigStruct configStruct = new ConfigStruct();
		configStruct.stepDelta = 1.0;
		configStruct.defaultExperimentStruct = defaultExperimentStruct;
		
	    Util.serializeOk(
	    	configStruct,
	    	CONSTANTS.STR_configStruct_0
  	    );

	    
	    
	}
	
	@Test
	public void T04_configStruct_deserialize() {

		
		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_configStruct_0,
	      ConfigStruct.class
	    );
		
		ConfigStruct configStruct_0 = (ConfigStruct) deserializedObject_0;
		
		assertEquals(1.0, configStruct_0.stepDelta, 0.0);
		
		assertEquals(123.03, configStruct_0.defaultExperimentStruct.startTime, 0.0);
		assertEquals(145.03, configStruct_0.defaultExperimentStruct.stopTime, 0.0);
		assertEquals(10.0, configStruct_0.defaultExperimentStruct.tolerance, 0.0);
		
	}
	
	
	@Test
	public void T05_messageStruct_serialize() {

    	MessageStruct messageStruct = new MessageStruct();
    	messageStruct.msgText = "This is the message text";
    	
    	messageStruct.setMessageTypeEnum(MessageType.messageType_debug);

		
    	Util.serializeOk(
    			messageStruct,
    			CONSTANTS.STR_messageStruct_0
    		);
    	
		
	}
	
	
	@Test
	public void T06_messageStruct_deserialize() {
		

		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_messageStruct_0,
	      MessageStruct.class
	    );
		
		MessageStruct messageStruct_0 = (MessageStruct) deserializedObject_0;
		
		assertEquals(0, messageStruct_0.messageType);
		assertEquals("This is the message text", messageStruct_0.msgText);
		
		
	}
	
	
	@Test
	public void T07_simStateNative_serialize() {

		//make SimStateNative 1
		SimStateNative simStateNative_0 = SimStateNative.simStateNative_0_uninitialized;
		
		
    	Util.serializeOk(
    			simStateNative_0,
    			CONSTANTS.STR_simStateNative_0
    		);

	}
	
	
	@Test
	public void T08_simStateNative_deserialize() {

		
		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_simStateNative_0,
	      SimStateNative.class
	    );
		
		SimStateNative simStateNative_0 = (SimStateNative) deserializedObject_0;
			
		assertEquals(0, simStateNative_0.getIntValue());
		assertEquals("simStateNative_0_uninitialized", simStateNative_0.name());
		

	}
	
	
	@Test
	public void T09_typeSpecReal_serialize() {

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
		

    	Util.serializeOk(
    			typeSpecReal_0,
    			CONSTANTS.STR_typeSpecReal_0
    		);

	}
	
	
	@Test
	public void T10_typeSpecReal_deserialize() {

		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_typeSpecReal_0,
			TypeSpecReal.class
		);
		
		TypeSpecReal typeSpecReal_0 = (TypeSpecReal) deserializedObject_0;
		
		assertEquals(20.25, typeSpecReal_0.start, 0.0);
		assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		assertEquals(22.25, typeSpecReal_0.min, 0.0);
		assertEquals(23.25, typeSpecReal_0.max, 0.0);
		assertEquals("K", typeSpecReal_0.unit);
		
	}
	

	
}

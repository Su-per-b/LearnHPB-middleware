package com.sri.straylight.fmuWrapper.test.serialization;

import static org.junit.Assert.assertEquals;






import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;






import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.event.SessionControlClientRequest;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
import com.sri.straylight.fmuWrapper.voManaged.SerializableVector;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlAction;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlModel;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;
import com.sri.straylight.fmuWrapper.test.base.OrderedRunner;
import com.sri.straylight.fmuWrapper.test.main.CONSTANTS;
import com.sri.straylight.fmuWrapper.test.main.TestDataGenerator;
import com.sri.straylight.fmuWrapper.test.main.Util;


@RunWith(OrderedRunner.class)
public class Event {
	
	
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
    public void T01_messageEvent_serialize() {
    	
    	MessageStruct messageStruct_0 = new MessageStruct();
    	messageStruct_0.msgText = "This is the test Message Text";
    	messageStruct_0.setMessageTypeEnum(MessageType.messageType_debug);
    	
    	MessageEvent event_0 = new MessageEvent(this, messageStruct_0);

		Util.serializeOk(
			event_0,
    	    CONSTANTS.STR_messageEvent_0
		);
		
    }
	
	

	@Test
    public void T02_messageEvent_deserialize() {
    	
		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_messageEvent_0,
	      MessageEvent.class
	    );
		
		MessageEvent event_0 = (MessageEvent) deserializedObject_0;
    	MessageStruct messageStruct_0 = event_0.getPayload();
		
		assertEquals("This is the test Message Text", messageStruct_0.msgText);
		assertEquals(MessageType.messageType_debug, messageStruct_0.getMessageTypeEnum());
		assertEquals(0, messageStruct_0.messageType);
		
    }
	
	
	@Test
	public void T03_configChangeNotify_serialize() {
		
		DefaultExperimentStruct.ByReference defaultExperimentStruct_0 
		= new DefaultExperimentStruct.ByReference();
		
		defaultExperimentStruct_0.startTime = 123.03;
		defaultExperimentStruct_0.stopTime = 145.03;
		defaultExperimentStruct_0.tolerance = 10.0;
		
		ConfigStruct configStruct_0 = new ConfigStruct();
		configStruct_0.stepDelta = 1.0;
		configStruct_0.defaultExperimentStruct = defaultExperimentStruct_0;
		
		
		ConfigChangeNotify event_0 = new ConfigChangeNotify(this,configStruct_0);

		
		Util.serializeOk(
			event_0,
    	    CONSTANTS.STR_configChangeNotify_0
		);
		
		
	}
	
	
	@Test
	public void T04_configChangeNotify_deserialize() {

		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_configChangeNotify_0,
	      ConfigChangeNotify.class
	    );
		
		ConfigChangeNotify event_0 = (ConfigChangeNotify) deserializedObject_0;
		
		ConfigStruct configStruct_0 = event_0.getPayload();
		
		assertEquals(1.0, configStruct_0.stepDelta, 0.0);
		
		assertEquals(123.03, configStruct_0.defaultExperimentStruct.startTime, 0.0);
		assertEquals(145.03, configStruct_0.defaultExperimentStruct.stopTime, 0.0);
		assertEquals(10.0, configStruct_0.defaultExperimentStruct.tolerance, 0.0);	
		
	}
	
	
	@Test
    public void T05_resultEvent_serialize() {
		
		//make real 1
		ScalarValueRealStruct scalarValueRealStruct_0 = new ScalarValueRealStruct();
		scalarValueRealStruct_0.idx = 1;
		scalarValueRealStruct_0.value = 2.0;
		ScalarValueReal scalarValueReal_0 = new ScalarValueReal(scalarValueRealStruct_0);
		
		//make real 2
		ScalarValueRealStruct scalarValueRealStruct_1 = new ScalarValueRealStruct();
		scalarValueRealStruct_1.idx = 2;
		scalarValueRealStruct_1.value = 3.53;
		ScalarValueReal scalarValueReal_1 = new ScalarValueReal(scalarValueRealStruct_1);
		
		//make real list
		SerializableVector<ScalarValueReal> realList_0 = new SerializableVector<ScalarValueReal>("ScalarValueReal");
		realList_0.add(scalarValueReal_0);
		realList_0.add(scalarValueReal_1);
		
		ScalarValueCollection scalarValueCollection_0 = new ScalarValueCollection();
		scalarValueCollection_0.setRealList(realList_0);
		
		//make real 3
		ScalarValueRealStruct scalarValueRealStruct_2 = new ScalarValueRealStruct();
		scalarValueRealStruct_2.idx = 3;
		scalarValueRealStruct_2.value = 258.2;
		ScalarValueReal scalarValueReal_2 = new ScalarValueReal(scalarValueRealStruct_2);
		
		//make real 4
		ScalarValueRealStruct scalarValueRealStruct_3 = new ScalarValueRealStruct();
		scalarValueRealStruct_3.idx = 4;
		scalarValueRealStruct_3.value = 78.0;
		ScalarValueReal scalarValueReal_3 = new ScalarValueReal(scalarValueRealStruct_3);
		
		//make real list
		SerializableVector<ScalarValueReal> realList_1 = new SerializableVector<ScalarValueReal>("ScalarValueReal");
		realList_1.add(scalarValueReal_2);
		realList_1.add(scalarValueReal_3);
		
		ScalarValueCollection scalarValueCollection_1 = new ScalarValueCollection();
		scalarValueCollection_1.setRealList(realList_1);
		
		//make ScalarValueResults
		ScalarValueResults scalarValueResults_0 = new ScalarValueResults();
		scalarValueResults_0.setInput(scalarValueCollection_0);
		scalarValueResults_0.setOutput(scalarValueCollection_1);
		scalarValueResults_0.setTime(2800.1);
		
		//make ResultEvent
		ResultEvent event_0 = new ResultEvent(this, scalarValueResults_0);
		
		Util.serializeOk(
			event_0,
    	    CONSTANTS.STR_resultEvent_0
		);	
		
		
		return;
		
    }
	
	

	@Test
    public void T06_resultEvent_deserialize() {
    	

		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_resultEvent_0,
	      ResultEvent.class
	    );

		ResultEvent event_0 = (ResultEvent) deserializedObject_0;
		ScalarValueResults scalarValueResults_0 = event_0.getPayload();
		
		
		assertEquals(2800.1, scalarValueResults_0.getTime(), 0.0);
		
		ScalarValueReal scalarValueReal_0 = scalarValueResults_0.getInput().getRealList().get(0);
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_1 = scalarValueResults_0.getInput().getRealList().get(1);
		assertEquals(2, scalarValueReal_1.getIdx());
		assertEquals(3.530, scalarValueReal_1.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_2 = scalarValueResults_0.getOutput().getRealList().get(0);
		assertEquals(3, scalarValueReal_2.getIdx());
		assertEquals(258.2, scalarValueReal_2.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_3 = scalarValueResults_0.getOutput().getRealList().get(1);
		assertEquals(4, scalarValueReal_3.getIdx());
		assertEquals(78.0, scalarValueReal_3.getValue(), 0.0);
		
		
    }
	
	
	
	@Test
	public void T07_simStateNativeRequest_serialize() {
		
		//make SimStateNative 1
		SimStateNative simStateNative_0 = SimStateNative.simStateNative_5_step_requested;
		SimStateNativeRequest event_0 = new SimStateNativeRequest(this, simStateNative_0);

		Util.serializeOk(
			event_0,
    	    CONSTANTS.STR_simStateNativeRequest_0
		);

		
	}
	
	
	@Test
	public void T08_simStateNativeRequest_deserialize() {
		

		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_simStateNativeRequest_0,
	      SimStateNativeRequest.class
	    );
		
		SimStateNativeRequest event_0 = (SimStateNativeRequest) deserializedObject_0;
		
		SimStateNative simStateNative_0 = event_0.getPayload();
		assertEquals(SimStateNative.class, simStateNative_0.getClass());
		assertEquals(17, simStateNative_0.getIntValue());
		assertEquals(SimStateNative.simStateNative_5_step_requested, simStateNative_0);

	}
	
	
	
	@Test
	public void T09_simStateNativeNotify_serialize() {
		
		//make SimStateNative 1
		SimStateNative simStateNative_0 = SimStateNative.simStateNative_3_ready;
		SimStateNativeNotify event_0 = new SimStateNativeNotify(this, simStateNative_0);

		Util.serializeOk(
			event_0,
    	    CONSTANTS.STR_simStateNativeNotify_0
		);
		

	}
	
	@Test
	public void T10_simStateNativeNotify_deserialize() {
		
		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_simStateNativeNotify_0,
	      SimStateNativeNotify.class
	    );

		SimStateNativeNotify event_0 = (SimStateNativeNotify) deserializedObject_0;
		
		SimStateNative simStateNative_0 = event_0.getPayload();
		assertEquals(SimStateNative.class, simStateNative_0.getClass());
		assertEquals(10, simStateNative_0.getIntValue());
		assertEquals(SimStateNative.simStateNative_3_ready, simStateNative_0);

	}
	
	
	
	@Test
	public void T11_xmlParsedEvent_serialize() {
		
		
		ScalarVariableCollection scalarVariableCollection_0 = TestDataGenerator.getScalarVariableCollection_A_();
		ScalarVariableCollection scalarVariableCollection_1 = TestDataGenerator.getScalarVariableCollection_B_();

		//make scalarVariablesAll 1
		ScalarVariablesAll scalarVariablesAll_0 = new ScalarVariablesAll();
		scalarVariablesAll_0.setInput(scalarVariableCollection_0);
		scalarVariablesAll_0.setOutput(scalarVariableCollection_1);

		
		Util.serializeOk(
				scalarVariablesAll_0,
    	    CONSTANTS.STR_scalarVariablesAll_0
		);
	
		

		XMLparsedInfo xmlParsedInfo_0 = new XMLparsedInfo(scalarVariablesAll_0);
		

		Util.serializeOk(
			xmlParsedInfo_0,
    	    CONSTANTS.STR_XMLparsedInfo_0
		);
		
		
		XMLparsedEvent event_0 = new XMLparsedEvent(this, xmlParsedInfo_0);
		
		Util.serializeOk(
			event_0,
    	    CONSTANTS.STR_xmlParsedEvent_0
		);
		
	}
	
	
	@Test
	public void T12_xmlParsedEvent_deserialize() {
		
		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_xmlParsedEvent_0,
	      XMLparsedEvent.class
	    );
		
		XMLparsedEvent event_0 = (XMLparsedEvent) deserializedObject_0;
		
		XMLparsedInfo xmlParsedInfo_0 = event_0.getPayload();
		
		Vector<ScalarVariableReal> scalarVariableRealList_input = xmlParsedInfo_0.getInputVariables(); 
		Vector<ScalarVariableReal> scalarVariableRealList_output = xmlParsedInfo_0.getOutputVariables(); 
		
		assertEquals( 2, scalarVariableRealList_input.size());
		assertEquals( 2, scalarVariableRealList_output.size());
		
		ScalarVariableReal scalarVariableReal_0 = scalarVariableRealList_input.elementAt(0);
		assertEquals( Enu.enu_input, scalarVariableReal_0.getCausalityAsEnum());
		assertEquals( 6, scalarVariableReal_0.getCausalityAsInt());
		assertEquals( "input", scalarVariableReal_0.getCausalityAsString());
		
		assertEquals( "scalarVar name", scalarVariableReal_0.getName());
		assertEquals( 1, scalarVariableReal_0.getIdx());
		
		assertEquals( Enu.enu_continuous, scalarVariableReal_0.getVariabilityAsEnum());
		assertEquals( 5, scalarVariableReal_0.getVariabilityAsInt());
		assertEquals( "continuous", scalarVariableReal_0.getVariabilityAsString());
		
		assertEquals( "The Description", scalarVariableReal_0.getDescription());
		assertEquals( "C1", scalarVariableReal_0.getUnit());
		
		TypeSpecReal typeSpecReal_0 = scalarVariableReal_0.getTypeSpecReal();
		
		assertEquals(20.25 , typeSpecReal_0.start, 0.0);
		assertEquals(21.25 , typeSpecReal_0.nominal, 0.0);
		assertEquals(22.25 , typeSpecReal_0.min, 0.0);
		assertEquals(23.25 , typeSpecReal_0.max, 0.0);
		assertEquals("C1" , typeSpecReal_0.unit);
		
	}
	
	

	
	
	
	@Test
	public void T13_scalarValueChangeRequest_serialize() {
		

		//make struct
		ScalarValueRealStruct struct_0 = new ScalarValueRealStruct();
		struct_0.idx = 1;
		struct_0.value = 2.0;
		
		//make Object
		ScalarValueReal scalarValueReal_0 = new ScalarValueReal(struct_0);

	
		SerializableVector<ScalarValueReal> realList_0 = new SerializableVector<ScalarValueReal>("ScalarValueReal");
		realList_0.add(scalarValueReal_0);

		
		ScalarValueCollection scalarValueCollection_0 = new ScalarValueCollection();
		scalarValueCollection_0.setRealList(realList_0);

    	ScalarValueChangeRequest event_0 = new ScalarValueChangeRequest(this, scalarValueCollection_0);

		Util.serializeOk(
			event_0,
    	    CONSTANTS.STR_scalarValueChangeRequest_0
		);
		
	}
	
	@Test
	public void T14_scalarValueChangeRequest_deserialize() {
		
		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_scalarValueChangeRequest_0,
	      ScalarValueChangeRequest.class
	    );
		
		ScalarValueChangeRequest event_0 = (ScalarValueChangeRequest) deserializedObject_0;
		
		ScalarValueCollection payload_0 = event_0.getPayload();
		
		Vector<ScalarValueReal> realList_0 = payload_0.getRealList();
		ScalarValueReal scalarValueReal_0 = realList_0.get(0);
		
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
		
	}
	
	
	@Test
	public void T15_sessionControlClientRequest_serialize() {
		

		SessionControlAction sessionControlAction_0 = SessionControlAction.attachToSession;
		SessionControlModel sessionControlModel_0 = new SessionControlModel(sessionControlAction_0, "SESS1342");
		
		SessionControlClientRequest event_0 = new SessionControlClientRequest(this, sessionControlModel_0);

		Util.serializeOk(
			event_0,
    	    CONSTANTS.STR_sessionControlClientRequest_0
		);
		
		
	}
	
	
	
	
	@Test
	public void T16_sessionControlClientRequest_deserialize() {
		
		Iserializable deserializedObject_0 = Util.deserializeOk(
	      CONSTANTS.STR_sessionControlClientRequest_0,
	      SessionControlClientRequest.class
	    );
		
		SessionControlClientRequest event_0 = (SessionControlClientRequest) deserializedObject_0;
		SessionControlModel sessionControlModel_0 = event_0.getPayload();
		
		assertEquals(SessionControlAction.attachToSession , sessionControlModel_0.getAction());
		assertEquals("SESS1342" , sessionControlModel_0.getValue());

	}
	
	
	
	
	
	
	
	
}

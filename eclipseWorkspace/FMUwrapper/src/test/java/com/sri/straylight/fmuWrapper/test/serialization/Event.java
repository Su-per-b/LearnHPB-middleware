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


	public final static String STR_messageEvent_0 = "{\"t\":\"MessageEvent\",\"payload\":{\"t\":\"MessageStruct\",\"msgText\":\"This is the test Message Text\",\"messageType\":0}}";
	
	public final static String STR_configChangeNotify_0 = "{\"t\":\"ConfigChangeNotify\",\"payload\":{\"t\":\"ConfigStruct\",\"stepDelta\":1,\"defaultExperimentStruct\":{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10}}}";
			
	public final static String STR_resultEvent_0 = "{\"t\":\"ResultEvent\",\"payload\":{\"t\":\"ScalarValueResults\",\"time_\":2800.1,\"input\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2},{\"i\":2,\"v\":3.53}]}},\"output\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":3,\"v\":258.2},{\"i\":4,\"v\":78}]}}}}";

	public final static String STR_simStateNativeRequest_0 = "{\"t\":\"SimStateNativeRequest\",\"payload\":{\"t\":\"SimStateNative\",\"intValue\":17}}";
	
	public final static String STR_simStateNativeNotify_0 = "{\"t\":\"SimStateNativeNotify\",\"payload\":{\"t\":\"SimStateNative\",\"intValue\":10}}";
    
	public final static String STR_xmlParsedEvent_0 = "{\"t\":\"XMLparsedEvent\",\"payload\":{\"t\":\"XMLparsedInfo\",\"scalarVariablesAll_\":{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}},\"internal\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}}},\"sessionID_\":\"xxo\"}}";
	
	public final static String STR_scalarValueChangeRequest_0 = "{\"t\":\"ScalarValueChangeRequest\",\"payload\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2}]}}}";
	
	public final static String STR_sessionControlClientRequest_0 = "{\"t\":\"SessionControlClientRequest\",\"payload\":{\"t\":\"SessionControlModel\",\"v\":\"SESS1342\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":0}}}";
	
	
	
	@Test
    public void T01_messageEvent_serialize() {
    	
    	MessageStruct messageStruct_0 = new MessageStruct();
    	messageStruct_0.msgText = "This is the test Message Text";
    	messageStruct_0.setMessageTypeEnum(MessageType.messageType_debug);
    	
    	MessageEvent event_0 = new MessageEvent(this, messageStruct_0);
    	String jsonString_0 = event_0.toJsonString();
    	
    	
		assertEquals(
				STR_messageEvent_0, 
				jsonString_0);

    }
	
	

	@Test
    public void T02_messageEvent_deserialize() {
    	

		Object deserializedObject_0 = gsonController_.fromJson(STR_messageEvent_0);
		
		//assert
		assertEquals(MessageEvent.class, deserializedObject_0.getClass());

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
		String jsonString_0 = event_0.toJsonString();
		
		assertEquals(
			STR_configChangeNotify_0,
			jsonString_0
		);
		
	}
	
	
	@Test
	public void T04_configChangeNotify_deserialize() {


		Object deserializedObject_0 = gsonController_.fromJson(STR_configChangeNotify_0);
		assertEquals(ConfigChangeNotify.class, deserializedObject_0.getClass());
		
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
		
		
		String jsonString_0 = event_0.toJsonString();
		
		assertEquals(
				STR_resultEvent_0,
				jsonString_0
		);
		
		return;
		
    }
	
	

	@Test
    public void T06_resultEvent_deserialize() {
    	


		Object deserializedObject_0 = gsonController_.fromJson(STR_resultEvent_0);
		
		//assert
		assertEquals(ResultEvent.class, deserializedObject_0.getClass());

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

		//serialize / deserialize 
		String jsonString_0 = event_0.toJsonString();
		

		assertEquals(
			STR_simStateNativeRequest_0,	
			jsonString_0);

	}
	
	
	@Test
	public void T08_simStateNativeRequest_deserialize() {
		
		Object deserializedObject_0 = gsonController_.fromJson(STR_simStateNativeRequest_0);
		assertEquals(SimStateNativeRequest.class, deserializedObject_0.getClass());
		
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

		//serialize / deserialize 
		String jsonString_0 = event_0.toJsonString();
		
		assertEquals(
				STR_simStateNativeNotify_0,
				jsonString_0);

	}
	
	@Test
	public void T10_simStateNativeNotify_deserialize() {
		
		Object deserializedObject_0 = gsonController_.fromJson(STR_simStateNativeNotify_0);
		assertEquals(SimStateNativeNotify.class, deserializedObject_0.getClass());
		
		SimStateNativeNotify event_0 = (SimStateNativeNotify) deserializedObject_0;
		
		SimStateNative simStateNative_0 = event_0.getPayload();
		assertEquals(SimStateNative.class, simStateNative_0.getClass());
		assertEquals(10, simStateNative_0.getIntValue());
		assertEquals(SimStateNative.simStateNative_3_ready, simStateNative_0);

	}
	
	
	
	@Test
	public void T11_xmlParsedEvent_serialize() {
		
		//make TypeSpecReal
		TypeSpecReal typeSpecReal_0 = new TypeSpecReal();
		typeSpecReal_0.start = 20.25;
		typeSpecReal_0.nominal = 21.25;
		typeSpecReal_0.min = 22.25;
		typeSpecReal_0.max = 23.25;
		typeSpecReal_0.unit = "C";
		
		typeSpecReal_0.startValueStatus = 1;
		typeSpecReal_0.nominalValueStatus = 1;
		typeSpecReal_0.minValueStatus = 1;
		typeSpecReal_0.maxValueStatus = 1;
		typeSpecReal_0.unitValueStatus = 1;
		
		//make ScalarVariableReal
		ScalarVariableReal scalarVariableReal_0 = new ScalarVariableReal(typeSpecReal_0);
		scalarVariableReal_0.setName("scalarVar name");
		scalarVariableReal_0.setIdx(1);
		scalarVariableReal_0.setCausality(Enu.enu_input);
		scalarVariableReal_0.setVariability(Enu.enu_continuous);
		scalarVariableReal_0.setDescription("The Description 1");
		scalarVariableReal_0.setValueReference(125420);
		
		//make TypeSpecReal 2
		TypeSpecReal typeSpecReal_1 = new TypeSpecReal();
		typeSpecReal_1.start = 2.25;
		typeSpecReal_1.nominal = 2.25;
		typeSpecReal_1.min = 2.25;
		typeSpecReal_1.max = 2.25;
		typeSpecReal_1.unit = "Pa";
		
		typeSpecReal_1.startValueStatus = 1;
		typeSpecReal_1.nominalValueStatus = 1;
		typeSpecReal_1.minValueStatus = 1;
		typeSpecReal_1.maxValueStatus = 1;
		typeSpecReal_1.unitValueStatus = 1;
		
		
		//make ScalarVariableReal 2
		ScalarVariableReal scalarVariableReal_1 = new ScalarVariableReal(typeSpecReal_1);
		scalarVariableReal_1.setName("scalarVar name");
		scalarVariableReal_1.setIdx(1);
		scalarVariableReal_1.setCausality(Enu.enu_input);
		scalarVariableReal_1.setVariability(Enu.enu_discrete);
		scalarVariableReal_1.setDescription("The Description");
		scalarVariableReal_1.setValueReference(125420);
		
		SerializableVector<ScalarVariableReal> realVarList = new SerializableVector<ScalarVariableReal>("ScalarVariableReal");
		realVarList.add(scalarVariableReal_0);
		realVarList.add(scalarVariableReal_1);
		
		ScalarVariableCollection sVarColl = new ScalarVariableCollection();
		sVarColl.setRealVarList(realVarList);
		
		//make scalarVariablesAll 1
		ScalarVariablesAll scalarVariablesAll = new ScalarVariablesAll();
		scalarVariablesAll.setInput(sVarColl);
		scalarVariablesAll.setOutput(sVarColl);
		scalarVariablesAll.setInternal(sVarColl);
		
		//make SimStateWrapper 1
		XMLparsedInfo xmlParsedInfo = new XMLparsedInfo(scalarVariablesAll);
		

		XMLparsedEvent event_0 = new XMLparsedEvent(this, xmlParsedInfo);
		
    	String jsonString_0 = event_0.toJsonString();
    	
		assertEquals(
				STR_xmlParsedEvent_0,
				jsonString_0
		);
    	
    	
	}
	
	
	@Test
	public void T12_xmlParsedEvent_deserialize() {
		
			
		Object deserializedObject = gsonController_.fromJson(STR_xmlParsedEvent_0);
		assertEquals(XMLparsedEvent.class, deserializedObject.getClass());
		
		XMLparsedEvent event_0 = (XMLparsedEvent) deserializedObject;
		
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
		
		assertEquals( "The Description 1", scalarVariableReal_0.getDescription());
		assertEquals( "C", scalarVariableReal_0.getUnit());
		
		TypeSpecReal typeSpecReal_0 = scalarVariableReal_0.getTypeSpecReal();
		
		assertEquals(20.25 , typeSpecReal_0.start, 0.0);
		assertEquals(21.25 , typeSpecReal_0.nominal, 0.0);
		assertEquals(22.25 , typeSpecReal_0.min, 0.0);
		assertEquals(23.25 , typeSpecReal_0.max, 0.0);
		assertEquals("C" , typeSpecReal_0.unit);
		
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

		String jsonString_0 = event_0.toJsonString();
		
		assertEquals(
				STR_scalarValueChangeRequest_0,
				jsonString_0
		);
		
	}
	
	@Test
	public void T14_scalarValueChangeRequest_deserialize() {
		
	
		Object deserializedObject = gsonController_.fromJson(STR_scalarValueChangeRequest_0);
		assertEquals(ScalarValueChangeRequest.class, deserializedObject.getClass());
		
		ScalarValueChangeRequest event_0 = (ScalarValueChangeRequest) deserializedObject;
		
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

		String jsonString_0 = event_0.toJsonString();
		
		assertEquals(
				STR_sessionControlClientRequest_0,
				jsonString_0);
		
		
	}
	
	
	
	
	@Test
	public void T16_sessionControlClientRequest_deserialize() {
		
		String jsonString = "{\"t\":\"SessionControlClientRequest\",\"payload\":{\"t\":\"SessionControlModel\",\"v\":\"SESS1342\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":0}}}";
				
		Object deserializedObject = gsonController_.fromJson(jsonString);
		assertEquals(SessionControlClientRequest.class, deserializedObject.getClass());
		
		SessionControlClientRequest event_0 = (SessionControlClientRequest) deserializedObject;
		SessionControlModel sessionControlModel_0 = event_0.getPayload();
		
		assertEquals(SessionControlAction.attachToSession , sessionControlModel_0.getAction());
		assertEquals("SESS1342" , sessionControlModel_0.getValue());

	}
	
	
	
	
	
	
	
	
}

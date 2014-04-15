package com.sri.straylight.client.test.serialization;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.event.SessionControlClientRequest;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueBoolean;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlAction;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlModel;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;

public class EventSerialization {
	
	
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
    public void messageEvent_serialize() {
    	
    	MessageStruct messageStruct_0 = new MessageStruct();
    	messageStruct_0.msgText = "testMessageStruct";
    	messageStruct_0.setMessageTypeEnum(MessageType.messageType_debug);
    	
    	MessageEvent event_0 = new MessageEvent(this, messageStruct_0);
    	String json = event_0.toJsonString();
    	
    	
		assertEquals(
				"{\"t\":\"MessageEvent\",\"payload\":{\"t\":\"MessageStruct\",\"msgText\":\"testMessageStruct\",\"messageType\":0}}", 
				json);

    }
	
	

	@Test
    public void messageEvent_deserialize() {
    	

		String jsonString = "{\"t\":\"MessageEvent\",\"payload\":{\"t\":\"MessageStruct\",\"msgText\":\"testMessageStruct\",\"messageType\":0}}"; 
				
		Object deserializedObject = gsonController_.fromJson(jsonString);
		
		//assert
		assertEquals(MessageEvent.class, deserializedObject.getClass());

		MessageEvent event_0 = (MessageEvent) deserializedObject;
    	MessageStruct messageStruct_0 = event_0.getPayload();
		
		assertEquals("testMessageStruct", messageStruct_0.msgText);
		assertEquals(MessageType.messageType_debug, messageStruct_0.getMessageTypeEnum());
		assertEquals(0, messageStruct_0.messageType);
		
    }
	
	
	@Test
    public void resultEvent_serialize() {
    	
		//make real 1
		ScalarValueRealStruct struct1 = new ScalarValueRealStruct();
		struct1.idx = 1;
		struct1.value = 2.0;
		ScalarValueReal scalarValueReal1 = new ScalarValueReal(struct1);
		
		//make real 2
		ScalarValueRealStruct struct2 = new ScalarValueRealStruct();
		struct2.idx = 2;
		struct2.value = 3.53;
		ScalarValueReal scalarValueReal2 = new ScalarValueReal(struct2);
		
		//make real list
		Vector<ScalarValueReal> realList = new Vector<ScalarValueReal>();
		realList.add(scalarValueReal1);
		realList.add(scalarValueReal2);
		
		//make bool 1
		ScalarValueBooleanStruct structBool1 = new ScalarValueBooleanStruct();
		structBool1.idx = 1;
		structBool1.value = true;
		ScalarValueBoolean scalarValueBool1 = new ScalarValueBoolean(structBool1);
		
		//make bool 2
		ScalarValueBooleanStruct structBool2 = new ScalarValueBooleanStruct();
		structBool1.idx = 2;
		structBool1.value = true;
		ScalarValueBoolean scalarValueBool2 = new ScalarValueBoolean(structBool2);
		
		//make bool list
		Vector<ScalarValueBoolean> boolList = new Vector<ScalarValueBoolean>();
		boolList.add(scalarValueBool1);
		boolList.add(scalarValueBool2);
		
		//make ScalarValueCollection
		ScalarValueCollection scalarValueCollection1 = new ScalarValueCollection();
		scalarValueCollection1.setRealList(realList);
		scalarValueCollection1.setBooleanList(boolList);
		
		//make ScalarValueResults
		ScalarValueResults scalarValueResults = new ScalarValueResults();
		scalarValueResults.setInput(scalarValueCollection1);
		scalarValueResults.setOutput(scalarValueCollection1);
		scalarValueResults.setTime(2.0);
		
		//make ResultEvent
		ResultEvent event_0 = new ResultEvent(this, scalarValueResults);
		
    	String json = event_0.toJsonString();
    	
    	
		assertEquals(
				"{\"t\":\"ResultEvent\",\"payload\":{\"t\":\"ScalarValueResults\",\"time_\":2.0,\"input\":{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0},{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":3.53}],\"booleanList\":[{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true},{\"t\":\"ScalarValueBoolean\",\"i\":0,\"v\":false}]},\"output\":{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0},{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":3.53}],\"booleanList\":[{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true},{\"t\":\"ScalarValueBoolean\",\"i\":0,\"v\":false}]}}}",
				json);

    }
	
	

	@Test
    public void resultEvent_deserialize() {
    	

		String jsonString = "{\"t\":\"MessageEvent\",\"payload\":{\"t\":\"MessageStruct\",\"msgText\":\"testMessageStruct\",\"messageType\":0}}"; 
				
		Object deserializedObject = gsonController_.fromJson(jsonString);
		
		//assert
		assertEquals(MessageEvent.class, deserializedObject.getClass());

		MessageEvent event_0 = (MessageEvent) deserializedObject;
    	MessageStruct messageStruct_0 = event_0.getPayload();
		
		assertEquals("testMessageStruct", messageStruct_0.msgText);
		assertEquals(MessageType.messageType_debug, messageStruct_0.getMessageTypeEnum());
		assertEquals(0, messageStruct_0.messageType);
		
    }
	
	

	
	
	
	

	
	@Test
	public void simStateNativeRequest_serialize() {
		
		//make SimStateNative 1
		SimStateNative simStateNative_0 = SimStateNative.simStateNative_3_init_dllLoaded;
		SimStateNativeRequest event_0 = new SimStateNativeRequest(this, simStateNative_0);

		//serialize / deserialize 
		String json = event_0.toJsonString();
		
		assertEquals(
				"{\"t\":\"SimStateNativeRequest\",\"payload\":{\"t\":\"SimStateNative\",\"intValue\":6}}",
				json);

	}
	
	@Test
	public void simStateNativeRequest_deserialize() {
		
		String jsonString = "{\"t\":\"SimStateNativeRequest\",\"payload\":{\"t\":\"SimStateNative\",\"intValue\":6}}";
		
		Object deserializedObject = gsonController_.fromJson(jsonString);
		assertEquals(SimStateNativeRequest.class, deserializedObject.getClass());
		
		SimStateNativeRequest event_0 = (SimStateNativeRequest) deserializedObject;
		
		SimStateNative payload = event_0.getPayload();
		assertEquals(SimStateNative.class, payload.getClass());
		assertEquals(6, payload.getIntValue());
		assertEquals(SimStateNative.simStateNative_3_init_dllLoaded, payload);

		
	}
	
	
	
	@Test
	public void simStateNativeNotify_serialize() {
		
		//make SimStateNative 1
		SimStateNative simStateNative_0 = SimStateNative.simStateNative_5_step_started;
		SimStateNativeNotify event_0 = new SimStateNativeNotify(this, simStateNative_0);

		//serialize / deserialize 
		String json = event_0.toJsonString();
		
		assertEquals(
				"{\"t\":\"SimStateNativeNotify\",\"payload\":{\"t\":\"SimStateNative\",\"intValue\":18}}",
				json);

	}
	
	@Test
	public void simStateNativeNotify_deserialize() {
		
		String jsonString = "{\"t\":\"SimStateNativeNotify\",\"payload\":{\"t\":\"SimStateNative\",\"intValue\":18}}";
		
		Object deserializedObject = gsonController_.fromJson(jsonString);
		assertEquals(SimStateNativeNotify.class, deserializedObject.getClass());
		
		SimStateNativeNotify event_0 = (SimStateNativeNotify) deserializedObject;
		
		SimStateNative payload = event_0.getPayload();
		assertEquals(SimStateNative.class, payload.getClass());
		assertEquals(18, payload.getIntValue());
		assertEquals(SimStateNative.simStateNative_5_step_started, payload);

	}
	
	
	@Test
	public void xmlParsedEvent_serialize() {
		
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
		
		Vector<ScalarVariableReal> realVarList = new Vector<ScalarVariableReal>();
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
		
    	String json = event_0.toJsonString();
		assertEquals(
				"{\"t\":\"XMLparsedEvent\",\"payload\":{\"t\":\"XMLparsedInfo\",\"scalarVariablesAll_\":{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]},\"internal\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}},\"sessionID_\":\"xxo\"}}",
				json);
    	
    	
	}
	
	
	@Test
	public void xmlParsedEvent_deserialize() {
		
		String jsonString = "{\"t\":\"XMLparsedEvent\",\"payload\":{\"t\":\"XMLparsedInfo\",\"scalarVariablesAll_\":{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]},\"internal\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}},\"sessionID_\":\"xxo\"}}";
		
		Object deserializedObject = gsonController_.fromJson(jsonString);
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
	public void configChangeNotify_serialize() {
		
		DefaultExperimentStruct.ByReference defaultExperimentStruct 
		= new DefaultExperimentStruct.ByReference();
		
		defaultExperimentStruct.startTime = 123.03;
		defaultExperimentStruct.stopTime = 145.03;
		defaultExperimentStruct.tolerance = 10.0;
		
		ConfigStruct configStruct = new ConfigStruct();
		configStruct.stepDelta = 1.0;
		configStruct.defaultExperimentStruct = defaultExperimentStruct;
		
		
		ConfigChangeNotify event = new ConfigChangeNotify(this,configStruct);
		String json = event.toJsonString();
		
		assertEquals(
				"{\"t\":\"ConfigChangeNotify\",\"payload\":{\"t\":\"ConfigStruct\",\"stepDelta\":1.0,\"defaultExperimentStruct\":{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10.0}}}",
				json);
		
	}
	
	
	@Test
	public void configChangeNotify_deserialize() {
		
		String jsonString = "{\"t\":\"ConfigChangeNotify\",\"payload\":{\"t\":\"ConfigStruct\",\"stepDelta\":1.0,\"defaultExperimentStruct\":{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10.0}}}";
		
		Object deserializedObject = gsonController_.fromJson(jsonString);
		assertEquals(ConfigChangeNotify.class, deserializedObject.getClass());
		
		ConfigChangeNotify event_0 = (ConfigChangeNotify) deserializedObject;
		
		ConfigStruct configStruct = event_0.getPayload();
		
		assertEquals(1.0, configStruct.stepDelta, 0.0);
		
		assertEquals(123.03, configStruct.defaultExperimentStruct.startTime, 0.0);
		assertEquals(145.03, configStruct.defaultExperimentStruct.stopTime, 0.0);
		assertEquals(10.0, configStruct.defaultExperimentStruct.tolerance, 0.0);	
		
	}
	
	
	
	@Test
	public void scalarValueChangeRequest_serialize() {
		

		//make struct
		ScalarValueRealStruct struct_0 = new ScalarValueRealStruct();
		struct_0.idx = 1;
		struct_0.value = 2.0;
		
		//make Object
		ScalarValueReal scalarValueReal_0 = new ScalarValueReal(struct_0);

	
		Vector<ScalarValueReal> realList_0 = new Vector<ScalarValueReal>();
		realList_0.add(scalarValueReal_0);

		
		ScalarValueCollection scalarValueCollection_0 = new ScalarValueCollection();
		scalarValueCollection_0.setRealList(realList_0);

    	ScalarValueChangeRequest event_0 = new ScalarValueChangeRequest(this, scalarValueCollection_0);

		String json = event_0.toJsonString();
		
		assertEquals(
				"{\"t\":\"ScalarValueChangeRequest\",\"payload\":{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0}],\"booleanList\":[]}}",
				json);
		
	}
	
	@Test
	public void scalarValueChangeRequest_deserialize() {
		
		String jsonString = "{\"t\":\"ScalarValueChangeRequest\",\"payload\":{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0}],\"booleanList\":[]}}";
				
		Object deserializedObject = gsonController_.fromJson(jsonString);
		assertEquals(ScalarValueChangeRequest.class, deserializedObject.getClass());
		
		ScalarValueChangeRequest event_0 = (ScalarValueChangeRequest) deserializedObject;
		
		ScalarValueCollection payload_0 = event_0.getPayload();
		
		Vector<ScalarValueReal> realList_0 = payload_0.getRealList();
		ScalarValueReal scalarValueReal_0 = realList_0.get(0);
		
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
		
	}
	
	
	@Test
	public void sessionControlClientRequest_serialize() {
		

		SessionControlAction sessionControlAction_0 = SessionControlAction.attachToSession;
		SessionControlModel sessionControlModel_0 = new SessionControlModel(sessionControlAction_0, "SESS1342");
		
		SessionControlClientRequest event_0 = new SessionControlClientRequest(this, sessionControlModel_0);

		String jsonString_0 = event_0.toJsonString();
		
		assertEquals(
				"{\"t\":\"SessionControlClientRequest\",\"payload\":{\"t\":\"SessionControlModel\",\"v\":\"SESS1342\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":0}}}",
				jsonString_0);
		
		
	}
	
	
	
	
	@Test
	public void sessionControlClientRequest_deserialize() {
		
		String jsonString = "{\"t\":\"SessionControlClientRequest\",\"payload\":{\"t\":\"SessionControlModel\",\"v\":\"SESS1342\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":0}}}";
				
		Object deserializedObject = gsonController_.fromJson(jsonString);
		assertEquals(SessionControlClientRequest.class, deserializedObject.getClass());
		
		SessionControlClientRequest event_0 = (SessionControlClientRequest) deserializedObject;
		SessionControlModel sessionControlModel_0 = event_0.getPayload();
		
		assertEquals(SessionControlAction.attachToSession , sessionControlModel_0.getAction());
		assertEquals("SESS1342" , sessionControlModel_0.getValue());

		
		
	}
	
	
	
	
	
	
	
	
}

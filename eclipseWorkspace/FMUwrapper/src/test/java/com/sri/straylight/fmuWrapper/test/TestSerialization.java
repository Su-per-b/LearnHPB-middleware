package com.sri.straylight.fmuWrapper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueBoolean;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
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

public class TestSerialization {
	
	
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
    public void messageStruct() {
    	
    	MessageStruct messageStruct = new MessageStruct();
    	messageStruct.msgText = "testMessageStruct";
    	
    	messageStruct.setMessageTypeEnum(MessageType.messageType_debug);
    	String json = messageStruct.toJson();
    	
    	JsonSerializable obj = gsonController_.fromJson(json);
    	assertEquals(MessageStruct.class,  obj.getClass());
    	
    }
    
    
	@Test
    public void messageEvent() {
    	
    	MessageStruct messageStruct1 = new MessageStruct();
    	messageStruct1.msgText = "testMessageStruct";
    	messageStruct1.setMessageTypeEnum(MessageType.messageType_debug);
    	
    	MessageEvent event1 = new MessageEvent(this, messageStruct1);
    	String json = event1.toJson();
    	
    	JsonSerializable obj = gsonController_.fromJson(json);
    	assertEquals(MessageEvent.class,  obj.getClass());
    	
    	MessageEvent event2 = (MessageEvent) obj;
    	MessageStruct messageStruct2 = event2.getPayload();
    	
    	assertEquals(messageStruct1, messageStruct2);
    	assertEquals(event1, event2);
    }
	
	
	@Test
    public void scalarValueReal() {
		
		//make struct
		ScalarValueRealStruct struct = new ScalarValueRealStruct();
		struct.idx = 1;
		struct.value = 2.0;
		
		//make Object
		ScalarValueReal scalarValueReal1 = new ScalarValueReal(struct);

		
		//serialize / deserialize
		String json = scalarValueReal1.toJson();
		JsonSerializable deserializedObj = gsonController_.fromJson(json);
		
		//assert 1
		assertEquals(ScalarValueReal.class,  deserializedObj.getClass());
		
		//cast
		ScalarValueReal scalarValueReal2 = (ScalarValueReal) deserializedObj;
		
		//test 2
    	assertEquals(scalarValueReal1, scalarValueReal2);

		Double value = scalarValueReal2.getValue();
		assertEquals(2.0, value, 0.0);
    	
	}
	
	@Test
    public void scalarValueBoolean() {
		
		//make struct
		ScalarValueBooleanStruct struct = new ScalarValueBooleanStruct();
		struct.idx = 1;
		struct.value = true;
		
		//make Object
		ScalarValueBoolean scalarValueBoolean1 = new ScalarValueBoolean(struct);

		
		//serialize / deserialize
		String json = scalarValueBoolean1.toJson();
		JsonSerializable deserializedObj = gsonController_.fromJson(json);
		
		//assert 1
		assertEquals(ScalarValueBoolean.class,  deserializedObj.getClass());
		
		//cast
		ScalarValueBoolean scalarValueBoolean2 = (ScalarValueBoolean) deserializedObj;
		
		//assert 2
    	assertEquals(scalarValueBoolean1, scalarValueBoolean2);

		boolean value = scalarValueBoolean2.getValue();
		assertEquals(true, value);
    	
	}
	
	
	
	
	@Test
    public void scalarValueCollection() {
	
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
		
		//serialize / deserialize list
		String json1 = gsonController_.toJson(realList, Vector.class);
		Object deserializedObjReal = gsonController_.fromJson(json1, Vector.class);
		
		String json2 = gsonController_.toJson(boolList, Vector.class);
		Object deserializedObjBool = gsonController_.fromJson(json2, Vector.class);
		
		//assert
		assertEquals(realList.getClass(), deserializedObjReal.getClass());
		assertEquals(boolList.getClass(), deserializedObjBool.getClass());
		
		assertEquals(Vector.class, deserializedObjReal.getClass());
		assertEquals(Vector.class, deserializedObjBool.getClass());
		
		assertNotSame(scalarValueBool1, scalarValueBool2);
		assertNotSame(scalarValueReal1, scalarValueReal2);
		
		
		//make ScalarValueCollection
		ScalarValueCollection scalarValueCollection1 = new ScalarValueCollection(realList, boolList);
//		scalarValueCollection1.setRealList(realList);
//		scalarValueCollection1.setBooleanList(boolList);
		
		//serialize / deserialize ScalarValueCollection
		String json3 = scalarValueCollection1.toJson();
		Object scalarValueCollection2 = gsonController_.fromJson(json3);
		
		//assert
		assertEquals(scalarValueCollection1.getClass(), scalarValueCollection2.getClass());
		assertEquals(ScalarValueCollection.class, scalarValueCollection2.getClass());
		assertEquals(scalarValueCollection1, scalarValueCollection2);
		
	}
	
	
	@Test
    public void scalarValueResults() {
		
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
		
		//serialize / deserialize 
		String json4 = scalarValueResults.toJson();
		Object deserializedObjSVal = gsonController_.fromJson(json4);
		
		//assert
		assertEquals(scalarValueResults.getClass(), deserializedObjSVal.getClass());
		assertEquals(ScalarValueResults.class, deserializedObjSVal.getClass());
		
		assertEquals(scalarValueResults, deserializedObjSVal);
		
		
	}
	
	
	
	@Test
    public void resultEvent() {
		
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
		ResultEvent event1 = new ResultEvent(this, scalarValueResults);
		
		//serialize / deserialize 
		String json5 = event1.toJson();
		Object deserializedObjEvent = gsonController_.fromJson(json5);
		
		//assert
		assertEquals(event1.getClass(), deserializedObjEvent.getClass());
		assertEquals(ResultEvent.class, deserializedObjEvent.getClass());
		
		assertEquals(event1, deserializedObjEvent);
		
		
	}
	
	
	@Test
    public void simStateNative() {
		
		//make SimStateNative 1
		SimStateNative simStateNative1 = SimStateNative.simStateNative_0_uninitialized;
		
		//serialize / deserialize 
		String json = simStateNative1.toJson();
		Object simStateNativeDeserialized = gsonController_.fromJson(json);
		
		//assert
		assertEquals(simStateNative1.getClass(), simStateNativeDeserialized.getClass());
		assertEquals(SimStateNative.class, simStateNativeDeserialized.getClass());
		
		assertEquals(simStateNative1, simStateNativeDeserialized);

	}
	
	
	
	@Test
	public void simStateNativeRequest() {
		
		//make SimStateNative 1
		SimStateNative simStateNative1 = SimStateNative.simStateNative_3_init_dllLoaded;
		SimStateNativeRequest event1 = new SimStateNativeRequest(this, simStateNative1);

		//serialize / deserialize 
		String json = event1.toJson();
		Object event1Deserialized = gsonController_.fromJson(json);
		
		//assert
		assertEquals(event1.getClass(), event1Deserialized.getClass());
		assertEquals(SimStateNativeRequest.class, event1Deserialized.getClass());
		
		assertEquals(event1, event1Deserialized);
		assertEquals("{\"type\":\"com.sri.straylight.fmuWrapper.event.SimStateNativeRequest\",\"payload\":{\"type\":\"com.sri.straylight.fmuWrapper.voNative.SimStateNative\",\"intValue\":6}}",
				json);
		

	}
	
	@Test
	public void simStateNativeNotify() {
		
		//make SimStateNative 1
		SimStateNative simStateNative1 = SimStateNative.simStateNative_3_init_dllLoaded;
		SimStateNativeNotify event1 = new SimStateNativeNotify(this, simStateNative1);
		
		//serialize / deserialize 
		String json = event1.toJson();
		Object event1Deserialized = gsonController_.fromJson(json);
		
		//assert
		assertEquals(event1.getClass(), event1Deserialized.getClass());
		assertEquals(SimStateNativeNotify.class, event1Deserialized.getClass());
		
		assertEquals(event1, event1Deserialized);
		
	}


	
	@Test
	public void typeSpecReal() {
		
		//make
		TypeSpecReal typeSpecReal = new TypeSpecReal();
		typeSpecReal.start = 20.25;
		typeSpecReal.nominal = 21.25;
		typeSpecReal.min = 22.25;
		typeSpecReal.max = 23.25;
		
		typeSpecReal.startValueStatus = 1;
		typeSpecReal.nominalValueStatus = 1;
		typeSpecReal.minValueStatus = 1;
		typeSpecReal.maxValueStatus = 1;
		
		//serialize / deserialize 
		String json = typeSpecReal.toJson();
		Object typeSpecRealDeserialized = gsonController_.fromJson(json);
		
		
		//assert
		assertEquals(typeSpecReal.getClass(), typeSpecRealDeserialized.getClass());
		assertEquals(TypeSpecReal.class, typeSpecRealDeserialized.getClass());
		assertEquals(typeSpecReal, typeSpecRealDeserialized);
	}
	
	
	@Test
	public void scalarVariableReal() {
		
		//make TypeSpecReal
		TypeSpecReal typeSpecReal = new TypeSpecReal();
		typeSpecReal.start = 20.25;
		typeSpecReal.nominal = 21.25;
		typeSpecReal.min = 22.25;
		typeSpecReal.max = 23.25;
		
		typeSpecReal.startValueStatus = 1;
		typeSpecReal.nominalValueStatus = 1;
		typeSpecReal.minValueStatus = 1;
		typeSpecReal.maxValueStatus = 1;
		
		//make ScalarVariableReal
		ScalarVariableReal scalarVar = new ScalarVariableReal(typeSpecReal);
		scalarVar.setName("scalarVar name");
		scalarVar.setIdx(1);
		scalarVar.setCausality(Enu.enu_input);
		scalarVar.setVariability(Enu.enu_discrete);
		scalarVar.setDescription("The Description");
		scalarVar.setValueReference(125420);
		
		//serialize / deserialize 
		String json = scalarVar.toJson();
		Object scalarVarDeserialized = gsonController_.fromJson(json);
		
		//assert
		assertEquals(scalarVar.getClass(), scalarVarDeserialized.getClass());
		assertEquals(ScalarVariableReal.class, scalarVarDeserialized.getClass());
		assertEquals(scalarVar, scalarVarDeserialized);

	}
	
	
	@Test
	public void scalarVariableCollection() {
		
		
		//make TypeSpecReal
		TypeSpecReal typeSpecReal = new TypeSpecReal();
		typeSpecReal.start = 20.25;
		typeSpecReal.nominal = 21.25;
		typeSpecReal.min = 22.25;
		typeSpecReal.max = 23.25;
		
		typeSpecReal.startValueStatus = 1;
		typeSpecReal.nominalValueStatus = 1;
		typeSpecReal.minValueStatus = 1;
		typeSpecReal.maxValueStatus = 1;
		
		//make ScalarVariableReal
		ScalarVariableReal sVarReal = new ScalarVariableReal(typeSpecReal);
		sVarReal.setName("scalarVar name");
		sVarReal.setIdx(1);
		sVarReal.setCausality(Enu.enu_input);
		sVarReal.setVariability(Enu.enu_discrete);
		sVarReal.setDescription("The Description");
		sVarReal.setValueReference(125420);
		
		//make TypeSpecReal 2
		TypeSpecReal typeSpecReal2 = new TypeSpecReal();
		typeSpecReal2.start = 2.25;
		typeSpecReal2.nominal = 2.25;
		typeSpecReal2.min = 2.25;
		typeSpecReal2.max = 2.25;
		
		typeSpecReal2.startValueStatus = 1;
		typeSpecReal2.nominalValueStatus = 1;
		typeSpecReal2.minValueStatus = 1;
		typeSpecReal2.maxValueStatus = 1;
		
		//make ScalarVariableReal 2
		ScalarVariableReal sVarReal2 = new ScalarVariableReal(typeSpecReal);
		sVarReal2.setName("scalarVar name");
		sVarReal2.setIdx(1);
		sVarReal2.setCausality(Enu.enu_input);
		sVarReal2.setVariability(Enu.enu_discrete);
		sVarReal2.setDescription("The Description");
		sVarReal2.setValueReference(125420);
		
		Vector<ScalarVariableReal> realVarList = new Vector<ScalarVariableReal>();
		realVarList.add(sVarReal);
		realVarList.add(sVarReal2);
		
		ScalarVariableCollection sVarColl = new ScalarVariableCollection();
		sVarColl.setRealVarList(realVarList);
		
		//serialize / deserialize 
		String json = sVarColl.toJson();
		Object sVarCollDeserialized = gsonController_.fromJson(json);
		
		//assert
		assertEquals(sVarColl.getClass(), sVarCollDeserialized.getClass());
		assertEquals(ScalarVariableCollection.class, sVarCollDeserialized.getClass());
		assertEquals(sVarColl, sVarCollDeserialized);

	}
	
	
	@Test
	public void scalarVariablesAll() {
		//make TypeSpecReal
		TypeSpecReal typeSpecReal = new TypeSpecReal();
		typeSpecReal.start = 20.25;
		typeSpecReal.nominal = 21.25;
		typeSpecReal.min = 22.25;
		typeSpecReal.max = 23.25;
		
		typeSpecReal.startValueStatus = 1;
		typeSpecReal.nominalValueStatus = 1;
		typeSpecReal.minValueStatus = 1;
		typeSpecReal.maxValueStatus = 1;
		
		//make ScalarVariableReal
		ScalarVariableReal sVarReal = new ScalarVariableReal(typeSpecReal);
		sVarReal.setName("scalarVar name");
		sVarReal.setIdx(1);
		sVarReal.setCausality(Enu.enu_input);
		sVarReal.setVariability(Enu.enu_discrete);
		sVarReal.setDescription("The Description");
		sVarReal.setValueReference(125420);
		
		//make TypeSpecReal 2
		TypeSpecReal typeSpecReal2 = new TypeSpecReal();
		typeSpecReal2.start = 2.25;
		typeSpecReal2.nominal = 2.25;
		typeSpecReal2.min = 2.25;
		typeSpecReal2.max = 2.25;
		
		typeSpecReal2.startValueStatus = 1;
		typeSpecReal2.nominalValueStatus = 1;
		typeSpecReal2.minValueStatus = 1;
		typeSpecReal2.maxValueStatus = 1;
		
		//make ScalarVariableReal 2
		ScalarVariableReal sVarReal2 = new ScalarVariableReal(typeSpecReal);
		sVarReal2.setName("scalarVar name");
		sVarReal2.setIdx(1);
		sVarReal2.setCausality(Enu.enu_input);
		sVarReal2.setVariability(Enu.enu_discrete);
		sVarReal2.setDescription("The Description");
		sVarReal2.setValueReference(125420);
		
		Vector<ScalarVariableReal> realVarList = new Vector<ScalarVariableReal>();
		realVarList.add(sVarReal);
		realVarList.add(sVarReal2);
		
		ScalarVariableCollection sVarColl = new ScalarVariableCollection();
		sVarColl.setRealVarList(realVarList);
		
		
		//make scalarVariablesAll 1
		ScalarVariablesAll scalarVariablesAll = new ScalarVariablesAll();
		scalarVariablesAll.setInput(sVarColl);
		scalarVariablesAll.setOutput(sVarColl);
		scalarVariablesAll.setInternal(sVarColl);
		
		//serialize / deserialize 
		String json = scalarVariablesAll.toJson();
		Object scalarVariablesAllDeserialized = gsonController_.fromJson(json);
		
		//assert
		assertEquals(scalarVariablesAll.getClass(), scalarVariablesAllDeserialized.getClass());
		assertEquals(ScalarVariablesAll.class, scalarVariablesAllDeserialized.getClass());
		assertEquals(scalarVariablesAll, scalarVariablesAllDeserialized);

	}
	
	
	@Test
	public void xmlParsedInfo() {
		//make TypeSpecReal
		TypeSpecReal typeSpecReal = new TypeSpecReal();
		typeSpecReal.start = 20.25;
		typeSpecReal.nominal = 21.25;
		typeSpecReal.min = 22.25;
		typeSpecReal.max = 23.25;
		
		typeSpecReal.startValueStatus = 1;
		typeSpecReal.nominalValueStatus = 1;
		typeSpecReal.minValueStatus = 1;
		typeSpecReal.maxValueStatus = 1;
		
		//make ScalarVariableReal
		ScalarVariableReal sVarReal = new ScalarVariableReal(typeSpecReal);
		sVarReal.setName("scalarVar name");
		sVarReal.setIdx(1);
		sVarReal.setCausality(Enu.enu_input);
		sVarReal.setVariability(Enu.enu_discrete);
		sVarReal.setDescription("The Description");
		sVarReal.setValueReference(125420);
		
		//make TypeSpecReal 2
		TypeSpecReal typeSpecReal2 = new TypeSpecReal();
		typeSpecReal2.start = 2.25;
		typeSpecReal2.nominal = 2.25;
		typeSpecReal2.min = 2.25;
		typeSpecReal2.max = 2.25;
		
		typeSpecReal2.startValueStatus = 1;
		typeSpecReal2.nominalValueStatus = 1;
		typeSpecReal2.minValueStatus = 1;
		typeSpecReal2.maxValueStatus = 1;
		
		//make ScalarVariableReal 2
		ScalarVariableReal sVarReal2 = new ScalarVariableReal(typeSpecReal);
		sVarReal2.setName("scalarVar name");
		sVarReal2.setIdx(1);
		sVarReal2.setCausality(Enu.enu_input);
		sVarReal2.setVariability(Enu.enu_discrete);
		sVarReal2.setDescription("The Description");
		sVarReal2.setValueReference(125420);
		
		Vector<ScalarVariableReal> realVarList = new Vector<ScalarVariableReal>();
		realVarList.add(sVarReal);
		realVarList.add(sVarReal2);
		
		ScalarVariableCollection sVarColl = new ScalarVariableCollection();
		sVarColl.setRealVarList(realVarList);
		
		//make scalarVariablesAll 1
		ScalarVariablesAll scalarVariablesAll = new ScalarVariablesAll();
		scalarVariablesAll.setInput(sVarColl);
		scalarVariablesAll.setOutput(sVarColl);
		scalarVariablesAll.setInternal(sVarColl);
		
		//make SimStateWrapper 1
		XMLparsedInfo xmlParsedInfo = new XMLparsedInfo(scalarVariablesAll);

		//serialize / deserialize 
		String json = xmlParsedInfo.toJson();
		Object xmlParsedInfoDeserialized = gsonController_.fromJson(json);
		
		
		//assert
		assertEquals(xmlParsedInfo.getClass(), xmlParsedInfoDeserialized.getClass());
		assertEquals(XMLparsedInfo.class, xmlParsedInfoDeserialized.getClass());
		assertEquals(xmlParsedInfo, xmlParsedInfoDeserialized);

	}
	
	
	
	
	@Test
	public void xmlParsedEvent() {
		//make TypeSpecReal
		TypeSpecReal typeSpecReal = new TypeSpecReal();
		typeSpecReal.start = 20.25;
		typeSpecReal.nominal = 21.25;
		typeSpecReal.min = 22.25;
		typeSpecReal.max = 23.25;
		
		typeSpecReal.startValueStatus = 1;
		typeSpecReal.nominalValueStatus = 1;
		typeSpecReal.minValueStatus = 1;
		typeSpecReal.maxValueStatus = 1;
		
		//make ScalarVariableReal
		ScalarVariableReal sVarReal = new ScalarVariableReal(typeSpecReal);
		sVarReal.setName("scalarVar name");
		sVarReal.setIdx(1);
		sVarReal.setCausality(Enu.enu_input);
		sVarReal.setVariability(Enu.enu_discrete);
		sVarReal.setDescription("The Description");
		sVarReal.setValueReference(125420);
		
		//make TypeSpecReal 2
		TypeSpecReal typeSpecReal2 = new TypeSpecReal();
		typeSpecReal2.start = 2.25;
		typeSpecReal2.nominal = 2.25;
		typeSpecReal2.min = 2.25;
		typeSpecReal2.max = 2.25;
		
		typeSpecReal2.startValueStatus = 1;
		typeSpecReal2.nominalValueStatus = 1;
		typeSpecReal2.minValueStatus = 1;
		typeSpecReal2.maxValueStatus = 1;
		
		//make ScalarVariableReal 2
		ScalarVariableReal sVarReal2 = new ScalarVariableReal(typeSpecReal);
		sVarReal2.setName("scalarVar name");
		sVarReal2.setIdx(1);
		sVarReal2.setCausality(Enu.enu_input);
		sVarReal2.setVariability(Enu.enu_discrete);
		sVarReal2.setDescription("The Description");
		sVarReal2.setValueReference(125420);
		
		Vector<ScalarVariableReal> realVarList = new Vector<ScalarVariableReal>();
		realVarList.add(sVarReal);
		realVarList.add(sVarReal2);
		
		ScalarVariableCollection sVarColl = new ScalarVariableCollection();
		sVarColl.setRealVarList(realVarList);
		
		//make scalarVariablesAll 1
		ScalarVariablesAll scalarVariablesAll = new ScalarVariablesAll();
		scalarVariablesAll.setInput(sVarColl);
		scalarVariablesAll.setOutput(sVarColl);
		scalarVariablesAll.setInternal(sVarColl);
		
		//make SimStateWrapper 1
		XMLparsedInfo xmlParsedInfo = new XMLparsedInfo(scalarVariablesAll);
		
		XMLparsedEvent event = new XMLparsedEvent(this, xmlParsedInfo);
		
		//serialize / deserialize 
		String json = event.toJson();
		Object eventDeserialized = gsonController_.fromJson(json);
		
		
		//assert
		assertEquals(event.getClass(), eventDeserialized.getClass());
		assertEquals(XMLparsedEvent.class, eventDeserialized.getClass());
		assertEquals(event, eventDeserialized);
		
		XMLparsedInfo payloadDeserialized =  event.getPayload();
		
		assertEquals(XMLparsedInfo.class, payloadDeserialized.getClass());
		assertEquals(xmlParsedInfo, payloadDeserialized);
		
		ScalarVariablesAll scalarVariablesAllDeserialized = xmlParsedInfo.getScalarVariablesAll();
		assertEquals(scalarVariablesAll, scalarVariablesAllDeserialized);
		
		ScalarVariableCollection scalarVariableCollectionDes = scalarVariablesAllDeserialized.getInput();
		assertEquals(sVarColl, scalarVariableCollectionDes);
		
		Vector<ScalarVariableReal> list = scalarVariableCollectionDes.getRealVarList();
		ScalarVariableReal sVar = list.get(0);
		
		assertEquals("scalarVar name", sVar.getName());
		assertEquals(1, sVar.getIdx());
		assertEquals(Enu.enu_input, sVar.getCausalityAsEnum());
		
		assertEquals(Enu.enu_discrete, sVar.getVariabilityAsEnum());
		assertEquals("The Description", sVar.getDescription());
		assertEquals(125420, sVar.getValueReference());
		
	}
	
	
	
	@Test
	public void defaultExperimentStruct() {

		
		DefaultExperimentStruct.ByReference struct 
			= new DefaultExperimentStruct.ByReference();
		
		struct.startTime = 123.03;
		struct.stopTime = 145.03;
		struct.tolerance = 10.0;
		String json = struct.toJson();
		
		Object structDeserialized = gsonController_.fromJson(json);
		//assert
		assertEquals(struct.getClass(), structDeserialized.getClass());
		assertEquals(DefaultExperimentStruct.ByReference.class, structDeserialized.getClass());
		assertEquals(struct, structDeserialized);
		
		
	}
	
	
	@Test
	public void configStruct() {
	
		
		DefaultExperimentStruct.ByReference defaultExperimentStruct 
			= new DefaultExperimentStruct.ByReference();
	
		defaultExperimentStruct.startTime = 123.03;
		defaultExperimentStruct.stopTime = 145.03;
		defaultExperimentStruct.tolerance = 10.0;
		
		ConfigStruct configStruct = new ConfigStruct();
		configStruct.stepDelta = 1.0;
		configStruct.defaultExperimentStruct = defaultExperimentStruct;
		
		
		String json = configStruct.toJson();
		
		Object configStructDeserialized = gsonController_.fromJson(json);
		
		//assert
		assertEquals(configStruct.getClass(), configStructDeserialized.getClass());
		assertEquals(ConfigStruct.class, configStructDeserialized.getClass());
		assertEquals(configStruct, configStructDeserialized);
		
	}
	
	
	@Test
	public void configChangeNotify() {
		
		
		DefaultExperimentStruct.ByReference defaultExperimentStruct 
		= new DefaultExperimentStruct.ByReference();
		
		defaultExperimentStruct.startTime = 123.03;
		defaultExperimentStruct.stopTime = 145.03;
		defaultExperimentStruct.tolerance = 10.0;
		
		ConfigStruct configStruct = new ConfigStruct();
		configStruct.stepDelta = 1.0;
		configStruct.defaultExperimentStruct = defaultExperimentStruct;
		
		
		ConfigChangeNotify event = new ConfigChangeNotify(this,configStruct);
		String json = event.toJson();
		
		Object eventDeserialized = gsonController_.fromJson(json);
		
		//assert
		assertEquals(event.getClass(), eventDeserialized.getClass());
		assertEquals(ConfigChangeNotify.class, eventDeserialized.getClass());
		assertEquals(event, eventDeserialized);
		
	}
	
	@Test
	public void scalarValueChangeRequest() {
		
		//make struct
		ScalarValueRealStruct struct = new ScalarValueRealStruct();
		struct.idx = 1;
		struct.value = 2.0;
		
		
		//make Object
		ScalarValueReal scalarValueReal1 = new ScalarValueReal(struct);

		
		//serialize / deserialize
		String json = scalarValueReal1.toJson();
		JsonSerializable deserializedObj = gsonController_.fromJson(json);
		
		//assert 1
		assertEquals(ScalarValueReal.class,  deserializedObj.getClass());
		
		//cast
		ScalarValueReal scalarValueReal2 = (ScalarValueReal) deserializedObj;
		
		//test 2
    	assertEquals(scalarValueReal1, scalarValueReal2);

		Double value = scalarValueReal2.getValue();
		assertEquals(2.0, value, 0.0);
		
		
		
		Vector<ScalarValueRealStruct> scalarValueList = new Vector<ScalarValueRealStruct>();
		scalarValueList.add(struct);
		
		
		
		
		
		ScalarValueChangeRequest event1 = new ScalarValueChangeRequest(this, scalarValueList);
		
		//String json = event1.toJson();
		
		
/*		
    	MessageStruct messageStruct1 = new MessageStruct();
    	messageStruct1.msgText = "testMessageStruct";
    	messageStruct1.setMessageTypeEnum(MessageType.messageType_debug);
    	
    	MessageEvent event1 = new MessageEvent(this, messageStruct1);
    	String json = event1.toJson();
    	
    	JsonSerializable obj = gsonController_.fromJson(json);
    	assertEquals(MessageEvent.class,  obj.getClass());
    	
    	MessageEvent event2 = (MessageEvent) obj;
    	MessageStruct messageStruct2 = event2.getPayload();
    	
    	assertEquals(messageStruct1, messageStruct2);
    	assertEquals(event1, event2);*/
		

		
	}
	
	
}

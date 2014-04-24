package com.sri.straylight.client.test.serialization;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;








import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueBoolean;
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
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;



public class VoManagedSerialization {
	
	
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
	public void scalarValueReal_serialize() {

		//make struct
		ScalarValueRealStruct struct = new ScalarValueRealStruct();
		struct.idx = 1;
		struct.value = 2.0;
		
		//make Object
		ScalarValueReal scalarValueReal_0 = new ScalarValueReal(struct);

		//serialize / deserialize
		String json = scalarValueReal_0.toJsonString();
		
		
		assertEquals(
				"{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0}", 
				json);
		
		
		
		ScalarValueReal scalarValueReal_1 = new ScalarValueReal(2, 14.2);
		String json2 = scalarValueReal_1.toJsonString();
		
		assertEquals(
				"{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":14.2}", 
				json2);
		
		
	}
	

	
	@Test
	public void scalarValueReal_deserialize() {
		
		String jsonString = "{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0}";
		
		Object deserializedObject = gsonController_.fromJson(jsonString);
		
		assertEquals(ScalarValueReal.class, deserializedObject.getClass());
		ScalarValueReal scalarValueReal = (ScalarValueReal) deserializedObject;
		

		assertEquals(1, scalarValueReal.getIdx());
		assertEquals(2.0, scalarValueReal.getValue(), 0.0);

	}
	
	
	
	
	
	@Test
	public void scalarValueBoolean_serialize() {

		//make struct
		ScalarValueBooleanStruct struct = new ScalarValueBooleanStruct();
		struct.idx = 1;
		struct.value = true;
		
		//make Object
		ScalarValueBoolean scalarValueBoolean_0 = new ScalarValueBoolean(struct);

		
		//serialize
		String json = scalarValueBoolean_0.toJsonString();
		
		
		assertEquals(
				"{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true}", 
				json);
		
		
		
		ScalarValueBoolean scalarValueBoolean_1 = new ScalarValueBoolean(2, false);
		String json2 = scalarValueBoolean_1.toJsonString();
		
		assertEquals(
				"{\"t\":\"ScalarValueBoolean\",\"i\":2,\"v\":false}", 
				json2);
		
		
	}
	
	@Test
	public void scalarValueBoolean_deserialize() {
		
		String jsonString_0 = "{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true}";
		
		Object deserializedObject_0 = gsonController_.fromJson(jsonString_0);
		
		assertEquals(ScalarValueBoolean.class, deserializedObject_0.getClass());
		ScalarValueBoolean scalarValueBoolean_0 = (ScalarValueBoolean) deserializedObject_0;
		
		assertEquals(1, scalarValueBoolean_0.getIdx());
		assertEquals(true, scalarValueBoolean_0.getValue());
		
		String jsonString_1 = "{\"t\":\"ScalarValueBoolean\",\"i\":2,\"v\":false}";
		
		Object deserializedObject_1 = gsonController_.fromJson(jsonString_1);
		
		assertEquals(ScalarValueBoolean.class, deserializedObject_1.getClass());
		ScalarValueBoolean scalarValueBoolean_1 = (ScalarValueBoolean) deserializedObject_1;
		
		assertEquals(2, scalarValueBoolean_1.getIdx());
		assertEquals(false, scalarValueBoolean_1.getValue());

	}
	
	
	@Test
	public void scalarValueCollection_deserialize() {
		String jsonString_0 = "{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0},{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":3.53}],\"booleanList\":[{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true},{\"t\":\"ScalarValueBoolean\",\"i\":2,\"v\":false}]}"; 
		Object deserializedObject_0 = gsonController_.fromJson(jsonString_0);
		
		assertEquals(ScalarValueCollection.class, deserializedObject_0.getClass());
		ScalarValueCollection scalarValueCollection_0 = (ScalarValueCollection) deserializedObject_0;
		
		Vector<ScalarValueReal> realList = scalarValueCollection_0.getRealList();
		
		ScalarValueReal scalarValueReal_0 = realList.get(0);
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_1 = realList.get(1);
		assertEquals(2, scalarValueReal_1.getIdx());
		assertEquals(3.53, scalarValueReal_1.getValue(), 0.0);
		
//		Vector<ScalarValueBoolean> boolList = scalarValueCollection_0.getBooleanList();
//		
//		ScalarValueBoolean scalarValueBoolean_0 = boolList.get(0);
//		assertEquals(1, scalarValueBoolean_0.getIdx());
//		assertEquals(false, scalarValueBoolean_0.getValue());
//		
//		ScalarValueBoolean scalarValueBoolean_1 = boolList.get(1);
//		assertEquals(2, scalarValueBoolean_1.getIdx());
//		assertEquals(false, scalarValueBoolean_1.getValue());

	}
	
	
	
	@Test
	public void scalarValueCollection_serialize() {
		
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
		SerializableVector<ScalarValueReal> realList = new SerializableVector<ScalarValueReal>("ScalarValueReal");
		realList.add(scalarValueReal1);
		realList.add(scalarValueReal2);
		
		
		String json_0 = gsonController_.toJsonString(realList, Vector.class);
		
		assertEquals(
		"[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0},{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":3.53}]", 
		json_0);
		
		
		//make bool 1
		ScalarValueBooleanStruct structBool1 = new ScalarValueBooleanStruct();
		structBool1.idx = 1;
		structBool1.value = true;
		ScalarValueBoolean scalarValueBool1 = new ScalarValueBoolean(structBool1);
		
		//make bool 2
		ScalarValueBooleanStruct structBool2 = new ScalarValueBooleanStruct();
		structBool2.idx = 2;
		structBool2.value = false;
		ScalarValueBoolean scalarValueBool2 = new ScalarValueBoolean(structBool2);
		
		//make bool list
		Vector<ScalarValueBoolean> boolList = new Vector<ScalarValueBoolean>();
		boolList.add(scalarValueBool1);
		boolList.add(scalarValueBool2);
		
		String json_1 = gsonController_.toJsonString(boolList, Vector.class);
		
		assertEquals(
		"[{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true},{\"t\":\"ScalarValueBoolean\",\"i\":2,\"v\":false}]", 
		json_1);
		
		ScalarValueCollection scalarValueCollection_0 = new ScalarValueCollection(realList);
		
		String json_2 = scalarValueCollection_0.toJsonString();
		
		assertEquals(
				"{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0},{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":3.53}],\"booleanList\":[{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true},{\"t\":\"ScalarValueBoolean\",\"i\":2,\"v\":false}]}", 
			json_2
		);
		
		
		
		
	}

	
	@Test
    public void scalarValueResults_deserialize() {
		
		
		String jsonString_0 = "{\"t\":\"ScalarValueResults\",\"time_\":2.0,\"input\":{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0},{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":3.53}],\"booleanList\":[{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true},{\"t\":\"ScalarValueBoolean\",\"i\":0,\"v\":false}]},\"output\":{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0},{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":3.53}],\"booleanList\":[{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true},{\"t\":\"ScalarValueBoolean\",\"i\":0,\"v\":false}]}}"; 
		
		
		Object deserializedObject_0 = gsonController_.fromJson(jsonString_0);
		
		assertEquals(ScalarValueResults.class, deserializedObject_0.getClass());
		
		ScalarValueResults scalarValueResults_0 = (ScalarValueResults) deserializedObject_0;
		
		assertEquals(2.0, scalarValueResults_0.getTime(), 0.0);
		
		ScalarValueReal scalarValueReal_0 = scalarValueResults_0.getInput().getRealList().get(0);
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_1 = scalarValueResults_0.getInput().getRealList().get(1);
		assertEquals(2, scalarValueReal_1.getIdx());
		assertEquals(3.530, scalarValueReal_1.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_2 = scalarValueResults_0.getOutput().getRealList().get(0);
		assertEquals(1, scalarValueReal_2.getIdx());
		assertEquals(2.0, scalarValueReal_2.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_3 = scalarValueResults_0.getOutput().getRealList().get(1);
		assertEquals(2, scalarValueReal_3.getIdx());
		assertEquals(3.530, scalarValueReal_3.getValue(), 0.0);
		
	}
	
	
	@Test
    public void scalarValueResults_serialize() {
		
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
		SerializableVector<ScalarValueReal> realList = new SerializableVector<ScalarValueReal>("ScalarValueReal");
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
//		scalarValueCollection1.setBooleanList(boolList);
		
		//make ScalarValueResults
		ScalarValueResults scalarValueResults = new ScalarValueResults();
		scalarValueResults.setInput(scalarValueCollection1);
		scalarValueResults.setOutput(scalarValueCollection1);
		scalarValueResults.setTime(2.0);
		
		//serialize / deserialize 
		String json_0 = scalarValueResults.toJsonString();

		assertEquals(
		"{\"t\":\"ScalarValueResults\",\"time_\":2.0,\"input\":{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0},{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":3.53}],\"booleanList\":[{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true},{\"t\":\"ScalarValueBoolean\",\"i\":0,\"v\":false}]},\"output\":{\"t\":\"ScalarValueCollection\",\"realList\":[{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2.0},{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":3.53}],\"booleanList\":[{\"t\":\"ScalarValueBoolean\",\"i\":1,\"v\":true},{\"t\":\"ScalarValueBoolean\",\"i\":0,\"v\":false}]}}", 
		json_0);
		
	}
	
	
	
	@Test
    public void scalarVariableReal_serialize() {
		
		//make TypeSpecReal
		TypeSpecReal typeSpecReal = new TypeSpecReal();
		typeSpecReal.start = 20.25;
		typeSpecReal.nominal = 21.25;
		typeSpecReal.min = 22.25;
		typeSpecReal.max = 23.25;
		typeSpecReal.unit ="C";
		
		typeSpecReal.startValueStatus = 1;
		typeSpecReal.nominalValueStatus = 1;
		typeSpecReal.minValueStatus = 1;
		typeSpecReal.maxValueStatus = 1;
		typeSpecReal.unitValueStatus = 1;
		
		//make ScalarVariableReal
		ScalarVariableReal scalarVariableReal = new ScalarVariableReal(typeSpecReal);
		scalarVariableReal.setName("scalarVar name");
		scalarVariableReal.setIdx(1);
		scalarVariableReal.setCausality(Enu.enu_input);
		scalarVariableReal.setVariability(Enu.enu_discrete);
		scalarVariableReal.setDescription("The Description");
		scalarVariableReal.setValueReference(125420);
		
		//serialize / deserialize 
		String json = scalarVariableReal.toJsonString();
		
		assertEquals(
				"{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}}",
				json);
		
		
	}
	


	@Test
    public void scalarVariableReal_deserialize() {
		
		String jsonString_0 = "{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}}"; 
		
		Object deserializedObject_0 = gsonController_.fromJson(jsonString_0);
		
		assertEquals(ScalarVariableReal.class, deserializedObject_0.getClass());
		ScalarVariableReal scalarVariableReal_0 = (ScalarVariableReal) deserializedObject_0;
		
		
		assertEquals(125420, scalarVariableReal_0.getValueReference());
		assertEquals(1, scalarVariableReal_0.getIdx());
		assertEquals("scalarVar name", scalarVariableReal_0.getName());
		
		assertEquals(6, scalarVariableReal_0.getCausalityAsInt());
		assertEquals(Enu.enu_input, scalarVariableReal_0.getCausalityAsEnum());
		
		assertEquals(4, scalarVariableReal_0.getVariabilityAsInt());
		assertEquals(Enu.enu_discrete, scalarVariableReal_0.getVariabilityAsEnum());
		
		assertEquals("The Description", scalarVariableReal_0.getDescription());
		
		TypeSpecReal typeSpecReal = scalarVariableReal_0.getTypeSpecReal();
		
		assertEquals(20.25, typeSpecReal.start, 0.0);
		assertEquals(21.25, typeSpecReal.nominal, 0.0);
		assertEquals(22.25, typeSpecReal.min, 0.0);
		assertEquals(23.25, typeSpecReal.max, 0.0);
		assertEquals("C", typeSpecReal.unit);
		
	}
	
	@Test
    public void scalarVariablesAll_serialize() {
		
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
		
		SerializableVector<ScalarVariableReal> realVarList = new SerializableVector<ScalarVariableReal>("ScalarVariableReal");
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
		String json = scalarVariablesAll.toJsonString();
		
		
		assertEquals(
				"{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}}]},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}}]},\"internal\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}}]}}",
				json);
					
		
	}
	
	@Test
    public void scalarVariablesAll_deserialize() {
		
		String jsonString_0 = "{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}}]},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}}]},\"internal\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"\"}}]}}";
							
		Object deserializedObject_0 = gsonController_.fromJson(jsonString_0);
		
		assertEquals(ScalarVariablesAll.class, deserializedObject_0.getClass());
		ScalarVariablesAll scalarVariablesAll_0 = (ScalarVariablesAll) deserializedObject_0;
		
		
		ScalarVariableCollection input = scalarVariablesAll_0.getInput();
		
		Vector<ScalarVariableReal> realList_0 = input.getRealVarList();
		
		ScalarVariableReal scalarVariableReal_0 = realList_0.get(0);
		

		assertEquals( "scalarVar name", scalarVariableReal_0.getName());
		
		
		TypeSpecReal typeSpecReal_0 = scalarVariableReal_0.getTypeSpecReal();
		
		assertEquals( 20.25, typeSpecReal_0.start, 0.0);
		assertEquals( 21.25, typeSpecReal_0.nominal, 0.0);
		assertEquals( 22.25, typeSpecReal_0.min, 0.0);
		assertEquals( 23.25, typeSpecReal_0.max, 0.0);
		
		
		Vector<ScalarVariableReal> realList_1 = input.getRealVarList();
		ScalarVariableReal scalarVariableReal_1 = realList_1.get(1);
		
		
		assertEquals( "scalarVar name", scalarVariableReal_1.getName());
		TypeSpecReal typeSpecReal_1 = scalarVariableReal_1.getTypeSpecReal();
		
		assertEquals( 20.25, typeSpecReal_1.start, 0.0);
		assertEquals( 21.25, typeSpecReal_1.nominal, 0.0);
		assertEquals( 22.25, typeSpecReal_1.min, 0.0);
		assertEquals( 23.25, typeSpecReal_1.max, 0.0);
		
		
		return;
		
	}
	
	

	@Test
    public void xmlParsedInfo_serialize() {
		
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

		//serialize
		String json = xmlParsedInfo.toJsonString();
		
		
		assertEquals(
				"{\"t\":\"XMLparsedInfo\",\"scalarVariablesAll_\":{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]},\"internal\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}},\"sessionID_\":\"xxo\"}",
				json
				);
		
		
	}
	
	
	
	@Test
    public void xmlParsedInfo_deserialize() {
		
		String jsonString_0 = "{\"t\":\"XMLparsedInfo\",\"scalarVariablesAll_\":{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]},\"internal\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":[{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}},\"sessionID_\":\"xxo\"}";
		Object deserializedObject_0 = gsonController_.fromJson(jsonString_0);
		
		assertEquals(XMLparsedInfo.class, deserializedObject_0.getClass());
		XMLparsedInfo xmlParsedInfo_0 = (XMLparsedInfo) deserializedObject_0;
		
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
		
		return;
		
	}
	
	
	
	@Test
    public void sessionControlAction_serialize() {
	
		
		SessionControlAction sessionControlAction_0 = SessionControlAction.attachToSession;
		
		String jsonString_0 = sessionControlAction_0.toJsonString();
		
		assertEquals(
				"{\"t\":\"SessionControlAction\",\"intValue\":0}", 
				jsonString_0);
		
		
		SessionControlAction sessionControlAction_1 = SessionControlAction.getInfo;
		
		String jsonString_1 = sessionControlAction_1.toJsonString();
		
		assertEquals(
				"{\"t\":\"SessionControlAction\",\"intValue\":1}", 
				jsonString_1);
		
		
	}
	
	
	@Test
	public void sessionControlAction_deserialize() {
		
		
		String jsonString_0 = "{\"t\":\"SessionControlAction\",\"intValue\":0}";
		Object deserializedObject_0 = gsonController_.fromJson(jsonString_0);
		
		assertEquals(SessionControlAction.class, deserializedObject_0.getClass());
		SessionControlAction sessionControlAction_0 = (SessionControlAction) deserializedObject_0;
		
		assertEquals(SessionControlAction.attachToSession , sessionControlAction_0);
		
		
		String jsonString_1 = "{\"t\":\"SessionControlAction\",\"intValue\":1}";
		Object deserializedObject_1 = gsonController_.fromJson(jsonString_1);
		
		assertEquals(SessionControlAction.class, deserializedObject_1.getClass());
		SessionControlAction sessionControlAction_1 = (SessionControlAction) deserializedObject_1;
		
		assertEquals(SessionControlAction.getInfo , sessionControlAction_1);

		
	}
	
	
	
	@Test
    public void SessionControlModel_serialize() {
	
		
		SessionControlAction sessionControlAction_0 = SessionControlAction.attachToSession;
		SessionControlModel sessionControlModel_0 = new SessionControlModel(sessionControlAction_0, "SESS1342");
		
		String jsonString_0 = sessionControlModel_0.toJsonString();
		
		assertEquals(
				"{\"t\":\"SessionControlModel\",\"v\":\"SESS1342\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":0}}", 
				jsonString_0);
		
		

		SessionControlAction sessionControlAction_1 = SessionControlAction.getInfo;
		SessionControlModel sessionControlModel_1 = new SessionControlModel(sessionControlAction_1);
		
		String jsonString_1 = sessionControlModel_1.toJsonString();
		
		assertEquals(
				"{\"t\":\"SessionControlModel\",\"v\":\"\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":1}}", 
				jsonString_1);

	}
	
	
	
	@Test
    public void SessionControlModel_deserialize() {
		
		String jsonString_0 = "{\"t\":\"SessionControlModel\",\"v\":\"SESS1342\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":0}}";
		Object deserializedObject_0 = gsonController_.fromJson(jsonString_0);
		
		assertEquals(SessionControlModel.class, deserializedObject_0.getClass());
		SessionControlModel sessionControlModel_0 = (SessionControlModel) deserializedObject_0;
		
		
		assertEquals(SessionControlAction.attachToSession , sessionControlModel_0.getAction());
		assertEquals("SESS1342" , sessionControlModel_0.getValue());
		
		
		
	}
	
	
	
}

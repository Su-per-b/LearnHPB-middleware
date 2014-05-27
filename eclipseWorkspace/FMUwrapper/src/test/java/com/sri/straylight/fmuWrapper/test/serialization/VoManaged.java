package com.sri.straylight.fmuWrapper.test.serialization;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.test.base.OrderedRunner;
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
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;



@RunWith(OrderedRunner.class)
public class VoManaged {
	
	
	/** The for serialization. */
	private  JsonController gsonController_ = JsonController.getInstance();
    
	public final static String STR_scalarValueReal_0 = "{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2}";
	
	public final static String STR_scalarValueReal_1 = "{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":14.2}";
	
	public final static String STR_serializableVector_0 = "{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2},{\"i\":2,\"v\":3.53}]}";
	
	public final static String STR_scalarValueCollection_0 = "{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2},{\"i\":2,\"v\":3.53}]}}";
	
	public final static String STR_scalarValueResults_0 = "{\"t\":\"ScalarValueResults\",\"time_\":2800.1,\"input\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2},{\"i\":2,\"v\":3.53}]}},\"output\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":3,\"v\":258.2},{\"i\":4,\"v\":78}]}}}";
	
	public final static String STR_scalarVariableReal_0 = "{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}}";

	public final static String STR_scalarVariableCollection_0 = "{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C1\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.26,\"min\":2.27,\"max\":2.28,\"unit\":\"C2\"}}]}}";
	
	public final static String STR_scalarVariableCollection_1 = "{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"ES-23\",\"i\":0,\"c\":7,\"vb\":5,\"d\":\"This is the pressure in the duct measured in Pa\",\"vr\":7547,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":1,\"nominal\":1000,\"min\":0,\"max\":5400.01,\"unit\":\"Pa\"}},{\"n\":\"PA57-FA\",\"i\":1,\"c\":6,\"vb\":3,\"d\":\"Pressure in Duct per meter\",\"vr\":55,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":12,\"nominal\":50,\"min\":2,\"max\":500000,\"unit\":\"Pa per meter\"}}]}}";
	
	public final static String STR_scalarVariablesAll_1 = "{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C1\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.26,\"min\":2.27,\"max\":2.28,\"unit\":\"C2\"}}]}},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"ES-23\",\"i\":0,\"c\":7,\"vb\":5,\"d\":\"This is the pressure in the duct measured in Pa\",\"vr\":7547,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":1,\"nominal\":1000,\"min\":0,\"max\":5400.01,\"unit\":\"Pa\"}},{\"n\":\"PA57-FA\",\"i\":1,\"c\":6,\"vb\":3,\"d\":\"Pressure in Duct per meter\",\"vr\":55,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":12,\"nominal\":50,\"min\":2,\"max\":500000,\"unit\":\"Pa per meter\"}}]}}}";
	
	public final static String STR_XMLparsedInfo_1 = "{\"t\":\"XMLparsedInfo\",\"scalarVariablesAll_\":{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}},\"internal\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description 1\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.25,\"min\":2.25,\"max\":2.25,\"unit\":\"Pa\"}}]}}},\"sessionID_\":\"xxo\"}";
	
	public final static String STR_sessionControlAction_0 = "{\"t\":\"SessionControlAction\",\"intValue\":0}";
	
	public final static String STR_sessionControlAction_1 = "{\"t\":\"SessionControlAction\",\"intValue\":1}";
	
	public final static String STR_sessionControlModel_0 = "{\"t\":\"SessionControlModel\",\"v\":\"SESS1342\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":0}}";
	
	public final static String STR_sessionControlModel_1 = "{\"t\":\"SessionControlModel\",\"v\":\"\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":1}}";
	
	
	
	
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
	public void T01_scalarValueReal_serialize() {

		
		//make struct
		ScalarValueRealStruct scalarValueRealStruct_0 = new ScalarValueRealStruct();
		scalarValueRealStruct_0.idx = 1;
		scalarValueRealStruct_0.value = 2.0;
		
		//make Object
		ScalarValueReal scalarValueReal_0 = new ScalarValueReal(scalarValueRealStruct_0);

		//serialize / deserialize
		String jsonString_0 = scalarValueReal_0.toJsonString();
		
		
		assertEquals(
				STR_scalarValueReal_0, 
				jsonString_0);
		
		ScalarValueReal scalarValueReal_1 = new ScalarValueReal(2, 14.2);
		String jsonString_1 = scalarValueReal_1.toJsonString();
		
		assertEquals(
				STR_scalarValueReal_1, 
				jsonString_1);
	}
	

	
	@Test
	public void T02_scalarValueReal_deserialize() {
		
		
		Object deserializedObject_0 = gsonController_.fromJson(STR_scalarValueReal_0);
		
		assertEquals(ScalarValueReal.class, deserializedObject_0.getClass());
		ScalarValueReal scalarValueReal_0 = (ScalarValueReal) deserializedObject_0;
		
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
	
		
		Object deserializedObject_1 = gsonController_.fromJson(STR_scalarValueReal_1);
		
		assertEquals(ScalarValueReal.class, deserializedObject_1.getClass());
		ScalarValueReal scalarValueReal_1 = (ScalarValueReal) deserializedObject_1;
		
		assertEquals(2, scalarValueReal_1.getIdx());
		assertEquals(14.2, scalarValueReal_1.getValue(), 0.0);
		
	}
	
	
	
	@Test
    public void T03_scalarVariableReal_serialize() {
		
		//make TypeSpecReal
		TypeSpecReal typeSpecReal_0 = new TypeSpecReal();
		typeSpecReal_0.start = 20.25;
		typeSpecReal_0.nominal = 21.25;
		typeSpecReal_0.min = 22.25;
		typeSpecReal_0.max = 23.25;
		typeSpecReal_0.unit ="C";
		
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
		scalarVariableReal_0.setDescription("The Description");
		scalarVariableReal_0.setValueReference(125420);
		
		//serialize
		String jsonString_0 = scalarVariableReal_0.toJsonString();
		
		assertEquals(
				STR_scalarVariableReal_0,
				jsonString_0);
		
	}
	
	
	@Test
    public void T04_scalarVariableReal_deserialize() {
		

		Object deserializedObject_0 = gsonController_.fromJson(STR_scalarVariableReal_0);
		
		assertEquals(ScalarVariableReal.class, deserializedObject_0.getClass());
		ScalarVariableReal scalarVariableReal_0 = (ScalarVariableReal) deserializedObject_0;
		
		
		assertEquals(125420, scalarVariableReal_0.getValueReference());
		assertEquals(1, scalarVariableReal_0.getIdx());
		assertEquals("scalarVar name", scalarVariableReal_0.getName());
		
		assertEquals(6, scalarVariableReal_0.getCausalityAsInt());
		assertEquals(Enu.enu_input, scalarVariableReal_0.getCausalityAsEnum());
		
		assertEquals(5, scalarVariableReal_0.getVariabilityAsInt());
		assertEquals(Enu.enu_continuous, scalarVariableReal_0.getVariabilityAsEnum());
		
		assertEquals("The Description", scalarVariableReal_0.getDescription());
		
		TypeSpecReal typeSpecReal_0 = scalarVariableReal_0.getTypeSpecReal();
		
		assertEquals(20.25, typeSpecReal_0.start, 0.0);
		assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		assertEquals(22.25, typeSpecReal_0.min, 0.0);
		assertEquals(23.25, typeSpecReal_0.max, 0.0);
		assertEquals("C", typeSpecReal_0.unit);
		
	}

	
	@Test
	public void T05_scalarValueCollection_serialize() {
		
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
		
		
		String jsonString_0 = gsonController_.toJsonString(realList_0);
		
		assertEquals(
			STR_serializableVector_0, 
			jsonString_0
		);
		
		ScalarValueCollection scalarValueCollection_0 = new ScalarValueCollection(realList_0);
		String jsonString_1 = scalarValueCollection_0.toJsonString();
		
		assertEquals(
			STR_scalarValueCollection_0, 
			jsonString_1
		);
		
	}
	
	
	
	@Test
	public void T06_scalarValueCollection_deserialize() {


		Object deserializedObject_1 = gsonController_.fromJson(STR_scalarValueCollection_0);
		
		assertEquals(ScalarValueCollection.class, deserializedObject_1.getClass());
		ScalarValueCollection scalarValueCollection_1 = (ScalarValueCollection) deserializedObject_1;
		
		Vector<ScalarValueReal> realList_1 = scalarValueCollection_1.getRealList();
		
		ScalarValueReal scalarValueReal_0 = realList_1.get(0);
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_1 = realList_1.get(1);
		assertEquals(2, scalarValueReal_1.getIdx());
		assertEquals(3.53, scalarValueReal_1.getValue(), 0.0);
		
	}
	
	
	
	
	
	@Test
	public void T07_serializableVector_serialize() {
		
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
		
		
		String jsonString_0 = gsonController_.toJsonString(realList_0);
		
		assertEquals(
				STR_serializableVector_0, 
				jsonString_0
			);
			
		
	}
	
	
	
	@Test
	public void T08_serializableVector_deserialize() {
		
		Iserializable deserializedObject_0 = gsonController_.fromJson(STR_serializableVector_0);
		SerializableVector<ScalarValueReal> realList_0  = (SerializableVector<ScalarValueReal>) deserializedObject_0;

		return;
	}
	
	
	

	
	@Test
    public void T09_scalarValueResults_serialize() {
		
		
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
		ScalarValueResults scalarValueResults = new ScalarValueResults();
		scalarValueResults.setInput(scalarValueCollection_0);
		scalarValueResults.setOutput(scalarValueCollection_1);
		scalarValueResults.setTime(2800.1);
		
		String jsonString_0 = scalarValueResults.toJsonString();

		assertEquals(
				STR_scalarValueResults_0,
				jsonString_0);
		
		
	}
	
	
	@Test
    public void T10_scalarValueResults_deserialize() {
		
		
		Object deserializedObject_0 = gsonController_.fromJson(STR_scalarValueResults_0);
		
		assertEquals(ScalarValueResults.class, deserializedObject_0.getClass());
		
		ScalarValueResults scalarValueResults_0 = (ScalarValueResults) deserializedObject_0;
		
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
    public void T11_scalarVariableCollection_serialize() {
      
		ScalarVariableCollection scalarVariableCollection_0 = getScalarVariableCollection_A_();

		//serialize
		String jsonString_0 = scalarVariableCollection_0.toJsonString();
		assertEquals(
				STR_scalarVariableCollection_0,
				jsonString_0);
	
		
		ScalarVariableCollection getScalarVariableCollection_1 = getScalarVariableCollection_B_();

		//serialize
		String jsonString_1 = getScalarVariableCollection_1.toJsonString();
		assertEquals(
				STR_scalarVariableCollection_1,
				jsonString_1);
		
		
	}
	
	
	
	@Test
	public void T12_scalarVariableCollection_deserialize() {
		
		Object deserializedObject_0 = gsonController_.fromJson(STR_scalarVariableCollection_0);
		
		assertEquals(ScalarVariableCollection.class, deserializedObject_0.getClass());
		ScalarVariableCollection scalarVariableCollection_0 = (ScalarVariableCollection) deserializedObject_0;
		
		
		ScalarVariableReal scalarVariableReal_0 = scalarVariableCollection_0.getRealVarList().get(0);
		
		assertEquals(125420, scalarVariableReal_0.getValueReference());
		assertEquals(1, scalarVariableReal_0.getIdx());
		assertEquals("scalarVar name", scalarVariableReal_0.getName());
		
		assertEquals(6, scalarVariableReal_0.getCausalityAsInt());
		assertEquals(Enu.enu_input, scalarVariableReal_0.getCausalityAsEnum());
		
		assertEquals(5, scalarVariableReal_0.getVariabilityAsInt());
		assertEquals(Enu.enu_continuous, scalarVariableReal_0.getVariabilityAsEnum());
		
		assertEquals("The Description", scalarVariableReal_0.getDescription());
		
		TypeSpecReal typeSpecReal_0 = scalarVariableReal_0.getTypeSpecReal();
		
		assertEquals(20.25, typeSpecReal_0.start, 0.0);
		assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		assertEquals(22.25, typeSpecReal_0.min, 0.0);
		assertEquals(23.25, typeSpecReal_0.max, 0.0);
		assertEquals("C1", typeSpecReal_0.unit);
		
		
		ScalarVariableReal scalarVariableReal_1 = scalarVariableCollection_0.getRealVarList().get(1);
		
		assertEquals(125420, scalarVariableReal_1.getValueReference());
		assertEquals(1, scalarVariableReal_1.getIdx());
		assertEquals("scalarVar name", scalarVariableReal_1.getName());
		
		assertEquals(6, scalarVariableReal_1.getCausalityAsInt());
		assertEquals(Enu.enu_input, scalarVariableReal_1.getCausalityAsEnum());
		
		assertEquals(4, scalarVariableReal_1.getVariabilityAsInt());
		assertEquals(Enu.enu_discrete, scalarVariableReal_1.getVariabilityAsEnum());
		
		assertEquals("The Description", scalarVariableReal_1.getDescription());
		
		TypeSpecReal typeSpecReal_1 = scalarVariableReal_1.getTypeSpecReal();
		
		assertEquals(2.25, typeSpecReal_1.start, 0.0);
		assertEquals(2.26, typeSpecReal_1.nominal, 0.0);
		assertEquals(2.27, typeSpecReal_1.min, 0.0);
		assertEquals(2.28, typeSpecReal_1.max, 0.0);
		assertEquals("C2", typeSpecReal_1.unit);
		
		
		
		Object deserializedObject_1 = gsonController_.fromJson(STR_scalarVariableCollection_1);
		
		assertEquals(ScalarVariableCollection.class, deserializedObject_1.getClass());
		ScalarVariableCollection scalarVariableCollection_1 = (ScalarVariableCollection) deserializedObject_1;
		
		ScalarVariableReal scalarVariableReal_2 = scalarVariableCollection_1.getRealVarList().get(0);
		assertEquals(7547, scalarVariableReal_2.getValueReference());
		
		assertEquals(0, scalarVariableReal_2.getIdx());
		assertEquals("ES-23", scalarVariableReal_2.getName());
		
		assertEquals(7, scalarVariableReal_2.getCausalityAsInt());
		assertEquals(Enu.enu_output, scalarVariableReal_2.getCausalityAsEnum());
		
		assertEquals(5, scalarVariableReal_2.getVariabilityAsInt());
		assertEquals(Enu.enu_continuous, scalarVariableReal_2.getVariabilityAsEnum());
		
		assertEquals("This is the pressure in the duct measured in Pa", scalarVariableReal_2.getDescription());
		
		TypeSpecReal typeSpecReal_2 = scalarVariableReal_2.getTypeSpecReal();
		
		assertEquals(1.0, typeSpecReal_2.start, 0.0);
		assertEquals(1000.0, typeSpecReal_2.nominal, 0.0);
		assertEquals(0.0, typeSpecReal_2.min, 0.0);
		assertEquals(5400.01, typeSpecReal_2.max, 0.0);
		assertEquals("Pa", typeSpecReal_2.unit);
		
		
		
		ScalarVariableReal scalarVariableReal_3 = scalarVariableCollection_1.getRealVarList().get(1);
		assertEquals(55, scalarVariableReal_3.getValueReference());
		
		assertEquals(1, scalarVariableReal_3.getIdx());
		assertEquals("PA57-FA", scalarVariableReal_3.getName());
		
		assertEquals(6, scalarVariableReal_3.getCausalityAsInt());
		assertEquals(Enu.enu_input, scalarVariableReal_3.getCausalityAsEnum());
		
		assertEquals(3, scalarVariableReal_3.getVariabilityAsInt());
		assertEquals(Enu.enu_parameter, scalarVariableReal_3.getVariabilityAsEnum());
		
		assertEquals("Pressure in Duct per meter", scalarVariableReal_3.getDescription());
		
		TypeSpecReal typeSpecReal_3 = scalarVariableReal_3.getTypeSpecReal();
		
		assertEquals(12.0, typeSpecReal_3.start, 0.0);
		assertEquals(50.0, typeSpecReal_3.nominal, 0.0);
		assertEquals(2.0, typeSpecReal_3.min, 0.0);
		assertEquals(500000.0, typeSpecReal_3.max, 0.0);
		assertEquals("Pa per meter", typeSpecReal_3.unit);
		
		
	}
	
	
	
	@Test
    public void T13_scalarVariablesAll_serialize() {
		

		ScalarVariableCollection scalarVariableCollection_0 = getScalarVariableCollection_A_();
		ScalarVariableCollection scalarVariableCollection_1 = getScalarVariableCollection_B_();

		
		//make scalarVariablesAll 1
		ScalarVariablesAll scalarVariablesAll_0 = new ScalarVariablesAll();
		scalarVariablesAll_0.setInput(scalarVariableCollection_0);
		scalarVariablesAll_0.setOutput(scalarVariableCollection_1);

		
		//serialize
		String jsonString_0 = scalarVariablesAll_0.toJsonString();
		
		assertEquals(
				STR_scalarVariablesAll_1,
				jsonString_0);
					
		
	}
	
	
	@Test
    public void T14_scalarVariablesAll_deserialize() {
		
		
		Object deserializedObject_0 = gsonController_.fromJson(STR_scalarVariablesAll_1);
		

		assertEquals(ScalarVariablesAll.class, deserializedObject_0.getClass());
		ScalarVariablesAll scalarVariablesAll_0 = (ScalarVariablesAll) deserializedObject_0;
		
		
		ScalarVariableCollection scalarVariableCollection_0 = scalarVariablesAll_0.getInput();
		
		Vector<ScalarVariableReal> realList_0 = scalarVariableCollection_0.getRealVarList();
		
		ScalarVariableReal scalarVariableReal_0 = realList_0.get(0);
		

		assertEquals( "scalarVar name", scalarVariableReal_0.getName());
		
		
		TypeSpecReal typeSpecReal_0 = scalarVariableReal_0.getTypeSpecReal();
		
		assertEquals( 20.25, typeSpecReal_0.start, 0.0);
		assertEquals( 21.25, typeSpecReal_0.nominal, 0.0);
		assertEquals( 22.25, typeSpecReal_0.min, 0.0);
		assertEquals( 23.25, typeSpecReal_0.max, 0.0);
		assertEquals( "C1", typeSpecReal_0.unit);
		
		
		Vector<ScalarVariableReal> realList_1 = scalarVariableCollection_0.getRealVarList();
		ScalarVariableReal scalarVariableReal_1 = realList_1.get(1);
		
		
		assertEquals( "scalarVar name", scalarVariableReal_1.getName());
		TypeSpecReal typeSpecReal_1 = scalarVariableReal_1.getTypeSpecReal();
		
		assertEquals( 2.25, typeSpecReal_1.start, 0.0);
		assertEquals( 2.26, typeSpecReal_1.nominal, 0.0);
		assertEquals( 2.27, typeSpecReal_1.min, 0.0);
		assertEquals( 2.28, typeSpecReal_1.max, 0.0);
		assertEquals( "C2", typeSpecReal_1.unit);
		
		return;
		
	}
	


	
	
	@Test
    public void T15_sessionControlAction_serialize() {
	
		
		SessionControlAction sessionControlAction_0 = SessionControlAction.attachToSession;
		String jsonString_0 = sessionControlAction_0.toJsonString();
		
		assertEquals(
				STR_sessionControlAction_0,
				jsonString_0);
		
		
		SessionControlAction sessionControlAction_1 = SessionControlAction.getInfo;
		String jsonString_1 = sessionControlAction_1.toJsonString();
		
		assertEquals(
				STR_sessionControlAction_1, 
				jsonString_1);

	}
	
	
	@Test
	public void T16_sessionControlAction_deserialize() {
		

		Object deserializedObject_0 = gsonController_.fromJson(STR_sessionControlAction_0);
		
		assertEquals(SessionControlAction.class, deserializedObject_0.getClass());
		SessionControlAction sessionControlAction_0 = (SessionControlAction) deserializedObject_0;
		
		assertEquals(SessionControlAction.attachToSession , sessionControlAction_0);
		
		
		Object deserializedObject_1 = gsonController_.fromJson(STR_sessionControlAction_1);
		
		assertEquals(SessionControlAction.class, deserializedObject_1.getClass());
		SessionControlAction sessionControlAction_1 = (SessionControlAction) deserializedObject_1;
		
		assertEquals(SessionControlAction.getInfo , sessionControlAction_1);

	}
	
	
	

	
	@Test
    public void T17_sessionControlModel_serialize() {
	
		
		SessionControlAction sessionControlAction_0 = SessionControlAction.attachToSession;
		SessionControlModel sessionControlModel_0 = new SessionControlModel(sessionControlAction_0, "SESS1342");
		
		String jsonString_0 = sessionControlModel_0.toJsonString();
		
		assertEquals(
				STR_sessionControlModel_0,
				jsonString_0);
		
		

		SessionControlAction sessionControlAction_1 = SessionControlAction.getInfo;
		SessionControlModel sessionControlModel_1 = new SessionControlModel(sessionControlAction_1);
		
		String jsonString_1 = sessionControlModel_1.toJsonString();
		
		assertEquals(
				STR_sessionControlModel_1, 
				jsonString_1);

	}
	
	
	
	@Test
    public void T18_sessionControlModel_deserialize() {
		
		Object deserializedObject_0 = gsonController_.fromJson(STR_sessionControlModel_0);
		
		assertEquals(SessionControlModel.class, deserializedObject_0.getClass());
		SessionControlModel sessionControlModel_0 = (SessionControlModel) deserializedObject_0;
		
		
		assertEquals(SessionControlAction.attachToSession , sessionControlModel_0.getAction());
		assertEquals("SESS1342" , sessionControlModel_0.getValue());

	}
	
	

	
	@Test
    public void T19_xmlParsedInfo_serialize() {
		
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
		
		ScalarVariableCollection scalarVariableCollection_0 = new ScalarVariableCollection();
		scalarVariableCollection_0.setRealVarList(realVarList);
		
		//make scalarVariablesAll 1
		ScalarVariablesAll scalarVariablesAll = new ScalarVariablesAll();
		scalarVariablesAll.setInput(scalarVariableCollection_0);
		scalarVariablesAll.setOutput(scalarVariableCollection_0);
		scalarVariablesAll.setInternal(scalarVariableCollection_0);
		
		//make SimStateWrapper 1
		XMLparsedInfo xmlParsedInfo_0 = new XMLparsedInfo(scalarVariablesAll);

		//serialize
		String jsonString_0 = xmlParsedInfo_0.toJsonString();
		
		assertEquals(
			STR_XMLparsedInfo_1,
			jsonString_0	
		);
	}
	
	
	
	@Test
    public void T20_xmlParsedInfo_deserialize() {
		
		Object deserializedObject_0 = gsonController_.fromJson(STR_XMLparsedInfo_1);
		
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

	private ScalarVariableCollection getScalarVariableCollection_A_() {
		
	    //make TypeSpecReal 0
		TypeSpecReal typeSpecReal_0 = new TypeSpecReal();
	    typeSpecReal_0.start = 20.25;
	    typeSpecReal_0.nominal = 21.25;
	    typeSpecReal_0.min = 22.25;
	    typeSpecReal_0.max = 23.25;
	    typeSpecReal_0.unit = "C1";
	    
	    typeSpecReal_0.startValueStatus = 1;
	    typeSpecReal_0.nominalValueStatus = 1;
	    typeSpecReal_0.minValueStatus = 1;
	    typeSpecReal_0.maxValueStatus = 1;
	    typeSpecReal_0.unitValueStatus = 1;
	    
	    //make ScalarVariableReal 0
	    ScalarVariableReal scalarVariableReal_0 = new ScalarVariableReal(typeSpecReal_0);
	    scalarVariableReal_0.setName("scalarVar name");
	    scalarVariableReal_0.setIdx(1);
	    scalarVariableReal_0.setCausality(Enu.enu_input);
	    scalarVariableReal_0.setVariability(Enu.enu_continuous);
	    scalarVariableReal_0.setDescription("The Description");
	    scalarVariableReal_0.setValueReference(125420);
	    
	    
	    //make TypeSpecReal 1
	    TypeSpecReal typeSpecReal_1 = new TypeSpecReal();
	    typeSpecReal_1.start = 2.25;
	    typeSpecReal_1.nominal = 2.26;
	    typeSpecReal_1.min = 2.27;
	    typeSpecReal_1.max = 2.28;
	    typeSpecReal_1.unit = "C2";
	    typeSpecReal_1.startValueStatus = 1;
	    typeSpecReal_1.nominalValueStatus = 1;
	    typeSpecReal_1.minValueStatus = 1;
	    typeSpecReal_1.maxValueStatus = 1;
	    typeSpecReal_1.unitValueStatus = 1;
	    
	    //make ScalarVariableReal 1
	    ScalarVariableReal scalarVariableReal_1 = new ScalarVariableReal(typeSpecReal_1);
	    scalarVariableReal_1.setName("scalarVar name");
	    scalarVariableReal_1.setIdx(1);
	    scalarVariableReal_1.setCausality(Enu.enu_input);
	    scalarVariableReal_1.setVariability(Enu.enu_discrete);
	    scalarVariableReal_1.setDescription("The Description");
	    scalarVariableReal_1.setValueReference(125420);
	    
	    SerializableVector<ScalarVariableReal> realList_0 = new SerializableVector<ScalarVariableReal>("ScalarVariableReal");
		realList_0.add(scalarVariableReal_0);
		realList_0.add(scalarVariableReal_1);
		
		ScalarVariableCollection scalarVariableCollection_0 = new ScalarVariableCollection();
	    scalarVariableCollection_0.setRealVarList(realList_0);
	    
	    return scalarVariableCollection_0;
	    
	}
	
	
	private ScalarVariableCollection getScalarVariableCollection_B_() {
		
	    //make TypeSpecReal 1
		TypeSpecReal typeSpecReal_0 = new TypeSpecReal();
	    typeSpecReal_0.start = 1.0;
	    typeSpecReal_0.nominal = 1000.0;
	    typeSpecReal_0.min = 0.0;
	    typeSpecReal_0.max = 5400.01;
	    typeSpecReal_0.unit = "Pa";
	    
	    typeSpecReal_0.startValueStatus = 1;
	    typeSpecReal_0.nominalValueStatus = 1;
	    typeSpecReal_0.minValueStatus = 1;
	    typeSpecReal_0.maxValueStatus = 1;
	    typeSpecReal_0.unitValueStatus = 1;
	    
	    //make ScalarVariableReal 1
	    ScalarVariableReal scalarVariableReal_0 = new ScalarVariableReal(typeSpecReal_0);
	    scalarVariableReal_0.setName("ES-23");
	    scalarVariableReal_0.setIdx(0);
	    scalarVariableReal_0.setCausality(Enu.enu_output);
	    scalarVariableReal_0.setVariability(Enu.enu_continuous);
	    scalarVariableReal_0.setDescription("This is the pressure in the duct measured in Pa");
	    scalarVariableReal_0.setValueReference(7547);
	    
	    //make TypeSpecReal 2
	    TypeSpecReal typeSpecReal_1 = new TypeSpecReal();
	    typeSpecReal_1.start = 12.0;
	    typeSpecReal_1.nominal = 50.0;
	    typeSpecReal_1.min = 2.0;
	    typeSpecReal_1.max = 500000.0;
	    typeSpecReal_1.unit = "Pa per meter";
	    typeSpecReal_1.startValueStatus = 1;
	    typeSpecReal_1.nominalValueStatus = 1;
	    typeSpecReal_1.minValueStatus = 1;
	    typeSpecReal_1.maxValueStatus = 1;
	    typeSpecReal_1.unitValueStatus = 1;
	    
	    //make ScalarVariableReal 2
	    ScalarVariableReal scalarVariableReal_1 = new ScalarVariableReal(typeSpecReal_1);
	    scalarVariableReal_1.setName("PA57-FA");
	    scalarVariableReal_1.setIdx(1);
	    scalarVariableReal_1.setCausality(Enu.enu_input);
	    scalarVariableReal_1.setVariability(Enu.enu_parameter);
	    scalarVariableReal_1.setDescription("Pressure in Duct per meter");
	    scalarVariableReal_1.setValueReference(55);
	    
	    SerializableVector<ScalarVariableReal> realList_0 = new SerializableVector<ScalarVariableReal>("ScalarVariableReal");
		realList_0.add(scalarVariableReal_0);
		realList_0.add(scalarVariableReal_1);
		
		ScalarVariableCollection scalarVariableCollection_0 = new ScalarVariableCollection();
	    scalarVariableCollection_0.setRealVarList(realList_0);
	    
	    return scalarVariableCollection_0;
	    
	}
	
}

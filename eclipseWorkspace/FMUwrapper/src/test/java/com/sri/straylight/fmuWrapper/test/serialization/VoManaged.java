package com.sri.straylight.fmuWrapper.test.serialization;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.test.base.OrderedRunner;
import com.sri.straylight.fmuWrapper.test.main.CONSTANTS;
import com.sri.straylight.fmuWrapper.test.main.TestDataGenerator;
import com.sri.straylight.fmuWrapper.test.main.Util;
import com.sri.straylight.fmuWrapper.voManaged.InitialState;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
import com.sri.straylight.fmuWrapper.voManaged.SerializableVector;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlAction;
import com.sri.straylight.fmuWrapper.voManaged.SessionControlModel;
import com.sri.straylight.fmuWrapper.voManaged.StringPrimitive;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;



@RunWith(OrderedRunner.class)
public class VoManaged {
	
	
	/** The for serialization. */
	private  JsonController gsonController_ = JsonController.getInstance();

	
	
	
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}


	
	@Test
	public void T01_scalarValueReal_serialize() {

		//make struct
		ScalarValueRealStruct scalarValueRealStruct_0 = new ScalarValueRealStruct();
		scalarValueRealStruct_0.idx = 1;
		scalarValueRealStruct_0.value = 2.0;
		
		//make Object
		ScalarValueReal scalarValueReal_0 = new ScalarValueReal(scalarValueRealStruct_0);

		
		Util.serializeOk(
    		scalarValueReal_0,
    	    CONSTANTS.STR_scalarValueReal_0
		);
	    	    
	    
		
		ScalarValueReal scalarValueReal_1 = new ScalarValueReal(2, 14.2);
		
		Util.serializeOk(
			scalarValueReal_1,
    	    CONSTANTS.STR_scalarValueReal_1
		);

	}
	

	
	@Test
	public void T02_scalarValueReal_deserialize() {
		
		
		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_scalarValueReal_0,
			ScalarValueReal.class
		);
		
		ScalarValueReal scalarValueReal_0 = (ScalarValueReal) deserializedObject_0;
		
		
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
	
		
		Iserializable deserializedObject_1 = Util.deserializeOk(
			CONSTANTS.STR_scalarValueReal_1,
			ScalarValueReal.class
		);
		
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
		
		
		Util.serializeOk(
			scalarVariableReal_0,
    	    CONSTANTS.STR_scalarVariableReal_0
		);
		
	}
	
	
	@Test
    public void T04_scalarVariableReal_deserialize() {
		

		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_scalarVariableReal_0,
			ScalarVariableReal.class
		);
		
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
		
		
		Util.serializeOk(
				realList_0,
	    	    CONSTANTS.STR_serializableVector_0
			);
		
		
		ScalarValueCollection scalarValueCollection_0 = new ScalarValueCollection(realList_0);

		
		Util.serializeOk(
				scalarValueCollection_0,
	    	    CONSTANTS.STR_scalarValueCollection_0
			);
		
		
	}
	
	
	
	@Test
	public void T06_scalarValueCollection_deserialize() {


		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_scalarValueCollection_0,
			ScalarValueCollection.class
		);
		
		ScalarValueCollection scalarValueCollection_0 = (ScalarValueCollection) deserializedObject_0;
		
		Vector<ScalarValueReal> realList_0 = scalarValueCollection_0.getRealList();
		
		ScalarValueReal scalarValueReal_0 = realList_0.get(0);
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_1 = realList_0.get(1);
		assertEquals(2, scalarValueReal_1.getIdx());
		assertEquals(3.53, scalarValueReal_1.getValue(), 0.0);
		
	}
	
	
	
	
	
	@Test
	public void T07_serializableVectorA_serialize() {
		
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
		SerializableVector<ScalarValueReal> serializableVector_0 = new SerializableVector<ScalarValueReal>("ScalarValueReal");
		serializableVector_0.add(scalarValueReal_0);
		serializableVector_0.add(scalarValueReal_1);
		
		
		Util.serializeOk(
				serializableVector_0,
	    	    CONSTANTS.STR_serializableVector_0
			);
		
		
	}
	
	
	
	@Test
	public void T08_serializableVectorA_deserialize() {

		
		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_serializableVector_0,
			SerializableVector.class
		);
			
		SerializableVector<ScalarValueReal> serializableVector_0  = (SerializableVector<ScalarValueReal>) deserializedObject_0;
		

		String itemTypeString  = serializableVector_0.getItemTypeString();
		assertEquals("ScalarValueReal", itemTypeString);
		
		ScalarValueReal scalarValueReal_0 = serializableVector_0.get(0);
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
		
		
		ScalarValueReal scalarValueReal_1 = serializableVector_0.get(1);
		assertEquals(2, scalarValueReal_1.getIdx());
		assertEquals(3.53, scalarValueReal_1.getValue(), 0.0);
		
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
		
		
		Util.serializeOk(
				scalarValueResults,
	    	    CONSTANTS.STR_scalarValueResults_0
			);
		
		
	}
	
	
	@Test
    public void T10_scalarValueResults_deserialize() {
		

		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_scalarValueResults_0,
			ScalarValueResults.class
		);
		

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
      
		ScalarVariableCollection scalarVariableCollection_0 = TestDataGenerator.getScalarVariableCollection_A_();

		Util.serializeOk(
			scalarVariableCollection_0,
    	    CONSTANTS.STR_scalarVariableCollection_0
		);
		
		
		ScalarVariableCollection getScalarVariableCollection_1 = TestDataGenerator.getScalarVariableCollection_B_();

		
		Util.serializeOk(
				getScalarVariableCollection_1,
    	    CONSTANTS.STR_scalarVariableCollection_1
		);
		
	}
	
	
	
	@Test
	public void T12_scalarVariableCollection_deserialize() {
		

		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_scalarVariableCollection_0,
			ScalarVariableCollection.class
		);
		
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
		
		
		
		Object deserializedObject_1 = gsonController_.fromJson(CONSTANTS.STR_scalarVariableCollection_1);
		
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
		
	}
	
	
	@Test
    public void T14_scalarVariablesAll_deserialize() {
		
		
		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_scalarVariablesAll_0,
			ScalarVariablesAll.class
		);
		
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

		Util.serializeOk(
			sessionControlAction_0,
    	    CONSTANTS.STR_sessionControlAction_0
		);

		
		SessionControlAction sessionControlAction_1 = SessionControlAction.getInfo;


		Util.serializeOk(
			sessionControlAction_1,
    	    CONSTANTS.STR_sessionControlAction_1
		);
		
	}
	
	
	@Test
	public void T16_sessionControlAction_deserialize() {
		

		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_sessionControlAction_0,
			SessionControlAction.class
		);
		
		
		SessionControlAction sessionControlAction_0 = (SessionControlAction) deserializedObject_0;
		assertEquals(SessionControlAction.attachToSession , sessionControlAction_0);
		
		
		Object deserializedObject_1 = gsonController_.fromJson(CONSTANTS.STR_sessionControlAction_1);
		
		assertEquals(SessionControlAction.class, deserializedObject_1.getClass());
		SessionControlAction sessionControlAction_1 = (SessionControlAction) deserializedObject_1;
		
		assertEquals(SessionControlAction.getInfo , sessionControlAction_1);

	}
	
	
	

	
	@Test
    public void T17_sessionControlModel_serialize() {
	
		
		SessionControlAction sessionControlAction_0 = SessionControlAction.attachToSession;
		SessionControlModel sessionControlModel_0 = new SessionControlModel(sessionControlAction_0, "SESS1342");
		
		
		Util.serializeOk(
			sessionControlModel_0,
    	    CONSTANTS.STR_sessionControlModel_0
		);
	
		

		SessionControlAction sessionControlAction_1 = SessionControlAction.getInfo;
		SessionControlModel sessionControlModel_1 = new SessionControlModel(sessionControlAction_1);
		

		Util.serializeOk(
			sessionControlModel_1,
    	    CONSTANTS.STR_sessionControlModel_1
		);
		
	}
	
	
	
	@Test
    public void T18_sessionControlModel_deserialize() {
		

		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_sessionControlModel_0,
			SessionControlModel.class
		);
		
		
		SessionControlModel sessionControlModel_0 = (SessionControlModel) deserializedObject_0;
		
		
		assertEquals(SessionControlAction.attachToSession , sessionControlModel_0.getAction());
		assertEquals("SESS1342" , sessionControlModel_0.getValue());

	}
	
	@Test
    public void T19_xmlParsedInfo_serialize() {
		
		ScalarVariableCollection scalarVariableCollection_0 = TestDataGenerator.getScalarVariableCollection_A_();
		assertEquals(ScalarVariableCollection.class, scalarVariableCollection_0.getClass());
		
		ScalarVariableCollection scalarVariableCollection_1 = TestDataGenerator.getScalarVariableCollection_B_();
		assertEquals(ScalarVariableCollection.class, scalarVariableCollection_1.getClass());
		
		ScalarVariablesAll scalarVariablesAll_0 = new ScalarVariablesAll();
		scalarVariablesAll_0.setInput(scalarVariableCollection_0);
		scalarVariablesAll_0.setOutput(scalarVariableCollection_1);

		//make SimStateWrapper 1
		XMLparsedInfo xmlParsedInfo_0 = new XMLparsedInfo(scalarVariablesAll_0);

		Util.serializeOk(
			xmlParsedInfo_0,
    	    CONSTANTS.STR_XMLparsedInfo_0
		);
	
	}
	
	
 
	
	
	@Test
    public void T20_xmlParsedInfo_deserialize() {
		

		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_XMLparsedInfo_0,
			XMLparsedInfo.class
		);
		
		
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
		
		assertEquals( "The Description", scalarVariableReal_0.getDescription());
		assertEquals( "C1", scalarVariableReal_0.getUnit());
		
		TypeSpecReal typeSpecReal_0 = scalarVariableReal_0.getTypeSpecReal();
		
		assertEquals(20.25 , typeSpecReal_0.start, 0.0);
		assertEquals(21.25 , typeSpecReal_0.nominal, 0.0);
		assertEquals(22.25 , typeSpecReal_0.min, 0.0);
		assertEquals(23.25 , typeSpecReal_0.max, 0.0);
		assertEquals("C1" , typeSpecReal_0.unit);
		
		return;
		
	}

	
	@Test
    public void T21_serializableVectorB_serialize() {
		
		SerializableVector<StringPrimitive> serializableVector_0 = 
				new SerializableVector<StringPrimitive>("StringPrimitive");
		
		serializableVector_0.add(new StringPrimitive("y_ZN[1]"));
		serializableVector_0.add(new StringPrimitive("y_ZN[5]"));
		
		Util.serializeOk(
				serializableVector_0,
	    	    CONSTANTS.STR_serializableVector_1
			);
		
		return;
		
	}
	
	
	@Test
    public void T22_serializableVectorB_deserialize() {
		
		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_serializableVector_1,
			SerializableVector.class
		);
		
		
		SerializableVector<StringPrimitive> serializableVector_0 = (SerializableVector<StringPrimitive>) deserializedObject_0;
		
		StringPrimitive stringPrimitive_0 =  serializableVector_0.get(0);
		String str_0 = stringPrimitive_0.getValue();
		assertEquals("y_ZN[1]" , str_0);
		  
		StringPrimitive stringPrimitive_1 =  serializableVector_0.get(1);
		String str_1 = stringPrimitive_1.getValue();
		assertEquals("y_ZN[5]" , str_1);
		
	}
    
	
	@Test
	public void T23_initialState_serialize() {
		
		
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
		
		
		Util.serializeOk(
				realList_0,
	    	    CONSTANTS.STR_serializableVector_0
			);
		
		
		ScalarValueCollection scalarValueCollection_0 = new ScalarValueCollection(realList_0);

		
		Util.serializeOk(
				scalarValueCollection_0,
	    	    CONSTANTS.STR_scalarValueCollection_0
			);

		
		DefaultExperimentStruct.ByReference defaultExperimentStruct 
			= new DefaultExperimentStruct.ByReference();

		defaultExperimentStruct.startTime = 123.03;
		defaultExperimentStruct.stopTime = 145.03;
		defaultExperimentStruct.tolerance = 10.0;
		
		ConfigStruct configStruct_0 = new ConfigStruct();
		configStruct_0.stepDelta = 1.0;
		configStruct_0.defaultExperimentStruct = defaultExperimentStruct;
		
	    Util.serializeOk(
	    	configStruct_0,
	    	CONSTANTS.STR_configStruct_0
  	    );

	    
		SerializableVector<StringPrimitive> serializableVector_0 = 
				new SerializableVector<StringPrimitive>("StringPrimitive");
		
		serializableVector_0.add(new StringPrimitive("y_ZN[1]"));
		serializableVector_0.add(new StringPrimitive("y_ZN[5]"));
		
		Util.serializeOk(
			serializableVector_0,
    	    CONSTANTS.STR_serializableVector_1
		);
		
		
		
		InitialState initialState_0 = new InitialState
			  ( scalarValueCollection_0,
			    configStruct_0,
			    serializableVector_0
			  );
	    
		Util.serializeOk(
				initialState_0,
	    	    CONSTANTS.STR_InitialState_0
			);
		
		
	}
	
	@Test
	public void T24_initialState_deserialize() {
		
		Iserializable deserializedObject_0 = Util.deserializeOk(
			CONSTANTS.STR_InitialState_0,
			InitialState.class
		);
		
		
		InitialState initialState_0 = (InitialState) deserializedObject_0;
		
		ScalarValueCollection parameters_0 = initialState_0.getParameters();
		SerializableVector<StringPrimitive> outputVarList_0 = initialState_0.getOutputVarList();
		ConfigStruct configStruct_0 = initialState_0.getConfigStruct();
		
		assertEquals(1.0, configStruct_0.stepDelta, 0.0);
		assertEquals(123.03, configStruct_0.defaultExperimentStruct.startTime, 0.0);
		assertEquals(145.03, configStruct_0.defaultExperimentStruct.stopTime, 0.0);
		assertEquals(10.0, configStruct_0.defaultExperimentStruct.tolerance, 0.0);
		
		StringPrimitive stringPrimitive_0 =  outputVarList_0.get(0);
		String str_0 = stringPrimitive_0.getValue();
		assertEquals("y_ZN[1]" , str_0);
		  
		StringPrimitive stringPrimitive_1 =  outputVarList_0.get(1);
		String str_1 = stringPrimitive_1.getValue();
		assertEquals("y_ZN[5]" , str_1);
		
		Vector<ScalarValueReal> realList_0 = parameters_0.getRealList();
		
		ScalarValueReal scalarValueReal_0 = realList_0.get(0);
		assertEquals(1, scalarValueReal_0.getIdx());
		assertEquals(2.0, scalarValueReal_0.getValue(), 0.0);
		
		ScalarValueReal scalarValueReal_1 = realList_0.get(1);
		assertEquals(2, scalarValueReal_1.getIdx());
		assertEquals(3.53, scalarValueReal_1.getValue(), 0.0);
		
	}
	
	
    
}

package com.sri.straylight.fmuWrapper.test.jna;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sri.straylight.fmuWrapper.JNAfmuWrapper.MessageCallbackInterface;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.ResultCallbackInterface;
import com.sri.straylight.fmuWrapper.JNAfmuWrapper.StateChangeCallbackInterface;
import com.sri.straylight.fmuWrapper.test.base.JNAdataTypes;
import com.sri.straylight.fmuWrapper.test.base.OrderedRunner;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voNative.BooleanValue;
import com.sri.straylight.fmuWrapper.voNative.DisplayUnitDefinitionStruct;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.EnumTypeMapper;
import com.sri.straylight.fmuWrapper.voNative.EnumerationItem;
import com.sri.straylight.fmuWrapper.voNative.IntegerValue;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.RealValue;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.StringValue;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionBoolean;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionEnum;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionInteger;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionReal;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionString;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;
import com.sri.straylight.fmuWrapper.voNative.ValueStatus;
import com.sun.jna.Library;
import com.sun.jna.Native;


@RunWith(OrderedRunner.class)
public class JNAdataTypesT {
	
	final private String pathToNativeLibs_ = "E:\\LHPB\\LearnHPB-middleware\\visualStudioWorkspace\\bin\\Debug";
//	final private String pathToFMUfolder_ = "E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test";
	

	
	
	private JNAdataTypes jnaDataTypes_;
	
	

	//runs before each test
	@Before
	public void beforeEachTest() {
		
		System.setProperty("jna.library.path", pathToNativeLibs_ );
		
		Map<String, Object> options = new HashMap<String, Object>();
		EnumTypeMapper mp = new EnumTypeMapper();
		
		options.put(Library.OPTION_TYPE_MAPPER, mp);
		
		jnaDataTypes_ = (JNAdataTypes) Native.loadLibrary("FMUwrapper",JNAdataTypes.class, options);
		assertNotNull(jnaDataTypes_);
		
		
	}
	
	//runs after each test
	@After
	public void afterEachTest() { }
	
	
	
	@Test
	public void T00_DataTypes() {
		

		ScalarVariablesAllStruct scalarVariablesAllStruct_0 = jnaDataTypes_.test_GetScalarVariablesAllStruct();
		assertNotNull(scalarVariablesAllStruct_0);
		
		ScalarVariableCollectionStruct scalarVariableCollectionStruct = jnaDataTypes_.test_GetScalarVariableCollectionStruct();
		assertNotNull(scalarVariableCollectionStruct);
		
		ScalarValueRealStruct scalarValueRealStruct = jnaDataTypes_.test_GetScalarValueRealStruct();
		assertNotNull(scalarValueRealStruct);

		ScalarVariableRealStruct scalarVariableRealStruct = jnaDataTypes_.test_GetScalarVariableRealStruct();
		assertNotNull(scalarVariableRealStruct);

		TypeSpecReal typeSpecReal = jnaDataTypes_.test_GetTypeSpecReal();
		assertNotNull(typeSpecReal);
		
		return;
	
	}
	
	@Test
	public void T01_TypeSpecReal() {
		
		TypeSpecReal typeSpecReal_0 = jnaDataTypes_.test_GetTypeSpecReal();
		assertNotNull(typeSpecReal_0);
		
		Assert.assertEquals(20.25, typeSpecReal_0.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_0.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_0.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_0.unit);

		Assert.assertEquals(1, typeSpecReal_0.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.unitValueStatus, 0.0);
		
		return;
	
	}
	
	
	@Test
	public void T02_ScalarVariableRealStruct() {
		

		ScalarVariableRealStruct scalarVariableReal_0 = jnaDataTypes_.test_GetScalarVariableRealStruct();
		assertNotNull(scalarVariableReal_0);
		
		
		int causality = Enu.enu_input.getIntValue();
		Assert.assertEquals(causality, scalarVariableReal_0.causality);
		Assert.assertEquals("The Description", scalarVariableReal_0.description);
		Assert.assertEquals(1, scalarVariableReal_0.idx);
		Assert.assertEquals("scalarVar name", scalarVariableReal_0.name);
		
		int variability = Enu.enu_continuous.getIntValue();
		Assert.assertEquals(variability, scalarVariableReal_0.variability);
		
		Assert.assertEquals(125420, scalarVariableReal_0.valueReference);
		TypeSpecReal.ByReference typeSpecReal_0 = scalarVariableReal_0.typeSpecReal;
		
		Assert.assertEquals(20.25, typeSpecReal_0.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_0.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_0.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_0.unit);

		Assert.assertEquals(1, typeSpecReal_0.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.unitValueStatus, 0.0);
		
		
		return;
	
	}
	
	
	
	@Test
	public void T03_ScalarVariableCollectionStruct() {
		
		
		ScalarVariableCollectionStruct svCollectionStruct_0 = jnaDataTypes_.test_GetScalarVariableCollectionStruct();
		assertNotNull(svCollectionStruct_0);
		
		//ScalarVariableRealStruct realValueAry = svCollectionStruct_0.realValue;
		ScalarVariableRealStruct[] realVariableAry = svCollectionStruct_0.getRealAsArray(0);
		
		int realSize = svCollectionStruct_0.realSize;
		
		Assert.assertEquals(2, realSize);
		
		ScalarVariableRealStruct  scalarVariableReal_0 = realVariableAry[0];
		ScalarVariableRealStruct  scalarVariableReal_1 = realVariableAry[1];
		
		assertNotNull(scalarVariableReal_0);
		assertNotNull(scalarVariableReal_1);
		
		int causality = Enu.enu_input.getIntValue();
		Assert.assertEquals(causality, scalarVariableReal_0.causality);
		Assert.assertEquals("The Description", scalarVariableReal_0.description);
		Assert.assertEquals(1, scalarVariableReal_0.idx);
		Assert.assertEquals("scalarVar name", scalarVariableReal_0.name);
		
		int variability = Enu.enu_continuous.getIntValue();
		Assert.assertEquals(variability, scalarVariableReal_0.variability);
		
		Assert.assertEquals(125420, scalarVariableReal_0.valueReference);
		TypeSpecReal.ByReference typeSpecReal_0 = scalarVariableReal_0.typeSpecReal;
		
		Assert.assertEquals(20.25, typeSpecReal_0.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_0.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_0.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_0.unit);
		
		Assert.assertEquals(1, typeSpecReal_0.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.unitValueStatus, 0.0);
		
		
		int causality2 = Enu.enu_input.getIntValue();
		Assert.assertEquals(causality2, scalarVariableReal_1.causality);
		Assert.assertEquals("The Description", scalarVariableReal_1.description);
		Assert.assertEquals(2, scalarVariableReal_1.idx);
		Assert.assertEquals("scalarVar name", scalarVariableReal_1.name);
		
		int variability2 = Enu.enu_continuous.getIntValue();
		Assert.assertEquals(variability2, scalarVariableReal_1.variability);
		
		Assert.assertEquals(125420, scalarVariableReal_1.valueReference);
		TypeSpecReal.ByReference typeSpecReal_1 = scalarVariableReal_1.typeSpecReal;
		
		Assert.assertEquals(20.25, typeSpecReal_1.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_1.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_1.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_1.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_1.unit);
		
		Assert.assertEquals(1, typeSpecReal_1.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.unitValueStatus, 0.0);
		return;
		
	}
	
	
	@Test
	public void T04_ScalarVariablesAllStruct() {
		
		ScalarVariablesAllStruct svAll_0 = jnaDataTypes_.test_GetScalarVariablesAllStruct();
		assertNotNull(svAll_0);
		
		ScalarVariableCollectionStruct svCollectionStruct_0 = svAll_0.input;
		assertNotNull(svCollectionStruct_0);
		
		ScalarVariableCollectionStruct svCollectionStruct_1 = svAll_0.output;
		assertNotNull(svCollectionStruct_1);
		
		ScalarVariableRealStruct[] realVariableAry_0 = svCollectionStruct_0.getRealAsArray(0);
		assertNotNull(realVariableAry_0);

		int realSize_0 = svCollectionStruct_0.realSize;
		Assert.assertEquals(2, realSize_0);
		 
		ScalarVariableRealStruct scalarVariableReal_0 = realVariableAry_0[0];
		ScalarVariableRealStruct scalarVariableReal_1 = realVariableAry_0[1];
			
		assertNotNull(scalarVariableReal_0);
		assertNotNull(scalarVariableReal_1);

		int causality = Enu.enu_input.getIntValue();
		Assert.assertEquals(causality, scalarVariableReal_0.causality);
		Assert.assertEquals("The Description", scalarVariableReal_0.description);
		Assert.assertEquals(1, scalarVariableReal_0.idx);
		Assert.assertEquals("scalarVar name", scalarVariableReal_0.name);
		
		int variability = Enu.enu_continuous.getIntValue();
		Assert.assertEquals(variability, scalarVariableReal_0.variability);

		Assert.assertEquals(125420, scalarVariableReal_0.valueReference);
		TypeSpecReal.ByReference typeSpecReal_0 = scalarVariableReal_0.typeSpecReal;
		
		Assert.assertEquals(20.25, typeSpecReal_0.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_0.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_0.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_0.unit);
		
		Assert.assertEquals(1, typeSpecReal_0.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.unitValueStatus, 0.0);
		
		int causality2 = Enu.enu_input.getIntValue();
		Assert.assertEquals(causality2, scalarVariableReal_1.causality);
		Assert.assertEquals("The Description", scalarVariableReal_1.description);
		Assert.assertEquals(2, scalarVariableReal_1.idx);
		Assert.assertEquals("scalarVar name", scalarVariableReal_1.name);
		
		int variability2 = Enu.enu_continuous.getIntValue();
		Assert.assertEquals(variability2, scalarVariableReal_1.variability);
		
		Assert.assertEquals(125420, scalarVariableReal_1.valueReference);
		TypeSpecReal.ByReference typeSpecReal_1 = scalarVariableReal_1.typeSpecReal;
		
		Assert.assertEquals(20.25, typeSpecReal_1.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_1.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_1.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_1.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_1.unit);
		
		Assert.assertEquals(1, typeSpecReal_1.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.unitValueStatus, 0.0);
	
		return;
	}
	
	
	@Test
	public void T05_DisplayUnitDefinitionStruct() {
		DisplayUnitDefinitionStruct duStruct = jnaDataTypes_.test_GetDisplayUnitDefinitionStruct();
		
		Assert.assertEquals("K", duStruct.displayUnit);
		Assert.assertEquals(1, duStruct.displayUnitValueStatus);
		
		Assert.assertEquals(-273.15, duStruct.offset, 0.0);
		Assert.assertEquals(1, duStruct.offsetValueStatus);
		
		Assert.assertEquals(0.001, duStruct.gain, 0.0);
		Assert.assertEquals(1, duStruct.gainValueStatus);
		
		return;
	}
	
	
	@Test
	public void T06_TypeDefinitionReal() {
		TypeDefinitionReal typeDefinitionReal = jnaDataTypes_.test_GetTypeDefinitionReal();
		
		Assert.assertEquals(1, typeDefinitionReal.idx);
		
		Assert.assertEquals("Modelica.Media.Interfaces.PartialMedium.AbsolutePressure", typeDefinitionReal.name.value);
		Assert.assertEquals("Pa", typeDefinitionReal.unit.value);
		
		Assert.assertEquals("Pressure", typeDefinitionReal.quantity.value);	
		Assert.assertEquals("bar", typeDefinitionReal.displayUnit.value);
		
		Assert.assertEquals(1.0, typeDefinitionReal.start.value, 0.0);	
		Assert.assertEquals(100.0, typeDefinitionReal.nominal.value, 0.0);
		Assert.assertEquals(0.0, typeDefinitionReal.min.value, 0.0);	
		Assert.assertEquals(100000000.0, typeDefinitionReal.max.value, 0.0);
		
		
		Assert.assertEquals(1, typeDefinitionReal.unit.status);
		Assert.assertEquals(1, typeDefinitionReal.quantity.status);
		Assert.assertEquals(1, typeDefinitionReal.displayUnit.status);
		
		Assert.assertEquals(1, typeDefinitionReal.start.status);
		Assert.assertEquals(1, typeDefinitionReal.nominal.status);
		Assert.assertEquals(1, typeDefinitionReal.min.status);
		Assert.assertEquals(1, typeDefinitionReal.max.status);
		
		return;
	}
	
	
	@Test
	public void T07_TypeDefinitionString() {
		
		TypeDefinitionString typeDefinitionString = jnaDataTypes_.test_GetTypeDefinitionString();
		
		Assert.assertEquals(3, typeDefinitionString.idx);
		
		Assert.assertEquals("AbsolutePressure2", typeDefinitionString.name.value);
		Assert.assertEquals("Pa2", typeDefinitionString.unit.value);
		
		Assert.assertEquals("Pressure2", typeDefinitionString.quantity.value);	
		Assert.assertEquals("bar2", typeDefinitionString.displayUnit.value);
		
		Assert.assertEquals(1, typeDefinitionString.unit.status);
		Assert.assertEquals(1, typeDefinitionString.quantity.status);
		Assert.assertEquals(1, typeDefinitionString.displayUnit.status);
		
		return;
	}
	
	
	@Test
	public void T08_TypeDefinitionInteger() {
		
		TypeDefinitionInteger typeDefinitionInteger = jnaDataTypes_.test_GetTypeDefinitionInteger();
		
		Assert.assertEquals(2, typeDefinitionInteger.idx);
		
		Assert.assertEquals("AbsolutePressure3", typeDefinitionInteger.name.value);
		Assert.assertEquals("Pa3", typeDefinitionInteger.unit.value);
		
		Assert.assertEquals("Pressure3", typeDefinitionInteger.quantity.value);	
		Assert.assertEquals("bar3", typeDefinitionInteger.displayUnit.value);
		
		Assert.assertEquals(1, typeDefinitionInteger.start.value);	
		Assert.assertEquals(100, typeDefinitionInteger.nominal.value);
		Assert.assertEquals(0, typeDefinitionInteger.min.value);	
		Assert.assertEquals(100000000, typeDefinitionInteger.max.value);
			
		Assert.assertEquals(1, typeDefinitionInteger.start.status);
		Assert.assertEquals(1, typeDefinitionInteger.nominal.status);
		Assert.assertEquals(1, typeDefinitionInteger.min.status);
		Assert.assertEquals(1, typeDefinitionInteger.max.status);
		
		return;
	}
	

	
	
	@Test
	public void T09_TypeDefinitionBoolean() {
		
		TypeDefinitionBoolean typeDefinitionBoolean = jnaDataTypes_.test_GetTypeDefinitionBoolean();
		
		Assert.assertEquals(4, typeDefinitionBoolean.idx);
		
		Assert.assertEquals("AbsolutePressure4", typeDefinitionBoolean.name.value);
		Assert.assertEquals("Pa4", typeDefinitionBoolean.unit.value);
		
		Assert.assertEquals("Pressure4", typeDefinitionBoolean.quantity.value);	
		Assert.assertEquals("bar4", typeDefinitionBoolean.displayUnit.value);
		
		Assert.assertEquals(true, typeDefinitionBoolean.start.value);	
		Assert.assertEquals(false, typeDefinitionBoolean.fixed.value);
		
		Assert.assertEquals(1, typeDefinitionBoolean.start.status);	
		Assert.assertEquals(1, typeDefinitionBoolean.fixed.status);
	
		
		return;
	}
	
	
	@Test
	public void T10_TypeDefinitionEnumeration() {
		

		TypeDefinitionEnum typeDefinitionEnum = jnaDataTypes_.test_GetTypeDefinitionEnum();

		Assert.assertEquals(6, typeDefinitionEnum.idx);
		Assert.assertEquals("typeDefinitionEnum_name_0", typeDefinitionEnum.name.value);
		Assert.assertEquals("unit6", typeDefinitionEnum.unit.value);
		Assert.assertEquals("quantity6", typeDefinitionEnum.quantity.value);
		Assert.assertEquals("displayUnit6", typeDefinitionEnum.displayUnit.value);
		
		Assert.assertEquals(1, typeDefinitionEnum.name.status);
		Assert.assertEquals(1, typeDefinitionEnum.unit.status);
		Assert.assertEquals(1, typeDefinitionEnum.quantity.status);
		Assert.assertEquals(1, typeDefinitionEnum.displayUnit.status);
		
		
		return;
	}

	
	@Test
	public void T11_ValueStatus() {
		
		ValueStatus valueStatus = jnaDataTypes_.test_GetValueStatus();
		
		Assert.assertEquals(ValueStatus.valueDefined, valueStatus);
		Assert.assertEquals(1, valueStatus.getIntValue());
		Assert.assertEquals("valueDefined", valueStatus.toString());
		
		return;
	}
	
	
	@Test
	public void T12_RealValue() {
		
		RealValue.ByValue realValue = jnaDataTypes_.test_GetRealValue();
		
		Assert.assertEquals(13.01, realValue.value, 0.00);
		Assert.assertEquals(1, realValue.status);
		Assert.assertEquals(ValueStatus.valueDefined, realValue.getStatusAsEnum());
		
		return;
	}
	
	
	@Test
	public void T13_IntegerValue() {
		
		IntegerValue.ByValue integerValue = jnaDataTypes_.test_GetIntegerValue();
		
		Assert.assertEquals(13, integerValue.value);
		Assert.assertEquals(1, integerValue.status);
		Assert.assertEquals(ValueStatus.valueDefined, integerValue.getStatusAsEnum());
		
		return;
	}
	
	@Test
	public void T14_BooleanValue() {
		
		BooleanValue.ByValue booleanValue = jnaDataTypes_.test_GetBooleanValue();
		
		Assert.assertEquals(true, booleanValue.value);
		Assert.assertEquals(1, booleanValue.status);
		Assert.assertEquals(ValueStatus.valueDefined, booleanValue.getStatusAsEnum());
		
		return;
	}
	
	@Test
	public void T15_StringValue() {
		
		StringValue.ByValue stringValue = jnaDataTypes_.test_GetStringValue();
		
		Assert.assertEquals("stringValue_0", stringValue.value);
		Assert.assertEquals(1, stringValue.status);
		Assert.assertEquals(ValueStatus.valueDefined, stringValue.getStatusAsEnum());
		
		return;
	}
	
	
	@Test
	public void T16_EnumerationItem() {
		
		EnumerationItem enumerationItem = jnaDataTypes_.test_GetEnumerationItem();
		
		Assert.assertEquals("name_0", enumerationItem.name);
		Assert.assertEquals("description_0", enumerationItem.description);

		return;
	}
	
	
	
//	@Test
//	public void T13_TypeDefinitionInteger2() {
//		
//		TypeDefinitionInteger2 typeDefinitionInteger = jnaDataTypes_.test_GetTypeDefinitionInteger2();
//		
//		Assert.assertEquals(2, typeDefinitionInteger.idx);
//		
//		Assert.assertEquals("AbsolutePressure3", typeDefinitionInteger.name);
//		Assert.assertEquals("Pa3", typeDefinitionInteger.unit);
//		
//		Assert.assertEquals("Pressure3", typeDefinitionInteger.quantity);	
//		Assert.assertEquals("bar3", typeDefinitionInteger.displayUnit);
//		
//		Assert.assertEquals(1, typeDefinitionInteger.start.value);	
//		Assert.assertEquals(100, typeDefinitionInteger.nominal.value);
//		Assert.assertEquals(0, typeDefinitionInteger.min.value);	
//		Assert.assertEquals(100000000, typeDefinitionInteger.max.value);
//		
//		Assert.assertEquals(1, typeDefinitionInteger.start.status);	
//		Assert.assertEquals(ValueStatus.valueDefined, typeDefinitionInteger.start.getStatusAsEnum());	
//		
//		Assert.assertEquals(1, typeDefinitionInteger.nominal.status);	
//		Assert.assertEquals(ValueStatus.valueDefined, typeDefinitionInteger.nominal.getStatusAsEnum());	
//		
//		Assert.assertEquals(1, typeDefinitionInteger.min.status);	
//		Assert.assertEquals(ValueStatus.valueDefined, typeDefinitionInteger.min.getStatusAsEnum());	
//		
//		Assert.assertEquals(1, typeDefinitionInteger.max.status);	
//		Assert.assertEquals(ValueStatus.valueDefined, typeDefinitionInteger.max.getStatusAsEnum());	
//		
//		return;
//	}
	
	
//	@Test
//	public void T13_ValueStatus() {
//		
//		ValueStatus.ByValue valueStatus = jnaDataTypes_.test_GetValueStatus2();
//		
//		Assert.assertEquals(ValueStatus.valueDefined, valueStatus);
//		Assert.assertEquals("valueDefined", valueStatus.toString());
//		
//		return;
//	}
	
	
	@Test
	public void T99_ScalarVariableCollection() {
		
		
		ScalarVariableCollectionStruct.ByReference svCollectionStruct_0 = jnaDataTypes_.test_GetScalarVariableCollectionStruct();
		assertNotNull(svCollectionStruct_0);
		
		
		ScalarVariableCollection svCollection_0 = new ScalarVariableCollection(svCollectionStruct_0);
		

		Vector<ScalarVariableReal> realList = svCollection_0.getRealVarList();
		
		ScalarVariableReal  scalarVariableReal_0 = realList.get(0);
		ScalarVariableReal  scalarVariableReal_1 = realList.get(1);
		
		assertNotNull(scalarVariableReal_0);
		assertNotNull(scalarVariableReal_1);
		
		Assert.assertEquals("C1", scalarVariableReal_0.getUnit());
		
		Assert.assertEquals( Enu.enu_input, scalarVariableReal_0.getCausalityAsEnum());
		Assert.assertEquals( Enu.enu_input.getIntValue(), scalarVariableReal_0.getCausalityAsInt());
		Assert.assertEquals( "input", scalarVariableReal_0.getCausalityAsString());
		
		Assert.assertEquals("The Description", scalarVariableReal_0.getDescription());
		Assert.assertEquals(1, scalarVariableReal_0.getIdx());
		Assert.assertEquals("scalarVar name", scalarVariableReal_0.getName());
		
		Assert.assertEquals( Enu.enu_continuous, scalarVariableReal_0.getVariabilityAsEnum());
		Assert.assertEquals( Enu.enu_continuous.getIntValue(), scalarVariableReal_0.getVariabilityAsInt());
		Assert.assertEquals( "continuous", scalarVariableReal_0.getVariabilityAsString());
		
		Assert.assertEquals(125420, scalarVariableReal_0.getValueReference());
			
		
		TypeSpecReal typeSpecReal_0 = scalarVariableReal_0.getTypeSpecReal();
		
		Assert.assertEquals(20.25, typeSpecReal_0.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_0.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_0.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_0.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_0.unit);
		
		Assert.assertEquals(1, typeSpecReal_0.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_0.unitValueStatus, 0.0);
		
		Assert.assertEquals("C1", scalarVariableReal_1.getUnit());
		
		Assert.assertEquals( Enu.enu_input, scalarVariableReal_1.getCausalityAsEnum());
		Assert.assertEquals( Enu.enu_input.getIntValue(), scalarVariableReal_1.getCausalityAsInt());
		Assert.assertEquals( "input", scalarVariableReal_1.getCausalityAsString());
		
		Assert.assertEquals("The Description", scalarVariableReal_1.getDescription());
		Assert.assertEquals(2, scalarVariableReal_1.getIdx());
		Assert.assertEquals("scalarVar name", scalarVariableReal_1.getName());
		
		Assert.assertEquals( Enu.enu_continuous, scalarVariableReal_1.getVariabilityAsEnum());
		Assert.assertEquals( Enu.enu_continuous.getIntValue(), scalarVariableReal_1.getVariabilityAsInt());
		Assert.assertEquals( "continuous", scalarVariableReal_1.getVariabilityAsString());
		
		Assert.assertEquals(125420, scalarVariableReal_1.getValueReference());
			
		
		TypeSpecReal typeSpecReal_1 = scalarVariableReal_1.getTypeSpecReal();
		
		Assert.assertEquals(20.25, typeSpecReal_1.start, 0.0);
		Assert.assertEquals(21.25, typeSpecReal_1.nominal, 0.0);
		Assert.assertEquals(22.25, typeSpecReal_1.min, 0.0);
		Assert.assertEquals(23.25, typeSpecReal_1.max, 0.0);
		Assert.assertEquals("C1", typeSpecReal_1.unit);
		
		Assert.assertEquals(1, typeSpecReal_1.startValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.nominalValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.minValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.maxValueStatus, 0.0);
		Assert.assertEquals(1, typeSpecReal_1.unitValueStatus, 0.0);
		
		
		return;
		
		
	}
	
	
	
	
	
	
	
	private class ResultCallback implements ResultCallbackInterface {

		public boolean resultCallback(
				ScalarValueResultsStruct scalarValueResultsStruct) {

			return true;
		}
	}
	
	
	
	private class StateChangeCallback implements StateChangeCallbackInterface {

		public boolean stateChangeCallback(SimStateNative simStateNative) {



			return true;
		}
	}
	
	
	
	private class MessageCallback implements MessageCallbackInterface {

		public boolean messageCallback(MessageStruct messageStruct) {



			return true;
		}

	}
	
	
	
	
}

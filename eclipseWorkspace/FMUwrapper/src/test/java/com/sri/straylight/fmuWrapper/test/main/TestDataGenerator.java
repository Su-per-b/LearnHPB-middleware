package com.sri.straylight.fmuWrapper.test.main;

import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.SerializableVector;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;

public class TestDataGenerator {

	public static ScalarVariableCollection getScalarVariableCollection_A_() {

		// make TypeSpecReal 0
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

		// make ScalarVariableReal 0
		ScalarVariableReal scalarVariableReal_0 = new ScalarVariableReal(
				typeSpecReal_0);
		scalarVariableReal_0.setName("scalarVar name");
		scalarVariableReal_0.setIdx(1);
		scalarVariableReal_0.setCausality(Enu.enu_input);
		scalarVariableReal_0.setVariability(Enu.enu_continuous);
		scalarVariableReal_0.setDescription("The Description");
		scalarVariableReal_0.setValueReference(125420);

		// make TypeSpecReal 1
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

		// make ScalarVariableReal 1
		ScalarVariableReal scalarVariableReal_1 = new ScalarVariableReal(
				typeSpecReal_1);
		scalarVariableReal_1.setName("scalarVar name");
		scalarVariableReal_1.setIdx(1);
		scalarVariableReal_1.setCausality(Enu.enu_input);
		scalarVariableReal_1.setVariability(Enu.enu_discrete);
		scalarVariableReal_1.setDescription("The Description");
		scalarVariableReal_1.setValueReference(125420);

		SerializableVector<ScalarVariableReal> realList_0 = new SerializableVector<ScalarVariableReal>(
				"ScalarVariableReal");
		realList_0.add(scalarVariableReal_0);
		realList_0.add(scalarVariableReal_1);

		ScalarVariableCollection scalarVariableCollection_0 = new ScalarVariableCollection();
		scalarVariableCollection_0.setRealVarList(realList_0);

		return scalarVariableCollection_0;

	}

	public static ScalarVariableCollection getScalarVariableCollection_B_() {
		// make TypeSpecReal 1
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

		// make ScalarVariableReal 1
		ScalarVariableReal scalarVariableReal_0 = new ScalarVariableReal(
				typeSpecReal_0);
		scalarVariableReal_0.setName("ES-23");
		scalarVariableReal_0.setIdx(0);
		scalarVariableReal_0.setCausality(Enu.enu_output);
		scalarVariableReal_0.setVariability(Enu.enu_continuous);
		scalarVariableReal_0
				.setDescription("This is the pressure in the duct measured in Pa");
		scalarVariableReal_0.setValueReference(7547);

		// make TypeSpecReal 2
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

		// make ScalarVariableReal 2
		ScalarVariableReal scalarVariableReal_1 = new ScalarVariableReal(
				typeSpecReal_1);
		scalarVariableReal_1.setName("PA57-FA");
		scalarVariableReal_1.setIdx(1);
		scalarVariableReal_1.setCausality(Enu.enu_input);
		scalarVariableReal_1.setVariability(Enu.enu_parameter);
		scalarVariableReal_1.setDescription("Pressure in Duct per meter");
		scalarVariableReal_1.setValueReference(55);

		SerializableVector<ScalarVariableReal> realList_0 = new SerializableVector<ScalarVariableReal>(
				"ScalarVariableReal");
		realList_0.add(scalarVariableReal_0);
		realList_0.add(scalarVariableReal_1);

		ScalarVariableCollection scalarVariableCollection_0 = new ScalarVariableCollection();
		scalarVariableCollection_0.setRealVarList(realList_0);

		return scalarVariableCollection_0;

	}

}

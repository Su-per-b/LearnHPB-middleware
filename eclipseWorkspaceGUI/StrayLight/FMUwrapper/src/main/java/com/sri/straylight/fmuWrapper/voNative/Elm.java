package com.sri.straylight.fmuWrapper.voNative;

// TODO: Auto-generated Javadoc
/**
 * The Enum Elm.
 */
public enum Elm implements JnaEnum<Elm> {

	elm_fmiModelDescription,
	elm_UnitDefinitions, 
	elm_BaseUnit, 
	elm_DisplayUnitDefinition, 
	elm_TypeDefinitions,

	elm_Type,
	elm_RealType,
	elm_IntegerType,
	elm_BooleanType,
	elm_StringType,
	elm_EnumerationType,
	elm_Item,

	elm_DefaultExperiment,
	elm_VendorAnnotations,
	elm_Tool,
	elm_Annotation,
	elm_ModelVariables,
	elm_ScalarVariable,

	elm_DirectDependency,
	elm_Name,
	elm_Real,
	elm_Integer,
	elm_Boolean,
	elm_String,
	elm_Enumeration,

	elm_Implementation,
	elm_CoSimulation_StandAlone,
	elm_CoSimulation_Tool,
	elm_Model,
	elm_File,
	elm_Capabilities;


	private static int start = 0;


	public int getIntValue() {
		return this.ordinal() + start;
	}


	public Elm getForValue(int i) {
		for (Elm o : Elm.values()) {
			if (o.getIntValue() == i) {
				return o;
			}
		}
		return null;
	}

}

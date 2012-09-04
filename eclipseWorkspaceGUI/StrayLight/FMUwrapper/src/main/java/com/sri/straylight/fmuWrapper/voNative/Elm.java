package com.sri.straylight.fmuWrapper.voNative;




// TODO: Auto-generated Javadoc
/**
 * The Enum Elm.
 */
public enum Elm implements JnaEnum<Elm> {
	

    /** The elm_fmi model description. */
    elm_fmiModelDescription,/** The elm_ unit definitions. */
elm_UnitDefinitions,/** The elm_ base unit. */
elm_BaseUnit,/** The elm_ display unit definition. */
elm_DisplayUnitDefinition,/** The elm_ type definitions. */
elm_TypeDefinitions,
    
    /** The elm_ type. */
    elm_Type,
/** The elm_ real type. */
elm_RealType,
/** The elm_ integer type. */
elm_IntegerType,
/** The elm_ boolean type. */
elm_BooleanType,
/** The elm_ string type. */
elm_StringType,
/** The elm_ enumeration type. */
elm_EnumerationType,
/** The elm_ item. */
elm_Item,
    
    /** The elm_ default experiment. */
    elm_DefaultExperiment,
/** The elm_ vendor annotations. */
elm_VendorAnnotations,
/** The elm_ tool. */
elm_Tool,
/** The elm_ annotation. */
elm_Annotation,
/** The elm_ model variables. */
elm_ModelVariables,
/** The elm_ scalar variable. */
elm_ScalarVariable,
    
    /** The elm_ direct dependency. */
    elm_DirectDependency,
/** The elm_ name. */
elm_Name,
/** The elm_ real. */
elm_Real,
/** The elm_ integer. */
elm_Integer,
/** The elm_ boolean. */
elm_Boolean,
/** The elm_ string. */
elm_String,
/** The elm_ enumeration. */
elm_Enumeration,
    
    /** The elm_ implementation. */
    elm_Implementation,
/** The elm_ co simulation_ stand alone. */
elm_CoSimulation_StandAlone,
/** The elm_ co simulation_ tool. */
elm_CoSimulation_Tool,
/** The elm_ model. */
elm_Model,
/** The elm_ file. */
elm_File,
/** The elm_ capabilities. */
elm_Capabilities;
    
	     
	/** The start. */
	private static int start = 0;
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.fmuWrapper.voNative.JnaEnum#getIntValue()
	 */
	public int getIntValue() {
	    return this.ordinal() + start;
	}
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.fmuWrapper.voNative.JnaEnum#getForValue(int)
	 */
	public Elm getForValue(int i) {
	    for (Elm o : Elm.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
    
    
    
}

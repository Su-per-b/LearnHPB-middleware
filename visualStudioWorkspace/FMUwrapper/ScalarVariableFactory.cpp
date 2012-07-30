#include "ScalarVariableFactory.h"

namespace Straylight
{
	ScalarVariableFactory::ScalarVariableFactory(void)
	{

	}


	ScalarVariableFactory::~ScalarVariableFactory(void)
	{

	}

	ScalarVariableRealStruct* ScalarVariableFactory::makeReal(ScalarVariable* scalarVariable) {
		ScalarVariableRealStruct * svStruct = new ScalarVariableRealStruct(); 

		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable); 

		ValueStatus descriptionValueStatus;
		svStruct->description = getStringAttribute(scalarVariable, &descriptionValueStatus, att_description);
		if (descriptionValueStatus == valueMissing) {
			svStruct->description = _T("{no description}");
		}

		svStruct->typeSpecReal = new TypeSpecReal();
		svStruct->typeSpecReal->declaredType = getString(scalarVariable->typeSpec, att_declaredType);

		ValueStatus startValueStatus;
		svStruct->typeSpecReal->start = getElementAttributeReal(scalarVariable->typeSpec, &startValueStatus, att_start);
		svStruct->typeSpecReal->startValueStatus = startValueStatus;

		ValueStatus nominalValueStatus;
		svStruct->typeSpecReal->nominal = getElementAttributeReal(scalarVariable->typeSpec,&nominalValueStatus, att_nominal);
		svStruct->typeSpecReal->nominalValueStatus = nominalValueStatus;

		ValueStatus minValueStatus;
		svStruct->typeSpecReal->min = getElementAttributeReal(scalarVariable->typeSpec,&minValueStatus, att_min);
		svStruct->typeSpecReal->minValueStatus = minValueStatus;

		ValueStatus maxValueStatus;
		svStruct->typeSpecReal->max = getElementAttributeReal(scalarVariable->typeSpec,&maxValueStatus, att_max);
		svStruct->typeSpecReal->maxValueStatus = maxValueStatus;


		
		return svStruct;
	}

	ScalarVariableBooleanStruct* ScalarVariableFactory::makeBoolean(ScalarVariable* scalarVariable) {
		ScalarVariableBooleanStruct * svStruct = new ScalarVariableBooleanStruct(); 

		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable); 

		ValueStatus descriptionValueStatus;
		svStruct->description = getStringAttribute(scalarVariable, &descriptionValueStatus, att_description);
		if (descriptionValueStatus == valueMissing) {
			svStruct->description = _T("{no description}");
		}

		svStruct->typeSpecBoolean = new TypeSpecBoolean();
		svStruct->typeSpecBoolean->declaredType = getString(scalarVariable->typeSpec, att_declaredType);

		ValueStatus startValueStatus;
		svStruct->typeSpecBoolean->start = getBooleanAttribute(scalarVariable, &startValueStatus,att_start);
		svStruct->typeSpecBoolean->startValueStatus = startValueStatus;

		ValueStatus fixedValueStatus;
		svStruct->typeSpecBoolean->fixed = getBooleanAttribute(scalarVariable, &fixedValueStatus,att_fixed);
		svStruct->typeSpecBoolean->fixedValueStatus = fixedValueStatus;


		return svStruct;
	}

	ScalarVariableIntegerStruct* ScalarVariableFactory::makeInteger(ScalarVariable* scalarVariable) {
		ScalarVariableIntegerStruct * svStruct = new ScalarVariableIntegerStruct(); 

		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable); 

		ValueStatus descriptionValueStatus;
		svStruct->description = getStringAttribute(scalarVariable, &descriptionValueStatus, att_description);
		if (descriptionValueStatus == valueMissing) {
			svStruct->description = _T("{no description}");
		}

		svStruct->typeSpecInteger = new TypeSpecInteger();
		svStruct->typeSpecInteger->declaredType = getString(scalarVariable->typeSpec, att_declaredType);

		ValueStatus startValueStatus;
		svStruct->typeSpecInteger->start = getIntegerAttribute(scalarVariable,&startValueStatus, att_start);
		svStruct->typeSpecInteger->startValueStatus = startValueStatus;

		ValueStatus nominalValueStatus;
		svStruct->typeSpecInteger->nominal = getIntegerAttribute(scalarVariable,&nominalValueStatus, att_nominal);
		svStruct->typeSpecInteger->nominalValueStatus = nominalValueStatus;

		ValueStatus minValueStatus;
		svStruct->typeSpecInteger->min = getIntegerAttribute(scalarVariable,&minValueStatus, att_min);
		svStruct->typeSpecInteger->minValueStatus = minValueStatus;

		ValueStatus maxValueStatus;
		svStruct->typeSpecInteger->max = getIntegerAttribute(scalarVariable,&maxValueStatus, att_max);
		svStruct->typeSpecInteger->maxValueStatus = maxValueStatus;

		ValueStatus fixedValueStatus;
		svStruct->typeSpecInteger->fixed = getIntegerAttribute(scalarVariable,&fixedValueStatus, att_fixed);
		svStruct->typeSpecInteger->fixedValueStatus = fixedValueStatus;


		return svStruct;
	}

	ScalarVariableEnumerationStruct* ScalarVariableFactory::makeEnumeration(ScalarVariable* scalarVariable) {
		ScalarVariableEnumerationStruct * svStruct = new ScalarVariableEnumerationStruct(); 

		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable); 


		ValueStatus descriptionValueStatus;
		svStruct->description = getStringAttribute(scalarVariable, &descriptionValueStatus, att_description);
		if (descriptionValueStatus == valueMissing) {
			svStruct->description = _T("{no description}");
		}


		svStruct->typeSpecEnumeration = new TypeSpecEnumeration();
		svStruct->typeSpecEnumeration->declaredType = getString(scalarVariable->typeSpec, att_declaredType);


		const char * enumerationDeclaredType = getEnumerationDeclaredType(scalarVariable);



		return svStruct;
	}

	ScalarVariableStringStruct* ScalarVariableFactory::makeString(ScalarVariable* scalarVariable) {
		ScalarVariableStringStruct * svStruct = new ScalarVariableStringStruct(); 
		svStruct->name = getName( scalarVariable );

		return svStruct;
	}

}
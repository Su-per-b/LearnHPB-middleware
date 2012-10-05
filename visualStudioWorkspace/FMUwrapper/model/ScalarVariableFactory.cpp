#include "ScalarVariableFactory.h"

namespace Straylight
{
	/*******************************************************//**
	 * Default constructor.
	 *******************************************************/
	ScalarVariableFactory::ScalarVariableFactory()
	{
	}

	/*******************************************************//**
	 * Destructor.
	 *******************************************************/
	ScalarVariableFactory::~ScalarVariableFactory(void)
	{
	}

	/*******************************************************//**
	 * Makes a real.
	 *
	 * @param [in,out]	scalarVariable	If non-null, the scalar variable.
	 * @param	i					  	Zero-based index of the.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	ScalarVariableRealStruct* ScalarVariableFactory::makeReal(ScalarVariable* scalarVariable, int i) {
		ScalarVariableRealStruct * svStruct = new ScalarVariableRealStruct();

		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable);
		svStruct->variability = getVariability(scalarVariable);
		svStruct->idx = i;

		ValueStatus descriptionValueStatus;
		svStruct->description = getObjectAttributeString(scalarVariable, &descriptionValueStatus, att_description);
		if (descriptionValueStatus == valueMissing) {
			svStruct->description = _T("{no description}");
		}

		if (svStruct->idx == 56106) {
			int x =0;
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

		bool autCorrectFlag = Config::getInstance()->getAutoCorrect();

		if (svStruct->typeSpecReal->nominalValueStatus == valueDefined &&
			svStruct->typeSpecReal->maxValueStatus == valueDefined)
		{
			if (svStruct->typeSpecReal->nominal > svStruct->typeSpecReal->max) {

				Logger::getInstance()->printErrorInt
				( "ScalarVariableFactory::makeReal() nominal value above maximum valueReference:%s", svStruct->valueReference);
				Logger::getInstance()->printDebugDouble("svStruct->typeSpecReal->nominal: %s\n", svStruct->typeSpecReal->nominal);
				Logger::getInstance()->printDebugDouble("svStruct->typeSpecReal->max: %s\n", svStruct->typeSpecReal->max);

				if (autCorrectFlag) {
					Logger::getInstance()->printDebug("***CORRECTING***");
					svStruct->typeSpecReal->max = svStruct->typeSpecReal->nominal;
				}
			}
		}


		if (svStruct->typeSpecReal->nominalValueStatus == valueDefined &&
			svStruct->typeSpecReal->minValueStatus == valueDefined)
		{
			if (svStruct->typeSpecReal->nominal < svStruct->typeSpecReal->min) {

				Logger::getInstance()->printErrorInt
				( "ScalarVariableFactory::makeReal() nominal value below minimum valueReference:%s", svStruct->valueReference);

				Logger::getInstance()->printDebugDouble("svStruct->typeSpecReal->nominal: %s\n", svStruct->typeSpecReal->nominal);
				Logger::getInstance()->printDebugDouble("svStruct->typeSpecReal->min: %s\n", svStruct->typeSpecReal->min);

				if (autCorrectFlag) {
					Logger::getInstance()->printDebug("***CORRECTING***");
					svStruct->typeSpecReal->min = svStruct->typeSpecReal->nominal;
				}

			}
		}


		if (svStruct->typeSpecReal->minValueStatus == valueDefined &&
			svStruct->typeSpecReal->startValueStatus == valueDefined)
		{
			if (svStruct->typeSpecReal->start < svStruct->typeSpecReal->min) {
				Logger::getInstance()->printDebugDouble("svStruct->typeSpecReal->start: %s\n", svStruct->typeSpecReal->start);
				Logger::getInstance()->printDebugDouble("svStruct->typeSpecReal->min: %s\n", svStruct->typeSpecReal->min);

				Logger::getInstance()->printErrorInt
				( "ScalarVariableFactory::makeReal() start value below minimum valueReference:%s", svStruct->valueReference);

				if (autCorrectFlag) {
					Logger::getInstance()->printDebug("***CORRECTING***");
					svStruct->typeSpecReal->min = svStruct->typeSpecReal->start;
				}

			}
		} else if (svStruct->typeSpecReal->startValueStatus == valueDefined &&
			svStruct->typeSpecReal->minValueStatus != valueDefined ) {

				Logger::getInstance()->printErrorInt
					( "ScalarVariableFactory::makeReal() start value defined, but min NOT defined valueReference:%s", svStruct->valueReference);

				if (autCorrectFlag) {
					Logger::getInstance()->printDebug("***CORRECTING***");

					if (svStruct->typeSpecReal->start < 0) {
						svStruct->typeSpecReal->min = svStruct->typeSpecReal->start;
					} else {
						svStruct->typeSpecReal->min = 0.0;
					}

				}

		}



		if (svStruct->typeSpecReal->maxValueStatus == valueDefined &&
			svStruct->typeSpecReal->startValueStatus == valueDefined)
		{
			if (svStruct->typeSpecReal->start > svStruct->typeSpecReal->max) {

				Logger::getInstance()->printDebugDouble("svStruct->typeSpecReal->start: %s\n", svStruct->typeSpecReal->start);
				Logger::getInstance()->printDebugDouble("svStruct->typeSpecReal->min: %s\n", svStruct->typeSpecReal->max);

				Logger::getInstance()->printErrorInt
					( "ScalarVariableFactory::makeReal() start value above maximum valueReference:%s", svStruct->valueReference);

				if (autCorrectFlag) {
					Logger::getInstance()->printDebug("***CORRECTING***");
					svStruct->typeSpecReal->max = svStruct->typeSpecReal->start;
				}

			}
		} else if (svStruct->typeSpecReal->startValueStatus == valueDefined &&
			svStruct->typeSpecReal->maxValueStatus != valueDefined ) {

				Logger::getInstance()->printErrorInt
					( "ScalarVariableFactory::makeReal() start value defined, but max NOT defined valueReference:%s", svStruct->valueReference);

				if (autCorrectFlag) {
					Logger::getInstance()->printDebug("***CORRECTING***");

					if (svStruct->typeSpecReal->start > 0) {
						svStruct->typeSpecReal->max = svStruct->typeSpecReal->start;
					} else {
						svStruct->typeSpecReal->max = 0.0;
					}

				}

		}




		return svStruct;
	}

	/*******************************************************//**
	 * Makes a boolean.
	 *
	 * @param [in,out]	scalarVariable	If non-null, the scalar variable.
	 * @param	i					  	Zero-based index of the.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	ScalarVariableBooleanStruct* ScalarVariableFactory::makeBoolean(ScalarVariable* scalarVariable, int i) {
		ScalarVariableBooleanStruct * svStruct = new ScalarVariableBooleanStruct();

		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable);
		svStruct->variability = getVariability(scalarVariable);

		ValueStatus descriptionValueStatus;
		svStruct->description = getObjectAttributeString(scalarVariable, &descriptionValueStatus, att_description);
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

	/*******************************************************//**
	 * Makes an integer.
	 *
	 * @param [in,out]	scalarVariable	If non-null, the scalar variable.
	 * @param	i					  	Zero-based index of the.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	ScalarVariableIntegerStruct* ScalarVariableFactory::makeInteger(ScalarVariable* scalarVariable, int i) {
		ScalarVariableIntegerStruct * svStruct = new ScalarVariableIntegerStruct();

		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable);
		svStruct->variability = getVariability(scalarVariable);

		ValueStatus descriptionValueStatus;
		svStruct->description = getObjectAttributeString(scalarVariable, &descriptionValueStatus, att_description);
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

	/*******************************************************//**
	 * Makes an enumeration.
	 *
	 * @param [in,out]	scalarVariable	If non-null, the scalar variable.
	 * @param	i					  	Zero-based index of the.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	ScalarVariableEnumerationStruct* ScalarVariableFactory::makeEnumeration(ScalarVariable* scalarVariable, int i) {
		ScalarVariableEnumerationStruct * svStruct = new ScalarVariableEnumerationStruct();

		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable);
		svStruct->variability = getVariability(scalarVariable);

		ValueStatus descriptionValueStatus;
		svStruct->description = getObjectAttributeString(scalarVariable, &descriptionValueStatus, att_description);
		if (descriptionValueStatus == valueMissing) {
			svStruct->description = _T("{no description}");
		}

		svStruct->typeSpecEnumeration = new TypeSpecEnumeration();
		svStruct->typeSpecEnumeration->declaredType = getString(scalarVariable->typeSpec, att_declaredType);

		const char * enumerationDeclaredType = getEnumerationDeclaredType(scalarVariable);

		return svStruct;
	}

	/*******************************************************//**
	 * Makes a string.
	 *
	 * @param [in,out]	scalarVariable	If non-null, the scalar variable.
	 * @param	i					  	Zero-based index of the.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	ScalarVariableStringStruct* ScalarVariableFactory::makeString(ScalarVariable* scalarVariable, int i) {
		ScalarVariableStringStruct * svStruct = new ScalarVariableStringStruct();
		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable);
		svStruct->variability = getVariability(scalarVariable);

		ValueStatus descriptionValueStatus;
		svStruct->description = getObjectAttributeString(scalarVariable, &descriptionValueStatus, att_description);
		if (descriptionValueStatus == valueMissing) {
			svStruct->description = _T("{no description}");
		}

		return svStruct;
	}
}


#pragma once

#include "ScalarVariableFactory.h";

namespace Straylight
{

	  ScalarVariableStruct *  ScalarVariableFactory::make(FMU* fmuPointer, int i) {
			ScalarVariableStruct * scalarVariableStruct = new ScalarVariableStruct(); // = new ScalarVariableStruct();

			ScalarVariable* scalarVariable = fmuPointer->modelDescription->modelVariables[i];

			scalarVariableStruct->idx = i;
			scalarVariableStruct->name = getName( scalarVariable );
			scalarVariableStruct->causality =  getCausality(scalarVariable);
			scalarVariableStruct->type = scalarVariable->typeSpec->type;
			scalarVariableStruct->description = getDescription(fmuPointer->modelDescription,  scalarVariable );
			scalarVariableStruct->element = new Element2();

			if (scalarVariableStruct->description == NULL) {
				scalarVariableStruct->description = _T("{no description}");
			}

			return scalarVariableStruct;
	}

	

}


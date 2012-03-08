#include "ResultItem.h"


namespace Straylight
{

	ResultItem::ResultItem(void)
	{
	}


	ResultItem::~ResultItem(void)
	{
	}

	void ResultItem::setTime(double time)
	{
		time_ = time;
	}


 char * ResultItem::getString()
	{
		char * cstr;
		std::list<ResultPrimitive*>::iterator it = resultPrimitiveList.begin();

		std::string str;
		std::string output = "";
		ResultPrimitive * rp;
		rp = (*it);
		str = rp->getString();
		output.append(str);

		++it;

		for(it; it != resultPrimitiveList.end(); ++it)
		{
			
			output.append(",");
			rp = (*it);
			str = rp->getString();
			output.append(str);

		}


		cstr = new char [output.size()+1];
        strcpy (cstr, output.c_str());


		return cstr;
	}



	void ResultItem::addValue(ScalarVariable * sv)
	{
		fmiValueReference vr;	
		fmiReal r;
		fmiInteger i;
		fmiBoolean b;
		fmiString s;

		vr = getValueReference(sv);

		ResultPrimitive * rp = new ResultPrimitive();


		switch (sv->typeSpec->type) {
		case elm_Real:
			fmuPointer_->getReal(fmiComp_, &vr, 1, &r);

			//rp->type = ResultPrimitive::VALUETYPE_REAL;
			rp->type = 0;
			rp->scalarReal = r;

			break;
		case elm_Integer:
			fmuPointer_->getInteger(fmiComp_, &vr, 1, &i);

			//rp->type = ResultPrimitive::VALUETYPE_INT;
			rp->type = 1;
			rp->scalarInt = i;

			break;
		case elm_Boolean:
			fmuPointer_->getBoolean(fmiComp_, &vr, 1, &b);

			//rp->type = ResultPrimitive::VALUETYPE_BOOL;
			rp->type = 2;
			rp->scalarInt = b;

			break;
		case elm_String:
			fmuPointer_->getString(fmiComp_, &vr, 1, &s);

			//rp->type = ResultPrimitive::VALUETYPE_STRING;
			rp->type = 3;
			rp->scalarString = i;

			break;
		}


		resultPrimitiveList.push_front(rp);

	}

	void ResultItem::addModelVariables(FMU* fmuPointer, fmiComponent fmiComp)
	{
		int k;



		fmuPointer_ = fmuPointer;
		fmiComp_ = fmiComp;

		ScalarVariable** vars = fmuPointer->modelDescription->modelVariables;


		for (k=0; vars[k]; k++) {
			ScalarVariable* sv = vars[k];
			if (getAlias(sv)!=enu_noAlias) continue;

			this->addValue(sv);



			int x = 0;
		}






	}

}
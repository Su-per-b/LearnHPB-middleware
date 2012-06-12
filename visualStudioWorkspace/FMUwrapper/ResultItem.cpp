#include "ResultItem.h"


namespace Straylight
{

	ResultItem::ResultItem(FMU* fmuPointer, fmiComponent fmiComp )
	{

		fmuPointer_ = fmuPointer;
		fmiComp_ = fmiComp;

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
		vector<ResultPrimitive*>::iterator it = resultPrimitiveList.begin();

		string str;
		string output = "";

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



	void ResultItem::addValue(ScalarVariable * sv, int idx)
	{
		fmiValueReference vr;	
		fmiReal r;
		fmiInteger i;
		fmiBoolean b;
		fmiString s;

		vr = getValueReference(sv);

		ResultPrimitive * rp = new ResultPrimitive(idx);

	//	rp->setValue();


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
			rp->scalarString = s;

			break;
		}


		resultPrimitiveList.push_back(rp);

	}




	 ResultStruct * ResultItem::toStruct ()
	{


		ResultStruct *riStruct = new ResultStruct;

		int len = resultPrimitiveList.size();
		ScalarValueStruct * primitiveArry = new ScalarValueStruct[len];


		for (int i = 0; i < len; i++)
		{
			Straylight::ResultPrimitive * rp  = resultPrimitiveList[i];

			primitiveArry[i].idx = rp->idx;
			string str = rp->getString();

			char * cstr = new char [str.size()+1];
			strcpy (cstr, str.c_str());
			primitiveArry[i].string = cstr;
		}

		riStruct->time = time_;
		riStruct->string = getString();
		riStruct->scalarValueStruct =  primitiveArry;
		riStruct->scalarValueCount = len;

		return riStruct;

	}

}
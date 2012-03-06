#include "ResultPrimitive.h"


namespace Straylight
{
	ResultPrimitive::ResultPrimitive(void)
	{
	}


	ResultPrimitive::~ResultPrimitive(void)
	{

	}

	std::string ResultPrimitive::getString(void)	{

		//fmuWrapper->doOneStep();
		//fmiReal d = fmuWrapper->getResultSnapshot();

		//fmiReal d = 0;


		//std::ostringstream os;
		//os << d;
		//std::string str = os.str();
		//std::stringstream ostr;

		//double d = 123.456;
		//ostr << d;
		//std::string output << ostr.str();
		// 
		std::stringstream ss;
		std::string str;
		const char* cstr2;


		switch (this->type) {
			case 0:


				ss << this->scalarReal;
				str = ss.str();


				return str;
				break;
			case 1:
				return "unKnownType";
				break;
			case 2:
				return "unKnownType";
				break;
			case 3:
				return "unKnownType";
				break;
			default:
				return "unKnownType";
				break;

		}
		



	}


}

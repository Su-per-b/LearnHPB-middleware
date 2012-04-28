#include "ResultPrimitive.h"


namespace Straylight
{
	ResultPrimitive::ResultPrimitive(int idx_local)
	{

		idx = idx_local;
	}


	ResultPrimitive::~ResultPrimitive(void)
	{

	}

	std::string ResultPrimitive::getString(void)	{

		std::stringstream ss;
		std::string str;

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

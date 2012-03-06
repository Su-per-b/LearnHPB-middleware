#pragma once

#include "stdafx.h"
#include <list>

#include "ResultItem.h"

namespace Straylight
{
	class ResultSet
	{
	public:
		ResultSet(void);
		~ResultSet(void);

		std::list<ResultItem* > resultItemList;

	};

}
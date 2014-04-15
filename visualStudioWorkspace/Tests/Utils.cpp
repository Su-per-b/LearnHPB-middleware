#include "Utils.h"


using namespace Microsoft::VisualStudio::CppUnitTestFramework;
using namespace Straylight;



namespace StraylightTests
{


	SimStateNative Utils::simStateNative;

	ScalarValueResultsStruct * Utils::scalarValueResultsStruct;

	MessageStruct * Utils::messageStruct;





	void Utils::resultCallbackFunc(ScalarValueResultsStruct * scalarValueResultsStruct) {
		Utils::scalarValueResultsStruct = scalarValueResultsStruct;
	}

	void Utils::stateChangeCallbackFunc(SimStateNative simStateNative) {
		//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
		//printf("Main.exe simStateNative: %s \n", _T("simStateNative"));

		//static_simStateNative = simStateNative;

		 Utils::simStateNative = simStateNative;

		//Microsoft::VisualStudio::CppUnitTestFramework::Logger::WriteMessage("simStateNative: ");

	}


	 void Utils::messageCallbackFunc(MessageStruct * messageStruct) {
		//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
		 Utils::messageStruct = messageStruct;
	}


}





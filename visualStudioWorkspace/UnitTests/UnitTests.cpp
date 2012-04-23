// useGtest.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "UnitTests.h"
#include "utils.h"

using namespace Straylight;





/*
FMUwrapper *fmuWrapper;
TEST(sample_test_case, sample_test)
{
    EXPECT_EQ(1, 1);
}

TEST(LearnGB, LearnGB_VAVReheat_ClosedLoopXP)
{
	init(_T("\\FMUs\\LearnGB_VAVReheat_ClosedLoopXP"));
	char * str = getResultItemAsString(fmuWrapper);

    EXPECT_STREQ("293.138", getResultItemAsString(fmuWrapper));
	EXPECT_STREQ("293.133", getResultItemAsString(fmuWrapper));
    EXPECT_STREQ("293.127", getResultItemAsString(fmuWrapper));
	EXPECT_STREQ("293.121", getResultItemAsString(fmuWrapper));

    EXPECT_STREQ("293.115", getResultItemAsString(fmuWrapper));
	EXPECT_STREQ("293.11", getResultItemAsString(fmuWrapper));
    EXPECT_STREQ("293.104", getResultItemAsString(fmuWrapper));
	EXPECT_STREQ("293.098", getResultItemAsString(fmuWrapper));
	EXPECT_STREQ("293.093", getResultItemAsString(fmuWrapper));

   // char * result = getResultItemAsString(fmuWrapper);
}


void init(_TCHAR * fmuSubPath) {


	fmuWrapper = new Straylight::FMUwrapper();

	_TCHAR *fmuUnzippedFolder=new TCHAR[MAX_PATH+1];

	getModuleFolderPath(fmuUnzippedFolder);
	_tcscat(fmuUnzippedFolder, fmuSubPath);

	fmuUnzippedFolder[lstrlen(fmuUnzippedFolder)] = '\0';

	char * wfmuUnzippedFolder = wstrdup (fmuUnzippedFolder);

	int result = fmuWrapper->parseXML(wfmuUnzippedFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();



}


*/




int _tmain(int argc, _TCHAR* argv[])
{

    testing::InitGoogleTest(&argc, argv); 
    RUN_ALL_TESTS(); 
    std::getchar(); // keep console window open until Return keystroke

	return 0;
}


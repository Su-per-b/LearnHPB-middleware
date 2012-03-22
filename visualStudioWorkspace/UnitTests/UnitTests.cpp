// useGtest.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "UnitTests.h"
#include "utils.h"

using namespace Straylight;


MainFMUwrapper *fmuWrapper;


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


	fmuWrapper = new Straylight::MainFMUwrapper();


	_TCHAR *fmuUnzippedFolder=new TCHAR[MAX_PATH+1];

	getModuleFolderPath(fmuUnzippedFolder);
	_tcscat(fmuUnzippedFolder, fmuSubPath);

	fmuUnzippedFolder[lstrlen(fmuUnzippedFolder)] = '\0';

	char * wfmuUnzippedFolder = wstrdup (fmuUnzippedFolder);

	int result = fmuWrapper->parseXML(wfmuUnzippedFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();



}



/*
TEST(LearnGB, LearnGB_VAVReheat_ClosedLoop)
{

	init(_T("\\FMUs\\LearnGB_VAVReheat_ClosedLoop"));
	char * str = getResultItemAsString(fmuWrapper);

	//EXPECT_STREQ( , str);

}

TEST(FMUwrapper, connect)
{
	MainFMUwrapper *fmuWrapper;

	fmuWrapper = new MainFMUwrapper();
    EXPECT_TRUE(fmuWrapper != NULL);

}


TEST(FMUwrapper, parseXML)
{
	TCHAR *szDir=new TCHAR[MAX_PATH+1];

	getUnzipFolder(szDir);
	fmuWrapper = new Straylight::MainFMUwrapper();

	char * dir = wstrdup (szDir);
	int result = fmuWrapper->parseXML(dir);
    EXPECT_TRUE(result == 0);
	_tcscat(szDir, L"\\modelDescription.xml");

	int result2 = fmuWrapper->loadDll();
    EXPECT_TRUE(result2 == 0);

	delete[] szDir;
    delete fmuWrapper;

}




TEST(FMUwrapper, runSimulation)
{

	fmuWrapper = new Straylight::MainFMUwrapper();

	TCHAR *szDir=new TCHAR[MAX_PATH+1];
	getUnzipFolder(szDir);
	char * dir = wstrdup (szDir);

	int result = fmuWrapper->parseXML(dir);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

	EXPECT_STREQ( "-0.5,2,2,1,1.5,-1,1,1,1,1", getResultItemAsString(fmuWrapper));
    EXPECT_STREQ( "0.5,3.5,2,1,2.5,-1,1,2.5,1,1", getResultItemAsString(fmuWrapper));
    EXPECT_STREQ( "2,4.5,2,1,4,-1,1,3.5,1,1", getResultItemAsString(fmuWrapper));

	EXPECT_STREQ( "3,6,2,1,5,-1,1,5,1,1", getResultItemAsString(fmuWrapper));
    EXPECT_STREQ( "4.5,7,2,1,6.5,-1,1,6,1,1", getResultItemAsString(fmuWrapper));
    EXPECT_STREQ( "5.5,8.5,2,1,7.5,-1,1,7.5,1,1", getResultItemAsString(fmuWrapper));

	EXPECT_STREQ( "7,9.5,2,1,9,-1,1,8.5,1,1", getResultItemAsString(fmuWrapper));
    EXPECT_STREQ( "8,11,2,1,10,-1,1,10,1,1", getResultItemAsString(fmuWrapper));
    EXPECT_STREQ( "9.5,12,2,1,11.5,-1,1,11,1,1", getResultItemAsString(fmuWrapper));

    EXPECT_STREQ( "10.5,13.5,2,1,12.5,-1,1,12.5,1,1", getResultItemAsString(fmuWrapper));
    EXPECT_STREQ( "12,14.5,2,1,14,-1,1,13.5,1,1", getResultItemAsString(fmuWrapper));

	fmuWrapper->printSummary();
	delete fmuWrapper;

}


*/




int _tmain(int argc, _TCHAR* argv[])
{

    testing::InitGoogleTest(&argc, argv); 
    RUN_ALL_TESTS(); 
    std::getchar(); // keep console window open until Return keystroke

	return 0;
}


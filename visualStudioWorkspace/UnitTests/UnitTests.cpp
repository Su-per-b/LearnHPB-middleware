// useGtest.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>

#include "UnitTests.h"

#include "gtest/gtest.h"
#include <MainFMUwrapper.h>


Straylight::MainFMUwrapper *fmuWrapper;


TEST(sample_test_case, sample_test)
{
    EXPECT_EQ(1, 1);
}

TEST(FMUwrapper, connect)
{
	Straylight::MainFMUwrapper *fmuWrapper;

	fmuWrapper = new Straylight::MainFMUwrapper();
    EXPECT_TRUE(fmuWrapper != NULL);

}
TEST(FMUwrapper, parseXML)
{

	fmuWrapper = new Straylight::MainFMUwrapper();
	int result = fmuWrapper->parseXML("C:\\Temp\\DoubleInputDoubleOutput.fmu_unzipped");

    EXPECT_TRUE(result == 0);
	EXPECT_STREQ( "C:\\Temp\\DoubleInputDoubleOutput.fmu_unzipped\\modelDescription.xml", fmuWrapper->getXmlFilePath());

	int result2 = fmuWrapper->loadDll();
    EXPECT_TRUE(result2 == 0);


    delete fmuWrapper;

}

TEST(FMUwrapper, runSimulation)
{
	//Straylight::MainFMUwrapper *fmuWrapper;

	fmuWrapper = new Straylight::MainFMUwrapper();
	int result = fmuWrapper->parseXML("C:\\Temp\\DoubleInputDoubleOutput.fmu_unzipped");
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

	EXPECT_STREQ( "-0.5,2,2,1,1.5,-1,1,1,1,1", getResultItemAsString());
    EXPECT_STREQ( "0.5,3.5,2,1,2.5,-1,1,2.5,1,1", getResultItemAsString());
    EXPECT_STREQ( "2,4.5,2,1,4,-1,1,3.5,1,1", getResultItemAsString());

	EXPECT_STREQ( "3,6,2,1,5,-1,1,5,1,1", getResultItemAsString());
    EXPECT_STREQ( "4.5,7,2,1,6.5,-1,1,6,1,1", getResultItemAsString());
    EXPECT_STREQ( "5.5,8.5,2,1,7.5,-1,1,7.5,1,1", getResultItemAsString());

	EXPECT_STREQ( "7,9.5,2,1,9,-1,1,8.5,1,1", getResultItemAsString());
    EXPECT_STREQ( "8,11,2,1,10,-1,1,10,1,1", getResultItemAsString());
    EXPECT_STREQ( "9.5,12,2,1,11.5,-1,1,11,1,1", getResultItemAsString());

    EXPECT_STREQ( "10.5,13.5,2,1,12.5,-1,1,12.5,1,1", getResultItemAsString());
    EXPECT_STREQ( "12,14.5,2,1,14,-1,1,13.5,1,1", getResultItemAsString());



	fmuWrapper->printSummary();
	delete fmuWrapper;

}

char * getResultItemAsString() {
	Straylight::ResultItem * ri;

	fmuWrapper->doOneStep();
	ri = fmuWrapper->getResultItem();

	return ri->getString();

}



int _tmain(int argc, _TCHAR* argv[])
{

    testing::InitGoogleTest(&argc, argv); 
    RUN_ALL_TESTS(); 
    std::getchar(); // keep console window open until Return keystroke

	return 0;
}


// useGtest.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>

#include "gtest/gtest.h"
#include <MainFMUwrapper.h>

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



int _tmain(int argc, _TCHAR* argv[])
{

    testing::InitGoogleTest(&argc, argv); 
    RUN_ALL_TESTS(); 
    std::getchar(); // keep console window open until Return keystroke

	return 0;
}


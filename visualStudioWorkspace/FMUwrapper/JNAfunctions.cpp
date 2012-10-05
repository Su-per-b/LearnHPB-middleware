/*******************************************************//**
 * @file	JNAfunctions.cpp
 *
 * Implements the jn afunctions class.
 *******************************************************/
#include "JNAfunctions.h"



/*******************************************************//**
 * Request state change.
 *
 * @param	simStateNative	The simulation state native.
 *******************************************************/
void requestStateChange (SimStateNative simStateNative) {
	mainController->requestStateChange(simStateNative);

/*******************************************************//**
 * Gets the configuration.
 *
 * @return	null if it fails, else the configuration.
 *******************************************************/
}

/*******************************************************//**
 * Gets the configuration.
 *
 * @return	null if it fails, else the configuration.
 *******************************************************/
ConfigStruct * getConfig() {
	return mainController->getConfig();
}

int setConfig(ConfigStruct * configStruct) {
	mainController->setConfig(configStruct);

	return 0;
}

/*******************************************************//**
 * Executes the message callback c action.
 *
 * @param [in,out]	messageStruct	If non-null, the message structure.
 *******************************************************/
void onMessageCallbackC(MessageStruct * messageStruct)
{
	messageCallbackPtr_(messageStruct);
	delete messageStruct;

/*******************************************************//**
 * Cleanups this object.
 *******************************************************/
}

/*******************************************************//**
 * Cleanups this object.
 *******************************************************/
void cleanup() {
	/*******************************************************//**
	 * Default constructor.
	 *******************************************************/
	mainController->cleanup();
}

/*******************************************************//**
 * Gets the force cleanup.
 *
 * @return	.
 *******************************************************/
int forceCleanup()
{
	//fmuWrapper->setState(fmuState_cleanedup);
	delete mainController;

	return 0;
}

int run()
{
	mainController->run();
	return 0;
}

/*******************************************************//**
 * Gets all scalar variables.
 *
 * @return	null if it fails, else all scalar variables.
 *******************************************************/
ScalarVariablesAllStruct * getAllScalarVariables() {
	Straylight::MainDataModel * model = mainController->getMainDataModel();

	ScalarVariablesAllStruct * allScalarVariables = new ScalarVariablesAllStruct();
	allScalarVariables->input = model->scalarVariableDataModel_->svInput_->toStruct();
	allScalarVariables->output = model->scalarVariableDataModel_->svOutput_->toStruct();
	allScalarVariables->internal = model->scalarVariableDataModel_->svInternal_->toStruct();

	return allScalarVariables;
}

/*******************************************************//**
 * Gets the is simulation complete.
 *
 * @return	.
 *******************************************************/
int isSimulationComplete () {
	return mainController->isSimulationComplete();
}

/*******************************************************//**
 * Connects the given message callback pointer.
 *
 * @param [in,out]	messageCallbackPtr	If non-null, the message callback pointer to connect.
 *******************************************************/
void connect (
	void (*messageCallbackPtr)(MessageStruct *),
	void (*resultCallbackPtr)(ScalarValueResultsStruct *),
	void (*stateChangeCallbackPtr)(SimStateNative )
	)
{
	Config::getInstance()->setAutoCorrect(true);

	mainController = new MainController();

	mainController->connect (
		messageCallbackPtr,
		resultCallbackPtr,
		stateChangeCallbackPtr
		);
}

/*******************************************************//**
 * XML parse.
 *
 * @param [in,out]	unzipFolder	If non-null, pathname of the unzip folder.
 *******************************************************/
void xmlParse (char * unzipFolder)
{
	int result = mainController->xmlParse(unzipFolder);
}

/*******************************************************//**
 * Initialises this object.
 *
 * @return	.
 *******************************************************/
int init ()
{
	return mainController->init();
}


/*******************************************************//**
 * Sets scalar value real.
 *
 * @param	idx  	The index.
 * @param	value	The value.
 *
 * @return	.
 *******************************************************/
fmiStatus setScalarValueReal (int idx, double value) {
	return mainController->setScalarValueReal(idx,value);
}

/*******************************************************//**
 * Sets scalar values.
 *
 * @param [in,out]	scalarValueAry	If non-null, the scalar value ary.
 * @param	length				  	The length.
 *******************************************************/
void setScalarValues (ScalarValueRealStruct * scalarValueAry , int length) {
		vector<ScalarValueRealStruct> scalarValueList;

		for (int i = 0; i < length; i++)
		{
			ScalarValueRealStruct st = scalarValueAry[i];
			scalarValueList.push_back(st);

			Logger::getInstance()->printDebug(_T("setScalarValues\n"));
		}

		mainController->setScalarValues(scalarValueAry,length);
}


ScalarValueResults * getScalarValueResults()
{
	return mainController->getScalarValueResults();
}


extern "C" DllExport ScalarValueResultsStruct* getTest()
{

	return mainController->getTest();

}


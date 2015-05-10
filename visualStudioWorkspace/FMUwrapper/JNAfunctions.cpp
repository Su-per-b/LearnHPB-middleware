/*******************************************************//**
 * @file	JNAfunctions.cpp
 *
 * Implements the jn afunctions class.
 *******************************************************/
#include "JNAfunctions.h"

#define DLL_VERSION  "0.8.11"


/*******************************************************//**
 * Message callback pointer.
 *
 * @param [in,out]	parameter1	If non-null, the first parameter.
 *******************************************************/
void (*messageCallbackPtr_)(MessageStruct *);

/*******************************************************//**
 * Result callback pointer.
 *
 * @param [in,out]	parameter1	If non-null, the first parameter.
 *******************************************************/
void (*resultCallbackPtr_)(ResultOfStepStruct *);

/*******************************************************//**
 * State change callback pointer.
 *
 * @param	parameter1	The first parameter.
 *******************************************************/
void (*stateChangeCallbackPtr_)(SimStateNative );


/*******************************************************//**
 * The main controller.
 *******************************************************/
MainController *  mainController;


/*******************************************************//**
 * Request state change.
 *
 * @param	simStateNative	The simulation state native.
 *******************************************************/
void requestStateChange (SimStateNative simStateNative) {
	mainController->requestStateChange(simStateNative);
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
	messageStruct = NULL;

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

	if (NULL == mainController) {
		return 0;
	}
	else {
		mainController->forceCleanup();
		//delete mainController;
		//mainController = NULL;
		return 0;
	}

}

/*
int run()
{
mainController->run();
return 0;
}
*/

/*******************************************************//**
 * Gets all scalar variables.
 *
 * @return	null if it fails, else all scalar variables.
 *******************************************************/
ScalarVariablesAllStruct * getAllScalarVariables() {

	Straylight::MainDataModel * model = mainController->getMainDataModel();

	ScalarVariablesAllStruct * scalarVariablesAllStruct = new ScalarVariablesAllStruct();

	ScalarVariableCollection * inputCollection = model->scalarVariableDataModel_->svInput_;
	ScalarVariableCollectionStruct * inputStruct = inputCollection->toStruct();
	scalarVariablesAllStruct->input = inputStruct;	

	ScalarVariableCollection * outputCollection = model->scalarVariableDataModel_->svOutput_;
	ScalarVariableCollectionStruct * outputStruct = outputCollection->toStruct();
	scalarVariablesAllStruct->output = outputStruct;

	ScalarVariableCollection * internalCollection = model->scalarVariableDataModel_->svInternal_;
	ScalarVariableCollectionStruct * internalStruct = internalCollection->toStruct();
	scalarVariablesAllStruct->internal = internalStruct;

	return scalarVariablesAllStruct;
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
	Config::getInstance()->setAutoCorrect(false);

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



void setOutputVariableNames(const char ** outputVariableNamesAry, int length) {

		StringMap * outputNamesStringMap = new StringMap();
		for (int i = 0; i < length; i++)
		{
			outputNamesStringMap->insert(std::make_pair(outputVariableNamesAry[i], true));
		}

		mainController->setOutputVariableNames(outputNamesStringMap);

}

void setInputVariableNames(const char ** inputVariableNamesAry, int length) {

	StringMap * inputNamesStringMap = new StringMap();
	for (int i = 0; i < length; i++)
	{
		inputNamesStringMap->insert(std::make_pair(inputVariableNamesAry[i], true));
	}

	mainController->setInputVariableNames(inputNamesStringMap);

}





ScalarValueResults * getScalarValueResults()
{
	return mainController->getScalarValueResults();
}


FMImodelAttributesStruct * getFMImodelAttributesStruct() {

	MainDataModel * mainDataModel = mainController->getMainDataModel();
	FMImodelAttributesStruct *  fmiModelAttributesStruct = mainDataModel->getFMImodelAttributesStruct();

	return fmiModelAttributesStruct;
}



BaseUnitStruct * getUnitDefinitions() {

	MainDataModel * mainDataModel = mainController->getMainDataModel();
	return mainDataModel->getBaseUnitStructAry();

}


SimStateNative getSimStateNative() {

	if (NULL == mainController) {
		return simStateNative_0_uninitialized;
	}
	else {
		return mainController->getState();
	}

}


TypeDefinitionsStruct * getTypeDefinitions() {

	TypeDefinitions * typeDefinitions = mainController->getTypeDefinitions();
	
	return typeDefinitions->toStruct();

}



char * getVersion() {

	return DLL_VERSION;

}





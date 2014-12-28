package com.sri.straylight.fmuWrapper.test.main;

public class CONSTANTS {

	//for VoNativeController
	public final static String STR_defaultExperimentStruct_0 = "{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10}";
	
	public final static String STR_defaultExperimentStruct_1 = "{\"t\":\"DefaultExperimentStruct\",\"startTime\":0,\"stopTime\":100,\"tolerance\":1.1}";
	
	public final static String STR_configStruct_0 = "{\"t\":\"ConfigStruct\",\"stepDelta\":1,\"defaultExperimentStruct\":{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10}}";
	
	public final static String STR_messageStruct_0 = "{\"t\":\"MessageStruct\",\"msgText\":\"This is the message text\",\"messageType\":0}";
	
	public final static String STR_scalarValueRealStruct_0 = "{\"t\":\"ScalarValueRealStruct\",\"i\":1,\"v\":2.1}";
	
	public final static String STR_simStateNative_0 = "{\"t\":\"SimStateNative\",\"intValue\":0}";
	
	public final static String STR_typeSpecReal_0 = "{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"K\"}";
	
	
	//for VoManagedController
	public final static String STR_scalarValueReal_0 = "{\"t\":\"ScalarValueReal\",\"i\":1,\"v\":2}";
	
	public final static String STR_scalarValueReal_1 = "{\"t\":\"ScalarValueReal\",\"i\":2,\"v\":14.2}";
	
	public final static String STR_serializableVector_0 = "{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2},{\"i\":2,\"v\":3.53}]}";
	
	public final static String STR_scalarValueCollection_0 = "{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2},{\"i\":2,\"v\":3.53}]}}";
	
	public final static String STR_scalarValueResults_0 = "{\"t\":\"ScalarValueResults\",\"time_\":2800.1,\"input\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2},{\"i\":2,\"v\":3.53}]}},\"output\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":3,\"v\":258.2},{\"i\":4,\"v\":78}]}}}";
	
	public final static String STR_scalarVariableReal_0 = "{\"t\":\"ScalarVariableReal\",\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C\"}}";

	public final static String STR_scalarVariableCollection_0 = "{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C1\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.26,\"min\":2.27,\"max\":2.28,\"unit\":\"C2\"}}]}}";
	
	public final static String STR_scalarVariableCollection_1 = "{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"ES-23\",\"i\":0,\"c\":7,\"vb\":5,\"d\":\"This is the pressure in the duct measured in Pa\",\"vr\":7547,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":1,\"nominal\":1000,\"min\":0,\"max\":5400.01,\"unit\":\"Pa\"}},{\"n\":\"PA57-FA\",\"i\":1,\"c\":6,\"vb\":3,\"d\":\"Pressure in Duct per meter\",\"vr\":55,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":12,\"nominal\":50,\"min\":2,\"max\":500000,\"unit\":\"Pa per meter\"}}]}}";
	
	public final static String STR_scalarVariablesAll_0 = "{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C1\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.26,\"min\":2.27,\"max\":2.28,\"unit\":\"C2\"}}]}},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"ES-23\",\"i\":0,\"c\":7,\"vb\":5,\"d\":\"This is the pressure in the duct measured in Pa\",\"vr\":7547,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":1,\"nominal\":1000,\"min\":0,\"max\":5400.01,\"unit\":\"Pa\"}},{\"n\":\"PA57-FA\",\"i\":1,\"c\":6,\"vb\":3,\"d\":\"Pressure in Duct per meter\",\"vr\":55,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":12,\"nominal\":50,\"min\":2,\"max\":500000,\"unit\":\"Pa per meter\"}}]}}}";
	
	public final static String STR_XMLparsedInfo_0 = "{\"t\":\"XMLparsedInfo\",\"sessionID_\":\"{not set}\",\"scalarVariablesAll_\":{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C1\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.26,\"min\":2.27,\"max\":2.28,\"unit\":\"C2\"}}]}},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"ES-23\",\"i\":0,\"c\":7,\"vb\":5,\"d\":\"This is the pressure in the duct measured in Pa\",\"vr\":7547,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":1,\"nominal\":1000,\"min\":0,\"max\":5400.01,\"unit\":\"Pa\"}},{\"n\":\"PA57-FA\",\"i\":1,\"c\":6,\"vb\":3,\"d\":\"Pressure in Duct per meter\",\"vr\":55,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":12,\"nominal\":50,\"min\":2,\"max\":500000,\"unit\":\"Pa per meter\"}}]}}}}";
	
	public final static String STR_sessionControlAction_0 = "{\"t\":\"SessionControlAction\",\"intValue\":0}";
	
	public final static String STR_sessionControlAction_1 = "{\"t\":\"SessionControlAction\",\"intValue\":1}";
	
	public final static String STR_sessionControlModel_0 = "{\"t\":\"SessionControlModel\",\"v\":\"SESS1342\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":0}}";
	
	public final static String STR_sessionControlModel_1 = "{\"t\":\"SessionControlModel\",\"v\":\"\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":1}}";
	
	
	//for EventController
	public final static String STR_messageEvent_0 = "{\"t\":\"MessageEvent\",\"payload\":{\"t\":\"MessageStruct\",\"msgText\":\"This is the test Message Text\",\"messageType\":0}}";
	
	public final static String STR_configChangeNotify_0 = "{\"t\":\"ConfigChangeNotify\",\"payload\":{\"t\":\"ConfigStruct\",\"stepDelta\":1,\"defaultExperimentStruct\":{\"t\":\"DefaultExperimentStruct\",\"startTime\":123.03,\"stopTime\":145.03,\"tolerance\":10}}}";
			
	public final static String STR_resultEvent_0 = "{\"t\":\"ResultEvent\",\"payload\":{\"t\":\"ScalarValueResults\",\"time_\":2800.1,\"input\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2},{\"i\":2,\"v\":3.53}]}},\"output\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":3,\"v\":258.2},{\"i\":4,\"v\":78}]}}}}";

	public final static String STR_simStateNativeRequest_0 = "{\"t\":\"SimStateNativeRequest\",\"payload\":{\"t\":\"SimStateNative\",\"intValue\":17}}";
	
	public final static String STR_simStateNativeNotify_0 = "{\"t\":\"SimStateNativeNotify\",\"payload\":{\"t\":\"SimStateNative\",\"intValue\":10}}";
    
	public final static String STR_xmlParsedEvent_0 = "{\"t\":\"XMLparsedEvent\",\"payload\":{\"t\":\"XMLparsedInfo\",\"sessionID_\":\"{not set}\",\"scalarVariablesAll_\":{\"t\":\"ScalarVariablesAll\",\"input\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":5,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":20.25,\"nominal\":21.25,\"min\":22.25,\"max\":23.25,\"unit\":\"C1\"}},{\"n\":\"scalarVar name\",\"i\":1,\"c\":6,\"vb\":4,\"d\":\"The Description\",\"vr\":125420,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":2.25,\"nominal\":2.26,\"min\":2.27,\"max\":2.28,\"unit\":\"C2\"}}]}},\"output\":{\"t\":\"ScalarVariableCollection\",\"realVarList_\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarVariableReal\",\"itemArray\":[{\"n\":\"ES-23\",\"i\":0,\"c\":7,\"vb\":5,\"d\":\"This is the pressure in the duct measured in Pa\",\"vr\":7547,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":1,\"nominal\":1000,\"min\":0,\"max\":5400.01,\"unit\":\"Pa\"}},{\"n\":\"PA57-FA\",\"i\":1,\"c\":6,\"vb\":3,\"d\":\"Pressure in Duct per meter\",\"vr\":55,\"typeSpecReal\":{\"t\":\"TypeSpecReal\",\"start\":12,\"nominal\":50,\"min\":2,\"max\":500000,\"unit\":\"Pa per meter\"}}]}}}}}";
	
	public final static String STR_scalarValueChangeRequest_0 = "{\"t\":\"ScalarValueChangeRequest\",\"payload\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2}]}}}";
	
	public final static String STR_sessionControlClientRequest_0 = "{\"t\":\"SessionControlClientRequest\",\"payload\":{\"t\":\"SessionControlModel\",\"v\":\"SESS1342\",\"action\":{\"t\":\"SessionControlAction\",\"intValue\":0}}}";
	
	public final static String STR_initialStateRequest_0 = "{\"t\":\"InitialStateRequest\",\"payload\":{\"t\":\"ScalarValueCollection\",\"realList\":{\"t\":\"SerializableVector\",\"itemType\":\"ScalarValueReal\",\"itemArray\":[{\"i\":1,\"v\":2},{\"i\":2,\"v\":3.53}]}}}";
	
	public final static String STR_serializableVector_1 = "{\"t\":\"SerializableVector\",\"itemType\":\"StringPrimitive\",\"itemArray\":[{\"v\":\"y_ZN[1]\"},{\"v\":\"y_ZN[5]\"}]}";

}

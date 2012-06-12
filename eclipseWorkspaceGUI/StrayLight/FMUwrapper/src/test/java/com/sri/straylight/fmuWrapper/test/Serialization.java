package com.sri.straylight.fmuWrapper.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.google.gson.Gson;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.serialization.GsonController;
import com.sri.straylight.fmuWrapper.serialization.SerializeableObject;
import com.sri.straylight.fmuWrapper.voManaged.Initialized;
import com.sri.straylight.fmuWrapper.voManaged.Result;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValue;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueStruct;
import com.sri.straylight.fmuWrapper.voNative.State;

/**
 * Unit test for JSON serialization
 */
public class Serialization 
    extends TestCase
{
	
	
	private static Gson gson = GsonController.getInstance().getGson();
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Serialization( String testName )
    {
        super( testName );
    }
    
    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	
        return new TestSuite( Serialization.class );
    }
    
    public void testInitializedEvent() {
    	Initialized initializedStruct1 = new Initialized();
    	initializedStruct1.outputVarNames = new String[2];
    	initializedStruct1.outputVarNames[0] = "col 1";
    	initializedStruct1.outputVarNames[1] = "col 2";
    	
    	InitializedEvent event1 = new InitializedEvent(this);
    	event1.initializedStruct = initializedStruct1;
    	
    	String jsonString = gson.toJson(event1);
    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
    	
    	assertEquals(event1.getClass().getCanonicalName(), obj.type);
    	
    	
    	InitializedEvent event2 = gson.fromJson(jsonString, InitializedEvent.class);
    	
    	assertEquals(event1.initializedStruct.outputVarNames[0], event2.initializedStruct.outputVarNames[0]);
    	
    }
    
    public void testInitializedStruct() {
    	Initialized initializedStruct1 = new Initialized();
    	initializedStruct1.outputVarNames = new String[2];
    	initializedStruct1.outputVarNames[0] = "col 1";
    	initializedStruct1.outputVarNames[1] = "col 2";
    	

    	String jsonString = gson.toJson(initializedStruct1);
    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
    	
    	assertEquals(initializedStruct1.getClass().getCanonicalName(), obj.type);
    	
    	Initialized initializedStruct2 = gson.fromJson(jsonString, Initialized.class);
    	
    	assertEquals(initializedStruct1.outputVarNames[0], initializedStruct2.outputVarNames[0]);
    	assertEquals(initializedStruct1.outputVarNames[1], initializedStruct2.outputVarNames[1]);
    	
    }
    
    
    
    public void testFMUstateEvent() {
    	State state1 = State.fmuState_level_2_dllLoaded;
    	
    	FMUstateEvent fmuStateEvent1 = new FMUstateEvent(this);
    	fmuStateEvent1.fmuState = state1;
    	
    	String jsonString = gson.toJson(fmuStateEvent1);
    	   	
    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
    	assertEquals(fmuStateEvent1.getClass().getCanonicalName(), obj.type);
    	
    	FMUstateEvent fmuStateEvent2 = gson.fromJson(jsonString, FMUstateEvent.class);
    	assertEquals(fmuStateEvent1.fmuState, fmuStateEvent2.fmuState);
	
    }
    
    
    /**
     * State
     */
    public void testState() {
    	State state1= State.fmuState_level_1_xmlParsed;
    	
    	String jsonString = gson.toJson(state1);
    	
    	assertNotNull(jsonString);
    	
    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
    	assertEquals(state1.getClass().getCanonicalName(), obj.type);
    	
    	State state2 = gson.fromJson(jsonString, State.class);
    	assertEquals(state1.getIntValue(), state2.getIntValue());
    	
    	
    	
    }
    
    
    
    /**
     * ResultEvent
     */
    public void testResultEvent()
    {
    	
    	Result resultItem1 = new Result();
    	
    	resultItem1.time = 2.51;
    	resultItem1.string = "two point five one";
    	resultItem1.scalarValueCount = 2;
    	
    	ScalarValue resultItemPrimitive1 = new ScalarValue();
    	resultItemPrimitive1.idx =1;
    	resultItemPrimitive1.string = "one";
    	
    	ScalarValue resultItemPrimitive2 = new ScalarValue();
    	resultItemPrimitive2.idx = 2;
    	resultItemPrimitive2.string = "two";
    	
    	resultItem1.scalarValueAry = new ScalarValue[2];
    	resultItem1.scalarValueAry[0] = resultItemPrimitive1;
    	resultItem1.scalarValueAry[1] = resultItemPrimitive2;
    	
    	ResultEvent resultEvent1 = new ResultEvent(this);
    	resultEvent1.resultItem = resultItem1;
    	
    	
    	String jsonString = gson.toJson(resultEvent1);
    	
    	assertNotNull(jsonString);

    	assertEquals(
    			"{\"type\":\"com.sri.straylight.fmuWrapper.event.ResultEvent\",\"resultString\":\"\",\"resultItem\":{\"type\":\"com.sri.straylight.fmuWrapper.ResultItem\",\"time\":2.51,\"string\":\"two point five one\",\"primitiveCount\":2,\"primitiveAry\":[{\"type\":\"com.sri.straylight.fmuWrapper.ResultItemPrimitive\",\"idx\":1,\"string\":\"one\"},{\"type\":\"com.sri.straylight.fmuWrapper.ResultItemPrimitive\",\"idx\":2,\"string\":\"two\"}]}}",
    			jsonString
    			);
    	
    	
    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
    	assertEquals(resultEvent1.getClass().getCanonicalName(), obj.type);
    	
    	
    	ResultEvent resultEvent2 = gson.fromJson(jsonString, ResultEvent.class);
    	
    	assertEquals(resultEvent1.resultItem.time, resultEvent2.resultItem.time);
    	assertEquals(resultEvent1.resultItem.string, resultEvent2.resultItem.string);
    	
    	
    }
    
    /**
     * ResultItem
     */
    public void testResultItem()
    {
    	Result resultItem1 = new Result();
    	
    	resultItem1.time = 2.51;
    	resultItem1.string = "two point five one";
    	resultItem1.scalarValueCount = 2;
    	
    	ScalarValue resultItemPrimitive1 = new ScalarValue();
    	resultItemPrimitive1.idx =1;
    	resultItemPrimitive1.string = "one";
    	
    	ScalarValue resultItemPrimitive2 = new ScalarValue();
    	resultItemPrimitive2.idx = 2;
    	resultItemPrimitive2.string = "two";
    	
    	resultItem1.scalarValueAry = new ScalarValue[2];
    	resultItem1.scalarValueAry[0] = resultItemPrimitive1;
    	resultItem1.scalarValueAry[1] = resultItemPrimitive2;
    	

    	
    	
    	String jsonString = gson.toJson(resultItem1);
    	
    	assertNotNull(jsonString);
    	
    	assertEquals(
    			"{\"type\":\"com.sri.straylight.fmuWrapper.ResultItem\",\"time\":2.51,\"string\":\"two point five one\",\"primitiveCount\":2,\"primitiveAry\":[{\"type\":\"com.sri.straylight.fmuWrapper.ResultItemPrimitive\",\"idx\":1,\"string\":\"one\"},{\"type\":\"com.sri.straylight.fmuWrapper.ResultItemPrimitive\",\"idx\":2,\"string\":\"two\"}]}",
    			jsonString
    			);
    	
    	
    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
    	assertEquals(resultItem1.getClass().getCanonicalName(), obj.type);
    	

    	Result resultItem2 = gson.fromJson(jsonString, Result.class);
    	
    	assertEquals(resultItem1.time, resultItem2.time);
    	assertEquals(resultItem1.string, resultItem2.string);
    	
    }

    
    
    /**
     * ResultItemPrimitiveStruct
     */
    public void testResultItemPrimitiveStruct()
    {
    	ScalarValueStruct resultItemPrimitiveStruct1 = new ScalarValueStruct();
    	resultItemPrimitiveStruct1.idx =1;
    	resultItemPrimitiveStruct1.string = "one";
    	
    	String jsonString = gson.toJson(resultItemPrimitiveStruct1);
    	
    	assertNotNull(jsonString);
    	
    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
    	assertEquals(resultItemPrimitiveStruct1.getClass().getCanonicalName(), obj.type);
    	
    	assertEquals(
    			"{\"type\":\"com.sri.straylight.fmuWrapper.ResultItemPrimitiveStruct\",\"idx\":1,\"string\":\"one\"}",
    			jsonString
    			);
    	
    	
    	ScalarValueStruct resultItemPrimitiveStruct2 = gson.fromJson(jsonString, ScalarValueStruct.class);
    	
    	assertEquals(resultItemPrimitiveStruct1.idx, resultItemPrimitiveStruct2.idx);
    	assertEquals(resultItemPrimitiveStruct1.string, resultItemPrimitiveStruct2.string);
    	
    }

    
    
    /**
     * MessageEvent
     */
    public void testMessageEvent()
    {
    	MessageStruct messageStruct1 = new MessageStruct();
    	messageStruct1.msgText = "Test Info";
    	messageStruct1.setMessageTypeEnum(MessageType.messageType_info);
    	
    	
    	MessageEvent messageEvent1 = new MessageEvent(this);
    	messageEvent1.messageStruct = messageStruct1;
    	

    	String jsonString = gson.toJson(messageEvent1);
    	
    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
    	assertEquals(messageEvent1.getClass().getCanonicalName(), obj.type);
    	
    	assertEquals(
    			"{\"type\":\"com.sri.straylight.fmuWrapper.event.MessageEvent\",\"messageStruct\":{\"type\":\"com.sri.straylight.fmuWrapper.MessageStruct\",\"msgText\":\"Test Info\",\"messageType\":1}}",
    			jsonString
    			);
    	
    	MessageEvent messageEvent2 = gson.fromJson(jsonString, MessageEvent.class);
    	

    	assertEquals(messageEvent1.messageStruct.msgText, messageEvent2.messageStruct.msgText);
    	
    }
    
    /**
     * MessageStruct
     */
    public void testMessageStruct()
    {
    	MessageStruct messageStruct1 = new MessageStruct();
    	
    	messageStruct1.msgText = "Test Info";
    	messageStruct1.setMessageTypeEnum(MessageType.messageType_info);

    	String jsonString = gson.toJson(messageStruct1);
    	
    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
    	assertEquals(messageStruct1.getClass().getCanonicalName(), obj.type);
    	
    	assertEquals(
    			"{\"type\":\"com.sri.straylight.fmuWrapper.MessageStruct\",\"msgText\":\"Test Info\",\"messageType\":1}",
    			jsonString
    			);
    	
    	MessageStruct messageStruct2 = gson.fromJson(jsonString, MessageStruct.class);

    	assertEquals(messageStruct1.messageType, messageStruct2.messageType);
    	assertEquals(messageStruct1.msgText, messageStruct2.msgText);

    }
    
}

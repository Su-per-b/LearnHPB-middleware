package com.sri.straylight.fmuWrapper.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.SimStateWrapperNotify;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;

// TODO: Auto-generated Javadoc
/**
 * Unit test for JSON serialization.
 */
public class Serialization 
    extends TestCase
{
	
	
	/** The for serialization. */
	private  JsonController gsonController_ = JsonController.getInstance();
    
	
    /**
     * Create the test case.
     *
     * @param testName name of the test case
     */
    public Serialization( String testName )
    {
        super( testName );
    }
    
    
    
    /**
     * Suite.
     *
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( Serialization.class );
    }
    
    

    /**
     * Test fmu state event.
     */
    public void testSimStateWrapper() {


    }
    
    
    public void testMessageStruct() {
    	
    	MessageStruct messageStruct = new MessageStruct();
    	messageStruct.msgText = "testMessageStruct";
    	
    	messageStruct.setMessageTypeEnum(MessageType.messageType_debug);
    	String json = messageStruct.toJson();
    	
    	JsonSerializable obj = gsonController_.fromJson(json);
    	assertEquals(MessageStruct.class,  obj.getClass());
    	
    }
    
    
    public void testMessageEvent() {
    	
    	MessageStruct messageStruct1 = new MessageStruct();
    	messageStruct1.msgText = "testMessageStruct";
    	messageStruct1.setMessageTypeEnum(MessageType.messageType_debug);
    	
    	MessageEvent event = new MessageEvent(this, messageStruct1);
    	
    	String json = event.toJson();
    	
    	JsonSerializable obj = gsonController_.fromJson(json);
    	
    	assertEquals(MessageEvent.class,  obj.getClass());
    	
    	MessageStruct messageStruct2 = event.getPayload();
    	assertEquals(MessageStruct.class,  messageStruct2.getClass());
    	
    	assertEquals(messageStruct1.msgText,  messageStruct2.msgText);
    	
    }  

    
    
    
    
    /**
     * ResultEvent.
     */
    public void testResultEvent()
    {
    	//ScalarValueResults scalarValueResults
    	
    	
    	
    //	ResultEvent resultItem1 = new ResultEvent(this,);
    	
//    	ResultOfStep resultItem1 = new ResultOfStep();
//    	
//    	resultItem1.time = 2.51;
//    	resultItem1.string = "two point five one";
//    	resultItem1.scalarValueCount = 2;
//    	
//    	ScalarValue resultItemPrimitive1 = new ScalarValue();
//    	resultItemPrimitive1.idx =1;
//    	resultItemPrimitive1.string = "one";
//    	
//    	ScalarValue resultItemPrimitive2 = new ScalarValue();
//    	resultItemPrimitive2.idx = 2;
//    	resultItemPrimitive2.string = "two";
//    	
//    	resultItem1.scalarValueAry = new ScalarValue[2];
//    	resultItem1.scalarValueAry[0] = resultItemPrimitive1;
//    	resultItem1.scalarValueAry[1] = resultItemPrimitive2;
//    	
//    	ResultEvent resultEvent1 = new ResultEvent(this);
//    	resultEvent1.resultOfStep = resultItem1;
//    	
//    	
//    	String jsonString = gson.toJson(resultEvent1);
//    	
//    	assertNotNull(jsonString);
//
//    	assertEquals(
//    			"{\"type\":\"com.sri.straylight.fmuWrapper.event.ResultEvent\",\"resultString\":\"\",\"resultItem\":{\"type\":\"com.sri.straylight.fmuWrapper.ResultItem\",\"time\":2.51,\"string\":\"two point five one\",\"primitiveCount\":2,\"primitiveAry\":[{\"type\":\"com.sri.straylight.fmuWrapper.ResultItemPrimitive\",\"idx\":1,\"string\":\"one\"},{\"type\":\"com.sri.straylight.fmuWrapper.ResultItemPrimitive\",\"idx\":2,\"string\":\"two\"}]}}",
//    			jsonString
//    			);
//    	
//    	
//    	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);
//    	assertEquals(resultEvent1.getClass().getCanonicalName(), obj.type);
//    	
//    	
//    	ResultEvent resultEvent2 = gson.fromJson(jsonString, ResultEvent.class);
//    	
//    	assertEquals(resultEvent1.resultOfStep.time, resultEvent2.resultOfStep.time);
//    	assertEquals(resultEvent1.resultOfStep.string, resultEvent2.resultOfStep.string);
    	
    	
    }

  

    
}

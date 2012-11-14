package com.sri.straylight.fmuWrapper.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateWrapperNotify;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueResultsStruct;

public class Serial {
	
	
	/** The for serialization. */
	private  JsonController gsonController_ = JsonController.getInstance();
    
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	
	@Test
	public void simStateWrapperNotify() {
		
    	SimStateWrapper state1 = SimStateWrapper.simStateServer_4_run_requested;
    	SimStateWrapperNotify event = new SimStateWrapperNotify(this, state1);
    	
    	String jsonString = event.toJson();
    	JsonSerializable obj = gsonController_.fromJson(jsonString);
    	
    	assertEquals(SimStateWrapperNotify.class,  obj.getClass());
    	
	}
	
	@Test
    public void messageEvent() {
    	
    	MessageStruct messageStruct1 = new MessageStruct();
    	messageStruct1.msgText = "testMessageStruct";
    	messageStruct1.setMessageTypeEnum(MessageType.messageType_debug);
    	
    	MessageEvent event = new MessageEvent(this, messageStruct1);
    	String json = event.toJson();
    	
    	JsonSerializable obj = gsonController_.fromJson(json);
    	assertEquals(MessageEvent.class,  obj.getClass());
    	
    	MessageEvent event2 = (MessageEvent) obj;

    	MessageStruct messageStruct2 = event2.getPayload();
    	assertEquals(messageStruct1, messageStruct2);
    	
    }
	
	
	@Test
    public void resutEvent() {
	
		
	//	ScalarValueCollection input = new ScalarValueCollection(struct.input);
		
		
		
		//ScalarValueCollectionStruct input = new ScalarValueCollectionStruct();
		//input.
		
		
		//ScalarValueResultsStruct struct = new ScalarValueResultsStruct();
		//struct.input =
		
		//ScalarValueResults scalarValueResults = new ScalarValueResults();
	
		//ResultEvent event = new ResultEvent();
		
		
		
	}
	
	
}

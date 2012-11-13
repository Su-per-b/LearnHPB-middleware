package com.sri.straylight.fmuWrapper.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;

public class EventSerialization {
	
	private  JsonController gsonController_ = JsonController.getInstance();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("@BeforeClass");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("@AfterClass");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("@Before");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("@After");
	}

	@Test
	public void test() {
		
    	MessageStruct structOrig = new MessageStruct();
    	structOrig.msgText = "testMessageStruct";
    	structOrig.setMessageTypeEnum(MessageType.messageType_debug);
    	
    	MessageEvent event = new MessageEvent(this, structOrig);
    	String json = event.toJson();
    	
    	JsonSerializable obj = gsonController_.fromJson(json);
    	assertEquals(MessageEvent.class,  obj.getClass());
    	
    	MessageStruct structDeserialized = event.getPayload();
    	
    	assertEquals(MessageStruct.class,  structDeserialized.getClass());
    	assertEquals(structOrig.msgText,  structDeserialized.msgText);
    	
    	
    	assertEquals(structOrig,  structDeserialized);
    	
    	
	}

}

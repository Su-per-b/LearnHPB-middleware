package com.sri.straylight.client.test;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sri.straylight.client.ConnectTo;
import com.sri.straylight.client.controller.SimulationController;
import com.sri.straylight.client.model.ClientConfig;
import com.sri.straylight.client.model.ClientConfigXML;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.client.test.OrderedRunner;


@RunWith(OrderedRunner.class)
public class WebsocketTs extends BaseTest {
	
	//private static SimulationController simulationController_;

	public WebsocketTs() {
		super();
	}

	
	@Test
	public void testA() throws Exception {
		System.out.println ("testA");
		
		ClientConfig configModel = ClientConfigXML.load();
		configModel.connectTo = ConnectTo.connectTo_localhost;
		
		new SimulationController( configModel );
		
		requestStateChange_(SimStateNative.simStateNative_1_connect_requested);
		awaitOnState(SimStateNative.simStateNative_1_connect_completed);
		
		System.out.println ("testA() done");
	}
	


	@Test
	public void testB() {
		
		
		System.out.println ("testB start");
	
		requestStateChange_(SimStateNative.simStateNative_2_xmlParse_requested);
		awaitOnState(SimStateNative.simStateNative_2_xmlParse_completed);
		
		
	}
	

	@EventSubscriber(eventClass=MessageEvent.class)
	public void onMessageEvent(MessageEvent event) {
		
		Thread t = Thread.currentThread();
	    String threadName = t.getName();
	    assert (threadName.equals("name=AWT-EventQueue-0"));
	    
	    MessageStruct struct = event.getPayload();
	    
	    System.out.println ("onMessageEvent(): " + struct.msgText);
	}
	
	
	
}



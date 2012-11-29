package com.sri.straylight.socketserver.controller;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateClientRequest;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.socketserver.FmuConnection;
import com.sri.straylight.socketserver.event.MessageReceived;
import com.sri.straylight.socketserver.event.MessageOut;
import com.sri.straylight.socketserver.event.WebSocketConnectionStateEvent;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class SimulationController extends AbstractController  {

	private FmuConnection fmuConnect_;
    
	final CyclicBarrier mainBarrier = new CyclicBarrier(2);
	private boolean firstRun_ = true;
    
	/**
	 * Instantiates a new simulation controller.
	 *
	 * @param parentController the parent controller
	 * @param configModel the config model
	 */
	public SimulationController (AbstractController parentController ) {
		super(parentController);
	}
	
	@EventSubscriber(eventClass=WebSocketConnectionStateEvent.class)
	public void onConnectionEvent(WebSocketConnectionStateEvent event) {
		
		WebSocketConnectionState connectionState = event.getPayload();
	
		switch(connectionState) {
			case opened_new : {
				SimStateNativeNotify.fire(this,getSimStateNative());
			}
			default:
				
		}
	}
	

	
	//this event comes from the client
	@EventSubscriber(eventClass=SimStateClientRequest.class)
    public void onSimStateClientRequest(SimStateClientRequest event) {
		
		SimStateNative requestedState = event.getPayload();
		
		switch (requestedState) {
			case simStateNative_1_connect_requested :
				fmuConnect_.connect();
				break;
			case simStateNative_2_xmlParse_requested :
				fmuConnect_.xmlParse();
				break;
			case simStateNative_4_run_requested :
				fmuConnect_.run();
				break;
			default:
				fmuConnect_.requestStateChange(requestedState);
		}
    }
	
	@EventSubscriber(eventClass=ScalarValueChangeRequest.class)
    public void onInputChangeRequest(ScalarValueChangeRequest event) {
		Vector<ScalarValueRealStruct> list = event.getPayload();
		fmuConnect_.setScalarValues(list);
	}
	
	
	
	public void init() {
		
		
    	fmuConnect_ = new FmuConnection();
    	

    	
//    	try {
//			fmuConnect_.connect();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	
//		awaitOnBarrier(mainBarrier);
//		fmuConnect_.xmlParse();
//		awaitOnBarrier(mainBarrier);
//		fmuConnect_.requestStateChange(SimStateNative.simStateNative_3_init_requested);
//		awaitOnBarrier(mainBarrier);
		
		
	}
	
	
	
	public FmuConnection getFmuConnect() {
		return fmuConnect_;
	}
	

	
	
	@EventSubscriber(eventClass=SimStateNativeRequest.class)
	public void onSimStateNativeRequest(SimStateNativeRequest event) {
		
		fmuConnect_.requestStateChange(event.getPayload());
	}
	
	
//
//	@EventSubscriber(eventClass=SimStateNativeNotify.class)
//	public void onSimStateNativeNotify(SimStateNativeNotify event) {
//
//		SimStateNative simStateNative = event.getPayload();
//
//		switch (simStateNative) {
//		case simStateNative_1_connect_completed:
//
//			awaitOnBarrier(mainBarrier);
//			break;
//		case simStateNative_2_xmlParse_completed:
//			awaitOnBarrier(mainBarrier);
//
//			break;
//		case simStateNative_3_ready:
//			
//			if (firstRun_) {
//				awaitOnBarrier(mainBarrier);
//				firstRun_ = false;
//			}
//			break;
//			
//		default:
//			
//
//		}
//		
//		
//		String jsonString = event.toJson();
//		EventBus.publish(
//				new MessageOut(this, jsonString)
//				);	
//		
//		
//	}
//	

	
	

	
	/**
	 * Calls barrier.await and supresses all its checked exceptions
	 *
	 * @param barrier the barrier
	 */
	private void awaitOnBarrier(CyclicBarrier barrier) {
		try {
			barrier.await(500, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		catch (BrokenBarrierException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		catch (TimeoutException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	public SimStateNative getSimStateNative() {
		return fmuConnect_.getSimStateNative();
	}
	
	
}

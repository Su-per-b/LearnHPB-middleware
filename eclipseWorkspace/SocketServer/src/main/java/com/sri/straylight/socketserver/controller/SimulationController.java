package com.sri.straylight.socketserver.controller;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.socketserver.FmuConnectLocal;
import com.sri.straylight.socketserver.event.MessageIn;
import com.sri.straylight.socketserver.event.MessageOut;


public class SimulationController extends AbstractController  {

	private FmuConnectLocal fmuConnect_;
    
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
	

	public void init() {
		
		
    	fmuConnect_ = new FmuConnectLocal();
    	
    	try {
			fmuConnect_.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		awaitOnBarrier(mainBarrier);
		fmuConnect_.xmlParse();
		awaitOnBarrier(mainBarrier);
		fmuConnect_.requestStateChange(SimStateNative.simStateNative_3_init_requested);
		awaitOnBarrier(mainBarrier);
		
		
	}
	
	
	
	public FmuConnectLocal getFmuConnect() {
		return fmuConnect_;
	}
	
	
	@EventSubscriber(eventClass=com.sri.straylight.socketserver.event.MessageIn.class)
	public void onMessageIn(MessageIn event) {
		
		String messageText = event.getPayload();
    	System.out.println(messageText);
		
//		switch (messageText) {
//
//			case "state_query":
//				FMUcontroller fmu = fmuConnect_.getFmu();
//				
//				SimStateWrapper state = fmu.getSimStateWrapper();
//				
//				SimStateWrapperNotify newEvent = new SimStateWrapperNotify(this,state);
//				String jsonString = newEvent.toJson();
//				
//				EventBus.publish(
//						new MessageOut(this, jsonString)
//						);	
//				
//				return;
//
//		}
		

    	JsonSerializable deserializedObject = JsonController.getInstance().fromJson(messageText);

    	if (deserializedObject instanceof BaseEvent) {
    		EventBus.publish(deserializedObject);
    	}
		
		
	}
	
	
	@EventSubscriber(eventClass=SimStateNativeRequest.class)
	public void onSimStateNativeRequest(SimStateNativeRequest event) {
		
		fmuConnect_.requestStateChange(event.getPayload());
	}
	
	

	@EventSubscriber(eventClass=SimStateNativeNotify.class)
	public void onSimStateWrapperNotify(SimStateNativeNotify event) {

		SimStateNative simStateNative = event.getPayload();

		switch (simStateNative) {
		case simStateNative_1_connect_completed:

			awaitOnBarrier(mainBarrier);
			break;
		case simStateNative_2_xmlParse_completed:
			awaitOnBarrier(mainBarrier);

			break;
		case simStateNative_3_ready:
			
			if (firstRun_) {
				awaitOnBarrier(mainBarrier);
				firstRun_ = false;
			}
			break;
			
		default:
			

		}
		
		
		String jsonString = event.toJson();
		EventBus.publish(
				new MessageOut(this, jsonString)
				);	
		
		
	}
	
	
	
	/**
	 * On result event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {

		ScalarValueResults  scalarValueResults = event.getPayload();
		String messageText = scalarValueResults.toString();
		
    	//Gson gson = GsonController.getInstance().getGson();
    	//String jsonString = gson.toJson(event);

		EventBus.publish(
				new MessageOut(this, messageText)
				);	
		
	}
	
	
	
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
	
	
}

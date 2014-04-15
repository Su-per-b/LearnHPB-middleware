package com.sri.straylight.fmuWrapper.test.base;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.Assert;

import com.sri.straylight.fmuWrapper.Controller.FMUcontroller;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public abstract class BaseThread extends Thread {
	

	
	/** The next state expected. */
	private static SimStateNative nextStateExpected_;
	
	private FMUcontroller fmuController_;
	
	/** The main barrier. */
	private final CyclicBarrier mainBarrier_ = new CyclicBarrier(2);
	
	private final CyclicBarrier stateChangeBarrier_ = new CyclicBarrier(2);
	
	
	
	public BaseThread(){
		AnnotationProcessor.process(this);
	}
	
	
	public void init(FMUcontroller fmuController) {
		fmuController_ = fmuController;
		registerSimulationListeners_();
	}
	
	protected void requestStateChangeTo_ 
		(SimStateNative stateChangeRequest, SimStateNative nextStateExpected) 
	{
		nextStateExpected_ = nextStateExpected;
		fmuController_.requestStateChange(stateChangeRequest);
		awaitOnStateChangeBarrier();
	}
	
	
	/**
	 * On onSimStateClientNotify Event
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateClientNotify.class)
	public void onSimStateClientNotify(SimStateClientNotify event) {
		
		
		String threadName = Thread.currentThread().getName();
		
	    Assert.assertEquals("AWT-EventQueue-0", threadName);
	    
	    SimStateNative simStateNative = event.getPayload();
	    Assert.assertEquals(nextStateExpected_, simStateNative);
	    
		awaitOnStateChangeBarrier();
	}
	
	
	public void start() {


		super.start();

	}
	
	public void awaitOnMainBarrier() {
		awaitOnBarrier(mainBarrier_);
	}
	
	private void awaitOnStateChangeBarrier() {
		awaitOnBarrier(stateChangeBarrier_);
	}
	
	
	/**
	 * Calls barrier.await and suppresses all its checked exceptions
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
	
	
	private void registerSimulationListeners_() {
		
		//SimStateNativeNotify
		fmuController_.registerEventListener(
				SimStateNativeNotify.class,
				new StraylightEventListener<SimStateNativeNotify, SimStateNative>() {
					@Override
					public void handleEvent(SimStateNativeNotify event) {
						
						
						SimStateNative simStateNative = event.getPayload();
						
						SimStateClientNotify newEvent = new SimStateClientNotify(this,
								simStateNative);
						EventBus.publish(newEvent);
						
					}
				});
		

		//MessageEvent
		fmuController_
		.registerEventListener(
				MessageEvent.class,
				new StraylightEventListener<MessageEvent, MessageStruct>() {
					@Override
					public void handleEvent(MessageEvent event) {
						
						EventBus.publish(event);
						
						
					}
				});
		
		//ResultEvent
		fmuController_
		.registerEventListener(
				ResultEvent.class,
				new StraylightEventListener<ResultEvent, ScalarValueResults>() {
					@Override
					public void handleEvent(ResultEvent event) {
						EventBus.publish(event);
					}
				});

		
		//XMLparsedEvent
		fmuController_
		.registerEventListener(
				XMLparsedEvent.class,
				new StraylightEventListener<XMLparsedEvent, XMLparsedInfo>() {
					@Override
					public void handleEvent(XMLparsedEvent event) {
						EventBus.publish(event);
					}
				});
		
		
		//ConfigChangeNotify
		fmuController_
		.registerEventListener(
				ConfigChangeNotify.class,
				new StraylightEventListener<ConfigChangeNotify, ConfigStruct>() {
					@Override
					public void handleEvent(ConfigChangeNotify event) {
						EventBus.publish(event);
					}
				});
	}
	
	
}

package com.sri.straylight.fmuWrapper.test.base;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
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
	private SimStateNative nextStateExpected_;
	
	private FMUcontroller fmuController_;
	
	/** The main barrier. */
	private CyclicBarrier mainBarrier_;
	
	final protected CyclicBarrier stateChangeBarrier_;
	
	protected CyclicBarrier xmlParseBarrier_;
	
	
	protected XMLparsedInfo xmlParsedInfo_;

	protected String threadName_;
	
	protected CountDownLatch stateCountDownLatch_;
	
	public BaseThread() {
		AnnotationProcessor.process(this);
		//mainBarrier_ = new CyclicBarrier(2);
		stateChangeBarrier_ = new CyclicBarrier(2);

	}
	
	
	public void init(FMUcontroller fmuController) {
		fmuController_ = fmuController;
		registerSimulationListeners_();
	}
	
	public void init(FMUcontroller fmuController, String threadName) {
		init(fmuController);
		threadName_ = threadName;
	}
	
	protected void requestStateChangeTo_ 
		(SimStateNative stateChangeRequest, SimStateNative nextStateExpected) 
	{
//		if (null == stateChangeBarrier_) {
//			stateChangeBarrier_ = new CyclicBarrier(2);
//		}
		stateCountDownLatch_ = new CountDownLatch(2);
		
		nextStateExpected_ = nextStateExpected;
		fmuController_.requestStateChange(stateChangeRequest);
		
    	debugStateBarrier_("requestStateChangeTo_:" + stateChangeRequest.toString());
    	
		awaitOnStateChangeBarrier();
		return;
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
	    
	    if (nextStateExpected_ == simStateNative) {
//	    	int numberWaiting = this.stateChangeBarrier_.getNumberWaiting();
//	    	
//	    	if (numberWaiting == 0) {
//				//throw new RuntimeException(e);
//				//e.printStackTrace();
//	    		//debug;
//				System.out.println ("numberWaiting == 0 ");
//	    	}
	    	
	    	debugStateBarrier_("onSimStateClientNotify:" + simStateNative.toString());
	    	
			awaitOnStateChangeBarrier();
	    } else {
	    	
			throw new RuntimeException();
	    }
	    

	}
	
	
	public void start() {


		super.start();

		//awaitOnMainBarrier();
	}
	
//	public void awaitOnMainBarrier() {
//		awaitOnBarrierLong(mainBarrier_);
//	}
	
	protected void awaitOnStateChangeBarrier() {
		//awaitOnBarrier(stateChangeBarrier_);
		//countDownLatch_
		
	      try {
	    	  stateCountDownLatch_.countDown();
	    	  stateCountDownLatch_.await();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	}
	
	
	public void cleanup() {

		unregisterSimulationListeners();
		fmuController_.forceCleanup();
	}
	
	
	
	/**
	 * Calls barrier.await and suppresses all its checked exceptions
	 *
	 * @param barrier the barrier
	 */
	protected void awaitOnBarrier(CyclicBarrier barrier) {
		try {

	    	debugStateBarrier_("awaitOnBarrier");
			int awaitResult = barrier.await(300, TimeUnit.SECONDS);
			String threadName = Thread.currentThread().getName();
			
			System.out.println ("### " + threadName + " - awaitResult:" + awaitResult);
			
			//barrier.reset();
			
	    	debugStateBarrier_("awaitOnBarrier_DONE");

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
		
		return;
	}
	
	private void debugStateBarrier_(String functionName) {
		
		String threadName = Thread.currentThread().getName();
		long numberWaiting = stateCountDownLatch_.getCount();
		System.out.println ("### " + threadName + ":" + functionName + "() - numberWaiting: " + numberWaiting);
		
		System.out.flush();
		
	}


	protected void awaitOnBarrierLong(CyclicBarrier barrier) {
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
		
		return;
	}
	
	
	public void unregisterSimulationListeners() {
		
		fmuController_.unregisterEventListener(SimStateNativeNotify.class);
		fmuController_.unregisterEventListener(MessageEvent.class);
		fmuController_.unregisterEventListener(ResultEvent.class);
		fmuController_.unregisterEventListener(XMLparsedEvent.class);
		fmuController_.unregisterEventListener(ConfigChangeNotify.class);
		
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
				}
			);
		

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

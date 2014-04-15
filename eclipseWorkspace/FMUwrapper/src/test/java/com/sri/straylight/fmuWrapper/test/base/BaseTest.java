package com.sri.straylight.fmuWrapper.test.base;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class BaseTest {
	
	protected SimStateNative blockThreadUntilState_;
	
	/** The main barrier. */
	protected final CyclicBarrier mainBarrier_ = new CyclicBarrier(2);
	
	
	public BaseTest() {
		AnnotationProcessor.process(this);
	}
	
	
	protected void awaitOnBarrier(CyclicBarrier barrier) {
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
	
	protected void awaitOnState(SimStateNative simStateNative) {
		
		blockThreadUntilState_ = simStateNative;
		awaitOnBarrier(mainBarrier_);
	}
	
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateNativeNotify.class)
	public void onSimStateNotify(SimStateNativeNotify event) {
		
		Thread t = Thread.currentThread();
	    String threadName = t.getName();
	    
	    assert (threadName.equals("name=AWT-EventQueue-0"));
	    
	    SimStateNative state = event.getPayload();
		//assert (state.equals(StateTransitions.nextStateExpected_) );
		
		if(state == blockThreadUntilState_) {
			
			awaitOnBarrier(mainBarrier_);
			System.out.println ("onSimStateNotify done");
		}

	}
}

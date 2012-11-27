package com.sri.straylight.fmuWrapper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.FMUcontroller;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


public class StateTransitions {

	/** The main barrier. */
	private final CyclicBarrier mainBarrier_ = new CyclicBarrier(2);
	
	/** The next state expected. */
	private static SimStateNative nextNativeStateExpected_;
	
	/** This is the rug that really ties the room together. */
	private FMUcontroller fmuController_;
	


	@Before
	public void setUp() throws Exception {
		AnnotationProcessor.process(this);

		FMUwrapperConfig config = FMUwrapperConfig.load();
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1", config.fmuFolderName);
		
		fmuController_ = new FMUcontroller(config);
		SimStateNative simStateNative  = fmuController_.getSimStateNative();
		assertEquals(SimStateNative.simStateNative_0_uninitialized, simStateNative);
		
		InitThread task = new InitThread();
		task.start();
		awaitOnBarrier(mainBarrier_);

		
		SimStateNative simStateNative2  = fmuController_.getSimStateNative();
		assertEquals(SimStateNative.simStateNative_3_ready, simStateNative2);
		
	}

	
	
	@After
	public void tearDown() throws Exception {
		
		TearDownThread task = new TearDownThread();
		task.start();
		awaitOnBarrier(mainBarrier_);
	}
	

	@Test
	public void testRun() {

		RunThread task = new RunThread();
		task.start();
		awaitOnBarrier(mainBarrier_);
		
		assertNotNull(task);
	}

	


	/**
	 * The Class InitThread.
	 */
	public class TearDownThread extends BaseThread
	{
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run()
		{
			Thread.currentThread().setName("TearDownThread");
			
			requestStateChange_(
					SimStateNative.simStateNative_7_terminate_requested,
					SimStateNative.simStateNative_7_terminate_completed
					);
			awaitOnBarrier(mainBarrier_);
		}
	}
	
	
	
	
	/**
	 * The Class InitThread.
	 */
	public class InitThread  extends BaseThread
	{


		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run()
		{
			Thread.currentThread().setName("InitThread");
			
			StateTransitions.nextNativeStateExpected_ = SimStateNative.simStateNative_1_connect_completed;
			

			try
			{
				fmuController_.connect();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			
			
			awaitOnBarrier(barrier);
			
			Thread t = Thread.currentThread();
		    String threadName = t.getName();
		    assert (threadName.equals("InitThread"));
		    
			StateTransitions.nextNativeStateExpected_ = SimStateNative.simStateNative_2_xmlParse_completed;
			fmuController_.xmlParse();
			awaitOnBarrier(barrier);
			
			
			requestStateChange_(
					SimStateNative.simStateNative_3_init_requested,
					SimStateNative.simStateNative_3_ready
					);
			
			awaitOnBarrier(mainBarrier_);
			
		}

	}
	
	
	/**
	 * The Class RunThread.
	 */
	public class RunThread  extends BaseThread
	{
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run()
		{
			Thread.currentThread().setName("RunThread");

			requestStateChange_(
					SimStateNative.simStateNative_5_step_requested,
					SimStateNative.simStateNative_3_ready
					);
			
			awaitOnBarrier(mainBarrier_);
			
		}
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
	
	
	public abstract class BaseThread  extends Thread {
		
		/** The barrier. */
		final CyclicBarrier barrier = new CyclicBarrier(2);
		
		public BaseThread(){
			AnnotationProcessor.process(this);
		}
		
		protected void requestStateChange_ (
				SimStateNative nativeStateRequest,
				SimStateNative nativeStateExpected
				) 
		{
			
			nextNativeStateExpected_ = nativeStateExpected;
			
			fmuController_.requestStateChange(nativeStateRequest);
			awaitOnBarrier(barrier);

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
			assert (state.equals(StateTransitions.nextNativeStateExpected_) );
			
			//SimStateNative simStateNative  = fmuController_.getSimStateNative();
			//assert (simStateNative.equals(StateTransitions.nextNativeStateExpected_) );
			
			if(state.equals(StateTransitions.nextNativeStateExpected_)) {
				awaitOnBarrier(barrier);
			}


		}
		
	}
	
}

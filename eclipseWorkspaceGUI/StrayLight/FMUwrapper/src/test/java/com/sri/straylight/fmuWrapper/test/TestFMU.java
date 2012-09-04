
package com.sri.straylight.fmuWrapper.test;




import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import junit.framework.TestCase;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.FMUcontroller;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateServerNotify;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.voManaged.ResultOfStep;
import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;

// TODO: Auto-generated Javadoc
/**
 * The Class TestFMU.
 */
public class TestFMU extends TestCase {
	
	
	/** The main barrier. */
	final CyclicBarrier mainBarrier = new CyclicBarrier(2);
	
	/** The next state expected. */
	private static SimStateServer nextStateExpected;
	
	/** The fmu controller_. */
	private FMUcontroller fmuController_;
	
	
	/**
	 * Test change input.
	 */
	public void testChangeInput() {
		
		AnnotationProcessor.process(this);
		
		FMUwrapperConfig config = FMUwrapperConfig.load();
		fmuController_ = new FMUcontroller(config);
		
		InitThread task = new InitThread();
		task.start();
		awaitOnBarrier(mainBarrier);
		

	}
	
	


	
	/**
	 * The Class InitThread.
	 */
	public class InitThread  extends Thread
	{
		
		
		/** The barrier. */
		final CyclicBarrier barrier = new CyclicBarrier(2);
		
		
		/**
		 * Instantiates a new inits the thread.
		 */
		public InitThread(){
			AnnotationProcessor.process(this);
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run()
		{
			Thread.currentThread().setName("InitThread");
			
			TestFMU.nextStateExpected = SimStateServer.simStateServer_1_connect_completed;
			
			
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
		    
		    TestFMU.nextStateExpected = SimStateServer.simStateServer_2_xmlParse_completed;
			fmuController_.xmlParse();
			awaitOnBarrier(barrier);
			
			TestFMU.nextStateExpected = SimStateServer.simStateServer_3_ready;
			fmuController_.init();
			awaitOnBarrier(barrier);
			//fmuController_.run();
			TestFMU.nextStateExpected = SimStateServer.simStateServer_3_ready;
			
			//fmuController_.requestStateChange(SimStateServer.simStateServer_5_step_requested);
			awaitOnBarrier(barrier);
			
			fmuController_.setScalarValueReal(56106, 22.5);
			//awaitOnBarrier(barrier);
			//fmuController_.requestStateChange(SimStateServer.simStateServer_5_step_requested);
			awaitOnBarrier(barrier);
			
			awaitOnBarrier(mainBarrier);
			
		}
		
		
		/**
		 * On xm lparsed event.
		 *
		 * @param event the event
		 */
		@EventSubscriber(eventClass=XMLparsedEvent.class)
		public void onXMLparsedEvent(XMLparsedEvent event) {
			
			Thread t = Thread.currentThread();
		    String threadName = t.getName();
		    assert (threadName.equals("name=AWT-EventQueue-0"));
		    
		    XMLparsed xmlParsed = event.xmlParsed;
			
			//assert (state.equals(ValueObjects.nextStateExpected) );
			//awaitOnBarrier(barrier);

		}
		
		/**
		 * On sim state notify.
		 *
		 * @param event the event
		 */
		@EventSubscriber(eventClass=SimStateServerNotify.class)
		public void onSimStateNotify(SimStateServerNotify event) {
			Thread t = Thread.currentThread();
		    String threadName = t.getName();
		    
		    assert (threadName.equals("name=AWT-EventQueue-0"));
		    
			SimStateServer state = event.getPayload();
			
			assert (state.equals(TestFMU.nextStateExpected) );
			awaitOnBarrier(barrier);

		}
		
		
		/**
		 * On result event.
		 *
		 * @param event the event
		 */
		@EventSubscriber(eventClass=ResultEvent.class)
		public void onResultEvent(ResultEvent event) {
			
			Thread t = Thread.currentThread();
		    String threadName = t.getName();
		    
		    assert (threadName.equals("name=AWT-EventQueue-0"));
		    ResultOfStep resultOfStep = event.resultOfStep;
		    

		    
		    String str1 = resultOfStep.inputToString();
		    System.out.println("input=" + str1);
		    
		    String str2 = resultOfStep.toString();
		    System.out.println("output=" + str2);
		    
		    
		    awaitOnBarrier(barrier);
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
	
	
	
	
}


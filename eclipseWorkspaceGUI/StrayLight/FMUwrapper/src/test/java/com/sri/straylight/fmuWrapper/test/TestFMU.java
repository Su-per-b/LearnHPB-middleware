
package com.sri.straylight.fmuWrapper.test;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.sri.straylight.fmuWrapper.FMU;
import com.sri.straylight.fmuWrapper.State;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;



/**
 * Unit test for FMU inputs
 */
public class TestFMU 
extends TestCase
{
	private static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
	private static String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
	private static String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";

	public void testMultiThreadedInit() {

		FMU fmu = new FMU(testFmuFile, nativeLibFolder);

		assert (fmu.getFmuState() == State.fmuState_level_0_uninitialized);
		final CyclicBarrier finishBarrier = new CyclicBarrier(2);  // +1 for the main thread


		fmu.fmuEventDispatacher.addListener(
				new FMUeventListener() {

					@Override
					public void onResultEvent(ResultEvent event) {
					}

					@Override
					public void onMessageEvent(MessageEvent event) {
						System.out.print(event.messageStruct.msgText);
					}

					@Override
					public void onInitializedEvent(InitializedEvent event) {
						awaitOnBarrier(finishBarrier, 60);
					}

					@Override
					public void onFMUstateEvent(FMUstateEvent event) {
						// TODO Auto-generated method stub

					}
				}

			);
		


		InitThread task = new InitThread(fmu);
		task.start();


		try {
			finishBarrier.await();
		} catch (InterruptedException
				| BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert (fmu.getFmuState() == State.fmuState_level_5_initializedFMU);

	}


	class InitThread extends  Thread  
	{
		private FMU fmu_;

		public InitThread(FMU fmu){
			fmu_ = fmu;
		}

		public void run()
		{
			Thread.currentThread().setName("Task FMU init");
			fmu_.init_1();
			fmu_.init_2(unzipFolder);
			fmu_.init_3(); 
		}


	}



	/** Calls barrier.await and supresses all its checked exceptions */
	private void awaitOnBarrier(CyclicBarrier barrier, int timeoutSeconds) {
		try {
			barrier.await(timeoutSeconds, TimeUnit.SECONDS);
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


	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public TestFMU( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{

		return new TestSuite( TestFMU.class );
	}




}

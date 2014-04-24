package com.sri.straylight.fmuWrapper.test.main;

import static org.junit.Assert.assertEquals;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.Controller.FMUcontroller;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.test.base.BaseThread;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class FMUcontrrollerT {


	final private String pathToFMUfolder_ = "E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test";

	
	/** This is the rug that really ties the room together. */
	private static FMUcontroller fmuController_;
	
	private static FMUwrapperConfig fmuWrapperConfig_;
	
	@BeforeClass
	public static void beforeClass() {
		
		
		fmuWrapperConfig_ = FMUwrapperConfig.load("fmuwrapper-config-for-unit-tests.xml");
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_test", fmuWrapperConfig_.fmuFolderName);
		fmuController_ = new FMUcontroller();
		

		fmuController_.setConcurrency(false);
		
		
	}

	
	//runs before each test
	@Before
	public void beforeEachTest() {
		
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		
	}
	
	
	//runs after each test
	@After
	public void afterEachTest() {
		
		
		fmuController_.forceCleanup();

		 
	}
	
	
	private void assertSimState_(SimStateNative simStateNativeExpected) {
		
		SimStateNative simStateNativeActual  = fmuController_.getSimStateNative();
		assertEquals(simStateNativeExpected, simStateNativeActual);
		
	}
	
	
	
	@Test
	public void fmuWrapperConfig() {
		
		
		assertEquals("E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test", fmuWrapperConfig_.fmuFolderAbsolutePath);
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_test", fmuWrapperConfig_.fmuFolderName);
		assertEquals("Client-Debug", fmuWrapperConfig_.id);
		assertEquals("E:\\LHPB\\LearnHPB-middleware\\visualStudioWorkspace\\bin\\Debug", fmuWrapperConfig_.nativeLibFolderAbsolutePath);
		assertEquals("\\..\\..\\visualStudioWorkspace\\bin\\Debug", fmuWrapperConfig_.nativeLibFolderRelativePath);
		assertEquals(500, (int)fmuWrapperConfig_.parseInternalVariableLimit);
		assertEquals("500", fmuWrapperConfig_.parseInternalVariableLimitStr);

		assertEquals(false, fmuWrapperConfig_.parseInternalVariablesFlag);
		assertEquals("false", fmuWrapperConfig_.parseInternalVariablesFlagStr);

		assertEquals("E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs", fmuWrapperConfig_.unzipFolderAbsolutePath);
		assertEquals("\\..\\..\\assets\\FMUs\\", fmuWrapperConfig_.unzipFolderRelativePath);
		
		return;		
	}
	
	
	
	@Test
	public void instantiateFMUcontroller() throws Exception {
		AnnotationProcessor.process(this);

		assertSimState_(SimStateNative.simStateNative_0_uninitialized);

	}
	
	

	@Test
	public void fmuConnect() throws Exception {
		
		FMUconnectThread thread = new FMUconnectThread();
		
		thread.init(fmuController_);
		thread.start();
		thread.awaitOnMainBarrier();
		
		return;
		
	}

	
	@Test
	public void fmuXmlParse() throws Exception {
		
		
		FMUconnectThread thread = new FMUconnectThread();
		
		thread.init(fmuController_);
		thread.start();
		thread.awaitOnMainBarrier();
		fmuController_.unregisterAllEventListener();
		
		thread.join();
		fmuController_.forceCleanup();

		
		fmuController_ = new FMUcontroller();
		fmuController_.setConcurrency(false);
		
		
		//fmuWrapperConfig_ = FMUwrapperConfig.load("fmuwrapper-config-for-unit-tests.xml");
		//assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_test", fmuWrapperConfig_.fmuFolderName);
		//fmuController_ = new FMUcontroller();
		
		//assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		//fmuController_.setConcurrency(false);
		
		
		FMUxmlParse thread2 = new FMUxmlParse();
		thread2.init(fmuController_);
		thread2.start();

		thread2.awaitOnMainBarrier();
		
		return;
	}
	
	
	
	
	
	
	private class FMUconnectThread extends BaseThread
	{

		public void run()
		
		{
			Thread.currentThread().setName("FMUconnectThread");

			
			requestStateChangeTo_(
					SimStateNative.simStateNative_1_connect_requested,
					SimStateNative.simStateNative_1_connect_completed
				);
			
			System.out.println ("FMUconnectThread done");
			
			awaitOnMainBarrier();
			return;
		}

	}
	
	
	


	
	
	private class FMUxmlParse extends BaseThread
	{

		public void run()
		
		{
			Thread.currentThread().setName("FMUxmlParse");
			
			stateChangeBarrier_.reset();
			
			
//			requestStateChangeTo_(
//					SimStateNative.simStateNative_1_connect_requested,
//					SimStateNative.simStateNative_1_connect_completed
//					);
			


//			requestStateChangeTo_(
//					SimStateNative.simStateNative_2_xmlParse_requested,
//					SimStateNative.simStateNative_2_xmlParse_completed
//					);
			
			awaitOnMainBarrier();
			
			
			System.out.println ("FMUxmlParse done");
		}


	}
	
	
	

	

	
}




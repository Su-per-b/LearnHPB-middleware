package com.sri.straylight.fmuWrapper.test;

import static org.junit.Assert.assertEquals;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.junit.Test;

import com.sri.straylight.fmuWrapper.Controller.FMUcontroller;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.test.base.BaseThread;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class FMUcontrrollerTest {


	final private String pathToFMUfolder_ = "E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test";

	
	/** This is the rug that really ties the room together. */
	private FMUcontroller fmuController_;
	
	
	

	@Test
	public void fmuWrapperConfig() {
		
		FMUwrapperConfig fmuWrapperConfig = FMUwrapperConfig.load("fmuwrapper-config-for-unit-tests.xml");
		
		assertEquals("E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test", fmuWrapperConfig.fmuFolderAbsolutePath);
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_test", fmuWrapperConfig.fmuFolderName);
		assertEquals("Client-Debug", fmuWrapperConfig.id);
		assertEquals("E:\\LHPB\\LearnHPB-middleware\\visualStudioWorkspace\\bin\\Debug", fmuWrapperConfig.nativeLibFolderAbsolutePath);
		assertEquals("\\..\\..\\visualStudioWorkspace\\bin\\Debug", fmuWrapperConfig.nativeLibFolderRelativePath);
		assertEquals(500, (int)fmuWrapperConfig.parseInternalVariableLimit);
		assertEquals("500", fmuWrapperConfig.parseInternalVariableLimitStr);

		assertEquals(false, fmuWrapperConfig.parseInternalVariablesFlag);
		assertEquals("false", fmuWrapperConfig.parseInternalVariablesFlagStr);

		assertEquals("E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs", fmuWrapperConfig.unzipFolderAbsolutePath);
		assertEquals("\\..\\..\\assets\\FMUs\\", fmuWrapperConfig.unzipFolderRelativePath);
		
		return;		
	}
	
	
	
	@Test
	public void instantiateFMUcontroller() throws Exception {
		AnnotationProcessor.process(this);

		FMUwrapperConfig config = FMUwrapperConfig.load("fmuwrapper-config-for-unit-tests.xml");
		
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_test", config.fmuFolderName);
		
		FMUcontroller fmuController = new FMUcontroller();
		
		SimStateNative simStateNative  = fmuController.getSimStateNative();
		assertEquals(SimStateNative.simStateNative_0_uninitialized, simStateNative);

	}
	
	

	@Test
	public void fmuConnect() throws Exception {
		
		AnnotationProcessor.process(this);

		FMUwrapperConfig config = FMUwrapperConfig.load("fmuwrapper-config-for-unit-tests.xml");
		
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_test", config.fmuFolderName);
		
		FMUcontroller fmuController = new FMUcontroller();
		fmuController.setConcurrency(false);
		
		
		SimStateNative simStateNative  = fmuController.getSimStateNative();
		assertEquals(SimStateNative.simStateNative_0_uninitialized, simStateNative);
		
		FMUconnectThread thread = new FMUconnectThread();
		thread.init(fmuController);
		
		thread.start();

		thread.awaitOnMainBarrier();
		
		fmuController.forceCleanup();
		System.out.println ("fmuConnect done");
	}

	
	@Test
	public void fmuXmlParse() throws Exception {
		
		AnnotationProcessor.process(this);

		FMUwrapperConfig config = FMUwrapperConfig.load("fmuwrapper-config-for-unit-tests.xml");
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_test", config.fmuFolderName);

		FMUcontroller fmuController = new FMUcontroller();
		fmuController.setConcurrency(false);
		
		SimStateNative simStateNative  = fmuController.getSimStateNative();
		assertEquals(SimStateNative.simStateNative_0_uninitialized, simStateNative);
		
		FMUxmlParse thread = new FMUxmlParse();
		thread.init(fmuController);
		thread.start();

		thread.awaitOnMainBarrier();
		
		fmuController.forceCleanup();
		System.out.println ("fmuInit done");
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
		}

	}
	
	
	


	
	
	private class FMUxmlParse extends BaseThread
	{

		public void run()
		
		{
			Thread.currentThread().setName("FMUinitThread");
			
			requestStateChangeTo_(
					SimStateNative.simStateNative_1_connect_requested,
					SimStateNative.simStateNative_1_connect_completed
					);
			

			requestStateChangeTo_(
					SimStateNative.simStateNative_2_xmlParse_requested,
					SimStateNative.simStateNative_2_xmlParse_completed
					);
			
			awaitOnMainBarrier();
			
			
			System.out.println ("FMUinitThread done");
		}


	}
	
	
	

	

	
}




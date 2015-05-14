package com.sri.straylight.fmuWrapper.test.main;

import static org.junit.Assert.assertEquals;

import java.util.Vector;
import java.util.concurrent.CyclicBarrier;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sri.straylight.fmuWrapper.Controller.FMUcontroller;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.test.base.BaseThread;
import com.sri.straylight.fmuWrapper.test.base.OrderedRunner;
import com.sri.straylight.fmuWrapper.voManaged.InitialState;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.SerializableVector;
import com.sri.straylight.fmuWrapper.voManaged.StringPrimitive;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


@RunWith(OrderedRunner.class)
public class FMUcontrollerTests {


//	final private String pathToFMUfolder_ = "E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test";

	
	/** This is the rug that really ties the room together. */
	private static FMUcontroller fmuController_;
	
	private static FMUwrapperConfig fmuWrapperConfig_;
	
	@BeforeClass
	public static void beforeClass() {
		
		

		
	}

	
	//runs before each test
	@Before
	public void beforeEachTest() {
		
		fmuWrapperConfig_ = FMUwrapperConfig.load("fmuwrapper-config-for-unit-tests.xml");
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_test", fmuWrapperConfig_.fmuFolderName);
		fmuController_ = new FMUcontroller();
		

		fmuController_.setConcurrency(false);
		
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		
	}
	
	
	//runs after each test
	@After
	public void afterEachTest() {
		
		//fmuController_.forceCleanup();

	}
	
	
	private void assertSimState_(SimStateNative simStateNativeExpected) {
		
		SimStateNative simStateNativeActual  = fmuController_.getSimStateNative();
		assertEquals(simStateNativeExpected, simStateNativeActual);
		
	}
	
	
	
	@Test
	public void T01_fmuWrapperConfig() {
		
		
		assertEquals("E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test", fmuWrapperConfig_.fmuFolderAbsolutePath);
		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_test", fmuWrapperConfig_.fmuFolderName);
		assertEquals("Client-Debug", fmuWrapperConfig_.id);
		assertEquals("E:\\LHPB\\LearnHPB-middleware\\visualStudioWorkspace\\bin\\Debug", fmuWrapperConfig_.nativeLibFolderAbsolutePath);
		assertEquals("\\..\\..\\visualStudioWorkspace\\bin\\Debug", fmuWrapperConfig_.nativeLibFolderRelativePath);
		
		assertEquals("500", fmuWrapperConfig_.parseInternalVariableLimitStr);
		assertEquals(500, (int)fmuWrapperConfig_.parseInternalVariableLimit);

		assertEquals("500", fmuWrapperConfig_.msPerTimeStepStr);
		assertEquals(500, (int)fmuWrapperConfig_.msPerTimeStep);
		
		assertEquals("5", fmuWrapperConfig_.scalarVariablePrecisionStr);
		assertEquals(5, (int)fmuWrapperConfig_.scalarVariablePrecision);
		
		assertEquals(false, fmuWrapperConfig_.parseInternalVariablesFlag);
		assertEquals("false", fmuWrapperConfig_.parseInternalVariablesFlagStr);

		assertEquals("E:\\LHPB\\LearnHPB-middleware\\assets\\FMUs", fmuWrapperConfig_.unzipFolderAbsolutePath);
		assertEquals("\\..\\..\\assets\\FMUs\\", fmuWrapperConfig_.unzipFolderRelativePath);
		
		
		return;
	}
	
	
	
	@Test
	public void T02_instantiateFMUcontroller() throws Exception {
		//AnnotationProcessor.process(this);

		assertSimState_(SimStateNative.simStateNative_0_uninitialized);

	}
	

	

	
//	@Test
//	public void T00a_fmuXmlParse() throws Exception {
//		
//	
//		//Thread.sleep(5000);
//		
//		XMLParseThread thread = new XMLParseThread();
//		XMLParseThread thread2 = new XMLParseThread();
//		
//		thread.init(fmuController_, "XMLParseThread 1");
//		
//		thread.start();
//		//thread.cleanup();
//		thread.join();
//		//thread.cleanup();
//		//assertSimState_(SimStateNative.simStateNative_0_uninitialized);
//		//thread.
//		
//		Thread.sleep(2000);
//		
//		fmuController_ = new FMUcontroller();
//		fmuController_.setConcurrency(false);
//		
//		
//		thread2.init(fmuController_, "XMLParseThread 2");
//		thread2.start();
//		
//		//thread2.cleanup();
//		thread2.join();
//		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
//		
//
//	}
	
	@Test
	public void T00b_fmuXmlParse() throws Exception {
		
	
		//Thread.sleep(5000);
		
		XMLParseThread thread = new XMLParseThread();
		thread.init(fmuController_);
		thread.start();
		
		thread.cleanup();
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);

	}
	
	
	@Test
	public void T05_setOutputVarList() throws Exception {
		

		SetOutputVarList thread = new SetOutputVarList();
		thread.init(fmuController_);
		thread.start();
		
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		thread.cleanup();
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);

		
	}
	
	
	@Test
	public void T04c_dontSetOutputVarList() throws Exception {
		

		DontSetOutputVarList thread = new DontSetOutputVarList();
		thread.init(fmuController_);
		thread.start();
		
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		thread.cleanup();
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		
	}
	
	
	@Test
	public void T07_setInputVarList() throws Exception {
		
		SetInputVarList thread = new SetInputVarList();
		thread.init(fmuController_);
		thread.start();
		
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		thread.cleanup();
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		
	}
	
	
	@Test
	public void T08_setInputAndOutputVarList() throws Exception {
		

		SetInputAndOutputVarList thread = new SetInputAndOutputVarList();
		thread.init(fmuController_);
		thread.start();
		
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		thread.cleanup();
		assertSimState_(SimStateNative.simStateNative_0_uninitialized);
		
	}
	
	
	
	
	
	private class XMLParseThread extends BaseThread
	{

		public void run()
		
		{
			
			Thread.currentThread().setName(threadName_);
			
			requestStateChangeTo_(
					SimStateNative.simStateNative_1_connect_requested,
					SimStateNative.simStateNative_1_connect_completed
				);
			

			//System.out.println ("XMLParseThread connect_completed");
			

			requestStateChangeTo_(
				SimStateNative.simStateNative_2_xmlParse_requested,
				SimStateNative.simStateNative_2_xmlParse_completed
			);
			
//			
//			requestStateChangeTo_(
//					SimStateNative.simStateNative_8_tearDown_requested,
//					SimStateNative.simStateNative_0_uninitialized
//				);
			
			
			this.unregisterSimulationListeners();
			//awaitOnMainBarrier();
			this.cleanup();
			
			System.out.println (threadName_ + " done");
		}



		
//		@EventSubscriber(eventClass=XMLparsedEvent.class)
//		public void onXMLparsedEvent(XMLparsedEvent event) {
//			
//			String threadName = Thread.currentThread().getName();
//			
//		    Assert.assertEquals("AWT-EventQueue-0", threadName);
//		    xmlParsedInfo_ = event.getPayload();
//			awaitOnBarrier(xmlParseBarrier_);
//			
//		    return;
//		}
		
	}
	
	
	
	private class SetInputVarList extends BaseThread
	{

		public void run()
		
		{
			
			Thread.currentThread().setName("SetInputVarList");
			
			requestStateChangeTo_(
					SimStateNative.simStateNative_1_connect_requested,
					SimStateNative.simStateNative_1_connect_completed
				);
			

			System.out.println ("SetInputVarList connect_completed");

			
			SerializableVector<StringPrimitive> serializableVector_0 = 
					new SerializableVector<StringPrimitive>("StringPrimitive");
			
			serializableVector_0.add(new StringPrimitive("u_ZN[14]"));
			serializableVector_0.add(new StringPrimitive("u_ZN[17]"));
			
			InitialState initialState = new InitialState();
			initialState.setInputVarList(serializableVector_0);
			
			fmuController_.setInitialState(initialState);
			xmlParseBarrier_ = new CyclicBarrier(2);
			
			requestStateChangeTo_(
				SimStateNative.simStateNative_2_xmlParse_requested,
				SimStateNative.simStateNative_2_xmlParse_completed
			);
			
			awaitOnBarrier(xmlParseBarrier_);
			
			Vector<ScalarVariableReal> inputVariables = this.xmlParsedInfo_.getInputVariables();
			int len = inputVariables.size();
			assertEquals(2, len);
			
			ScalarVariableReal scalarVariableReal_0 = inputVariables.get(0);
			assertEquals("u_ZN[14]", scalarVariableReal_0.getName());
			assertEquals(56973, scalarVariableReal_0.getIdx());
			
			ScalarVariableReal scalarVariableReal_1 = inputVariables.get(1);
			assertEquals("u_ZN[17]", scalarVariableReal_1.getName());
			assertEquals(56976, scalarVariableReal_1.getIdx());
			
//			awaitOnMainBarrier();
			
			System.out.println ("SetInputVarList done");
			
		}
		
		
		@EventSubscriber(eventClass=XMLparsedEvent.class)
		public void onXMLparsedEvent(XMLparsedEvent event) {
			
			String threadName = Thread.currentThread().getName();
			
		    Assert.assertEquals("AWT-EventQueue-0", threadName);
		    xmlParsedInfo_ = event.getPayload();
			awaitOnBarrier(xmlParseBarrier_);
			
		    return;
		}
		
	}
		
	
	
	
	
	
	
	
	private class SetInputAndOutputVarList extends BaseThread
	{

		public void run()
		
		{
			
			Thread.currentThread().setName("SetInputAndOutputVarList");
			
			requestStateChangeTo_(
					SimStateNative.simStateNative_1_connect_requested,
					SimStateNative.simStateNative_1_connect_completed
				);
			
			System.out.println ("SetInputAndOutputVarList connect_completed");
			SerializableVector<StringPrimitive> serializableVector_0 = 
					new SerializableVector<StringPrimitive>("StringPrimitive");
			
			serializableVector_0.add(new StringPrimitive("y_ZN[1]"));
			serializableVector_0.add(new StringPrimitive("y_ZN[5]"));
			
			InitialState initialState = new InitialState();
			initialState.setOutputVarList(serializableVector_0);

			
			
			SerializableVector<StringPrimitive> serializableVector_1 = 
					new SerializableVector<StringPrimitive>("StringPrimitive");
			
			serializableVector_1.add(new StringPrimitive("u_ZN[14]"));
			serializableVector_1.add(new StringPrimitive("u_ZN[17]"));
			
			initialState.setInputVarList(serializableVector_1);
			fmuController_.setInitialState(initialState);
			
			xmlParseBarrier_ = new CyclicBarrier(2);
			
			requestStateChangeTo_(
				SimStateNative.simStateNative_2_xmlParse_requested,
				SimStateNative.simStateNative_2_xmlParse_completed
			);
			
			awaitOnBarrier(xmlParseBarrier_);
			
			Vector<ScalarVariableReal> outputVariables = this.xmlParsedInfo_.getOutputVariables();
			int len = outputVariables.size();
			assertEquals(2, len);
			
			ScalarVariableReal scalarVariableReal_0 = outputVariables.get(0);
			assertEquals("y_ZN[1]", scalarVariableReal_0.getName());
			assertEquals(62271, scalarVariableReal_0.getIdx());
			
			ScalarVariableReal scalarVariableReal_1 = outputVariables.get(1);
			assertEquals("y_ZN[5]", scalarVariableReal_1.getName());
			assertEquals(62275, scalarVariableReal_1.getIdx());
			
			
			Vector<ScalarVariableReal> inputVariables = this.xmlParsedInfo_.getInputVariables();
			int len1 = inputVariables.size();
			assertEquals(2, len1);
			
			ScalarVariableReal scalarVariableReal_2 = inputVariables.get(0);
			assertEquals("u_ZN[14]", scalarVariableReal_2.getName());
			assertEquals(56973, scalarVariableReal_2.getIdx());
			
			ScalarVariableReal scalarVariableReal_3 = inputVariables.get(1);
			assertEquals("u_ZN[17]", scalarVariableReal_3.getName());
			assertEquals(56976, scalarVariableReal_3.getIdx());
			

//			awaitOnMainBarrier();
			
			System.out.println ("SetInputAndOutputVarList done");
		}
		
		
		@EventSubscriber(eventClass=XMLparsedEvent.class)
		public void onXMLparsedEvent(XMLparsedEvent event) {
			
			String threadName = Thread.currentThread().getName();
			
		    Assert.assertEquals("AWT-EventQueue-0", threadName);
		    xmlParsedInfo_ = event.getPayload();
			awaitOnBarrier(xmlParseBarrier_);
			
		    return;
		}
	}
	
	
	private class SetOutputVarList extends BaseThread
	{

		public void run()
		
		{
			
			Thread.currentThread().setName("SetOutputVarList");
			
			requestStateChangeTo_(
					SimStateNative.simStateNative_1_connect_requested,
					SimStateNative.simStateNative_1_connect_completed
				);
			

			System.out.println ("SetOutputVarList connect_completed");

			
			SerializableVector<StringPrimitive> serializableVector_0 = 
					new SerializableVector<StringPrimitive>("StringPrimitive");
			
			serializableVector_0.add(new StringPrimitive("y_ZN[1]"));
			serializableVector_0.add(new StringPrimitive("y_ZN[5]"));
			
			InitialState initialState = new InitialState();
			initialState.setOutputVarList(serializableVector_0);
			
			fmuController_.setInitialState(initialState);
			xmlParseBarrier_ = new CyclicBarrier(2);
			
			requestStateChangeTo_(
				SimStateNative.simStateNative_2_xmlParse_requested,
				SimStateNative.simStateNative_2_xmlParse_completed
			);
			
			awaitOnBarrier(xmlParseBarrier_);
			
			Vector<ScalarVariableReal> outputVariables = this.xmlParsedInfo_.getOutputVariables();
			int len = outputVariables.size();
			assertEquals(2, len);
			
			ScalarVariableReal scalarVariableReal_0 = outputVariables.get(0);
			assertEquals("y_ZN[1]", scalarVariableReal_0.getName());
			assertEquals(62271, scalarVariableReal_0.getIdx());
			
			ScalarVariableReal scalarVariableReal_1 = outputVariables.get(1);
			assertEquals("y_ZN[5]", scalarVariableReal_1.getName());
			assertEquals(62275, scalarVariableReal_1.getIdx());
			
			
//			awaitOnMainBarrier();
			
			System.out.println ("SetOutputVarList done");
		}
		
		@EventSubscriber(eventClass=XMLparsedEvent.class)
		public void onXMLparsedEvent(XMLparsedEvent event) {
			
			String threadName = Thread.currentThread().getName();
			
		    Assert.assertEquals("AWT-EventQueue-0", threadName);
		    xmlParsedInfo_ = event.getPayload();
			awaitOnBarrier(xmlParseBarrier_);
			
		    return;
		}
	}
		
		
		private class DontSetOutputVarList extends BaseThread
		{

			public void run()
			
			{
				
				Thread.currentThread().setName("DontSetOutputVarList");
				
				requestStateChangeTo_(
						SimStateNative.simStateNative_1_connect_requested,
						SimStateNative.simStateNative_1_connect_completed
					);
				

				System.out.println ("DontSetOutputVarList connect_completed");
				xmlParseBarrier_ = new CyclicBarrier(2);
				
				requestStateChangeTo_(
					SimStateNative.simStateNative_2_xmlParse_requested,
					SimStateNative.simStateNative_2_xmlParse_completed
				);
				
				awaitOnBarrier(xmlParseBarrier_);
				
				Vector<ScalarVariableReal> outputVariables = this.xmlParsedInfo_.getOutputVariables();
				int len = outputVariables.size();
				assertEquals(138, len);
				
				ScalarVariableReal scalarVariableReal_0 = outputVariables.get(0);
				assertEquals("y_BOI[1]", scalarVariableReal_0.getName());
				assertEquals(61807, scalarVariableReal_0.getIdx());
				
				ScalarVariableReal scalarVariableReal_1 = outputVariables.get(1);
				assertEquals("y_BOI[2]", scalarVariableReal_1.getName());
				assertEquals(61808, scalarVariableReal_1.getIdx());
				
				
				assertSimState_(SimStateNative.simStateNative_2_xmlParse_completed);
				
				requestStateChangeTo_(
						SimStateNative.simStateNative_8_tearDown_requested,
						SimStateNative.simStateNative_0_uninitialized
					);
				
				assertSimState_(SimStateNative.simStateNative_0_uninitialized);
//				awaitOnMainBarrier();
//				
				System.out.println ("DontSetOutputVarList done");
			}
			

		
			@EventSubscriber(eventClass=XMLparsedEvent.class)
			public void onXMLparsedEvent(XMLparsedEvent event) {
				
				String threadName = Thread.currentThread().getName();
				
			    Assert.assertEquals("AWT-EventQueue-0", threadName);
			    xmlParsedInfo_ = event.getPayload();
				awaitOnBarrier(xmlParseBarrier_);
				
			    return;
			}
		
	}
	
}
	




//@Test
//public void T99_fmuConnect() throws Exception {
//	
//	ConnectThread thread = new ConnectThread();
//	
//	thread.init(fmuController_);
//	thread.start();
//
//	
//	//assertEquals(SimStateNative.simStateNative_1_connect_completed, fmuController_.getSimStateNative());
//	thread.cleanup();
//	assertEquals(SimStateNative.simStateNative_0_uninitialized, fmuController_.getSimStateNative());
//	//Thread.sleep(1000);
//}


//private class ConnectThread extends BaseThread
//{
//
//	public void run()
//	
//	{
//		Thread.currentThread().setName("ConnectThread");
//	
//		requestStateChangeTo_(
//				SimStateNative.simStateNative_1_connect_requested,
//				SimStateNative.simStateNative_1_connect_completed
//			);
//		
//		requestStateChangeTo_(
//				SimStateNative.simStateNative_8_tearDown_requested,
//				SimStateNative.simStateNative_0_uninitialized
//			);
//		
//		System.out.println ("ConnectThread done");
//		
//		awaitOnMainBarrier();
//		
//		System.out.println ("ConnectThread done");
//		return;
//	}
//
//}
//



	





package com.sri.straylight.fmuWrapper.test.main;



public class StateTransitionTests {

//	/** The main barrier. */
//	private final CyclicBarrier mainBarrier_ = new CyclicBarrier(2);
//	
//	/** The next state expected. */
//	private static SimStateNative blockThreadUntilState_;
//	
//	/** This is the rug that really ties the room together. */
//	private FMUcontroller fmuController_;
//	
//
//
//	@Before
//	public void setUp() throws Exception {
//		AnnotationProcessor.process(this);
//
//		FMUwrapperConfig config = FMUwrapperConfig.load("fmuwrapper-config-for-unit-tests.xml");
//		
//		assertEquals("LearnGB_0v4_02_VAVReheat_ClosedLoop_edit2", config.fmuFolderName);
//		
//		fmuController_ = new FMUcontroller();
//		
//		SimStateNative simStateNative  = fmuController_.getSimStateNative();
//		assertEquals(SimStateNative.simStateNative_0_uninitialized, simStateNative);
//		
//		SetupThread thread = new SetupThread();
//		thread.start();
//		
//		awaitOnBarrier(mainBarrier_);
//		
//		System.out.println ("setUp done");
//	}
//
//	
//	
//	@After
//	public void tearDown() throws Exception {
//		
//		TearDownThread task = new TearDownThread();
//		task.start();
//		awaitOnBarrier(mainBarrier_);
//		
//		System.out.println ("tearDown done");
//	}
//	
//
//	@Test
//	public void step() {
//
//		StepThread task = new StepThread();
//		task.start();
//		
//		awaitOnBarrier(mainBarrier_);
//		
//		assertNotNull(task);
//		System.out.println ("step done");
//	}
//
//	
//
//
//
//
//	public class TearDownThread extends BaseThread
//	{
//		/* (non-Javadoc)
//		 * @see java.lang.Thread#run()
//		 */
//		public void run()
//		{
//			Thread.currentThread().setName("TearDownThread");
//			
//
//			StateTransitionTests.blockThreadUntilState_ = SimStateNative.simStateNative_7_terminate_completed;
//			fmuController_.requestStateChange(SimStateNative.simStateNative_7_terminate_requested);
//			
//			awaitOnBarrier(mainBarrier_);
//			System.out.println ("TearDownThread done");
//		}
//	}
//	
//	
//	
//
//	public class SetupThread extends BaseThread
//	{
//
//		public void run()
//		{
//			Thread.currentThread().setName("SetupThread");
//			
//			StateTransitionTests.blockThreadUntilState_ = SimStateNative.simStateNative_1_connect_completed;
//			
//
//			fmuController_.requestStateChange(SimStateNative.simStateNative_1_connect_requested);
//			awaitOnBarrier(barrier);
//			
//			Thread t = Thread.currentThread();
//		    String threadName = t.getName();
//		    
//		    assert (threadName.equals("SetupThread"));
//		    
//			StateTransitionTests.blockThreadUntilState_ = SimStateNative.simStateNative_2_xmlParse_completed;
//			
//
//			fmuController_.requestStateChange(SimStateNative.simStateNative_2_xmlParse_requested);
//			
//			awaitOnBarrier(barrier);
//			
//			
//			requestStateChange_(
//					SimStateNative.simStateNative_3_init_requested,
//					SimStateNative.simStateNative_3_ready
//					);
//			
//			awaitOnBarrier(mainBarrier_);
//			
//			System.out.println ("InitThread done");
//		}
//
//	}
//	
//	
//	/**
//	 * The Class RunThread.
//	 */
//	public class StepThread  extends BaseThread
//	{
//		
//		public void run()
//		{
//			Thread.currentThread().setName("StepThread");
//			Thread.currentThread().setName("InitThread");
//			StateTransitionTests.blockThreadUntilState_ = SimStateNative.simStateNative_3_ready;
//			
//			fmuController_.requestStateChange(SimStateNative.simStateNative_5_step_requested);
//			awaitOnBarrier(mainBarrier_);
//			System.out.println ("RunThread done");
//		}
//	}
//	
//	
//	
//	/**
//	 * Calls barrier.await and supresses all its checked exceptions
//	 *
//	 * @param barrier the barrier
//	 */
//	private void awaitOnBarrier(CyclicBarrier barrier) {
//		try {
//			barrier.await(500, TimeUnit.SECONDS);
//		}
//		catch (InterruptedException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//		catch (BrokenBarrierException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//		catch (TimeoutException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//	
//	
//	public abstract class BaseThread  extends Thread {
//		
//		/** The barrier. */
//		final CyclicBarrier barrier = new CyclicBarrier(2);
//		
//		public BaseThread(){
//			AnnotationProcessor.process(this);
//		}
//		
//		protected void requestStateChange_ 
//			(SimStateNative stateRequested, SimStateNative stateExpected) 
//		{
//			blockThreadUntilState_ = stateExpected;
//			fmuController_.requestStateChange(stateRequested);
//			awaitOnBarrier(barrier);
//			
//			System.out.println ("requestStateChange_ done");
//		}
//		
//		
//		/**
//		 * On sim state notify.
//		 *
//		 * @param event the event
//		 */
//		@EventSubscriber(eventClass=SimStateNativeNotify.class)
//		public void onSimStateNotify(SimStateNativeNotify event) {
//			
//			Thread t = Thread.currentThread();
//		    String threadName = t.getName();
//		    
//		    assert (threadName.equals("name=AWT-EventQueue-0"));
//		    
//		    SimStateNative state = event.getPayload();
//			//assert (state.equals(StateTransitions.nextStateExpected_) );
//			
//			if(state == StateTransitionTests.blockThreadUntilState_) {
//				
//				awaitOnBarrier(barrier);
//				System.out.println ("onSimStateNotify done");
//			}
//
//		}
//		
//	}
	
}

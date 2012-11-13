package com.sri.straylight.socketserver.controller;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.event.SimStateWrapperNotify;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.socketserver.SocketServer;
import com.sri.straylight.socketserver.event.MessageIn;


public class MainController extends AbstractController  {

	private SocketServer socketServer_;
	private SimulationController simulationController_;
	
	final CyclicBarrier mainBarrier = new CyclicBarrier(2);
	 
	public static MainController instance;
	
	
	public MainController() {
		super(null);
		instance = this;
	}
	 
	public void init() {
		
		simulationController_ = new SimulationController(this);
		simulationController_.init();
	
		socketServer_ = new SocketServer();
		socketServer_.showBanner();
		socketServer_.start();
		
	}
	
	
	public void ThreadPooledServerInit() {
	
	
		ThreadPooledServer server = new ThreadPooledServer(9000);
		new Thread(server).start();
	
		try {
		    Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();
	}
	
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event 
	 */
	//EventSubscriber(eventClass=com.sri.straylight.fmuWrapper.event.SimStateWrapperNotify.class)
	public void onSimStateNotify(SimStateWrapperNotify event) {

		SimStateWrapper fmuWrapperState = event.getPayload();

		
		switch (fmuWrapperState) {
		case simStateServer_1_connect_completed:

			break;
		case simStateServer_2_xmlParse_completed:

			break;
		case simStateServer_3_ready:
			awaitOnBarrier(mainBarrier);
			break;
		case simStateServer_4_run_completed:

			break;
		case simStateServer_4_run_started:

			break;
		case simStateServer_5_step_completed:

			break;
		case simStateServer_7_terminate_completed:

			break;
		case simStateServer_7_reset_completed:

			break;
			
		case simStateServer_e_error:

			break;
			
		default:
			throw new IllegalStateException("serverState not defined");

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
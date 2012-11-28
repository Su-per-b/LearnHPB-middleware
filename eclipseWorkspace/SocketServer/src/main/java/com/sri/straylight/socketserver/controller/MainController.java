package com.sri.straylight.socketserver.controller;

import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.socketserver.SocketServer;


public class MainController extends AbstractController  {

	private SocketServer socketServer_;
	private SimulationController simulationController_;
	
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
	
	

	


	

}
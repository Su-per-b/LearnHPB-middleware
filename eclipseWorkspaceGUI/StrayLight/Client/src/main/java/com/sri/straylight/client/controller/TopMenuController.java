package com.sri.straylight.client.controller;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.action.MenuEvent_About_Help;
import com.sri.straylight.client.event.action.RequestInit;
import com.sri.straylight.client.event.action.SelectRuntime;
import com.sri.straylight.client.event.menu.options.SelectSimulationEngine;
import com.sri.straylight.client.model.Config;

public class TopMenuController {
	
	
	   private Config configModel_;
	
	   
	   @EventSubscriber(eventClass=SelectSimulationEngine.class)
	   public void onSelectSimulationEngine(SelectSimulationEngine menuEvent) {
	      //  new SimulationEngineDialog(mainView_.getJframe(), configModel_);
	   }
	    
	   
	   @EventSubscriber(eventClass=SelectRuntime.class)
	   public void onSelectRuntime(SelectRuntime event) {
		   
	      //  new FmuConfigDialog(mainView_.getJframe(), configModel_);
	   }
	    
	   
	   @EventSubscriber(eventClass=RequestInit.class)
	   public void onRequestInit(RequestInit event) {
		   //initFmu_();
		 //  fmuController.init();
	   }
	    
	   
	   
	   @EventSubscriber(eventClass=MenuEvent_About_Help.class)
	   public void onMenuEvent_About_Help(MenuEvent_About_Help event) {
		  // new AboutDialog(mainView_.getJframe(), configModel_);
	   }
	   
	   
}

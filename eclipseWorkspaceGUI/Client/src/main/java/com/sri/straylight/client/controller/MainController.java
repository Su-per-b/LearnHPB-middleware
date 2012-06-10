package com.sri.straylight.client.controller;


import org.bushe.swing.event.annotation.*;
import com.sri.straylight.client.event.menu.options.SelectSimulationEngine;
import com.sri.straylight.client.event.ui.MenuEvent_About_Help;
import com.sri.straylight.client.event.ui.RequestInit;
import com.sri.straylight.client.event.ui.SelectRuntime;
import com.sri.straylight.client.model.Config;
import com.sri.straylight.client.model.FmuConnectLocal;
import com.sri.straylight.client.model.FmuConnectRemote;
import com.sri.straylight.client.model.IFmuConnect;
import com.sri.straylight.client.model.MainModel;
import com.sri.straylight.client.view.AboutDialog;
import com.sri.straylight.client.view.FmuConfigDialog;
import com.sri.straylight.client.view.MainView;
import com.sri.straylight.client.view.SimulationEngineDialog;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;

public class MainController   {

	
	   private final static MainController instance = new MainController();
	   
	   private MainView mainView_;
	   private MainModel mainModel_;

	   
	   private FmuController fmuController;
	   
	   private TopMenuController topMenuController_;
	   
	   private IFmuConnect fmuConnect_;
	    
	    
	   private MainController() { 
		   AnnotationProcessor.process(this);
	   }
	   

	   public static MainController getInstance()
	   { 
		   return instance; 
	   }
	   
	   public void init() {
		   
		 //  configModel_ = new Config();
		   
		   
		   mainView_ = new MainView(mainModel_);
		   
		   topMenuController_ = new TopMenuController();
		   
		   
		   fmuController = new FmuController();
		   

		   
		   
	   }
	   
	   
	   
	   
	   

	   

	    
	    
	    
	
}

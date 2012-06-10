package com.sri.straylight.SwingGUI.controller;


import org.bushe.swing.event.annotation.*;
import com.sri.straylight.SwingGUI.event.menu.options.SelectSimulationEngine;
import com.sri.straylight.SwingGUI.event.ui.MenuEvent_About_Help;
import com.sri.straylight.SwingGUI.event.ui.RequestInit;
import com.sri.straylight.SwingGUI.event.ui.SelectRuntime;
import com.sri.straylight.SwingGUI.model.Config;
import com.sri.straylight.SwingGUI.model.FmuConnectLocal;
import com.sri.straylight.SwingGUI.model.FmuConnectRemote;
import com.sri.straylight.SwingGUI.model.IFmuConnect;
import com.sri.straylight.SwingGUI.model.MainModel;
import com.sri.straylight.SwingGUI.view.AboutDialog;
import com.sri.straylight.SwingGUI.view.FmuConfigDialog;
import com.sri.straylight.SwingGUI.view.MainView;
import com.sri.straylight.SwingGUI.view.SimulationEngineDialog;
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

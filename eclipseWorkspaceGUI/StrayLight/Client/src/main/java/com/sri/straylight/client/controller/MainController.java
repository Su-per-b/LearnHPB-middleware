package com.sri.straylight.client.controller;


import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.sri.straylight.client.model.IFmuConnect;
import com.sri.straylight.client.model.MainModel;
import com.sri.straylight.client.view.MainView;

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

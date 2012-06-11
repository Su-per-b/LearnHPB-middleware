package com.sri.straylight.client.controller;




import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;



import com.sri.straylight.client.framework.*;
import com.sri.straylight.client.model.MainModel;
import com.sri.straylight.client.view.MainView2;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;

public class MainController2 extends AbstractController {

    // View

	//private JFrame view2;
 //   private JFrame view_jFrame_;
    private MainModel model_;
    
    private TopPanelController topPanelController_;
    private SimulationController simulationController_;
    private DebugConsoleController debugConsoleController_;
    private InputTableController inputTableController_;
    private ResultsTableController resultsTableController_;
    private InternalTableController internalTableController_;
    
    
    private JTabbedPane tabbedPane_;
    
    
    private int numbeOfTabs = 0;
    
    public MainController2() {
        super( null);
        
        
        MainView2 mainView2 = new MainView2();
        
        topPanelController_  = new TopPanelController(this);
        simulationController_ = new SimulationController(this);
        debugConsoleController_ = new DebugConsoleController(this);
        inputTableController_ = new InputTableController(this);
        resultsTableController_ = new ResultsTableController(this);
        internalTableController_ = new InternalTableController(this);
        
        
         
        mainView2.add(topPanelController_.getView(), BorderLayout.NORTH);

        tabbedPane_ = new JTabbedPane(JTabbedPane.TOP);
	    tabbedPane_.addTab("Debug Console", null, debugConsoleController_.getView(), null);
	    
	    mainView2.add(tabbedPane_, BorderLayout.CENTER);
        
        // Display the window
	    mainView2.pack();
	    mainView2.setLocation(300,0);
	    mainView2.setVisible(true);
        
	    setView_(mainView2);
	    

    }
    
    
	@EventSubscriber(eventClass=InitializedEvent.class)
	public void onInitializedEvent(InitializedEvent event) {
		
		numbeOfTabs++;
		inputTableController_.init(event.initializedStruct);
		
	    tabbedPane_.addTab(
	    		"Input Table " + String.valueOf(numbeOfTabs),
	    		null, 
	    		inputTableController_.getView(), 
	    		null);
	    
	    resultsTableController_.init(event.initializedStruct);
	    
	    tabbedPane_.addTab(
	    		"Results Table " + String.valueOf(numbeOfTabs),
	    		null, 
	    		resultsTableController_.getView(), 
	    		null);
	    
	    
	    internalTableController_.init(event.initializedStruct);
	    
	    tabbedPane_.addTab(
	    		"Internal Table " + String.valueOf(numbeOfTabs),
	    		null, 
	    		internalTableController_.getView(), 
	    		null);
			}
    

      
    
}

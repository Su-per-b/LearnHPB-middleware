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



import com.sri.straylight.client.event.menu.About_Help;
import com.sri.straylight.client.event.menu.Options_SelectSimulationEngine;
import com.sri.straylight.client.framework.*;
import com.sri.straylight.client.model.Config;
import com.sri.straylight.client.model.MainModel;
import com.sri.straylight.client.view.AboutDialog;
import com.sri.straylight.client.view.MainView;
import com.sri.straylight.client.view.SimulationEngineDialog;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;

public class MainController extends AbstractController {

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
	private TopMenuController topMenuController_;


	private JTabbedPane tabbedPane_;


	private int numbeOfTabs = 0;
    private Config configModel_ = new Config();
	
	private MainView mainView_;
	
	public MainController() {
		super( null);


		mainView_ = new MainView(configModel_);

		topPanelController_  = new TopPanelController(this);
		simulationController_ = new SimulationController(this, configModel_);
		debugConsoleController_ = new DebugConsoleController(this);
		inputTableController_ = new InputTableController(this);
		resultsTableController_ = new ResultsTableController(this);
		internalTableController_ = new InternalTableController(this);
		topMenuController_ = new TopMenuController(this);

		mainView_.add(topPanelController_.getView(), BorderLayout.NORTH);

		tabbedPane_ = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_.addTab("Debug Console", null, debugConsoleController_.getView(), null);

		mainView_.add(tabbedPane_, BorderLayout.CENTER);

		// Display the window
		mainView_.pack();
		mainView_.setLocation(300,0);
		mainView_.setVisible(true);

		mainView_.setJMenuBar(topMenuController_.getMenuBar());
		setView_(mainView_);
	}
	

	@EventSubscriber(eventClass=Options_SelectSimulationEngine.class)
	public void onSelectSimulationEngine(Options_SelectSimulationEngine event) {
		  new SimulationEngineDialog((MainView) getView(), configModel_);
	}

	@EventSubscriber(eventClass=About_Help.class)
	public void onSelectSimulationEngine(About_Help event) {
		  
		//new AboutDialog((MainView) getView(), configModel_);
		
		JOptionPane.showMessageDialog(
				mainView_, 
				"Version: " + Config.VERSION,
				"About Straylight Simulation Client",
				JOptionPane.INFORMATION_MESSAGE
				);
		
		
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

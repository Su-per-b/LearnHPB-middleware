package com.sri.straylight.client.controller;




import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.SimStateNotify;
import com.sri.straylight.client.event.menu.About_Help;
import com.sri.straylight.client.event.menu.Options_SelectSimulationEngine;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.model.ClientConfig;
import com.sri.straylight.client.model.ClientConfigXML;
import com.sri.straylight.client.model.SimStateClient;
import com.sri.straylight.client.view.MainView;
import com.sri.straylight.client.view.SimulationEngineDialog;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;

public class MainController extends AbstractController {
	

	private TopPanelController topPanelController_;
	private SimulationController simulationController_;
	private DebugConsoleController debugConsoleController_;
	private InputFormController inputFormController_;
	private InputDetailController inputDetailController_;
	
	private ResultsTableController resultsTableController_;
	private InternalTableController internalTableController_;
	private ResultsFormController resultsFormController_;
	
	private TopMenuController topMenuController_;
	private ConfigController configController_;
	
	private JTabbedPane tabbedPane_;
    private ClientConfig configModel_;
	private MainView mainView_;
	
	public static MainController instance;
	
	public MainController() {
		super(null);
		
		configModel_ = ClientConfigXML.load();
		
		mainView_ = new MainView(configModel_);

		topPanelController_  = new TopPanelController(this);
		simulationController_ = new SimulationController(this, configModel_);
		debugConsoleController_ = new DebugConsoleController(this);
		inputFormController_ = new InputFormController(this);
		inputDetailController_ = new InputDetailController(this);
		resultsTableController_ = new ResultsTableController(this);
		resultsFormController_ = new ResultsFormController(this);
		internalTableController_ = new InternalTableController(this);
		topMenuController_ = new TopMenuController(this);
		configController_ = new ConfigController(this);
	
		
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
		
		instance = this;
		
	}
	

	@EventSubscriber(eventClass=Options_SelectSimulationEngine.class)
	public void onSelectSimulationEngine(Options_SelectSimulationEngine event) {
		  new SimulationEngineDialog((MainView) getView(), configModel_);
	}

	@EventSubscriber(eventClass=About_Help.class)
	public void onSelectSimulationEngine(About_Help event) {
		  
		
		JOptionPane.showMessageDialog(
				mainView_, 
				"Version: " + ClientConfigXML.VERSION,
				"About Straylight Simulation Client",
				JOptionPane.INFORMATION_MESSAGE
				);
	}
	
	@EventSubscriber(eventClass=SimStateNotify.class)
    public void onSimStateNotify(SimStateNotify event) {
		
		if (event.getPayload() == SimStateClient.level_6_reset_completed) {
			
			resultsTableController_.reset();
			debugConsoleController_.reset();
		}

	}
	
	
	
	@EventSubscriber(eventClass=XMLparsedEvent.class)
	public void onXMLparsedEvent(XMLparsedEvent event) {
		  

		if (tabbedPane_.getTabCount() < 2) {
			
			configController_.init(event.metaDataStruct);
			inputFormController_.init(event.xmlParsed);
			
			inputDetailController_.init(event.xmlParsed);
			
			resultsFormController_.init(event.xmlParsed);
			resultsTableController_.init(event.xmlParsed);
			internalTableController_.init(event.xmlParsed);
			
			tabbedPane_.addTab(
					"Configuration",
					null, 
					configController_.getView(), 
					null);
			tabbedPane_.addTab(
					"Input Form",
					null, 
					inputFormController_.getView(), 
					null);
			
			tabbedPane_.addTab(
					"Input Detail",
					null, 
					inputDetailController_.getView(), 
					null);		
			tabbedPane_.addTab(
					"Results Form",
					null, 
					resultsFormController_.getView(), 
					null);
			tabbedPane_.addTab(
					"Results Table ",
					null, 
					resultsTableController_.getView(), 
					null);
			tabbedPane_.addTab(
					"Internal Table " ,
					null, 
					internalTableController_.getView(), 
					null);
		} else{
			inputFormController_.reset(event.xmlParsed);
		}

	}
	

}

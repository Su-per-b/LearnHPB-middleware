package com.sri.straylight.client.controller;




import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.SimStateNotify;
import com.sri.straylight.client.event.menu.About_Help;
import com.sri.straylight.client.event.menu.Options_SelectSimulationEngine;
import com.sri.straylight.client.model.ClientConfig;
import com.sri.straylight.client.model.ClientConfigXML;
import com.sri.straylight.client.model.SimStateClient;
import com.sri.straylight.client.view.MainView;
import com.sri.straylight.client.view.SimulationEngineDialog;
import com.sri.straylight.fmuWrapper.event.ExceptionThrowingEventService;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;

// TODO: Auto-generated Javadoc
/**
 * The Class MainController.
 */
public class MainController extends AbstractController {
	

	/** The top panel controller_. */
	private TopPanelController topPanelController_;
	
	/** The simulation controller_. */
	private SimulationController simulationController_;
	
	/** The debug console controller_. */
	private DebugConsoleController debugConsoleController_;
	
	/** The input form controller_. */
	private InputFormController inputFormController_;
	
	/** The input detail controller_. */
	private InputDetailController inputDetailController_;
	
	/** The results table controller_. */
	private ResultsTableController resultsTableController_;
	
	/** The internal table controller_. */
	private InternalTableController internalTableController_;
	
	/** The results form controller_. */
	private ResultsFormController resultsFormController_;
	
	/** The top menu controller_. */
	private TopMenuController topMenuController_;
	
	/** The config controller_. */
	private ConfigController configController_;
	
	/** The tabbed pane_. */
	private JTabbedPane tabbedPane_;
    
    /** The config model_. */
    private ClientConfig configModel_;
	
	/** The main view_. */
	private MainView mainView_;
	
	/** The instance. */
	public static MainController instance;
	
	/**
	 * Instantiates a new main controller.
	 */
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
	

	/**
	 * On select simulation engine.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=Options_SelectSimulationEngine.class)
	public void onSelectSimulationEngine(Options_SelectSimulationEngine event) {
		  new SimulationEngineDialog((MainView) getView(), configModel_);
	}

	/**
	 * On select simulation engine.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=About_Help.class)
	public void onSelectSimulationEngine(About_Help event) {
		  
		
		JOptionPane.showMessageDialog(
				mainView_, 
				"Version: " + ClientConfigXML.VERSION,
				"About Straylight Simulation Client",
				JOptionPane.INFORMATION_MESSAGE
				);
	}
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateNotify.class)
    public void onSimStateNotify(SimStateNotify event) {
		
		if (event.getPayload() == SimStateClient.level_6_reset_completed) {
			
			resultsTableController_.reset();
			debugConsoleController_.reset();
		}

	}
	
	
	
	/**
	 * On xm lparsed event.
	 *
	 * @param event the event
	 */
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

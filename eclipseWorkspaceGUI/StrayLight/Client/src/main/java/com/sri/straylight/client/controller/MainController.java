package com.sri.straylight.client.controller;




import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ViewInitialized;
import com.sri.straylight.client.event.menu.About_Help;
import com.sri.straylight.client.event.menu.Options_SelectSimulationEngine;
import com.sri.straylight.client.model.ClientConfig;
import com.sri.straylight.client.model.ClientConfigXML;
import com.sri.straylight.client.view.BaseView;
import com.sri.straylight.client.view.MainView;
import com.sri.straylight.client.view.OutputView;
import com.sri.straylight.client.view.SimulationEngineDialog;
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
	private ConsoleController consoleController_;
	
	/** The input form controller_. */
	private InputController inputController_;
	
	/** The results table controller_. */
	private ResultsLogController resultsLogController_;
	
	/** The internal table controller_. */
	private InternalTableController internalTableController_;
	
	/** The results form controller_. */
	private OutputController outputController_;
	
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
	
	
	private Vector<BaseView> views_;
	
	/**
	 * Instantiates a new main controller.
	 */
	public MainController() {
		super(null);
		
		configModel_ = ClientConfigXML.load();
		
		mainView_ = new MainView(configModel_);

		topPanelController_  = new TopPanelController(this);
		simulationController_ = new SimulationController(this, configModel_);
		consoleController_ = new ConsoleController(this);
		inputController_ = new InputController(this);
		
		resultsLogController_ = new ResultsLogController(this);
		outputController_ = new OutputController(this);
		
		topMenuController_ = new TopMenuController(this);
		configController_ = new ConfigController(this);
	
		mainView_.add(topPanelController_.getView(), BorderLayout.NORTH);

		tabbedPane_ = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_.addTab("Console", null, consoleController_.getView(), null);

		mainView_.add(tabbedPane_, BorderLayout.CENTER);

		// Display the window
		mainView_.pack();
		mainView_.setLocation(300,0);
		mainView_.setVisible(true);

		mainView_.setJMenuBar(topMenuController_.getMenuBar());
		setView_(mainView_);
		
		instance = this;
		
		views_ = new Vector<BaseView>();

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

	

	private void addTab_(BaseView view) {
		
		tabbedPane_.addTab(
				view.getTitle(),
				null, 
				view, 
				null);
		
		
	}
	
	
	
	@EventSubscriber(eventClass=ViewInitialized.class)
	public void onViewInitialized(ViewInitialized event) {
		
		BaseView view = event.getPayload();
		boolean isViewInitialized = views_.contains(view);
		
		if (!isViewInitialized) {
			views_.add(view);
		}
		
		if (views_.size() > 3) {
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	initTabs_();
			    }
			});
		}
	}


	private void initTabs_() {
		addTab_(configController_.getView());
		addTab_(inputController_.getView());
		addTab_(outputController_.getView());
		addTab_(resultsLogController_.getView());
	}
	
	

	

}

package com.sri.straylight.client.controller;




import java.awt.BorderLayout;

import javax.swing.JOptionPane;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.TabViewInitialized;
import com.sri.straylight.client.event.WebSocketEvent;
import com.sri.straylight.client.event.WebSocketEventStruct;
import com.sri.straylight.client.event.menu.About_Help;
import com.sri.straylight.client.event.menu.Options_SelectSimulationEngine;
import com.sri.straylight.client.model.ClientConfig;
import com.sri.straylight.client.model.ClientConfigXML;
import com.sri.straylight.client.view.BaseView;
import com.sri.straylight.client.view.MainView;
import com.sri.straylight.client.view.SimulationEngineDialog;
import com.sri.straylight.client.view.TopPanelView;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Class MainController.
 */
public class MainController extends BaseController {
	

	/** The top panel controller_. */
	private TopPanelController topPanelController_;
	
	/** The simulation controller_. */
	@SuppressWarnings("unused")
	private SimulationController simulationController_;
	
	/** The debug console controller_. */
	private ConsoleController consoleController_;
	
	/** The input form controller_. */
	private InputVariablesController inputController_;

	private InternalVariablesController internalTableController_;
	
	/** The results form controller_. */
	private OutputVariablesController outputController_;
	
	/** The results table controller_. */
	private ResultsLogController resultsLogController_;
	
	
	/** The top menu controller_. */
	private TopMenuController topMenuController_;
	
	/** The config controller_. */
	private ConfigController configController_;
	

    /** The config model_. */
    private ClientConfig configModel_;
	
	/** The main view_. */
	private MainView view_;
	

	private FMUInfoController fmuInfoController;
	
	
	
	/**
	 * Instantiates a new main controller.
	 */
	public MainController() {
		super(null);
		
		configModel_ = ClientConfigXML.load();
		view_ = new MainView(configModel_);
		
		simulationController_ = new SimulationController(this, configModel_);
		
		topPanelController_  = new TopPanelController(this);
		TopPanelView topPanelView = topPanelController_.getView();
		view_.add(topPanelView, BorderLayout.NORTH);

		topMenuController_ = new TopMenuController(this);
		
		consoleController_ = new ConsoleController(this);
		configController_ = new ConfigController(this);
		inputController_ = new InputVariablesController(this);
		internalTableController_ = new InternalVariablesController(this);
		outputController_ = new OutputVariablesController(this);
		resultsLogController_ = new ResultsLogController(this);
		
		view_.setJMenuBar(topMenuController_.getMenuBar());
		view_.showWindow();
		
	}
	




	/**
	 * On select simulation engine.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=Options_SelectSimulationEngine.class)
	public void onSelectSimulationEngine(Options_SelectSimulationEngine event) {
		@SuppressWarnings("unused")
		SimulationEngineDialog dialog =  new SimulationEngineDialog( view_ , configModel_);
	}

	
	/**
	 * On select simulation engine.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=About_Help.class)
	public void onAbout_Help(About_Help event) {
		JOptionPane.showMessageDialog(
				view_, 
				"Version: " + ClientConfigXML.VERSION,
				"About Straylight Simulation Client",
				JOptionPane.INFORMATION_MESSAGE
				);
	}

	

	
	
	@EventSubscriber(eventClass=WebSocketEvent.class)
	public void onWebSocketEvent(WebSocketEvent event) {
		
		WebSocketEventStruct payload = event.getPayload();
		
		JOptionPane.showMessageDialog(
				view_, 
				payload.eventDetail,
				"Error: " + payload.eventTitle,
				JOptionPane.ERROR_MESSAGE
				);

	}
	
	
	
	
	@EventSubscriber(eventClass=TabViewInitialized.class)
	public void onTabViewInitialized(TabViewInitialized event) {
		

		BaseView tabView = event.getPayload();
		view_.addTab(tabView);
	}
	
	




	

	

}

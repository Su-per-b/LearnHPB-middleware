package com.sri.straylight.client.controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ClearViewAction;
import com.sri.straylight.client.event.SimStateNotify;
import com.sri.straylight.client.event.SimStateRequest;
import com.sri.straylight.client.model.SimStateClient;
import com.sri.straylight.fmuWrapper.framework.AbstractController;




// TODO: Auto-generated Javadoc
/**
 * The Class TopPanelController.
 */
public class TopPanelController extends AbstractController {

	/** The btn clear_. */
	private final JButton btnClear_ = new JButton("Clear -");
	
	/** The btn connect_. */
	private final JButton btnConnect_ = new JButton("Connect ~");
	
	/** The btn xml parse_. */
	private final JButton btnXmlParse_ = new JButton("XML Parse {}");
	
	/** The btn init_. */
	private final JButton btnInit_ = new JButton("Init ^");
	
	/** The btn step_. */
	private final JButton btnStep_ = new JButton("Step >|");
	
	/** The btn run_. */
	private final JButton btnRun_ = new JButton("Run >");
	
	/** The btn stop_. */
	private final JButton btnStop_ = new JButton("Stop []");
	
	/** The btn terminate_. */
	private final JButton btnTerminate_ = new JButton("Terminate .");
	
	/** The btn reset_. */
	//private final JButton btnReset_ = new JButton("Reset .^");



	/** The simulation state_. */
	private SimStateClient simulationState_ = SimStateClient.level_0_uninitialized;
	
	/** The buttons_. */
	private Vector<JButton> buttons_;
	
	/** The state map_. */
	Hashtable<SimStateClient, JButton[]> stateMap_ = new Hashtable<SimStateClient, JButton[]>();
	
	/**
	 * Instantiates a new top panel controller.
	 *
	 * @param parentController the parent controller
	 */
	public TopPanelController (AbstractController parentController) {

		super(parentController);

		JPanel panel = new JPanel();

		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		panel.setMaximumSize(new Dimension(32767, 60));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		
		
		btnConnect_.setName("TopPanel.Button_Connect");
		btnXmlParse_.setName("TopPanel.Button_XML_Parse");
		btnInit_.setName("TopPanel.Button_Init");
		btnRun_.setName("TopPanel.Button_Run");
		btnStep_.setName("TopPanel.Button_Step");
		
		
		buttons_ = new Vector<JButton>();
	
		buttons_.add(btnConnect_);
		buttons_.add(btnXmlParse_);
		buttons_.add(btnInit_);
		buttons_.add(btnRun_);
		buttons_.add(btnStep_);
		buttons_.add(btnStop_);
		buttons_.add(btnTerminate_);
		//buttons_.add(btnReset_);
		buttons_.add(btnClear_);

		//Array<SimStateClient>[] states = {}
		
		int len = buttons_.size();
		
		Insets insets = new Insets(0, 2, 0, 2);
		
		for (int i = 0; i < len; i++) {
			JButton b = buttons_.get(i);
			b.setMargin(insets);
			panel.add(b);
		}
		
		setupFSM_();
		
		bindActions_();
		updateGUI_();
		
		setView_(panel);

	}

	/**
	 * Setup the Finite State Machine functionality.
	 */
	private void setupFSM_() {
		
		
		//JButton[] ary  = {btnConnect_};
		
		stateMap_.put(
				SimStateClient.level_0_uninitialized,
				new JButton[]{btnConnect_}
				);
		
		stateMap_.put(
				SimStateClient.level_1_connect_completed,
				new JButton[]{btnXmlParse_, btnClear_}
				);
		
		stateMap_.put(
				SimStateClient.level_2_xmlParse_completed,
				new JButton[]{btnInit_, btnClear_}
				);
		
		stateMap_.put(
				SimStateClient.level_3_ready,
				new JButton[]{btnRun_, btnStep_,  btnTerminate_, btnClear_}
				);
		
		stateMap_.put(
				SimStateClient.level_4_run_started,
				new JButton[]{btnStop_}
				);
		
		stateMap_.put(
				SimStateClient.level_4_run_completed,
				new JButton[]{ btnTerminate_}
				);
		
		stateMap_.put(
				SimStateClient.level_7_terminate_completed,
				new JButton[]{btnInit_, btnClear_}
				);

		
	}


	/**
	 * Bind actions_.
	 */
	private void bindActions_() {

		btnConnect_.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent ae) {

		        
		        
				requestStateChange_(SimStateClient.level_1_connect_requested);
				
				int x = 1;
				
				
				
			}
		}
				);
		
		btnXmlParse_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
			//	btnXmlParse_.setEnabled(false);
				requestStateChange_(SimStateClient.level_2_xmlParse_requested);
				
			}
		}
				);
		

		btnInit_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateClient.level_3_init_requested);
				
			}

		}
				);
		
		btnStep_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateClient.level_5_step_requested);
				
			}

		}
				);
		

		btnRun_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				requestStateChange_(SimStateClient.level_4_run_requested);
			}
		}
				);
		

		btnStop_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateClient.level_5_stop_requested);
			}
		}
				);
		
		btnTerminate_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateClient.level_7_terminate_requested);
			}
		}
				);
		
/*
		btnReset_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateClient.level_7_reset_requested);
			}
		}
				);

*/
		
		btnClear_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fireClear();
			}
		}
				);
		
		
		

	}

	private void fireClear()
	{
		EventBus.publish(
				new ClearViewAction()
				);	
		
	}
	
	
	
	
	/**
	 * Request state change_.
	 *
	 * @param state_arg the state_arg
	 */
	private void requestStateChange_(SimStateClient state_arg)
	{
		EventBus.publish(
				new SimStateRequest(this, state_arg)
				);	
		
	}
	
	/**
	 * On sim state request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateRequest.class)
	public void onSimStateRequest(SimStateRequest event) {
		simulationState_ = event.getPayload();
	
		updateGUI_();
		
	}
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateNotify.class)
	public void onSimStateNotify(SimStateNotify event) {
		simulationState_ = event.getPayload();
		updateGUI_();
	}


	/**
	 * Update gu i_.
	 */
	private void updateGUI_() {

		int len = buttons_.size();
		for (int i = 0; i < len; i++) {
			buttons_.get(i).setEnabled(false);
		}
		
		JButton[] activeButtons = stateMap_.get(simulationState_);
		
		if (activeButtons != null) {
			
			int len2 = activeButtons.length;
			for (int j = 0; j < len2; j++) {
				activeButtons[j].setEnabled(true);
			}
		}
		

		return;
		
	}



}

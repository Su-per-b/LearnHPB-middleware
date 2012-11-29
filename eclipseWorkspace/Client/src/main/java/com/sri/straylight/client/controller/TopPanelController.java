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

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ClearViewAction;
import com.sri.straylight.client.view.BaseView;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateClientRequest;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


// TODO: Auto-generated Javadoc
/**
 * The Class TopPanelController.
 */
public class TopPanelController extends BaseController {

	/** Declare Buttons */
	private final JButton btnClear_ = new JButton("Clear -");
	private final JButton btnConnect_ = new JButton("Connect ~");
	private final JButton btnXmlParse_ = new JButton("XML Parse {}");
	private final JButton btnInit_ = new JButton("Init ^");
	private final JButton btnStep_ = new JButton("Step >|");
	private final JButton btnRun_ = new JButton("Run >");
	private final JButton btnStop_ = new JButton("Stop []");
	private final JButton btnTerminate_ = new JButton("Terminate .");
	

	/** The simulation state_. */
	private SimStateNative simStateNative_ = SimStateNative.simStateNative_0_uninitialized;
	
	/** The buttons_. */
	private Vector<JButton> buttons_;
	
	/** The state map_. */
	Hashtable<SimStateNative, JButton[]> stateMap_ = new Hashtable<SimStateNative, JButton[]>();
	
	/**
	 * Instantiates a new top panel controller.
	 *
	 * @param parentController the parent controller
	 */
	public TopPanelController (AbstractController parentController) {

		super(parentController);

		BaseView theView = new BaseView("Top Panel");

		FlowLayout flowLayout = (FlowLayout) theView.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		theView.setMaximumSize(new Dimension(32767, 60));
		theView.setAlignmentX(Component.LEFT_ALIGNMENT);
		theView.setAlignmentY(Component.TOP_ALIGNMENT);
		
		
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
		buttons_.add(btnClear_);

		int len = buttons_.size();
		Insets insets = new Insets(0, 2, 0, 2);
		
		for (int i = 0; i < len; i++) {
			JButton b = buttons_.get(i);
			b.setMargin(insets);
			theView.add(b);
		}
		
		setupFSM_();
		
		bindActions_();
		updateGUI_();
		
		setView_(theView);

	}

	/**
	 * Setup the Finite State Machine functionality.
	 */
	private void setupFSM_() {
		
		
		//JButton[] ary  = {btnConnect_};
		
		stateMap_.put(
				SimStateNative.simStateNative_0_uninitialized,
				new JButton[]{btnConnect_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_1_connect_completed,
				new JButton[]{btnXmlParse_, btnClear_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_2_xmlParse_completed,
				new JButton[]{btnInit_, btnClear_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_3_ready,
				new JButton[]{btnRun_, btnStep_,  btnTerminate_, btnClear_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_4_run_started,
				new JButton[]{btnStop_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_4_run_completed,
				new JButton[]{ btnTerminate_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_7_terminate_completed,
				new JButton[]{btnInit_, btnClear_}
				);

		
	}


	/**
	 * Bind actions_.
	 */
	private void bindActions_() {

		btnConnect_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateNative.simStateNative_1_connect_requested);
			}
		}
				);
		
		btnXmlParse_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateNative.simStateNative_2_xmlParse_requested);
			}
		}
				);
		

		btnInit_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateNative.simStateNative_3_init_requested);
			}

		}
				);
		
		btnStep_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateNative.simStateNative_5_step_requested);
				
			}

		}
				);
		

		btnRun_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				requestStateChange_(SimStateNative.simStateNative_4_run_requested);
			}
		}
				);
		

		btnStop_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateNative.simStateNative_5_stop_requested);
			}
		}
				);
		
		btnTerminate_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateNative.simStateNative_7_terminate_requested);
			}
		}
				);

		
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
	private void requestStateChange_(SimStateNative simStateNative)
	{
		EventBus.publish(
				new SimStateClientRequest(this, simStateNative)
				);	
		
	}
	
	/**
	 * On sim state request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateClientRequest.class)
	public void onSimStateClientRequest(SimStateClientRequest event) {
		
		simStateNative_ = event.getPayload();
	
		updateGUI_();
		
	}
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateClientNotify.class)
	public void onSimStateNotify(SimStateClientNotify event) {
		simStateNative_ = event.getPayload();
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
		
		JButton[] activeButtons = stateMap_.get(simStateNative_);
		
		if (activeButtons != null) {
			
			int len2 = activeButtons.length;
			for (int j = 0; j < len2; j++) {
				activeButtons[j].setEnabled(true);
			}
		}
		

		return;
		
	}



}

package com.sri.straylight.client.view;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.client.controller.TopPanelController2;
import com.sri.straylight.client.event.ClearViewAction;
import com.sri.straylight.client.event.SimStateClientRequest;
import com.sri.straylight.client.model.TopPanelDataModel;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class TopPanelView extends BaseView {
	
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Top Panel";
	
	
	/** Declare Buttons */
	private final JButton btnClear_ = new JButton("Clear -");
	private final JButton btnConnect_ = new JButton("Connect ~");
	private final JButton btnXmlParse_ = new JButton("XML Parse {}");
	private final JButton btnInit_ = new JButton("Init ^");
	private final JButton btnStep_ = new JButton("Step >|");
	private final JButton btnRun_ = new JButton("Run >");
	private final JButton btnStop_ = new JButton("Stop []");
	private final JButton btnTerminate_ = new JButton("Terminate .");
	private final JButton btnTearDown_ = new JButton("Tear Down");
	
	/** The buttons_. */
	private Vector<JButton> buttons_;
	
	/** The state map_. */
	Hashtable<SimStateNative, JButton[]> stateMap_ = new Hashtable<SimStateNative, JButton[]>();
	private TopPanelDataModel topPanelModel_;
//	private TopPanelController2 topPanelController_;
	
	
	public TopPanelView(TopPanelController2 topPanelController, TopPanelDataModel  topPanelModel ) {
		
		super(TITLE);
		
		topPanelModel_ = topPanelModel;
//		topPanelController_ = topPanelController;
		
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
		buttons_.add(btnTearDown_);
		

		int len = buttons_.size();
		Insets insets = new Insets(0, 2, 0, 2);
		
		for (int i = 0; i < len; i++) {
			JButton b = buttons_.get(i);
			b.setMargin(insets);
			this.add(b);
		}
		
		
		setupFSM_();
		
		bindActions_();
		updateGUI_();
		
		
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
				new JButton[]{btnXmlParse_, btnClear_, btnTearDown_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_2_xmlParse_completed,
				new JButton[]{btnInit_, btnClear_, btnTearDown_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_3_ready,
				new JButton[]{btnRun_, btnStep_,  btnTerminate_, btnClear_, btnTearDown_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_4_run_started,
				new JButton[]{btnStop_, btnTearDown_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_4_run_completed,
				new JButton[]{ btnTerminate_, btnTearDown_}
				);
		
		stateMap_.put(
				SimStateNative.simStateNative_7_terminate_completed,
				new JButton[]{btnInit_, btnClear_, btnTearDown_}
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
		
		btnTearDown_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateNative.simStateNative_8_tearDown_requested);
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
	 * Update gu i_.
	 */
	private void updateGUI_() {

		int len = buttons_.size();
		for (int i = 0; i < len; i++) {
			buttons_.get(i).setEnabled(false);
		}
		
		JButton[] activeButtons = stateMap_.get(topPanelModel_.simStateNative);
		
		if (activeButtons != null) {
			
			int len2 = activeButtons.length;
			for (int j = 0; j < len2; j++) {
				activeButtons[j].setEnabled(true);
			}
		}
		

		return;
	}
	
	private void fireClear()
	{
		EventBus.publish(
				new ClearViewAction()
				);	
		
	}
	
	

	
	public void updateSimStateClient(SimStateNative simStateNative) {
		topPanelModel_.simStateNative = simStateNative;
		updateGUI_();
	}
	


	
	
}

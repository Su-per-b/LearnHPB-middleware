package com.sri.straylight.client.controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.SimStateNotify;
import com.sri.straylight.client.event.SimStateRequest;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.model.SimStateClient;




public class TopPanelController extends AbstractController {

	private final JButton btnClear_ = new JButton("Clear Console");
	private final JButton btnConnect_ = new JButton("Connect");
	private final JButton btnXmlParse_ = new JButton("XML Parse");
	private final JButton btnInit_ = new JButton("Init");
	private final JButton btnStep_ = new JButton("Step >|");
	private final JButton btnRun_ = new JButton("Run >");
	private final JButton btnStop_ = new JButton("Stop []");
	private final JButton btnReset_ = new JButton("Reset");



	private SimStateClient simulationState_ = SimStateClient.level_0_uninitialized;
	private Vector<JButton> buttons_;
	
	Hashtable<SimStateClient, JButton[]> stateMap_ = new Hashtable<SimStateClient, JButton[]>();
	
	public TopPanelController (AbstractController parentController) {

		super(parentController);

		JPanel panel = new JPanel();

		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		panel.setMaximumSize(new Dimension(32767, 60));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		
		buttons_ = new Vector<JButton>();
	
		buttons_.add(btnConnect_);
		buttons_.add(btnXmlParse_);
		buttons_.add(btnInit_);
		buttons_.add(btnRun_);
		buttons_.add(btnStep_);
		buttons_.add(btnStop_);
		buttons_.add(btnReset_);
		buttons_.add(btnClear_);

		//Array<SimStateClient>[] states = {}
		
		int len = buttons_.size();
		
		for (int i = 0; i < len; i++) {
			panel.add(buttons_.get(i));
		}
		
		setupFSM_();
		
		bindActions_();
		updateGUI_();
		
		setView_(panel);

	}

	private void setupFSM_() {
		
		
		JButton[] ary  = {btnConnect_};
		
		stateMap_.put(
				SimStateClient.level_0_uninitialized,
				new JButton[]{btnConnect_}
				);
		
		stateMap_.put(
				SimStateClient.level_1_connect_completed,
				new JButton[]{btnXmlParse_}
				);
		
		stateMap_.put(
				SimStateClient.level_2_xmlParse_completed,
				new JButton[]{btnInit_}
				);
		
		stateMap_.put(
				SimStateClient.level_3_ready,
				new JButton[]{btnRun_, btnStep_}
				);
		
		stateMap_.put(
				SimStateClient.level_4_run_started,
				new JButton[]{btnStop_}
				);
		

		
	}


	private void bindActions_() {

		btnConnect_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateClient.level_1_connect_requested);
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
		
		btnReset_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateClient.level_6_reset_requested);
			}
		}
				);

	}


	private void requestStateChange_(SimStateClient state_arg)
	{
		EventBus.publish(
				new SimStateRequest(this, state_arg)
				);	
		
	}
	
	@EventSubscriber(eventClass=SimStateRequest.class)
	public void onSimStateRequest(SimStateRequest event) {
		simulationState_ = event.getPayload();
		updateGUI_();
	}
	
	@EventSubscriber(eventClass=SimStateNotify.class)
	public void onSimStateNotify(SimStateNotify event) {
		simulationState_ = event.getPayload();
		updateGUI_();
	}


	private void updateGUI_() {

		int len = buttons_.size();
		for (int i = 0; i < len; i++) {
			buttons_.get(i).setEnabled(false);
		}
		
		JButton[] activeButtons = stateMap_.get(simulationState_);
		int len2 = activeButtons.length;
		for (int j = 0; j < len2; j++) {
			activeButtons[j].setEnabled(true);
		}
		
		
		return;
		
	}



}

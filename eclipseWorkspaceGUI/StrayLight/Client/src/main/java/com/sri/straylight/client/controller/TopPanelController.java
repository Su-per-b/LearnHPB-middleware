package com.sri.straylight.client.controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private final JButton btnxmlParse_ = new JButton("XML Parse");
	private final JButton btnInit_ = new JButton("Init");
	private final JButton btnRun_ = new JButton("Run");
	private final JButton btnStop_ = new JButton("Stop");
	private final JButton btnResume_ = new JButton("Resume");



	private SimStateClient simulationState_ = SimStateClient.level_0_uninitialized;
	
	public TopPanelController (AbstractController parentController) {

		super(parentController);

		JPanel panel = new JPanel();

		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		panel.setMaximumSize(new Dimension(32767, 60));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);

		panel.add(btnConnect_);
		panel.add(btnxmlParse_);
		panel.add(btnInit_);
		panel.add(btnRun_);
		panel.add(btnStop_);
		panel.add(btnResume_);
		panel.add(btnClear_);

		bindActions_();
		updateGUI_();
		
		setView_(panel);

	}




	private void bindActions_() {

		btnConnect_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				requestStateChange_(SimStateClient.level_1_connect_requested);
			}
		}
				);
		
		btnxmlParse_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnxmlParse_.setEnabled(false);
				requestStateChange_(SimStateClient.level_2_xmlParse_requested);
				
			}
		}
				);
		

		btnInit_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnInit_.setEnabled(false);
				requestStateChange_(SimStateClient.level_3_init_requested);
				
			}

		}
				);

		btnRun_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnRun_.setEnabled(false);
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
		
		btnResume_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnResume_.setEnabled(false);
				requestStateChange_(SimStateClient.level_7_resume_requested);
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
	
	@EventSubscriber(eventClass=SimStateNotify.class)
	public void onSimStateNotify(SimStateNotify event) {
		simulationState_ = event.getPayload();
		updateGUI_();
	}


	private void updateGUI_() {
		
		if (simulationState_ == SimStateClient.level_0_uninitialized) {
			btnConnect_.setEnabled(true);
			btnxmlParse_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnStop_.setEnabled(false);
			btnResume_.setEnabled(false);
			
		}  else if (simulationState_ == SimStateClient.level_1_connect_completed) {
			btnConnect_.setEnabled(false);
			btnxmlParse_.setEnabled(true);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnStop_.setEnabled(false);
			btnResume_.setEnabled(false);
		} else if (simulationState_ == SimStateClient.level_2_xmlParse_completed) {
			btnConnect_.setEnabled(false);
			btnxmlParse_.setEnabled(false);
			btnInit_.setEnabled(true);
			btnRun_.setEnabled(false);
			btnStop_.setEnabled(false);
			btnResume_.setEnabled(false);
		} else if (simulationState_ == SimStateClient.level_3_init_completed) {
			btnConnect_.setEnabled(false);
			btnxmlParse_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(true);
			btnStop_.setEnabled(false);
			btnResume_.setEnabled(false);
		} else if (simulationState_ == SimStateClient.level_4_run_completed) {
			btnConnect_.setEnabled(false);
			btnxmlParse_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnStop_.setEnabled(true);
			btnResume_.setEnabled(false);
		} else if (simulationState_ == SimStateClient.level_4_run_started) {
			btnConnect_.setEnabled(false);
			btnxmlParse_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnStop_.setEnabled(true);
			btnResume_.setEnabled(false);
		} else if (simulationState_ == SimStateClient.level_5_stop_completed) {
			btnConnect_.setEnabled(false);
			btnxmlParse_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnStop_.setEnabled(false);
			btnResume_.setEnabled(true);
	}
		
		
	}



}

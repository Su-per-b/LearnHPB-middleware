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

import com.sri.straylight.client.event.action.ClearDebugConsoleAction;
import com.sri.straylight.client.event.action.InitAction;
import com.sri.straylight.client.event.action.LoadAction;
import com.sri.straylight.client.event.action.RunSimulationAction;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.voNative.State;




public class TopPanelController extends AbstractController {

	private final JButton btnClear_ = new JButton("Clear Console");
	private final JButton btnLoad_ = new JButton("Load");
	private final JButton btnInit_ = new JButton("Init");
	private final JButton btnCancel_ = new JButton("Cancel");

	private final JButton btnRun_ = new JButton("Run Simulation");

	private State fmuState_ = State.fmuState_level_0_uninitialized;
	
	public TopPanelController (AbstractController parentController) {

		super(parentController);

		JPanel panel = new JPanel();

		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		panel.setMaximumSize(new Dimension(32767, 60));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);


		panel.add(btnLoad_);
		panel.add(btnInit_);
		panel.add(btnRun_);
		panel.add(btnCancel_);
		panel.add(btnClear_);

		bindActions_();
		updateGUI_();
		
		setView_(panel);

	}




	private void bindActions_() {

		btnLoad_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnLoad_.setEnabled(false);
				EventBus.publish(new LoadAction());
			}
		}
				);


		btnInit_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnInit_.setEnabled(false);
				EventBus.publish(new InitAction());
			}

		}
				);

		btnRun_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				btnRun_.setEnabled(false);
				EventBus.publish(new RunSimulationAction());
			}
		}
				);

		btnClear_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				EventBus.publish(new ClearDebugConsoleAction());
			}
		}
				);

	}



	@EventSubscriber(eventClass=FMUstateEvent.class)
	public void onFMUstateEvent(FMUstateEvent event) {
		fmuState_ = event.fmuState;
		updateGUI_();
	}


	private void updateGUI_() {
		
		btnRun_.setEnabled(fmuState_ == State.fmuState_level_5_initializedFMU);

		if (fmuState_ == State.fmuState_cleanedup) {
			btnLoad_.setEnabled(true);
		}
		
		if (fmuState_ == State.fmuState_level_0_uninitialized) {
			btnLoad_.setEnabled(true);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnCancel_.setEnabled(false);
			
		} else if (fmuState_ == State.fmuState_level_1_xmlParsed) {
			btnLoad_.setEnabled(false);
			btnInit_.setEnabled(true);
			btnRun_.setEnabled(false);
			btnCancel_.setEnabled(false);
		} else if (fmuState_ == State.fmuState_level_2_dllLoaded) {
			btnLoad_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnCancel_.setEnabled(false);
		} else if (fmuState_ == State.fmuState_level_3_instantiatedSlaves) {
			btnLoad_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnCancel_.setEnabled(false);
		} else if (fmuState_ == State.fmuState_level_4_initializedSlaves) {
			btnLoad_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnCancel_.setEnabled(false);
		} else if (fmuState_ == State.fmuState_level_5_initializedFMU) {
			btnLoad_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(true);
			btnCancel_.setEnabled(false);
		} else if (fmuState_ == State.fmuState_runningSimulation) {
			btnLoad_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(false);
			btnCancel_.setEnabled(true);
			
		} else if (fmuState_ == State.fmuState_completedSimulation) {
			btnLoad_.setEnabled(false);
			btnInit_.setEnabled(false);
			btnRun_.setEnabled(true);
			btnCancel_.setEnabled(false);
			
		}
		

	}
	
	private void updateDataModel_() {

		
	}


}

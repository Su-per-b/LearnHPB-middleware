package com.sri.straylight.client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.SimStateNotify;
import com.sri.straylight.client.event.SimStateRequest;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.model.SimStateClient;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.ConfigChangeRequest;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigController.
 */
public class ConfigController extends AbstractController {
	
	
	/** The txt start time_. */
	private JFormattedTextField txtStartTime_;
	
	/** The txt stop time_. */
	private JFormattedTextField txtStopTime_;
	
	/** The txt step delta_. */
	private JFormattedTextField txtStepDelta_;
	
	/** The btn apply_. */
	private final JButton btnApply_ = new JButton("Apply");

	/** The lbl start time_. */
	private final JLabel lblStartTime_ = new JLabel("Start Time");
	
	/** The lbl stop time_. */
	private final JLabel lblStopTime_ = new JLabel("Stop Time");
	
	/** The lbl step delta_. */
	private final JLabel lblStepDelta_ = new JLabel("Step Delta");

	
	/** The config struct_. */
	private ConfigStruct configStruct_;
	
	
	/** The simulation state_. */
	private SimStateClient simulationState_ = SimStateClient.level_0_uninitialized;
	
	/**
	 * Instantiates a new config controller.
	 *
	 * @param parentController the parent controller
	 */
	public ConfigController(AbstractController parentController) {
		super(parentController);
	}


	/**
	 * Bind actions_.
	 */
	private void bindActions_() {
		
		btnApply_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	//btnApply_.setEnabled(false);
            	updateDataModel_();
            	EventBus.publish(new ConfigChangeRequest(configStruct_));
             }
           }
        );
		
	}
	

	/**
	 * Inits the.
	 *
	 * @param configStruct the config struct
	 */
	public void init(ConfigStruct configStruct) {
		
		configStruct_ = configStruct;
		
		txtStartTime_ = new JFormattedTextField(new Double(0.0));
	//	txtStartTime_.setEnabled(false);
		txtStopTime_ = new JFormattedTextField(new Double(0.0));
		txtStepDelta_ = new JFormattedTextField(new Double(0.0));
		
		txtStartTime_.setColumns(10);
		txtStopTime_.setColumns(10);
		txtStepDelta_.setColumns(10);
		
		DefaultFormatter fmt = new NumberFormatter(new DecimalFormat("#.0#"));
		fmt.setValueClass(txtStartTime_.getValue().getClass());
		DefaultFormatterFactory fmtFactory = new DefaultFormatterFactory(fmt, fmt, fmt);
		
		txtStartTime_.setFormatterFactory(fmtFactory);
		txtStopTime_.setFormatterFactory(fmtFactory);
		txtStepDelta_.setFormatterFactory(fmtFactory);
		
		lblStartTime_.setEnabled(false);
		JPanel panel = new JPanel();

		
		panel.setLayout(new MigLayout("", "[][grow][290.00,grow]", "[][]"));
		
		panel.add(lblStartTime_, "cell 0 0,alignx trailing");
		panel.add(txtStartTime_, "cell 1 0,growx");
		
		panel.add(lblStopTime_, "cell 0 1,alignx trailing");
		panel.add(txtStopTime_, "cell 1 1,growx");

		panel.add(lblStepDelta_, "cell 0 2,alignx trailing");
		panel.add(txtStepDelta_, "cell 1 2,growx");	

		panel.add(btnApply_, "cell 1 4");
		
		setView_(panel);
		bindActions_();
		
		updateGUI_();
	}
	
	
	/**
	 * On sim state request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ConfigChangeRequest.class)
	public void onSimStateRequest(SimStateRequest event) {
		btnApply_.setEnabled(false);
		simulationState_ = event.getPayload();
		updateGUIFromState_();
	}
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ConfigChangeNotify.class)
	public void onSimStateNotify(SimStateNotify event) {
		btnApply_.setEnabled(true);
		simulationState_ = event.getPayload();
		updateGUIFromState_();
	}

	
	/**
	 * Update gui from state_.
	 */
	private void updateGUIFromState_() {
		//if (simulationState_ == SimStateClient.)
		
		if (simulationState_ == SimStateClient.level_2_xmlParse_completed) {
			
		}
		
	}
	
	
	
	/**
	 * Update gu i_.
	 */
	private void updateGUI_() {
		txtStartTime_.setValue(configStruct_.defaultExperimentStruct.startTime);
		txtStopTime_.setValue(configStruct_.defaultExperimentStruct.stopTime);
		txtStepDelta_.setValue(configStruct_.stepDelta);
	}
	
	/**
	 * Update data model_.
	 */
	private void updateDataModel_() {
		configStruct_.defaultExperimentStruct.startTime = (double)txtStartTime_.getValue();
		configStruct_.defaultExperimentStruct.stopTime = (double)txtStopTime_.getValue();
		configStruct_.stepDelta = (double)txtStepDelta_.getValue();
	}
	
	
}

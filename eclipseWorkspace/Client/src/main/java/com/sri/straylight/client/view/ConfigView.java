package com.sri.straylight.client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;

import com.sri.straylight.client.controller.ConfigController;
import com.sri.straylight.client.model.ConfigModel;
import com.sri.straylight.fmuWrapper.event.ConfigChangeRequest;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Class OutputView.
 */
public class ConfigView extends BaseView {


	private static final long serialVersionUID = 1L;

	
	protected ConfigModel dataModel_;
	
	private JFormattedTextField txtStartTime_;
	private JFormattedTextField txtStopTime_;
	private JFormattedTextField txtStepDelta_;
	
	/** The btn apply_. */
	private final JButton btnApply_ = new JButton("Apply");
	private final JLabel lblStartTime_ = new JLabel("Start Time");
	private final JLabel lblStopTime_ = new JLabel("Stop Time");
	private final JLabel lblStepDelta_ = new JLabel("Step Delta");
	
	
	public ConfigView(ConfigModel dataModel, ConfigController parentController) {
		
		super(dataModel, parentController);
		
		dataModel_ = dataModel;
		
		txtStartTime_ = new JFormattedTextField(new Double(0.0));
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
		
		setLayout(new MigLayout("", "[][grow][290.00,grow]", "[][]"));
		
		add(lblStartTime_, "cell 0 0,alignx trailing");
		add(txtStartTime_, "cell 1 0,growx");
		
		add(lblStopTime_, "cell 0 1,alignx trailing");
		add(txtStopTime_, "cell 1 1,growx");

		add(lblStepDelta_, "cell 0 2,alignx trailing");
		add(txtStepDelta_, "cell 1 2,growx");	

		add(btnApply_, "cell 1 4");
		
		bind_();
		
		updateGUIFromDataModel();
	}
	

	/**
	 * Bind actions_.
	 */
	private void bind_() {
		
		btnApply_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	
        		setEnabled(false);
        		ConfigStruct configStruct = getStructFromGUI();
        		
        		ConfigChangeRequest event = new ConfigChangeRequest(this,configStruct);
        		fireEvent(event);
        		
        		setEnabled(true);
             }
           }
        );
	}
	
	
	public ConfigStruct getStructFromGUI() {
		
		ConfigStruct configStruct = new ConfigStruct();
		
		configStruct.defaultExperimentStruct = new DefaultExperimentStruct.ByReference();
		
		configStruct.defaultExperimentStruct.startTime = (double)txtStartTime_.getValue();
		configStruct.defaultExperimentStruct.stopTime = (double)txtStopTime_.getValue();
		
		configStruct.stepDelta = (double)txtStepDelta_.getValue();
		
		return configStruct;
		
	}
	

	public void updateGUIFromDataModel() {
		
		ConfigStruct configStruct = dataModel_.getStruct();
		
		txtStartTime_.setValue(configStruct.defaultExperimentStruct.startTime);
		txtStopTime_.setValue(configStruct.defaultExperimentStruct.stopTime);
		txtStepDelta_.setValue(configStruct.stepDelta);
		
	}
	
	


	public void updateState() {
		
		SimStateNative simStateNative = dataModel_.getSimStateNative();
		
		boolean startTimeEnabled = (simStateNative == SimStateNative.simStateNative_2_xmlParse_completed ||
				simStateNative == SimStateNative.simStateNative_7_terminate_completed
		);
		
		boolean stopTimeEnabled = (simStateNative == SimStateNative.simStateNative_2_xmlParse_completed ||
				simStateNative == SimStateNative.simStateNative_7_terminate_completed ||
				simStateNative == SimStateNative.simStateNative_3_ready
		);
		
		boolean stepDeltaEnabled = (simStateNative == SimStateNative.simStateNative_2_xmlParse_completed ||
				simStateNative == SimStateNative.simStateNative_7_terminate_completed ||
				simStateNative == SimStateNative.simStateNative_3_ready
				);
		
		boolean applyButtonEnabled = (simStateNative == SimStateNative.simStateNative_2_xmlParse_completed ||
				simStateNative == SimStateNative.simStateNative_7_terminate_completed ||
				simStateNative == SimStateNative.simStateNative_3_ready
				);
		
		
		lblStartTime_.setEnabled(startTimeEnabled);
		txtStartTime_.setEnabled(startTimeEnabled);
		
		lblStopTime_.setEnabled(stopTimeEnabled);
		txtStopTime_.setEnabled(stopTimeEnabled);
		
		lblStepDelta_.setEnabled(stepDeltaEnabled);
		txtStepDelta_.setEnabled(stepDeltaEnabled);
		
		btnApply_.setEnabled(applyButtonEnabled);
		
	}
	
	
}

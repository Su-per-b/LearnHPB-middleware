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

import com.sri.straylight.client.event.action.MetaDataChangeAction;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.voNative.MetaDataStruct;
import com.sri.straylight.fmuWrapper.voNative.State;

public class MetaDataController extends AbstractController {
	
	
	private JFormattedTextField txtStartTime_;
	private JFormattedTextField txtStopTime_;
	private JFormattedTextField txtStepDelta_;
	
	private final JButton btnApply_ = new JButton("Apply");

	private final JLabel lblStartTime_ = new JLabel("Start Time");
	private final JLabel lblStopTime_ = new JLabel("Stop Time");
	private final JLabel lblStepDelta_ = new JLabel("Step Delta");

	
	private MetaDataStruct metaDataStruct_;
	
	public MetaDataController(AbstractController parentController) {
		super(parentController);
	}


	private void bindActions_() {
		
		btnApply_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	//btnApply_.setEnabled(false);
            	updateDataModel_();
            	EventBus.publish(new MetaDataChangeAction(metaDataStruct_));
             }
           }
        );
		
	}
	
	
	@EventSubscriber(eventClass=FMUstateEvent.class)
	public void onFMUstateEvent(FMUstateEvent event) {
		
		if (event.fmuState == State.fmuState_level_1_xmlParsed) {
			//btnApply_.setEnabled(true);
		}
		
	}
	
	
	public void init(MetaDataStruct metaDataStruct) {
		
		metaDataStruct_ = metaDataStruct;
		
		txtStartTime_ = new JFormattedTextField(new Double(0.0));
		txtStartTime_.setEnabled(false);
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
	


	private void updateGUI_() {
		
		txtStartTime_.setValue(metaDataStruct_.defaultExperimentStruct.startTime);
		txtStopTime_.setValue(metaDataStruct_.defaultExperimentStruct.stopTime);
		txtStepDelta_.setValue(metaDataStruct_.stepDelta);
		
		
		//txtStartTime_.setText(Double.toString());
		//txtStopTime_.setText(Double.toString(metaDataStruct_.defaultExperimentStruct.stopTime));		
		//txtStepDelta_.setText(Double.toString(metaDataStruct_.stepDelta));		
	}
	
	private void updateDataModel_() {

		metaDataStruct_.defaultExperimentStruct.startTime = (double)txtStartTime_.getValue();
		metaDataStruct_.defaultExperimentStruct.stopTime = (double)txtStopTime_.getValue();
		
		metaDataStruct_.stepDelta = (double)txtStepDelta_.getValue();
		
	}
	
	
}

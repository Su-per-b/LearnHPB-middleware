package com.sri.straylight.client.controller;

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

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ViewInitialized;
import com.sri.straylight.client.view.BaseView;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.ConfigChangeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigController.
 */
public class ConfigController extends BaseController {
	
	private JFormattedTextField txtStartTime_;
	private JFormattedTextField txtStopTime_;
	private JFormattedTextField txtStepDelta_;
	
	/** The btn apply_. */
	private final JButton btnApply_ = new JButton("Apply");
	private final JLabel lblStartTime_ = new JLabel("Start Time");
	private final JLabel lblStopTime_ = new JLabel("Stop Time");
	private final JLabel lblStepDelta_ = new JLabel("Step Delta");
	private ConfigStruct configStruct_;
	
	private SimStateNative simStateNative_ = SimStateNative.simStateNative_0_uninitialized;
	
	private boolean isInititialized_ = false;
	
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
            	
        		setEnabled(false);
        		
            	updateDataModel_();
            	EventBus.publish(new ConfigChangeRequest(this,configStruct_));
             }
           }
        );
	}
	
	

	/**
	 * Inits the.
	 *
	 * @param configStruct the config struct
	 */
	private void init_() {
		
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
		
	
		BaseView theView = new BaseView("Config", this);
		theView.setLayout(new MigLayout("", "[][grow][290.00,grow]", "[][]"));
		
		theView.add(lblStartTime_, "cell 0 0,alignx trailing");
		theView.add(txtStartTime_, "cell 1 0,growx");
		
		theView.add(lblStopTime_, "cell 0 1,alignx trailing");
		theView.add(txtStopTime_, "cell 1 1,growx");

		theView.add(lblStepDelta_, "cell 0 2,alignx trailing");
		theView.add(txtStepDelta_, "cell 1 2,growx");	

		theView.add(btnApply_, "cell 1 4");
		
		setView_(theView);
		bindActions_();
		
		updateGUI_();
	    
	    new ViewInitialized(this, theView).fire();
	    
    }
	
	
	
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ConfigChangeNotify.class)
	public void onConfigChangeNotify(ConfigChangeNotify event) {

		configStruct_ = event.getPayload();
		
		if (isInititialized_) {
			updateGUIFromState_();
		} else {
			isInititialized_ = true;
			init_();
		}

	}

	
	/**
	 * On sim state request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateNativeRequest.class)
	public void onSimStateRequest(SimStateNativeRequest event) {
		setEnabled(false);
	}
	
	
	
	public void setEnabled (boolean isEnabled) {
		
		btnApply_.setEnabled(isEnabled);
		lblStartTime_.setEnabled(isEnabled);
		lblStopTime_.setEnabled(isEnabled);
		lblStepDelta_.setEnabled(isEnabled);
		
		if (null != txtStartTime_) {
			txtStartTime_.setEnabled(isEnabled);
			txtStopTime_.setEnabled(isEnabled);
			txtStepDelta_.setEnabled(isEnabled);
		}
	}
	
	
	
	
	@EventSubscriber(eventClass=SimStateNativeNotify.class)
	public void onSimStateNotify(SimStateNativeNotify event) {
	
		simStateNative_ = event.getPayload();
		updateGUIFromState_();
		
	}
	

	
	/**
	 * Update gui from state_.
	 */
	private void updateGUIFromState_() {

		boolean isEnabled = (simStateNative_ == SimStateNative.simStateNative_2_xmlParse_completed ||
				simStateNative_ == SimStateNative.simStateNative_7_terminate_completed
				);
		setEnabled(isEnabled);
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

package com.sri.straylight.client.view;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.FlowLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class SimulationConfig.
 */
public class SimulationConfig extends JPanel {
	
	/** The text field. */
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public SimulationConfig() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		JLabel lblNewLabel = new JLabel("Time Start");
		add(lblNewLabel);
		
		textField = new JTextField();
		add(textField);
		textField.setColumns(10);

	}

}
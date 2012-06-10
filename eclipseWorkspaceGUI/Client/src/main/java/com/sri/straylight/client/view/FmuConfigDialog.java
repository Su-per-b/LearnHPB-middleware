package com.sri.straylight.client.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import com.sri.straylight.client.model.Config;



public class FmuConfigDialog extends JDialog {
	
	private Config configModel_;
	
	public FmuConfigDialog(JFrame parent, Config configModel) {
		super(parent, "Select Simulation Run Time:", true);

		configModel_ = configModel;
		
		setContent_();
		
        // Show it.
        this.setSize(new Dimension(400, 150));
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
        
	}
	
	
	private void setContent_() {
		JPanel serverSelectionPanel = new JPanel();
    	serverSelectionPanel.setSize(600, 150);
    	

	    
	    setContentPane(serverSelectionPanel);
	    
	    
	}
	
	
	
	
}

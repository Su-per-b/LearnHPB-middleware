package com.sri.straylight.client.view;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sri.straylight.client.model.Config;



public class AboutDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Config configModel_;
	
	public AboutDialog(JFrame parent, Config configModel) {
		
		super(parent, "AboutDialog", true);

		configModel_ = configModel;
		
		setContent_();
		
        // Show it.
        this.setSize(new Dimension(400, 300));
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
        
	}
	
	
	private void setContent_() {
		JPanel serverSelectionPanel = new JPanel();
    	serverSelectionPanel.setSize(600, 150);
    	
	    setContentPane(serverSelectionPanel);
	}
	
	
	
	
}

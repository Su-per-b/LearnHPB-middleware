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

import com.sri.straylight.client.ConnectTo;
import com.sri.straylight.client.model.ClientConfig;




// TODO: Auto-generated Javadoc
/**
 * The Class SimulationEngineDialog.
 */
public class SimulationEngineDialog extends JDialog  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ClientConfig configModel_;
	
	
	/**
	 * Instantiates a new simulation engine dialog.
	 *
	 * @param parent the parent
	 * @param configModel the config model
	 */
	public SimulationEngineDialog(JFrame parent, ClientConfig configModel) {
		super(parent, "Select Simulation engine", true);

		configModel_ = configModel;
		
		setContent_();
		
        // Show it.
        this.setSize(new Dimension(400, 150));
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
        
	}
	
	
	
	/**
	 * Sets the content_.
	 */
	private void setContent_() {
		JPanel serverSelectionPanel = new JPanel();
    	serverSelectionPanel.setSize(600, 150);
    	
	    Border border = BorderFactory.createTitledBorder("Connect to:");
	    ButtonGroup serverSelectionGroup = new ButtonGroup();
	    
	    
	    JRadioButton rb_localhost = new JRadioButton("localhost");
	    rb_localhost.addActionListener(
    		new ActionListener() {
    		      public void actionPerformed(ActionEvent actionEvent) {
    		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
    		        System.out.println("Selected: " + aButton.getText());
    		        
    		        configModel_.connectTo = ConnectTo.connectTo_localhost;
    		      }
    		    }
	    		
	    );
	    
	    
	    JRadioButton rb_pfalco_local = new JRadioButton("Pfalco Local");
	    rb_pfalco_local.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
	    		        System.out.println("Selected: " + aButton.getText());
	    		        configModel_.connectTo = ConnectTo.connectTo_pfalco_local;
	    		      }
	    		    }
		    		
		    );
	    
	    
	    JRadioButton rb_pfalco_global = new JRadioButton("Pfalco global");
	    rb_pfalco_global.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
	    		        System.out.println("Selected: " + aButton.getText());
	    		        configModel_.connectTo = ConnectTo.connectTo_pfalco_global;
	    		      }
	    		    }
		    		
		    );
	    
	    
	    
	    JRadioButton rb_fmu_file = new JRadioButton("FMU file");
	    rb_fmu_file.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
	    		        System.out.println("Selected: " + aButton.getText());
	    		        configModel_.connectTo = ConnectTo.connectTo_file;
	    		      }
	    		    }
	    		
		    );
	    
	    
	    
	    
	    
	    serverSelectionGroup.add(rb_localhost);
	    serverSelectionGroup.add(rb_pfalco_global);
	    serverSelectionGroup.add(rb_pfalco_local);
	    
	    serverSelectionGroup.add(rb_fmu_file);
	    
	    serverSelectionPanel.setBorder(border);
	    serverSelectionPanel.add(rb_localhost);
	    serverSelectionPanel.add(rb_pfalco_global);
	    serverSelectionPanel.add(rb_pfalco_local);
	    serverSelectionPanel.add(rb_fmu_file);
	    
	    setContentPane(serverSelectionPanel);
	    
	    //set the default 
	    rb_localhost.setSelected(configModel_.connectTo == ConnectTo.connectTo_localhost);
	    rb_pfalco_global.setSelected(configModel_.connectTo == ConnectTo.connectTo_pfalco_global);
	    rb_pfalco_local.setSelected(configModel_.connectTo == ConnectTo.connectTo_pfalco_local);
	    rb_fmu_file.setSelected(configModel_.connectTo == ConnectTo.connectTo_file);
	    

	}
	

}


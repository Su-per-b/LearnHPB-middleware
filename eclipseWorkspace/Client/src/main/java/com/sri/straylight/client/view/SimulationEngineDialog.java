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

	    JRadioButton rb_straylightsim_com = new JRadioButton("wintermute.straylightsim.com");
	   // rb_straylightsim_com.setSelected(true);
	    rb_straylightsim_com.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
	    		        System.out.println("Selected: " + aButton.getText());
	    		        configModel_.connectTo = ConnectTo.connecTo_straylightsim_com;
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
	    serverSelectionGroup.add(rb_straylightsim_com);
	    serverSelectionGroup.add(rb_fmu_file);
	    
	    serverSelectionPanel.setBorder(border);
	    serverSelectionPanel.add(rb_localhost);
	    serverSelectionPanel.add(rb_straylightsim_com);
	    serverSelectionPanel.add(rb_fmu_file);
	    
	    setContentPane(serverSelectionPanel);
	    
	    rb_localhost.setSelected(configModel_.connectTo == ConnectTo.connectTo_localhost);
	    rb_straylightsim_com.setSelected(configModel_.connectTo == ConnectTo.connecTo_straylightsim_com);
	    rb_fmu_file.setSelected(configModel_.connectTo == ConnectTo.connectTo_file);
	    

	}
	

}


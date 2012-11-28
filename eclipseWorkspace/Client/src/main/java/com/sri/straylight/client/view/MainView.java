package com.sri.straylight.client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.sri.straylight.client.model.ClientConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class MainView.
 */
public class MainView extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	
	private ClientConfig configModel_;
	
    /**
     * Instantiates a new main view.
     *
     * @param configModel the config model
     */
    public MainView(ClientConfig configModel) {
    	
    	configModel_ = configModel;
    	
    	init();
    }
    
    
	/**
	 * Inits the.
	 */
	private void init() {
		
		setTitle ("Straylight Simulation Client");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
    	setPreferredSize(new Dimension(704, 800));
        setLayout(new BorderLayout(0, 0));
        
    	setPreferredSize(new Dimension(704, 800));
        setLayout(new BorderLayout(0, 0));

        ImageIcon imageIcon = new ImageIcon(configModel_.windowIconUrl);
        Image image = imageIcon.getImage();
        setIconImage(image);
        
    }
	
}

package com.sri.straylight.client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sri.straylight.client.model.ConfigClient;

public class MainView extends JFrame {
	
	
	private ConfigClient configModel_ = new ConfigClient();
	
    public MainView(ConfigClient configModel) {
    	
    	configModel_ = configModel;
    	
    	init();
    }
    
    
	private void init() {
		
		setTitle ("Straylight Simulation Client");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
    	setPreferredSize(new Dimension(704, 800));
        setLayout(new BorderLayout(0, 0));
        
    	setPreferredSize(new Dimension(704, 800));
        setLayout(new BorderLayout(0, 0));

        
    	//set icon
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL imageUrl = classLoader.getResource(ConfigClient.WindowIcon);

        ImageIcon imageIcon = new ImageIcon(imageUrl);
        Image image = imageIcon.getImage();
        setIconImage(image);
        
    }
	
}

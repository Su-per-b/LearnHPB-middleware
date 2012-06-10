package com.sri.straylight.client.view;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.client.event.menu.options.SelectSimulationEngine;
import com.sri.straylight.client.event.ui.MenuEvent_About_Help;
import com.sri.straylight.client.event.ui.SelectRuntime;
import com.sri.straylight.client.model.Config;
import com.sri.straylight.client.model.MainModel;

public class MainView {

	
	private JFrame jFrame_ ;
	private MainModel mainModel_;
    private JMenuBar menuBar_;
    public MainPanel mainPanel_;
    
    
    
	public MainView(MainModel mainModel) {
		
		mainModel_ = mainModel;
		init_();
		initMenu_();
	}
	
	public JFrame getJframe() {
		
		return jFrame_;
	}
	
	private void init_() {
		
						
		//Create and set up the window.
		JFrame.setDefaultLookAndFeelDecorated(true);
				
		jFrame_ = new JFrame("Straylight Simulation Client");
		jFrame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set icon
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL imageUrl = classLoader.getResource(Config.WindowIcon);
		
		ImageIcon imageIcon = new ImageIcon(imageUrl);
		Image image = imageIcon.getImage();
		jFrame_.setIconImage(image);
		
		//Create and set up the content pane.
		mainPanel_ = new MainPanel();
		mainPanel_.setOpaque(true); //content panes must be opaque
		jFrame_.setContentPane(mainPanel_);
		
		//Display the window.
		jFrame_.pack();
		jFrame_.setVisible(true);
	}
	
	private void initMenu_() {
		
		// Create the menu bar
		menuBar_ = new JMenuBar();
		
		initMenuOptions_();
		initMenuHelp_();
		
		// Install the menu bar in the frame
		jFrame_.setJMenuBar(menuBar_);
		
	}
	
	
	
	private void initMenuOptions_() {
		// Create a menu
		JMenu menu = new JMenu("Options");


		// Create a menu item
		JMenuItem item1 = new JMenuItem("Simulation Engine...");
		
		item1.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		    	  EventBus.publish(new SelectSimulationEngine());
	    		      }
	    		    }
				);
		
		
		menu.add(item1);
		
		JMenuItem item2 = new JMenuItem("FMU Config...");
		
		item2.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		    	  EventBus.publish(new SelectRuntime());
	    		      }
	    		    }
				);
		
		menu.add(item2);
		menuBar_.add(menu);
	
	}
	
	
	private void initMenuHelp_() {
	
		// Create a menu
		JMenu menu = new JMenu("Help");


		// Create a menu item
		JMenuItem item1 = new JMenuItem("About...");
		
		item1.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		    	  EventBus.publish(new MenuEvent_About_Help());
	    		      }
	    		    }
				);
		
		menu.add(item1);
		
		menuBar_.add(menu);

	}
	

}

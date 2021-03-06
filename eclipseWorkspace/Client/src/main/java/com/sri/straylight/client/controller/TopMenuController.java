package com.sri.straylight.client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.client.event.menu.About_Help;
import com.sri.straylight.client.event.menu.Options_SelectSimulationEngine;
import com.sri.straylight.fmuWrapper.framework.AbstractController;

// TODO: Auto-generated Javadoc
/**
 * The Class TopMenuController.
 */
public class TopMenuController  extends BaseController  {

	
    /** The menu bar_. */
    private JMenuBar menuBar_;
    
	/**
	 * Instantiates a new top menu controller.
	 *
	 * @param parentController the parent controller
	 */
	public TopMenuController(AbstractController parentController) {
		super(parentController);
		
		menuBar_ = new JMenuBar();
		
		initMenuOptions_();
		initMenuHelp_();
		
		setView_(menuBar_);
	}
	
	

	/**
	 * Gets the menu bar.
	 *
	 * @return the menu bar
	 */
	public JMenuBar getMenuBar() {
		return menuBar_;
	}
	
	
	
	/**
	 * Inits the menu options_.
	 */
	private void initMenuOptions_() {
		// Create a menu
		JMenu menu = new JMenu("Options");


		// Create a menu item
		JMenuItem item1 = new JMenuItem("Simulation Engine...");
		
		item1.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		    	  EventBus.publish(new Options_SelectSimulationEngine());
	    		      }
	    		    }
				);
		
		
		menu.add(item1);
		menuBar_.add(menu);
		
	}
	
	
	/**
	 * Inits the menu help_.
	 */
	private void initMenuHelp_() {
	
		// Create a menu
		JMenu menu = new JMenu("Help");


		// Create a menu item
		JMenuItem item1 = new JMenuItem("About...");
		
		item1.addActionListener(
	    		new ActionListener() {
	    		      public void actionPerformed(ActionEvent actionEvent) {
	    		    	  EventBus.publish(new About_Help());
	    		      }
	    		    }
				);
		
		menu.add(item1);
		
		menuBar_.add(menu);

	}
	
	






}

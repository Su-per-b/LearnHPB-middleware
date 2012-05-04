package com.sri.straylight.SwingGUI;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {
	
	
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
    	
        //Create and set up the window.
    	JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Straylight Simulation Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL imageUrl = classLoader.getResource(ConfigModel.WindowIcon);
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        Image image = imageIcon.getImage();
        frame.setIconImage(image);
        
        //Create and set up the content pane.
        MainPanel newContentPane = new MainPanel();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }

    
    
}

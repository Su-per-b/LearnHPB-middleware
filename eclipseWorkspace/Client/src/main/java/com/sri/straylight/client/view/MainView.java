package com.sri.straylight.client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.sri.straylight.client.model.ClientConfig;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

// TODO: Auto-generated Javadoc
/**
 * The Class MainView.
 */
public class MainView extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	
	private ClientConfig configModel_;
	
	/** The tabbed pane_. */
	private JTabbedPane tabbedPane_;
	
//	private Vector<BaseView> tabList_;
	
	private Hashtable<String, Boolean> tabList_ = new Hashtable<String, Boolean>();
	
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
        
        ImageIcon imageIcon = new ImageIcon(configModel_.windowIconUrl);
        Image image = imageIcon.getImage();
        setIconImage(image);
        
		tabbedPane_ = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane_, BorderLayout.CENTER);
    }


	public void showWindow() {
		
		// Display the window
		pack();
		setLocation(300,0);
		setVisible(true);

	}


	public void addTab(BaseView tabView) {
		
		String title = tabView.getTitle();
		
		if (!tabList_.containsKey(title)) {
			
			tabList_.put(title, true);
			
			tabbedPane_.addTab(
					tabView.getTitle(),
					null, 
					tabView, 
					null);
			
		}
		


		
	}
	
	
	
	
}

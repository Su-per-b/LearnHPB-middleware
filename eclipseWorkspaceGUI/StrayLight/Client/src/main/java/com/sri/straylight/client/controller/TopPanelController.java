package com.sri.straylight.client.controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.action.ClearDebugConsoleAction;
import com.sri.straylight.client.event.action.InitAction;
import com.sri.straylight.client.event.action.RunSimulationAction;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.voNative.State;




public class TopPanelController extends AbstractController {
	
    private final JButton btnClear_ = new JButton("Clear Debug Console");
    private final JButton btnInit_ = new JButton("Init");
    private final JButton btnRun_ = new JButton("Run Simulation");
    
    
    public TopPanelController (AbstractController parentController) {
		
		super(parentController);
		
        JPanel panel = new JPanel();
        
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        
        panel.setMaximumSize(new Dimension(32767, 60));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        
        btnRun_.setEnabled(false);

        panel.add(btnInit_);
        panel.add(btnRun_);
        panel.add(btnClear_);
        
        bindActions_();
        
        setView_(panel);
        
	}
	
	
	
	
	private void bindActions_() {
		
		btnInit_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	btnInit_.setEnabled(false);
            	EventBus.publish(new InitAction());
             }
           }
        );
		
		
        btnRun_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	btnRun_.setEnabled(false);
            	EventBus.publish(new RunSimulationAction());
             }
           }
        );
        
        btnClear_.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	EventBus.publish(new ClearDebugConsoleAction());
             }
           }
        );

	}
	

	
	@EventSubscriber(eventClass=FMUstateEvent.class)
    public void onFMUstateEvent(FMUstateEvent event) {
		
    	 btnRun_.setEnabled(event.fmuState == State.fmuState_level_5_initializedFMU);
    	 
    	 if (event.fmuState == State.fmuState_cleanedup) {
    		 btnInit_.setEnabled(true);
    	 }

    }
    

	

}

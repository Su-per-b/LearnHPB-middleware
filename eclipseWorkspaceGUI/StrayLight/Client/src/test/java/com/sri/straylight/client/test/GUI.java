package com.sri.straylight.client.test;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.HashMap;

import javax.swing.JButton;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sri.straylight.client.Main;
import com.sri.straylight.client.controller.MainController;
import com.sri.straylight.client.event.SimStateNotify;
import com.sri.straylight.client.model.SimStateClient;
import com.sri.straylight.fmuWrapper.event.ExceptionThrowingEventService;

public class GUI {

	private Container mainView_;
	
	private MainController applicationController;
	
	private HashMap<Integer, Point> buttonPointMap_;
	
	private Robot robot_;
	
	private static final int BTN_CONNECT = 0;
	private static final int BTN_XMLPARSE = 1;
	private static final int BTN_INIT = 2;
	private static final int BTN_RUN = 3;
	private static final int BTN_STEP = 4;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		System.out.println("@BeforeClass");

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("@AfterClass");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("@Before");
		init_();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("@After");
	}

	
	

	private void init_() throws AWTException {
		AnnotationProcessor.process(this);
		

		
		buttonPointMap_ = new HashMap<Integer, Point>();
		buttonPointMap_.put(BTN_CONNECT, new Point(20,60));
		buttonPointMap_.put(BTN_XMLPARSE, new Point(120,60));
		buttonPointMap_.put(BTN_INIT, new Point(188,60));
		buttonPointMap_.put(BTN_RUN, new Point(232,60));
		buttonPointMap_.put(BTN_STEP, new Point(284,60));
		
		robot_ = new Robot();
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
            	
        		System.setProperty(EventServiceLocator.SERVICE_NAME_EVENT_BUS,
        				ExceptionThrowingEventService.class.getName());
        		
        		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            	
            	DOMConfigurator.configure(Main.class.getResource("/log4j.xml"));
            	Logger  logger = Logger.getLogger(EventService.class.getCanonicalName());
            	
            	logger.setLevel(Level.WARN);
            	
    	 		try {
		 			EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_EVENT_BUS,
		 				new ExceptionThrowingEventService());
		 		} catch (EventServiceExistsException e) {
		 			logger.warn("Unable to register EventService.", e);
		 		}
            	
    	 		
            	
            	applicationController = new MainController();
        		mainView_ = applicationController.getView();
            }
        });
	}
	

	  
    private static void listComponents(Container container) {
		
        Component[] components = container.getComponents();
        for (int i = 0; i < components.length; i++) {
            System.out.println(components[i]);
            if (components[i] instanceof Container) {
                listComponents((Container) components[i]);
            }
        }
    }

    
    private static Component getComponentNamed(Container container, String theName) {
    	
		
        Component[] components = container.getComponents();
        
        for (int i = 0; i < components.length; i++) {
           // System.out.println(components[i]);
        	
        	Component theComponent = components[i];

        	if (theComponent.getName() == theName) {
        		return theComponent;
        	} else if (theComponent instanceof Container) {
        		
            	Component result = getComponentNamed((Container)theComponent, theName);
            	if (result != null) {
            		return result;
            	}
        	}
            
        }
        
        return null;
    }
    
    
    
	@Test
	public void ClickConnect() throws AWTException, InterruptedException {

		while(null == mainView_ || !mainView_.isShowing()) {
			robot_.delay(250);
		}
		
		robot_.delay(500);

		
		JButton btnConnect = (JButton) getComponentNamed(mainView_, "TopPanel.Button_Connect");
		btnConnect.doClick(250);
		
		JButton btnXml = (JButton) getComponentNamed(mainView_, "TopPanel.Button_XML_Parse");
		
		while(!btnXml.isEnabled()) {
			robot_.delay(250);
		}
		
		btnXml.doClick(250);
		
		JButton btnInit = (JButton) getComponentNamed(mainView_, "TopPanel.Button_Init");
		
		while(!btnInit.isEnabled()) {
			robot_.delay(250);
		}
		
		btnInit.doClick(250);
		
		robot_.delay(3000);
	}
	
	
	
	@SuppressWarnings("incomplete-switch")
	@EventSubscriber(eventClass=SimStateNotify.class)
    private void onSimStateNotify(SimStateNotify event) {
		
		SimStateClient simulationStateClient = event.getPayload();
		
		switch (simulationStateClient) {
			case level_1_connect_completed:
				clickButton_(BTN_XMLPARSE);
				break;
			case level_2_xmlParse_completed :
				clickButton_(BTN_INIT);
				break;
		}
		
	}
	
	
	
	private void clickButton_(Integer btn) {
		
		Point relativePoint = buttonPointMap_.get(btn);
		Point windowLocation = mainView_.getLocationOnScreen();
		
		
		Point absolutePoint = new Point (
				windowLocation.x + relativePoint.x,
				windowLocation.y + relativePoint.y
				);
		
		
		robot_.mouseMove(absolutePoint.x, absolutePoint.y);
		
	    robot_.mousePress(InputEvent.BUTTON1_MASK);
	    robot_.mouseRelease(InputEvent.BUTTON1_MASK);
	    
	}
	

}

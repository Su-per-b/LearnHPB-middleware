package com.sri.straylight.fmuWrapper.framework;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;

// TODO: Auto-generated Javadoc
/**
 * Abstract superclass for building a hierarchical controller structure (HMVC).
 * <p>
 * HMVC works with a tree of triads, these triads are a Model (usually several
 * JavaBeans and their binding models for the UI), a View (usually several Swing
 * UI components), and a Controller. This is a basic implementation of a controller
 * that has a pointer to a parent controller (can be null if its the root of the
 * tree) and a collection of subcontrollers (can be empty, usually isn't empty).
 * <p>
 * This needs to be subclassed to be used; if subclassed directly for an application,
 * use an appropriate constructor.
 * <p>
 * The hierarchy of controller supports propagation of action execution and
 * propagation of events.
 * <p>
 * If a controllers view is a <tt>Frame</tt>, you should also register it as a
 * <tt>WindowListener</tt>, so that it can properly clean up its state when the
 * window is closed.
 *
 * @author Christian Bauer
 */
public abstract class AbstractController implements ActionListener, WindowListener {

    /** The log. */
    private static Log log = LogFactory.getLog(AbstractController.class);

    /** The view. */
    protected Container view_;
    
    /** The parent controller. */
    private AbstractController parentController;
    
    /** The sub controllers. */
    private List<AbstractController> subControllers = new ArrayList<AbstractController>();
    
    /** The actions. */
    private Map<String, DefaultAction> actions = new HashMap<String, DefaultAction>();
    
    /** The event listeners. */
//    private Map<Class, java.util.List<DefaultEventListener>> eventListeners =
//            new HashMap<Class, java.util.List<DefaultEventListener>>();
    
    private Map	<  Class<? extends BaseEvent<?>>, 
    			   List<StraylightEventListener<? extends BaseEvent<?>,?>>  
    			> eventListeners = new HashMap	<  Class<? extends BaseEvent<?>>, 
    		 			   List<StraylightEventListener<? extends BaseEvent<?>,?>>  
     			>();
    
    

    
    
    
    /**
     * Subclass wants to control own view and is root controller.
     */
    public AbstractController() { 
    	this(null, null);
    }

    /**
     * Subclass wants to control own view and is a subcontroller.
     *
     * @param parentController the parent controller
     */
    public AbstractController(AbstractController parentController) {
        this(null, parentController);
    }

    /**
     * Subclass is completely dependend on the given view and is a subcontroller.
     *
     * @param view the view
     * @param parentController the parent controller
     */
    public AbstractController(Container view, AbstractController parentController) {

    	init_(view, parentController);
    }
    
    /**
     * Init_.
     *
     * @param view the view
     * @param parentController the parent controller
     */
    private void init_(Container view, AbstractController parentController) {
    	AnnotationProcessor.process(this);
    	
        this.view_ = view;

        // Check if this is a subcontroller or a root controller
        if (parentController != null) {
            this.parentController = parentController;
            parentController.getSubControllers().add(this);
        }

    }
    
    
    public void setParentController (AbstractController parentController) {
        // Check if this is a subcontroller or a root controller
        if (parentController != null) {
            this.parentController = parentController;
            parentController.getSubControllers().add(this);
        }
    }
    
    /**
     * Gets the view.
     *
     * @return the view
     */
    public Container getView() {
        return view_;
    }
    
    /**
     * Sets the view_.
     *
     * @param c the new view_
     */
    protected void setView_(Container c) {
        view_ = c;
    }
    
    /**
     * Gets the parent controller.
     *
     * @return the parent controller
     */
    public AbstractController getParentController() {
        return parentController;
    }

    /**
     * Gets the sub controllers.
     *
     * @return the sub controllers
     */
    public java.util.List<AbstractController> getSubControllers() {
        return subControllers;
    }


    /**
     * Close controller and all children, detach it from parent controller.
     */
    public void dispose() {
        log.debug("Disposing controller");
        for (AbstractController subController : getSubControllers()) {
            subController.dispose();
        }
        if (getParentController() != null) {
            getParentController().getSubControllers().remove(this);
        }
    }

    /**
     * Register an action that can be executed by this controller.
     *
     * @param source The source component, this method sets action command and registers the controller as listener.
     * @param actionCommand The action command, used as a key when registering and executing actions.
     * @param action An actual action implementation.
     */
    public void registerAction(AbstractButton source, String actionCommand, DefaultAction action) {
        source.setActionCommand(actionCommand);
        source.addActionListener(this);
        this.actions.put(actionCommand, action);
    }


    /**
     * Register an event listener that is being executed when an event is intercepted by this controller.
     *
     * @param eventClass The actual event class this listeners is interested in.
     * @param eventListener The listener implementation.
     */
    public void registerEventListener(
    		Class<? extends BaseEvent<?>> eventClass, 
    		StraylightEventListener<? extends BaseEvent<?>,?> eventListener) 
    {
    	
    	
        log.debug("Registering listener: " + eventListener + " for event type: " + eventClass.getName());
        
        List<StraylightEventListener<? extends BaseEvent<?>,?>>  listenersForEvent = eventListeners.get(eventClass);
        
        if (listenersForEvent == null) { 
        	
        	//listenersForEvent = new ArrayList<StraylightEventListener<? extends BaseEvent<?>,?>>(); 
        	listenersForEvent = new ArrayList<StraylightEventListener<? extends BaseEvent<?>,?>>();
        	
        }
        
        listenersForEvent.add(eventListener);
        eventListeners.put(eventClass, listenersForEvent);
    }


    public void unregisterEventListener(
    		Class<? extends BaseEvent<?>> eventClass) 
    {
    	
    	eventListeners.remove(eventClass);
    	
    }
    
    public void unregisterAllEventListener() 
    {
    	
    	eventListeners.clear();

    }
    
    
    
    
    /**
     * Fire an event and pass it into the hierarchy of controllers.
     * <p>
     * The event is propagated only to the controller instance and its subcontrollers, not upwards in the hierarchy.
     *
     * @param event The event to be propagated.
     */
//    public void fireEvent(DefaultEvent event) {
//        fireEvent(event, false);
//    }
    
    public void fireEvent(BaseEvent<?> event) {
    	fireEvent(event, false);
    }

    
    
    
    /**
     * Fire an event and pass it into the hierarchy of controllers.
     * <p>
     * The event is propagated to the controller instance, its subcontrollers, and upwards into the controller
     * hierarchy. This operation effectively propagats the event to every controller in the whole hierarchy.
     *
     * @param event The event to be propagated.
     */
    public void fireEventGlobal(BaseEvent<?> event) {
        fireEvent(event, true);
    }

    
    /**
     * Fire event.
     *
     * @param event the event
     * @param global the global
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected void fireEvent(BaseEvent<?> event, boolean global) {
    	
        if (!event.alreadyFired(this)) {
        	
        	Class<?> eventClass = event.getClass();
        	
            if (eventListeners.get(eventClass) != null) {
            	
            	List<StraylightEventListener<? extends BaseEvent<?>,?>>  listenersForEvent = eventListeners.get(eventClass);
            	
                for (StraylightEventListener eventListener : listenersForEvent) {
                    log.debug("Event: " + event.getClass().getName() + " with listener: " + eventListener.getClass().getName());
                    eventListener.handleEvent(event);
                }
            }
            
            event.addFiredInController(this);
            log.debug("Passing event: " + event.getClass().getName() + " DOWN in the controller hierarchy");
            for (AbstractController subController : subControllers) subController.fireEvent(event, global);
        }
        if (getParentController() != null
            && !event.alreadyFired(getParentController())
            && global) {
            log.debug("Passing event: " + event.getClass().getName() + " UP in the controller hierarchy");
            getParentController().fireEvent(event, global);
        }
    }

    

    
    
    /**
     * Executes an action if it has been registered for this controller, otherwise passes it up the chain.
     * <p>
     * This method extracts the source of the action (an <tt>AbstractButton</tt>) and gets the action
     * command. If the controller has this command registered, the registered action is executed. Otherwise
     * the action is passed upwards in the hierarchy of controllers.
     *
     * @param actionEvent the action event
     */
    public void actionPerformed(ActionEvent actionEvent) {

        try {
            AbstractButton button = (AbstractButton)actionEvent.getSource();
            String actionCommand = button.getActionCommand();
            DefaultAction action = actions.get(actionCommand);

            if (action != null) {
                // This controller can handle the action
                log.debug("Handling command: " + actionCommand + " with action: " + action.getClass());
                try {
                    preActionExecute();
                    log.debug("Dispatching to action for execution");
                    action.executeInController(this, actionEvent);
                    postActionExecute();
                } catch (Exception ex) {
                    failedActionExecute();
                    throw new RuntimeException(ex);
                } finally {
                    finalActionExecute();
                }
            } else {
                // Let's try the parent controller in the hierarchy
                if(getParentController() != null) {
                    log.debug("Passing action on to parent controller");
                    parentController.actionPerformed(actionEvent);
                } else {
                    throw new RuntimeException("Nobody is responsible for action command: " + actionCommand);
                }
            }
        }
        catch(ClassCastException e) {
            throw new IllegalArgumentException("Action source is not an Abstractbutton: " + actionEvent);
        }
    }

    /**
     * Pre action execute.
     */
    protected void preActionExecute() {}
    
    /**
     * Post action execute.
     */
    protected void postActionExecute() {}
    
    /**
     * Failed action execute.
     */
    protected void failedActionExecute() {}
    
    /**
     * Final action execute.
     */
    protected void finalActionExecute() {}

    // If this controller is responsible for a JFrame, close it and all its children when the
    // window is closed.
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent windowEvent) { dispose(); }
    
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(WindowEvent windowEvent) {}
    
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent windowEvent) {}
    
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(WindowEvent windowEvent) {}
    
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */
    public void windowDeiconified(WindowEvent windowEvent) {}
    
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent windowEvent) {}
    
    /* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */
    public void windowDeactivated(WindowEvent windowEvent) {}

}


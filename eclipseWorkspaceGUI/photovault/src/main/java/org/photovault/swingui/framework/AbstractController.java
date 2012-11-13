/*
  Copyright (c) 2005, Christian Bauer <christian@hibernate.org>
  Copyright (c) 2007, Harri Kaimio
  
  This file is part of Photovault.

  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA 

 
 
  Based on SWING/Hibernate framework by Christian Bauer. Original SW 
  licensed with the following conditions:

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are met:

  - Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

  - Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

  Neither the name of the original author nor the names of contributors may be
  used to endorse or promote products derived from this software without
  specific prior written permission.
 */

package org.photovault.swingui.framework;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.util.*;

import javax.swing.*;
import org.photovault.command.CommandHandler;

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

    private static Log log = LogFactory.getLog(AbstractController.class);

    private Container view;
    private AbstractController parentController;
    private java.util.List<AbstractController> subControllers = new ArrayList<AbstractController>();
    private Map<String, DefaultAction> actions = new HashMap<String, DefaultAction>();
    private Map<Class, java.util.List<DefaultEventListener>> eventListeners =
            new HashMap<Class, java.util.List<DefaultEventListener>>();

    /**
     * Subclass wants to control own view and is root controller.
     */
    public AbstractController() {}

    /**
     * Subclass wants to control own view and is a subcontroller.
     *
     * @param parentController
     */
    public AbstractController(AbstractController parentController) {
        this(null, parentController);
    }

    /**
     * Subclass is completely dependend on the given view and is a subcontroller.
     * @param view
     * @param parentController
     */
    public AbstractController(Container view, AbstractController parentController) {
        this.view = view;

        // Check if this is a subcontroller or a root controller
        if (parentController != null) {
            this.parentController = parentController;
            parentController.getSubControllers().add(this);
        }
    }


    public Container getView() {
        return view;
    }

    public AbstractController getParentController() {
        return parentController;
    }

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
        final String cmd = actionCommand;
        final AbstractController ctrl = this;
        // TODO: Is this really needed???
        action.addPropertyChangeListener( new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( evt.getPropertyName().equals( "enabled" ) ) {
                    fireEvent( new ActionStateChangeEvent( ctrl, cmd, (Boolean) evt.getNewValue() ) );
                }
            }
            
        });
    }

    /**
     Register an action that can be executed by this controller. This method does 
     not register the controller as a listener for the action, so this must be 
     done elsewhere.
     
     @param actionCommand The action command, used as a key when registering and 
     executing actions.
     @param action An actual action implementation.
     */
    public void registerAction( String actionCommand, DefaultAction action ) {
        this.actions.put(actionCommand, action);        
    }

    /**
     * Register an event listener that is being executed when an event is intercepted by this controller.
     *
     * @param eventClass The actual event class this listeners is interested in.
     * @param eventListener The listener implementation.
     */
    public void registerEventListener(Class eventClass, DefaultEventListener eventListener) {
        log.debug("Registering listener: " + eventListener + " for event type: " + eventClass.getName());
        java.util.List<DefaultEventListener> listenersForEvent = eventListeners.get(eventClass);
        if (listenersForEvent == null) { listenersForEvent = new ArrayList<DefaultEventListener>(); }
        listenersForEvent.add(eventListener);
        eventListeners.put(eventClass, listenersForEvent);
    }

    /**
     * Fire an event and pass it into the hierarchy of controllers.
     * <p>
     * The event is propagated only to the controller instance and its subcontrollers, not upwards in the hierarchy.
     *
     * @param event The event to be propagated.
     */
    public void fireEvent(DefaultEvent event) {
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
    public void fireEventGlobal(DefaultEvent event) {
        fireEvent(event, true);
    }

    private void fireEvent(DefaultEvent event, boolean global) {
        if (!event.alreadyFired(this)) {
            if (eventListeners.get(event.getClass()) != null) {
                for (DefaultEventListener eventListener : eventListeners.get(event.getClass())) {
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
     Get an {@link ActionAdapter} that can be bind to Swing component and 
     fires the given action command in this controller
     @param actionCommand The action command that will be performed by this adapter
     @return Adapter to command registered to this action.
     */
    public Action getActionAdapter( String actionCommand ) {
        Action action = actions.get( actionCommand );
        Action adapter = null;
        if ( action != null ) {
            adapter = new ActionAdapter( action, actionCommand, this );
        } else if ( parentController != null ) {
            adapter = parentController.getActionAdapter( actionCommand );
        } 
        return adapter;
    }

    /**
     * Executes an action if it has been registered for this controller, otherwise passes it up the chain.
     * <p>
     * This method extracts the source of the action (an <tt>AbstractButton</tt>) and gets the action
     * command. If the controller has this command registered, the registered action is executed. Otherwise
     * the action is passed upwards in the hierarchy of controllers.
     *
     * @param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {

        try {
            AbstractButton button = (AbstractButton)actionEvent.getSource();
            // String actionCommand = button.getActionCommand();
            String actionCommand = actionEvent.getActionCommand();
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

    protected void preActionExecute() {}
    protected void postActionExecute() {}
    protected void failedActionExecute() {}
    protected void finalActionExecute() {}

    // If this controller is responsible for a JFrame, close it and all its children when the
    // window is closed.
    public void windowClosing(WindowEvent windowEvent) { dispose(); }
    public void windowOpened(WindowEvent windowEvent) {}
    public void windowClosed(WindowEvent windowEvent) {}
    public void windowIconified(WindowEvent windowEvent) {}
    public void windowDeiconified(WindowEvent windowEvent) {}
    public void windowActivated(WindowEvent windowEvent) {}
    public void windowDeactivated(WindowEvent windowEvent) {}

    /**
     Command handler used for executing commands in this controller
     */
    protected CommandHandler commandHandler = null;
    
    /**
     Get the command handler that is used with this controller
     */
    public CommandHandler getCommandHandler() {
        CommandHandler ret = commandHandler;
        if ( ret == null && getParentController() != null ) {
            ret = getParentController().getCommandHandler();
        }
        return ret;
    }

    /**
     Set the command handler associated with this controller
     */
    public void setCommandHandler(CommandHandler c) {
        commandHandler = c;
    }
    

}


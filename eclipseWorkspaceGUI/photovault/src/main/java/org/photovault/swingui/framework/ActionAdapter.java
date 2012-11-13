/*
  Copyright (c) 2007 Harri Kaimio
  
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
*/

package org.photovault.swingui.framework;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 This class wraps an Action and executes it inside given {@link AbstractController}.
 
 */
public class ActionAdapter implements Action, PropertyChangeListener {
    
    /** 
     Creates a new instance of ActionAdapter 
     @param action The action that will be executed by actionPerformed method.
     @param ctrl The controller in which the action will be executed
     */
    public ActionAdapter( Action action, String command, AbstractController ctrl ) {
        this.action = action;
        action.addPropertyChangeListener( this );
        this.ctrl = ctrl;
        this.command = command;
    }

    Action action;
    AbstractController ctrl;
    String command;
    
    public void actionPerformed(ActionEvent e) {
        // Create a new event that contains the correct action command.
        ActionEvent newEvent = new ActionEvent( e.getSource(), e.getID(), 
                command, e.getWhen(), e.getModifiers() );
        ctrl.actionPerformed( newEvent );
        
    }

    public Object getValue(String key) {
        return action.getValue( key );
    }

    public void putValue(String key, Object value) {
        action.putValue( key, value );
    }

    public void setEnabled(boolean b) {
        action.setEnabled( b );
    }

    public boolean isEnabled() {
        return action.isEnabled();
    }

    Set<PropertyChangeListener> propertyChangeListeners = 
            new HashSet<PropertyChangeListener>();
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeListeners.add( listener );
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeListeners.remove( listener );
    }

    public void propertyChange( PropertyChangeEvent ev ) {
        firePropertyChangeEvent( ev );        
    }
    
    private void firePropertyChangeEvent( PropertyChangeEvent ev ) {
        for ( PropertyChangeListener l : propertyChangeListeners ) {
            l.propertyChange( ev );
        }
    }
}

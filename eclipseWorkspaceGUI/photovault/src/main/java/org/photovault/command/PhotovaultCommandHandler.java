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

package org.photovault.command;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.context.ManagedSessionContext;
import org.hibernate.type.Type;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;

/**
  Basic command handler for Photovault.
 */
public class PhotovaultCommandHandler implements CommandHandler {
    
    static private Log log = 
            LogFactory.getLog( PhotovaultCommandHandler.class.getName() );
    
    Session session;
    
    /** Creates a new instance of PhotovaultCommandHandler */
    public PhotovaultCommandHandler( Session session ) {
        this.session = session;
    }

    public Command executeCommand(Command command) throws CommandException {
        command.execute();
        return command;
    }

    public DataAccessCommand executeCommand(DataAccessCommand command) 
            throws CommandException {
        Session commandSession = session;
        ChangeInterceptor changeInterceptor = null;
        boolean shouldCloseSession = false;
        if ( commandSession == null ) {
            changeInterceptor = new ChangeInterceptor();
            commandSession = HibernateUtil.getSessionFactory().openSession( changeInterceptor );
            shouldCloseSession = true;
        }
        Session oldSession = ManagedSessionContext.bind( (org.hibernate.classic.Session) commandSession);
        HibernateDAOFactory df =
                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        df.setSession( commandSession );
        command.setDAOFactory( df );

        Transaction tx = commandSession.beginTransaction();
        try {
            command.execute();
            commandSession.flush();
            tx.commit();
            fireCommandEvent( new CommandExecutedEvent( this, command ) );
        } catch ( Exception e ) {
            tx.rollback();
            throw new CommandException( "Exception thrown by command: ", e );
        } finally {
            if ( shouldCloseSession ) {
                commandSession.close();
            }

            if ( oldSession != null ) {
                ManagedSessionContext.bind(
                        (org.hibernate.classic.Session) oldSession );
            } else {
                ManagedSessionContext.unbind( HibernateUtil.getSessionFactory() );
            }

            if ( changeInterceptor != null ) {
                for ( Object o : changeInterceptor.getChangedObjects() ) {
                    fireChangeEvent( o );
                }
            }

        }
        return command;
    }
        
    Set<CommandChangeListener> listeners = new HashSet<CommandChangeListener>();
    
    public void addChangeListener( CommandChangeListener l ) {
        listeners.add( l );
    }
    
    public void removeChangeListener( CommandChangeListener l ) {
        listeners.remove( l );
    }
    
    private void fireChangeEvent( Object o ) {
        for ( CommandChangeListener l : listeners ) {
            l.entityChanged( o );
        }
    }
    
    static class ChangeInterceptor extends EmptyInterceptor {
        
        Set changedObjects = new HashSet();
        
        public void onDelete(Object entity,
                Serializable id,
                Object[] state,
                String[] propertyNames,
                Type[] types) {
            changedObjects.add( entity );
        }

        public boolean onSave(Object entity,
                Serializable id,
                Object[] state,
                String[] propertyNames,
                Type[] types) {
            changedObjects.add( entity );
            return false;
        }

        public boolean onFlushDirty(Object entity,
                Serializable id,
                Object[] state,
                Object[] prevousState,
                String[] propertyNames,
                Type[] types) {
            changedObjects.add( entity );
            return false;
        }
        
        public Set getChangedObjects()  {
            return changedObjects;
        }

    }
    
    Set<CommandListener> commandListeners = new HashSet<CommandListener>();

    /**
     Add a new listener that will be notified of succesful execution of a 
     command. The listener will be called after the transaction for the command
     has been completed, so databatase will reflect changes caused by the command
     at the time of a call.
     @param l The new listener
     */
    public void addCommandListener( CommandListener l ) {
        commandListeners.add( l );
    }

    /**
     Remove an existing command listener
     @param l The listener that is removed.
     */
    public void removeCommandListener( CommandListener l ) {
        commandListeners.remove( l );
    }

    /**
     Send an event to all listeners
     @param event The event that will be sent.
     */
    private void fireCommandEvent( CommandExecutedEvent event ) {
        for ( CommandListener l : commandListeners ) {
            l.commandExecuted( event );
        }
    }
}

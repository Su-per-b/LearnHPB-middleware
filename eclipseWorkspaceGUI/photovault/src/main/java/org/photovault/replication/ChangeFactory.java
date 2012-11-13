/*
  Copyright (c) 2008 Harri Kaimio
  
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

package org.photovault.replication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.persistence.DAOFactory;

/**
 ChangeFactory provides a link between the change replication logic and local 
 persistence management layer. It can be used to create local persistent 
 instances from a serialized change.
 */
public class ChangeFactory<T> {
    static private Log log = LogFactory.getLog( ChangeFactory.class );
    /**
     DAO used for accessing local persistent data.
     */
    ChangeDAO<T> dao;
    
    /**
     Construct a new ChangeFactory
     @param dao The DAO that is used to access local persistent data
     */
    public ChangeFactory( ChangeDAO<T> dao ) {
        this.dao = dao;
    }
    
    /**
     Reads a serialized {@link ChangeDTO} from input stream and persists the 
     {@link Change} described by it in the context associated with this factory 
     if the change is not yet known in this context.
     @param is The stream from which the change is read
     @return The read change
     @throws IOException if reading the change fails
     @throws ClassNotFoundException if the change subclass or some of its fields
     are not known.
     @throws IllegalStateException if the serialized change is somehow corrupted
     (i.e. its content does not match its UUID)
     */
    public Change<T> readChange( ObjectInputStream is ) 
            throws IOException, ClassNotFoundException {
        ChangeDTO<T> data = (ChangeDTO<T>) is.readObject();
        return createChange( data );
    }

    public void addObjectHistory( ObjectHistoryDTO<T> h ) 
            throws ClassNotFoundException, IOException {
        log.debug( "addObjectHistory: entry" );
        UUID targetUuid = h.getTargetUuid();
        ObjectHistory<T> targetHistory = dao.findObjectHistory( h.getTargetUuid() );
        if ( targetHistory == null ) {
            /*
            The target object was not known to local database.
            Create local copy
             */
            log.debug( "addObjectHistory: creating " + h.getTargetClassName() + " " + h.getTargetUuid() );
            targetHistory = createTarget( h.getTargetClassName(), h.getTargetUuid() );
        }
        for ( ChangeDTO<T> ch : h.getChanges() ) {
            if ( !targetUuid.equals( ch.targetUuid) ) {
                // TODO: handle error!!!
            }
            addChange( targetHistory, ch );
        }
        dao.flush();
    }

    
    /**
     Creates a local persistent change based on a DTO, if the change is not yet 
     known. The change is also added to the correct history object.
     @param data The change DTO
     @return Local persisted change object based on the DTO. If the change was
     already known, the old change is returned. Otherwise, a new Change is 
     created.
     @throws java.lang.ClassNotFoundException
     @throws java.io.IOException
     */
    private Change<T> createChange( ChangeDTO<T> data ) 
            throws ClassNotFoundException, IOException {
        UUID changeUuid = data.changeUuid;
        Change<T> existingChange = dao.findChange( changeUuid );
        if ( existingChange != null ) {
            return existingChange;
        }
        /*
        The change is not yet known in this context. Add it to the history of
        the target object.
         */
        ObjectHistory<T> targetHistory = dao.findObjectHistory( data.targetUuid );
        Change<T> change = null;
        if ( targetHistory == null ) {
            /*
            The target object was not known to local database.
            Create local copy
             */
            targetHistory = createTarget( data.targetClassName, data.targetUuid );
            change = targetHistory.getVersion();
        } else {
            change = addChange( targetHistory, data );
            dao.flush();
        }
        return change;
    }

    /**
     * Creates the target object of a change
     * @param ch The change dto
     * @return History of the created object.
     * @throws java.lang.ClassNotFoundException if target class is not found
     * @throws java.io.IOException if the target class cannot be instantiated
     */
    private ObjectHistory<T> createTarget( String className, UUID uuid )
            throws ClassNotFoundException, IOException {
        ObjectHistory<T> targetHistory;

        try {
            Class targetClass = Class.forName( className );
            DAOFactory df;
            VersionedObjectEditor<T> e =
                    new VersionedObjectEditor<T>( targetClass, uuid, null );
            T target = e.getTarget();
            dao.makePersistent( target );
            dao.flush();
            targetHistory = e.history;
        } catch ( InstantiationException ex ) {
            throw new IOException( "Cannot instantiate history of class " +
                    className + " for object " + uuid, ex );
        } catch ( IllegalAccessException ex ) {
            throw new IOException( "Cannot instantiate history of class " +
                    className + " for object " + uuid, ex );
        }
        return targetHistory;
    }

    /**
     * Add change based on change DTO to existing object's history.
     * @param targetHistory History of the object
     * @param data Change DTO describing the change
     * @return Added change
     */
    private Change<T> addChange( ObjectHistory<T> targetHistory, ChangeDTO<T> data ) {

        log.debug( "addChange: entry" );
        Change<T> change = targetHistory.getChange( data.changeUuid );
        if ( change != null ) {
            log.debug( "addChange: change already exists" );
            return change;
        }
        change = new Change<T>();
        change.setTargetHistory( targetHistory );
        change.setUuid( data.changeUuid );
        change.setSerializedChange( data.xmlData );
        // Try to find parents of this change
        Set<Change<T>> parents = new HashSet<Change<T>>();
        for ( UUID parentId : data.parentIds ) {
            Change<T> parent = dao.findById( parentId, false );
            parents.add( parent );
            parent.addChildChange( change );
        }
        change.setParentChanges( parents );
        change.setChangedFields( data.changedFields );
        dao.makePersistent( change );
        // dao.flush();
        targetHistory.addChange( change );
        log.debug( "addChange: exit" );
        return change;
    }
}

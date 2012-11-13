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
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 Data transfer object to transfer or store information about object's history. 
 This is just a simple container for {@link ChangeDTO} instances related to a 
 single versioned object. It can contain either the whole history or just part 
 of it.

 @author Harri Kaimio
 @since 0.6.0
 */
public class ObjectHistoryDTO<T> implements Serializable {

    static final long serialVersionUID = 8078939998849780257l;

    
    /**
     UUID of the target object
     @serial
     */
    private UUID targetUuid;
    
    /**
     Name of the target class
     @serial 
     */
    private String targetClassName;    
    
    /**
     List of changes in the history, topologically sorted so that all parents
     of a change are before it in the list
     */
    private transient List<ChangeDTO> changes;
    
    /**
     Constructor, for serialization
     */
    private ObjectHistoryDTO() {}
    
    /**
     Constructor
     @param history History of the object
     */
    public ObjectHistoryDTO( ObjectHistory<T> history ) {
        targetUuid = history.getTargetUuid();
        targetClassName = history.getTargetClassName();
        changes = new ArrayList();
        Set<UUID> writtenIds = new HashSet<UUID>();
        for ( Change<T> ch : history.getHeads() ) {
            prepareDtos( ch, writtenIds );
        }
    }

    public ObjectHistoryDTO( Class<T> targetClass, UUID uuid ) {
        targetUuid = uuid;
        targetClassName = targetClass.getName();
        changes = new ArrayList();
    }

    /**
     Returns UUID of the target object
     */
    public UUID getTargetUuid() {
        return targetUuid;
    }

    /**
     Returns name of the target object's class
     */
    public String getTargetClassName() {
        return targetClassName;
    }

    /**
     Returns list of the changes, topologically sorted from oldest to youngest.
     */
    public List<ChangeDTO> getChanges() {
        return Collections.unmodifiableList( changes );
    }    

    /**
     * Add new change to the change history
     * @param ch The change to add
     */
    public void addChange( ChangeDTO ch ) {
        changes.add( ch );
    }

    /**
     Prepares {@link ChangeDTO} objects for serialization for certain change and 
     its predecessors if they have not been prepared already. This fuction is 
     called by writeChanges().
     
     @param ch The change
     @param writtenIds Set of uuids of those changes that have been prepared 
     already. IDs of changes prepared by this call are added to the set.
     @param dtoList List of DTOs. DTOs prepared by this method will be appended 
     to end of the list.
     */
    private void prepareDtos( Change<T> ch, Set<UUID> writtenIds )  {
        // First, ensure that all predecessors are written
        for ( Change<T> parent : ch.getParentChanges() ) {
            if ( !writtenIds.contains( parent.getUuid() ) ) {
                prepareDtos( parent, writtenIds );
            }
        }
        ChangeDTO<T> dto = new ChangeDTO<T>( ch );
        changes.add( dto );
        writtenIds.add( ch.getUuid() );
    }    
    
    private void writeObject( ObjectOutputStream os ) throws IOException {
        os.defaultWriteObject();
        os.writeInt( changes.size() );
        for ( ChangeDTO<T> ch : changes ) {
            byte[] data = ch.getXmlData();
            os.writeInt( data.length );
            os.write( data );
        }
    }

    /**
     @serialData First, the default serialized fields are stored. The next field
     is an integer that tells the number of changes stored in this container,
     followed by the {@link ChangeDTO} objects in topologically sorted order,
     so that all ancestors are always before their children.
     @param is
     @throws java.io.IOException
     @throws java.lang.ClassNotFoundException
     */
    private void readObject( ObjectInputStream is ) throws IOException, ClassNotFoundException {
        is.defaultReadObject();
        int changeCount = is.readInt();
        changes = new ArrayList( changeCount );
        Class targetClass = Class.forName( targetClassName );
        for ( int n = 0 ; n < changeCount ; n++ ) {
            int dataSize = is.readInt();

            byte[] data = new byte[dataSize];
            is.readFully( data );
            ChangeDTO ch = ChangeDTO.createChange( data, targetClass );
            changes.add( ch );
        }
        
    }

}

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
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 Manages version history and version changes for a single object. Each versioned 
 object must maintain an embedden instance of this class and provide a method
 for accessing it (indicated by the {@link History} annotation.
 
 @author Harri Kaimio
 @since 0.6
 
 @param <T> Class of the target obejct
 */
@Entity
@Table(name = "pv_version_histories")
public class ObjectHistory<T> {

    static private Log log = LogFactory.getLog( ObjectHistory.class.getName()  );
    
    /**
     Name of the class of target
     */
    String targetClassName;    
    
    /**
     Identifier of this change history. THis is the same as the UUID of the 
     target object
     */
    private UUID uuid;
    
    /**
     Current version of the target object
     */
    Change<T> currentVersion;
    
    /**
     Head changes (i.e. changes that do not have children)
     */
    Set<Change<T>> heads = new HashSet<Change<T>>();
    
    /**
     All changes to target obejct that are known 
     */
    Set<Change<T>> allChanges = new HashSet<Change<T>>();


    /**
     Default constructor for persistence & replication layers, do not use otherwise
     */
    public ObjectHistory() {}
    
    /**
     Constructor
     @param target
     */
    public ObjectHistory( T target ) {
        targetClassName = target.getClass().getName();
    }
    
    /**
     Returns the UUID of the target obejct
     */
    @Id
    @Column( name = "uuid" )
    @org.hibernate.annotations.Type( type = "org.photovault.persistence.UUIDUserType" )    
    public UUID getTargetUuid() {
        return uuid;
    } 
    
    public void setTargetUuid( UUID uuid ) {
        this.uuid = uuid;
    }

    /**
     Add a new change to change history. Called bu {@link Change} when it is 
     freezed
     @param c The new change
     */
    void addChange( Change<T> c ) {
        for ( Change<T> parent : c.getParentChanges() ) {
            heads.remove( parent );
        }
        heads.add( c );
        allChanges.add( c );
    }

    /**
     Create a new empty change object for target object
     @return A new, unfrozen Change object
     */
    public Change<T> createChange() {
        return new Change<T>( this );
    }

    /**
     Returns name of the target object's class
     */
    @Column( name = "target_class" )
    public String getTargetClassName() {
        return targetClassName;
    }
    
    /**
     Set the name of target object's class
     @param cl
     */
    public void setTargetClassName( String cl ) {
        targetClassName = cl;
    }
    

    /**
     Returns all known changes in target objects's history. 
     */
    @OneToMany( mappedBy="targetHistory", cascade=CascadeType.ALL, 
                targetEntity=Change.class )
    public Set<Change<T>> getChanges() {
        return this.allChanges;
    }

    
    void setChanges( Set c ) {
        allChanges=c;
    }

    @Transient
    public Change<T> getChange( UUID id ) {
        for ( Change<T> c : allChanges ) {
            if ( id.equals( c.getUuid() ) ) {
                return c;
            }
        }
        return null;
    }

    /**
     Returns the set of head changes, i.e. changes that do not have a child.
     */
    @OneToMany( targetEntity=Change.class )
    @JoinTable( name="pv_change_unmerged_branches",
                joinColumns=@JoinColumn( name="target_uuid" ), 
                inverseJoinColumns=@JoinColumn( name = "change_uuid" ) )
    public Set<Change<T>> getHeads() {
        return heads;
    }
    
    void setHeads( Set heads ) {
        this.heads = heads;
    }

    void setVersion( Change<T> version ) {
        currentVersion = version;
    }

    @OneToOne( targetEntity=Change.class )
    @JoinColumn( name = "version_uuid" )
    public Change<T> getVersion() {
        return currentVersion;
    }
    
    /**
     Write all changes of the target object to output stream. First, the total 
     number of changes is written as integer. Then the changes are 
     written in topologically sorted order so that all predecessors of a change 
     are written before it.
     
     @param os The output stream
     @throws java.io.IOException If an error occurs during writing.
     */
    public void writeChanges( ObjectOutputStream os ) throws IOException {
        Set<UUID> writtenIds = new HashSet<UUID>();
        List<ChangeDTO<T>> changesToWrite = new ArrayList<ChangeDTO<T>>( allChanges.size() );
        for ( Change<T> ch : heads ) {
            prepareDtos( ch, writtenIds, changesToWrite );
        }
        os.writeInt( changesToWrite.size() );
        for ( ChangeDTO<T> dto : changesToWrite ) {
            os.writeObject( dto );
        }
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
    private void prepareDtos( 
            Change<T> ch, Set<UUID> writtenIds, List<ChangeDTO<T>> dtoList ) 
            throws IOException {
        // First, ensure that all predecessors are written
        for ( Change<T> parent : ch.getParentChanges() ) {
            if ( !writtenIds.contains( parent.getUuid() ) ) {
                prepareDtos( parent, writtenIds, dtoList );
            }
        }
        ChangeDTO<T> dto = new ChangeDTO<T>( ch );
        dtoList.add( dto );
        writtenIds.add( ch.getUuid() );
    }
}

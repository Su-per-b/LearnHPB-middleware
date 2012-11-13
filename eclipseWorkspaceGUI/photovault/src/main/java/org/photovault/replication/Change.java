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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 Description of a change made to an object.
 
 Change is an immutable object describing some atomic change made to another 
 object. In that way, it can ve considered also as a version identifier for the 
 object state at certain moment of time.
 <p>
 It consists of information about changed status (field value changes and changed 
 associations to other objects) and reference to the change identifying the version
 of changed object before the change. Based on these, an unique identifier is 
 calculated.
 <p>
 The change object will have two distinct phases in its life cycle. When it is 
 created, it is in unfrozen start, and information about the state change can be 
 modified. After the object is frozed by calling the freeze() method, it cannot 
 be modified anymore.
 
 @see ObjectHistory
 
 @author Harri Kaimio
 @since 0.6
 
 @param <T> Class of the object whose changes are being tracked.
 */
@Entity
@Table( name="pv_changes" )
public class Change<T> {
    
    static private UUID NULL_UUID = UUID.fromString( "00000000-0000-0000-0000-000000000000" );
    
    static private Log log = LogFactory.getLog( Change.class.getName() );
    
    /**
     ObjectHistory object handling change history of the target object
     */
    private ObjectHistory targetHistory;

    /**
     Has this change been frozen?
     */
    private boolean frozen = false;
    
    /**
     Unique ID for this change, calculated when the change is frozen
     */
    private UUID uuid;
    
    /**
     Previous change on top of which this change can be applied
     */
    private Change prevChange;
    
    /**
     True if this change is a head of a branch (i.e. it does not have any child 
     changes
     */
    private boolean head = true;
    
    /**
     Changes that are based on this change
     */
    private Set<Change<T>> childChanges = new HashSet<Change<T>>();
    
    private Set<Change<T>> parentChanges = new HashSet<Change<T>>();
    
    /**
     Fields of {@link target} that have been changed by this change. If this field
     * is <code>null</code>, it will be initialized from {@link #serializedForm}
     * by calling {@link #initChangedFields()} before accessing it.
     */
    private Map<String, FieldChange> changedFields = null;
    
    /**
     If this is a merge change, conflicting fields
     */
    private Map<String, ValueFieldConflict> fieldConflicts = 
            new HashMap<String, ValueFieldConflict>();
    
    /**
     This change serialized to XML. This byte string is actually defining this 
     change as UUID is calucated from it. The form is determined as part of 
     freezing the change - if change is not freezed this is <code>null</code>
     */
    private byte[] serializedForm = null;
    
    
    /**
     Default constructor, for construction by Hibernate or {@link ChangeFactory}.
     This constructor set the frozen field to true, so it is extremely important 
     that the change must not be accessed by user code before second phase of 
     construction has been finalized!!!
     */
    Change() {
        frozen = true;
    }
    
    /**
     Constructor. This constructor is used by ObjectHistory class to create 
     new changes
     
     @param t The ObjectHistory object handling change history of target
     */
    Change( ObjectHistory<T> t ) {
        targetHistory = t;
        changedFields = new HashMap<String, FieldChange>();
    }
    
    @Id
    @Column( name="change_uuid" )
    @org.hibernate.annotations.Type( type = "org.photovault.persistence.UUIDUserType" )
    public UUID getUuid() {
        return uuid;
    }

    
    void setUuid( UUID uuid ) {
        this.uuid = uuid;
    }
    
    @ManyToOne( targetEntity=ObjectHistory.class )
    @JoinColumn( name="target_uuid" )
    ObjectHistory<T> getTargetHistory() {
        return targetHistory;
    }

    void setTargetHistory( ObjectHistory<T> h ) {
        targetHistory = h;
    }

    /**
     Set field value. 
     @param fieldName The field to be changed
     @param newValue New value for the field
     @throws IllegalStateException if the change has laready been frozen.
     */
    public void setField( String fieldName, Object newValue ) {
        assertNotFrozen();
        changedFields.put( fieldName, new ValueChange( fieldName, newValue ) );
    }


    /**
     * Change a property of a field that is a Java bean
     * @param fieldName Name of the field
     * @param subfield property to be changed
     * @param newValue New value for the property
     */
    public void setFieldProperty( String fieldName, String property, Object newValue ) {
        assertNotFrozen();
        ValueChange existingChange = (ValueChange) changedFields.get( fieldName );
        if ( existingChange == null ) {
            changedFields.put( fieldName, new ValueChange( fieldName, property, newValue ) );
        } else {
            existingChange.addPropChange( property, newValue );
        }
    }

    /**
     Get field value. The If the field is not modified by this change, return 
     the value of the field after previous change.
     @param field The field that will be returned
     @return Value that the field will have after the change is applied.
     @todo What if this is a merge change and there is a conflict
     */
    public Object getField( String field ) {
        if ( changedFields == null ) {
            initChangedFields();
        }
        if ( changedFields.containsKey( field ) ) {
            FieldChange lastChange = changedFields.get( field );
            if ( lastChange instanceof ValueChange ) {
                return ((ValueChange)lastChange).getValue();
            }
        } else if ( prevChange != null ) {
            return prevChange.getField( field );
        }
        return null;
    }
    
    FieldChange getFieldChange( String field ) {
        if ( changedFields == null ) {
            initChangedFields();
        }
        return changedFields.get( field );
    }
    
    void setFieldChange( String field, FieldChange c ) {
        changedFields.put(  field, c);
    }
    
    
    @Column( name = "serialized", length = 1048576  )
    @Lob
    byte[] getSerializedChange() {
        return serializedForm;       
    }

    void setSerializedChange( byte[] c ) {
        if ( c != null ) {
            serializedForm = Arrays.copyOf( c, c.length );
        }
    }
    
    
    /**
     Get all fields modified in this change
     @return Map from the field description objects to their new values.
     */
    @Transient
    public Map<String,FieldChange> getChangedFields() {
        if ( changedFields == null ) {
            initChangedFields();
        }
        return Collections.unmodifiableMap( changedFields );
    }
    
    void setChangedFields( Map<String,FieldChange> changes ) {
        changedFields = changes;
    }

    private void initChangedFields() {
        if ( serializedForm == null || targetHistory == null ) {
            throw new RuntimeException( "Cannot initialize changed fields yet" );
        }
        try {
            Class targetClass = Class.forName( targetHistory.getTargetClassName() );
            ChangeDTO<T> dto = ChangeDTO.createChange( serializedForm, targetClass );
            changedFields = dto.changedFields;
        } catch ( ClassNotFoundException ex ) {
            throw new RuntimeException( "Cannot find class " + 
                    targetHistory.getTargetClassName(), ex );
        }
        
    }

    @ManyToMany( targetEntity=Change.class, cascade=CascadeType.ALL )
    @JoinTable(name = "pv_change_relations",
        joinColumns = {@JoinColumn(name = "child_uuid")},
        inverseJoinColumns = {@JoinColumn(name = "parent_uuid")})
    public Set<Change<T>> getParentChanges() {
        return parentChanges;
    }

    void setParentChanges(  Set changes ) {
        parentChanges = changes;
    }
    
    @Column( name = "head" )
    public boolean isHead() {
        return head;
    }
    
    void setHead( boolean b ) {
        head = b;
    }
    
    /**
     Returns the previous change on top of which this change is applied
     @return the first parent change
     @deprecated As there can be many parents, use {@link #getParentChanges() }
     instead.
     */
    @Transient
    public Change getPrevChange() {
        if ( prevChange == null && !parentChanges.isEmpty() ) {
            prevChange = parentChanges.iterator().next();
        }
        return prevChange;
    }

    /**
     Set the predecessor of this change
     @param c
     @throws IllegalStateException if the change has already been frozen
     */
    public void setPrevChange( Change<T> c ) {
        assertNotFrozen();
        if ( c.getTargetHistory() != targetHistory ) {
            throw new IllegalArgumentException( "Cannot be based on change to different object" );
        }
        if ( prevChange != null ) {
            parentChanges.remove( prevChange );
        }
        parentChanges.add(  c );
        prevChange = c;        
    }
    
    /**
     Returns the children of this change.
     */
    @ManyToMany( mappedBy="parentChanges", targetEntity=Change.class )
    public Set<Change<T>> getChildChanges() {
        return childChanges;
    }
    
    void setChildChanges( Set c ) {
        childChanges = c;
    }
    
    /**
     Add a child to thi change
     @param child The new child
     */
    void addChildChange( Change child ) {
        childChanges.add( child );
        head = false;
    }
    
    /**
     Removes a child change
     @param child The chipd to be removed
     */
    void removeChildChange( Change child ) {
        childChanges.remove( child );
        head = childChanges.isEmpty();
    }
    
    @Transient
    public boolean isFrozen() {
        return frozen;
    }

    /**
     Returns <code>true</code> if this change has unresolved conflicts
     */
    public boolean hasConflicts() {
        if ( changedFields == null ) {
            initChangedFields();
        }
        for ( FieldChange fc : changedFields.values() ) {
            if ( fc.getConflicts().size() > 0 ) {
                return true;
            }
        }
        return false;
    }
    
    /**
     Returns all field conflicts
     @return
     */
    @Transient
    public Collection<FieldConflictBase> getFieldConficts() {
        if ( changedFields == null ) {
            initChangedFields();
        }
        List<FieldConflictBase> conflicts = new ArrayList<FieldConflictBase>();
        for ( FieldChange fc : changedFields.values() ) {
            conflicts.addAll(  fc.getConflicts() );
        }
        return conflicts;
    }
    
    /**
     Creates a change that merges 2 branches into one. The change will contain 
     needed operations so that it will change target object to same state 
     when applied to either branch head. If a field is modified in onluy one 
     branch it will be set in this change as well. If a field is modified 
     differently in the branches, a conflict is created.
     @param other The change that will be merged with this one
     @return A new merge change.
     */
    public Change<T> merge( Change<T> other ) {
        assertFrozen();
        if ( other.targetHistory != targetHistory ) {
            throw new IllegalArgumentException( "Cannot merge changes to separate objects" );
        }
        if ( changedFields == null ) {
            initChangedFields();
        }
        // Find the common ancestor version
        Set<Change> ancestors = new HashSet<Change>();
        for ( Change c = this ; c != null; c = c.getPrevChange() ) {
            ancestors.add( c );
        }
        Change<T> commonBase = null;
        Map<String, FieldChange> changedFieldsOther = new HashMap();
        for ( Change<T> c = other ; c != null ; c = c.getPrevChange() ) {
            if ( ancestors.contains( c ) ) {
                commonBase = c;
                break;
            }
            
            // record all field changes that are still valid currently
            for ( Map.Entry<String,FieldChange> e : c.getChangedFields().entrySet() ) {
                if ( !changedFieldsOther.containsKey( e.getKey() ) ) {
                    try {
                        changedFieldsOther.put( e.getKey(), (FieldChange) e.getValue().clone() );
                    } catch ( CloneNotSupportedException ex ) {
                        log.error( ex );
                        throw new IllegalStateException( "All FieldValue objects must be cloneable" );
                    }
                } else {
                    FieldChange fc = changedFieldsOther.get( e.getKey() );
                    fc.addEarlier( e.getValue() );
                }
            }
        }
        
        /*
         Common ancestor is now found, collect changes done between it and 
         this change
         */
        Map<String, FieldChange> changedFieldsThis = new HashMap();
        for ( Change<T> c = this ; c != commonBase ; c = c.getPrevChange() ) {
            for ( Map.Entry<String,FieldChange> e : c.getChangedFields().entrySet() ) {
                FieldChange fc = (FieldChange) changedFieldsThis.get( e.getKey() );
                if ( fc == null ) {
                    try {
                        changedFieldsThis.put( e.getKey(),
                                (FieldChange) e.getValue().clone() );
                    } catch ( CloneNotSupportedException ex ) {
                        log.error( ex );
                        throw new IllegalStateException( 
                                "All FieldValue objects must be cloneable" );
                    }
                } else {
                    fc.addEarlier( e.getValue() );
                }
            }            
        }
        
        /*
         Combine the changes. If field is changed only in one path or it gets 
         same value in both, it can be merged automatically. Otherwise, 
         conflict is created
         */
        Change<T> merged = new Change<T>( targetHistory );
        merged.parentChanges.add( this );
        merged.parentChanges.add( other );
        Set<String> allChangedFields = 
                new HashSet<String>( changedFieldsThis.keySet() );
        allChangedFields.addAll( changedFieldsOther.keySet() );
        for ( String f : allChangedFields ) {
            boolean changedInThis = changedFieldsThis.containsKey( f );
            boolean changedInOther = changedFieldsOther.containsKey( f );
            try {
            if ( changedInThis && !changedInOther ) {
                merged.setFieldChange(f,
                        (FieldChange) changedFieldsThis.get( f ).clone() );
            } else if ( !changedInThis && changedInOther ) {                
                merged.setFieldChange(f,
                        (FieldChange) changedFieldsOther.get( f ).clone() );
            } else {
                // The field has been changed in both paths
                FieldChange chThis = changedFieldsThis.get( f );
                FieldChange chOther = changedFieldsOther.get( f );
                FieldChange chMerged = chThis.merge(  chOther );
                merged.setFieldChange( f, chMerged );                        
            }
            } catch ( CloneNotSupportedException ex ) {
                log.error( ex );
                throw new IllegalStateException( 
                        "FieldChanges must support cloning", ex );
            }
                    
        }
         
        return merged;
    }


    /**
     Helper method to find the change in which a field was last modified
     @param field The field name
     @param start The change whose ancestors are looked
     @return The change in which f was modified
     */
    private Change<T> findLastFieldChange( String field, Change<T> start ) {
        if ( changedFields == null ) {
            initChangedFields();
        }
        Change<T> c = start;
        while( !c.changedFields.containsKey( field ) ) {
            c = c.prevChange;
        }
        return c;
    }
    
    /**
     Add a new conflict
     @param c The new conflict
     */
    private void addFieldConflict( ValueFieldConflict c ) {
        fieldConflicts.put( c.getFieldName(), c );
    }
        
    
    /**
     Calculate UUID for this change
     */
    private void calcUuid() {
        ChangeDTO<T> data = new ChangeDTO<T>( this );
        try {
            uuid = data.calcUuid();
            
        } catch ( IOException e ) {
            log.error( "Error calculating change UUID: " + e.getMessage() );
            log.error( e );
        }
    }
    
    /**
     Serializes the field changes to a stream
     @param s
     */
    private void writeFieldChanges( ObjectOutputStream s ) throws IOException {
        if ( changedFields == null ) {
            initChangedFields();
        }
        s.writeInt( changedFields.size() );
        List<String> fieldsSorted = 
                new ArrayList<String>( changedFields.keySet() );
        Collections.sort( fieldsSorted );
        for ( String f : fieldsSorted ) {
            s.writeObject( f );
            s.writeObject( changedFields.get( f ) );
        }
    }
    
    private void readFieldChanges( ObjectInputStream s ) throws IOException, ClassNotFoundException {
        int count = s.readInt();
        changedFields = new HashMap<String, FieldChange>();
        for ( int n = 0; n < count; n++ ) {
            String field = (String) s.readObject();
            FieldChange val = (FieldChange) s.readObject();
            changedFields.put( field, val );
        }
    }
    

    /**
     Freeze this change. When change is freezed
     <ul>
     <li>If the prevChange has not been set, the current version of 
     target object is set as parent of this change.</li>
     <li>The change is added to change hsitory of the target obejct</li>
     <li>UUID of this change is calculated from serialized form of the cahnge</li>
     </ul>
     
     After freezing a change it cannot be modified anymore.
     
     TODO: Now calling initFirstChange here is dangerous because it cannot 
     initialize fields with special resolvers properly. This method should 
     probably throw an exception instead.
     */
    public void freeze() {
        if ( hasConflicts() ) {
            throw new IllegalStateException( "Cannot freeze change that has unresolved conflicts" );
        }

        // TODO: Parent change handling should probably be done in editor...
        if ( parentChanges.isEmpty() ) {
            Change<T> targetVersion = targetHistory.getVersion();
            if ( targetVersion != null ) {
                parentChanges.add( targetHistory.getVersion() );
            }
        }

        /* Construct serialized XML form and UUID */
        ChangeDTO<T> dto = new ChangeDTO<T>( this );
        serializedForm = dto.getXmlData();
        uuid = dto.changeUuid;
        frozen = true;
        for ( Change<T> parent : parentChanges ) {
            parent.addChildChange( this );
        }
        targetHistory.addChange( this );
//        targetHistory.applyChange( this );
    }
    
    /**
     Helper method to check that this cahnge is frozen and throw exception if it
     is not.
     @throws IllegalStateException if the change is not frozen
     */
    private void assertFrozen() {
        if ( !frozen ) {
            throw new IllegalStateException( "Change is not frozen" );
        }
    }
    
    /**
     Helper method to check that this cahnge is not frozen and throw exception 
     if it is.
     @throws IllegalStateException if the change is frozen
     */
    private void assertNotFrozen() {
        if ( frozen ) {
            throw new IllegalStateException( "Change is frozen" );
        }
    }
    
    @Override
    public boolean equals( Object o ) {
        if ( o == null ) {
            return false;
        }
        if ( ! (o instanceof Change) ) {
            return false;
        }
        Change c = (Change)o;
        if ( !c.frozen ) {
            throw new IllegalArgumentException( "Cannot compare equality to non-frozen change!!!");
        }
        return ( c.uuid == uuid || ( uuid != null && uuid.equals( c.uuid ) ) );
    }

    @Override
    public int hashCode() {
        if ( !frozen) {
            throw new IllegalArgumentException( "Cannot compare equality to non-frozen change!!!");
        }
        int hash = 3;
        hash = 89 * hash + (this.uuid != null ? this.uuid.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("UUID: " ).append( this.uuid ).append( "\n" );
        buf.append( "Target: " ).append( this.targetHistory.getTargetUuid() ).append( "\n" );
        buf.append( "Predecessors:\n" );
        if ( parentChanges.size() == 0 ) {
            buf.append( "  None\n" );
        }
        for ( Change c : this.parentChanges ) {
            buf.append( "  " );
            buf.append( c.getUuid() );
            buf.append(  "\n" );
        }
        buf.append( "Changed fields:\n" );
        if ( changedFields.size() == 0 ) {
            buf.append( "  None\n" );
        }
        for ( Map.Entry<String,FieldChange> fc : this.changedFields.entrySet() ) {
            buf.append( "  " );
            buf.append( fc.getKey() );
            buf.append( " -> " );
            buf.append( fc.getValue() );
            buf.append(  "\n" );
        }
        return buf.toString();
    }
}

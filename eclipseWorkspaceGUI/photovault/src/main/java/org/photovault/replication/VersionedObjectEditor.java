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
import java.lang.Class;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 VersionedObjectEditor is the main interface for accessing and changing state of
 versioned and replcable objects. It can be used to construct new changes as well 
 as for changing state of the working copy objects between different versions
 
 * @author Harri Kaimio
 @since 0.6.0
 @param <T> Class of the target object
 
 */
public class VersionedObjectEditor<T> {
    
    private static Log log = LogFactory.getLog( VersionedObjectEditor.class );
    
    /**
     History of the target object
     */
    ObjectHistory history;
    
    /**
     The object being edited
     */
    T target;
    
    /**
     Description of the class of target object
     */
    private VersionedClassDesc classDesc; 
    
    /**
     Factory for creating field resolvers
     */
    DTOResolverFactory fieldResolver;
    
    /**
     Change that is currently being constructed
     */
    Change<T> change;

    static Map<Class, VersionedClassDesc> analyzedClasses = 
            new HashMap<Class, VersionedClassDesc>();
    
    public VersionedObjectEditor( T target, DTOResolverFactory fieldResolver ) {
        this.target =target;
        classDesc = getClassDescriptor( target.getClass() );
        this.history = classDesc.getObjectHistory( target );
        this.fieldResolver = fieldResolver;
        change = history.createChange();
        Change<T> prevChange = history.getVersion();
        if ( prevChange != null ) {
            change.setPrevChange( history.getVersion() );
        }        
    }

    /**
     Create a editor that creates a new object
     @param clazz Class of the object that will be created
     @param uuid UUID of the obejct. If uuid is <code>null</code>, assign a 
            random uuid.
     @param df 
     @throws InstantiationException if clazz does not have default constructor
     @throws IllegalAccessException if default clazz constructor cannot be 
             accessed
     */
    public VersionedObjectEditor( Class<T> clazz, UUID uuid, DTOResolverFactory df ) 
            throws InstantiationException, IllegalAccessException {
        fieldResolver = df;
        classDesc = getClassDescriptor( clazz );
        target = clazz.newInstance();
        history = classDesc.getObjectHistory( target );
        history.setTargetUuid( uuid );
        Change<T> initialChange = history.createChange();
        initialChange.freeze();
        history.setVersion( initialChange );
        change = history.createChange();
        change.setPrevChange( initialChange );
    }

    /**
     * Create a editor for a new object based on history data
     * @param histData History data for the object to be created
     * @param df Resolver factory
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalStateException if the history data contains a change whose
     * parent is not known or the history data is not topologically sorted.
     */
    public VersionedObjectEditor( ObjectHistoryDTO<T> histData, DTOResolverFactory df ) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        fieldResolver = df;
        Class<T> clazz = (Class<T>) Class.forName( histData.getTargetClassName() );
        classDesc = getClassDescriptor( clazz );
        target = clazz.newInstance();
        history = classDesc.getObjectHistory( target );
        history.setTargetUuid( histData.getTargetUuid() );
        for ( ChangeDTO<T> chd : histData.getChanges() ) {
            change = new Change<T>();
            change.setTargetHistory( history );
            change.setUuid( chd.changeUuid );
            change.setSerializedChange( chd.xmlData );
            // Try to find parents of this change
            Set<Change<T>> parents = new HashSet<Change<T>>();
            for ( UUID parentId : chd.parentIds ) {
                Change<T> parent = history.getChange( parentId );
                if ( parent != null ) {
                parents.add( parent );
                parent.addChildChange( change );
                } else {
                    throw new IllegalStateException(
                            "Unknown parent " + parentId +
                            " for change " + chd.changeUuid );
                }
            }
            change.setParentChanges( parents );
            change.setChangedFields( chd.changedFields );
            // dao.makePersistent( change );
            // dao.flush();
            history.addChange( change );
        }
        Set<Change<T>> heads = history.getHeads();
        if ( heads.size() == 1 ) {
            this.changeToVersion( heads.iterator().next() );
        }
    }

    public Object getProxy() {
        EditorProxyInvocationHandler ih = 
                new EditorProxyInvocationHandler( this, classDesc );
        Class editorClass = classDesc.getEditorClass();
        if ( editorClass == null ) {
            throw new IllegalStateException( 
                    "Cannot create editor proxy for class " + 
                    target.getClass().getName() + 
                    "as it has not been defined" );
        }
        return Proxy.newProxyInstance( 
                this.getClass().getClassLoader(), 
                new Class[]{editorClass}, ih );
    }
    
    public T getTarget() {
        return target;
    }

    /**
     * Returns the history of edited object
     * @return
     */
    public ObjectHistory<T> getHistory() {
        return history;
    }

    /**
     Add changes described by an ObjectHistoryDTO to the history of this object.
     If the dto contains a change that is child of current version and the 
     current version is head, the object's state is updated to match the latest
     change in this branch.
     @param h The history information to add
     @param cf ChangeFactory used to construct changes from the dto.
     @throws java.lang.ClassNotFoundException
     @throws java.io.IOException
     */
    public void addToHistory( ObjectHistoryDTO<T> h, ChangeFactory<T> cf ) 
            throws ClassNotFoundException, IOException {
        log.debug( "addToHistory: entry" );
        if ( !h.getTargetUuid().equals( history.getTargetUuid() ) ) {
            throw new IllegalArgumentException( "trying to merge with history of another object" );
        }
        if ( !h.getTargetClassName().equals( history.getTargetClassName() ) ) {
            throw new IllegalArgumentException( "trying to merge with history of another object" );
        }
        
        Change<T> oldVersion = history.getVersion();
        Set<Change<T>> oldHeads = new HashSet( history.getHeads() );
        boolean wasAtHead = oldHeads.contains( oldVersion );
        
        cf.addObjectHistory( h );
        
        /*
         If new changes were added to current branch, update to new head
         */
        if ( wasAtHead && !history.getHeads().contains( oldVersion ) ) {
            log.debug( "addToHistory: updating version" );
            Change<T> newVersion = oldVersion;
            Set<Change<T>> children = newVersion.getChildChanges();
            while ( children.size() == 1 ) {
                newVersion = children.iterator().next();
                children = newVersion.getChildChanges();
            }
            if ( oldVersion != newVersion ) {
                changeToVersion( newVersion );
            }
        }        
    }
    
    /**
     Get value of a field in the current state of the obejct
     @param field Field name
     @return Value of the field. If the field has been edited by this editor,
     return the edited value. Otherwise, returns value of the field in base
     version of this change.
     */
    public Object getField( String field ) {
        // Has this field been set?
        Map<String,FieldChange> changes = change.getChangedFields();
        if ( changes.containsKey( field ) ) {
            Object val = change.getField( field );

            DTOResolver resolver =
                    fieldResolver.getResolver( classDesc.getFieldResolverClass( field ) );
            return resolver.getObjectFromDto( val );
        }
        // The field has not been changed, return value in target object

        return classDesc.getFieldValue( target, field );

    }

    /**
     Set a new value for a field in the change that is being edited
     @param field Name of the field
     @param value New value for field
     */
    public void setField( String field, Object value ) {
        String subfield = null;
        int subFieldStart = field.indexOf( "." );
        if ( subFieldStart > 0 ) {
            subfield = field.substring( subFieldStart+1 );
            field = field.substring( 0, subFieldStart );
        }

        if ( subfield == null ) {
            DTOResolver resolver =
                    fieldResolver.getResolver( classDesc.getFieldResolverClass(
                    field ) );
            change.setField( field, resolver.getDtoFromObject( value ) );
        } else {
            change.setFieldProperty( field, subfield, value );
        }
    }
    
    public void addToSet( String setFieldName, Object value ) {
        SetChange sc = (SetChange) change.getFieldChange( setFieldName );
        if ( sc == null ) {
            sc = new SetChange( setFieldName );
        }
            change.setFieldChange( setFieldName, sc );
        DTOResolver resolver = 
                    fieldResolver.getResolver( classDesc.getFieldResolverClass( setFieldName ) );
        sc.addItem( resolver.getDtoFromObject( value ) );
    }
    
    public void removeFromSet( String setFieldName, Object value ) {
        SetChange sc = (SetChange) change.getFieldChange( setFieldName );
        if ( sc == null ) {
            sc = new SetChange( setFieldName );
            change.setFieldChange( setFieldName, sc );
        }
        DTOResolver resolver = 
                    fieldResolver.getResolver( classDesc.getFieldResolverClass( setFieldName ) );
        sc.removeItem( resolver.getDtoFromObject( value ) );        
    }
    
    /**
     Apply the changes made in this editor to the working copy and add the change
     to object history.
     */
    public Change<T> apply() {
        if ( change.isFrozen() ) {
            if ( history.getVersion() != change ) {
                throw new IllegalStateException(
                        "Frozen change does not match object state" );
            }
            return change;
        }

        if ( change.getParentChanges().isEmpty() ) {
            /*
             This is an initial change. Set the default values for all value 
             fields
             */
            for ( FieldDesc f : classDesc.getFields() ) {
                if ( !change.getChangedFields().containsKey( f.name ) && f instanceof ValueFieldDesc ) {
                    try {
                        Object v = f.getter.invoke( target );
                        DTOResolver resolver =
                                fieldResolver.getResolver( f.dtoResolverClass );
                        Object dto = resolver.getDtoFromObject( v );
                        change.setField( f.name, dto );
                    } catch ( Exception ex ) {
                        throw new IllegalStateException(
                                "Cannot access getter for field " + f.name, ex );
                    }
                }
            }
        }
        change.freeze();
        for ( Change<T> parent : change.getParentChanges() ) {
            parent.addChildChange( change );
        }
        Change<T> currentVersion = history.getVersion();
        if ( currentVersion == null ? !change.getParentChanges().isEmpty() :
                !change.getParentChanges().contains( currentVersion ) ) {
            throw new IllegalStateException( "Cannot apply change on top of unrelated change" );
        }
        for ( Map.Entry<String,FieldChange> fc : change.getChangedFields().entrySet() ) {       
            FieldChange ch = fc.getValue();
            classDesc.applyChange( target, ch, fieldResolver );
        }
        history.setVersion( change );                
        return change;
    }
    
    /**
     Change the state of target object to match given version
     @param version
     */
    public void changeToVersion( Change<T> newVersion ) {
        Change<T> oldVersion = history.getVersion();
        
        if ( newVersion == oldVersion ) {
            return;
        }
        
        // Find the common ancestor between these versions
        Set<Change<T>> oldAncestors = new HashSet<Change<T>>();
        for ( Change<T> c = oldVersion ; c != null ; c = c.getPrevChange() ) {
            oldAncestors.add( c );
        }
        
        Change<T> commonBase = null;
        for ( Change<T> c = newVersion ; c != null ; c = c.getPrevChange() ) {
            if ( oldAncestors.contains( c ) ) {
                commonBase = c;
                break;
            }
        }
        
        /*
         Find out the fields that were changed between old version and common 
         base
         */
        Map<String, FieldChange> baseToOldVersion = 
                new HashMap<String, FieldChange>();
        for ( Change<T> c = oldVersion ; c != commonBase ; c = c.getPrevChange() ) {
            for ( FieldChange fc : c.getChangedFields().values() ) {
                FieldChange ofc = baseToOldVersion.get( fc.getName() );
                if ( ofc != null ) {
                    ofc.addEarlier( fc );
                } else {
                    try {
                        baseToOldVersion.put( fc.getName(), (FieldChange) fc.clone() );
                    } catch ( CloneNotSupportedException ex ) {
                        log.error( ex );
                    }
                }
            }
        }

        /*
         Find out the changes between common base and new version
         */
        Map<String, FieldChange> baseToNewVersion = 
                new HashMap<String, FieldChange>();
        for ( Change<T> c = newVersion ; c != commonBase ; c = c.getPrevChange() ) {
            for ( FieldChange fc : c.getChangedFields().values() ) {
                FieldChange ofc = baseToNewVersion.get( fc.getName() );
                if ( ofc != null ) {
                    ofc.addEarlier( fc );
                } else {
                    try {
                        baseToNewVersion.put( fc.getName(), (FieldChange) fc.clone() );
                    } catch ( CloneNotSupportedException ex ) {
                        log.error( ex );
                    }
                }
            }
        }
        
        /*
         Create the list of changes that must be applied to convert from old to 
         new version. If field was changed between common base and old version,
         apply the reverse change before change from base to new version
         */
        for ( FieldChange fc : baseToOldVersion.values() ) {
            FieldChange reverse = fc.getReverse( commonBase );
            FieldChange nfc = baseToNewVersion.get( reverse.getName() );
            if ( nfc != null ) {
                nfc.addEarlier( reverse );
            } else {
                baseToNewVersion.put( reverse.getName(), reverse );
            }            
        }
        
        /*
        Finally, apply all field changes
         */
        for ( FieldChange fc : baseToNewVersion.values() ) {
            classDesc.applyChange( target, fc, fieldResolver );
        }

        history.setVersion( newVersion );
        // change.setPrevChange( newVersion );
    }
    
    public Change<T> getChange() {
        return change;
    }

    /**
     Get the class descriptor for given class
     @param clazz The class
     @return Class descriptor of clazz. If it does not yet exist, analyzes class 
     and creates new one.
     */
    static VersionedClassDesc getClassDescriptor( Class clazz ) {
        VersionedClassDesc cd = analyzedClasses.get( clazz );
        if ( cd == null && clazz != null ) {
            cd = new VersionedClassDesc( clazz );
            analyzedClasses.put( clazz, cd );
        }
        return cd;
    }
}

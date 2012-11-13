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

package org.photovault.imginfo.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawSettingsFactory;
import org.photovault.folder.PhotoFolder;
import org.photovault.image.ChannelMapOperationFactory;
import org.photovault.image.ChannelMapRuleSet;
import org.photovault.imginfo.FuzzyDate;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 Imports data from XML file previously saved with {@link XmlExporter} 
 
 
 */
public class XmlImporter {

    static Log log = LogFactory.getLog( XmlImporter.class.getName() );
    
    /**
     Reader from which the data is read.
     */
    private BufferedReader reader;
    
    /**
     * Creates a new instance of XmlImporter
     */
    public XmlImporter( BufferedReader reader ) {
        this.reader = reader;
    }

    /**
     Status code sent to listeners when import operation starts
     */
    public static final int IMPORTING_STARTED = 1;
    

    /**
     Status code sent to listener when import operation completes
     */    
    public static final int IMPORTING_COMPLETED = 2;        

    /**
     Number of photos imported
     */
    int photoCount = 0;

    /**
     Number of folders imported
     */
    int folderCount = 0;

    /**
     Get the number of imported new folders
     @return Number of new folders imported during the ongoing operation
     */
    public int getImportedFolderCount() {
        return folderCount;
    }
    
    /**
     Get the number of imported photos
     @return Number of previously unknown photos imported during the ongoing operation
     */
    public int getImportedPhotoCount() {
        return photoCount;
    }
    
    Set listeners = new HashSet();
    
    /**
     Ask that a {@link XmlImportListener} should be notified of events in this 
     importer.
     @param l The object that should be notified of events
     */
    public void addListener( XmlImportListener l ) {
        listeners.add( l );
    }
    
    /**
     Ask that a {@link XmlImportListener} should not anymore be notified of events in this 
     importer.
     @param l The object that should be removed form listeners.
     */    
    public void removeListener( XmlImportListener l ) {
        listeners.remove( l );
    }
    
    /**
     Inform listeners about status change to this importer
     */
    private void fireStatusChangeEvent( int status ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            XmlImportListener l = ( XmlImportListener ) iter.next();
            l.xmlImportStatus( this, status );
        }
    }
    
    /**
     Inform listeners about error in importing
     @param errorMsg The error message that is sent to listeners
     */
    private void fireErrorEvent( String errorMsg ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            XmlImportListener l = ( XmlImportListener ) iter.next();
            l.xmlImportError( this, errorMsg );
        }
    }
    
    /**
     Inform listeners that an object has been imported
     @param obj the exported object
     */
    private void fireObjectImportedEvent( Object obj ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            XmlImportListener l = ( XmlImportListener ) iter.next();
            l.xmlImportObjectImported( this, obj );
        }
    }
    
    
    
    /**
     Factory used for creating and fetching PhotoFolder in Digester rule. It
     first stris to find a fodler with the uuid specified in XML. If no such 
     folder is found, a new folder is created.
     */
    public static class FolderFactory extends AbstractObjectCreationFactory {

        /**
         If true, always set the parent of the folder from either parent-id 
         attribute or if it does not exist from the parent XML node. 
         <p>
         If false, do not set parent for an <i>existing</i> folder unless parent 
         is explicitly set with parent-id attribute.
         */
        private boolean forceReparent;
        
        public FolderFactory( boolean forceReparent ) {
            this.forceReparent = forceReparent;
        }
        
        /**
         This callback returns the folder to Diester. Unlike the name suggests,
         the method tries not to create a folder, but returns an existing folder
         with given uuid if such one exists.
         @param attrs attributes of the &lt;folder&gr; element.
         @return The folder with give uuid.
         */
        public Object createObject( Attributes attrs ) {
            String uuidStr = attrs.getValue( "id" );
            UUID uuid = UUID.fromString( uuidStr );
            PhotoFolder folder = PhotoFolder.getFolderByUUID( uuid );
            
            // Find out the parent
            PhotoFolder parent = PhotoFolder.getRoot();
            String parentUuidStr = attrs.getValue( "parent-id" );
            if ( parentUuidStr == null ) {
                // No parent specified in XML file, use topmost object in stack
                Object top = getDigester().peek();
                if ( top instanceof PhotoFolder ) {
                    parent = (PhotoFolder) top;
                }
            } else {
                PhotoFolder parentCandidate =
                        PhotoFolder.getFolderByUUID( UUID.fromString( parentUuidStr ) );
                if ( parentCandidate != null ) {
                    parent = parentCandidate;
                }
                
            }
            
            if ( folder == null ) {
                folder = PhotoFolder.create( uuid, parent );
            } else if ( parentUuidStr != null || forceReparent ) {
                folder.reparentFolder( parent );
            }
            return folder;
        }
    }

    /**
     Name for a stack that is used to communicate from begin tag to end tag 
     whether importer is constructing a new object (Boolean(true) pushed to stack) 
     or not.
     */
    final static String STACK_CREATING_NEW = "org.photovault.imginfo.xml.isCreatingNewObject";
    
    /**
     Factory used for creating and fetching PhotoInfo in Digester rule. It
     first tries to find a photo with the uuid specified in XML. If no such 
     photo is found, a new folder is created.
     */
    public static class PhotoFactory extends AbstractObjectCreationFactory {
        public PhotoFactory( ) {

        }
        
        public Object createObject( Attributes attrs ) {
            String uuidStr = attrs.getValue( "id" );
            UUID uuid = UUID.fromString( uuidStr );
            PhotoInfo p = null;
            PhotoInfoDAO photoDao = null;
            p = photoDao.findByUUID( uuid );

            if ( p == null ) {
                p = PhotoInfo.create( uuid );
                digester.push( STACK_CREATING_NEW, Boolean.TRUE );
            } else {
                digester.push( STACK_CREATING_NEW, Boolean.FALSE );                
            }
            return p;
        }
    }

    /**
     Factory used for creating and fetching ImageInstance in Digester rule. It
     first tries to find a photo with the uuid specified in XML. If no such 
     photo is found, a new folder is created.
     
     TODO: Change this to create ImageDesriptors instead.
     */
    public static class InstanceFactory extends AbstractObjectCreationFactory {
        public InstanceFactory() {
            
        }
        
        public Object createObject( Attributes attrs ) {
/*            String uuidStr = attrs.getValue( "id" );
            UUID uuid = UUID.fromString( uuidStr );
            ImageInstance i = null;
            i = ImageInstance.retrieveByUuid(uuid);
            if ( i == null ) {
                i = ImageInstance.create( uuid );
            } 
            String type = attrs.getValue( "type" );
            if ( type != null ) {
                if ( type.equals( "original" ) ) {
                    i.setInstanceType( ImageInstance.INSTANCE_TYPE_ORIGINAL );
                } else if ( type.equals( "thumbnail" ) ) {
                    i.setInstanceType( ImageInstance.INSTANCE_TYPE_THUMBNAIL );
                } else if ( type.equals( "modified" ) ) {
                    i.setInstanceType( ImageInstance.INSTANCE_TYPE_MODIFIED );
                } else {
                    log.error( "ERROR: Unknown instance type \"" + type + 
                            "\" for instance with uuid " + uuid );
                }
                
            }
            return i;
*/
            return null;
        }
    }

    /**
     Factory for creating fuzzy date in Digester rule
     */
    public static class FuzzyDateFactory extends AbstractObjectCreationFactory {
        public FuzzyDateFactory() {
            
        }
        
        public Object createObject( Attributes attrs ) {
            String timeStr = attrs.getValue( "time" );
            long timeMsec = Long.parseLong( timeStr );
            String accStr = attrs.getValue( "accuracy" );
            long accMsec = Long.parseLong( accStr );
            Date time = new Date( timeMsec );
            double accDays = ((double) accMsec ) / (24.0 * 3600000.0 );
            FuzzyDate fd = new FuzzyDate( time, accDays );
            return fd;
        }
    }    

    /**
     Import data from XML file according to current settings in this object.
     */
    public void importData() {
        Digester digester = new Digester();
        digester.push(this); // Push controller servlet onto the stack
        digester.setValidating(false);
        digester.addFactoryCreate( "*/folders/folder", 
                new FolderFactory( true ) );
        digester.addFactoryCreate( "*/folder/folder", 
                new FolderFactory( true ) );
        digester.addCallMethod( "*/folder/name", "setName", 0 );
        digester.addCallMethod( "*/folder/description", "setDescription", 0 );
        
        // PhotoInfo mappings
        digester.addFactoryCreate( "*/photos/photo", new PhotoFactory() );
        // After the photo  is ready, inform listeners  if a new photo was created.
        digester.addRule( "*/photos/photo", new Rule() {
            @Override
            public void end( String namespace, String name ) {
                Boolean isCreatingNew = (Boolean) digester.pop( STACK_CREATING_NEW );
                if ( isCreatingNew.booleanValue() ) {
                    photoCount++;
                    fireObjectImportedEvent( digester.peek() );
                }
            }
        });
        digester.addCallMethod( "*/photos/photo/shooting-place", "setShootingPlace", 0 );
        digester.addCallMethod( "*/photos/photo/photographer", "setPhotographer", 0 );
        digester.addCallMethod( "*/photos/photo/camera", "setCamera", 0 );
        digester.addCallMethod( "*/photos/photo/lens", "setLens", 0 );
        digester.addCallMethod( "*/photos/photo/film", "setFilm", 0 );
        digester.addCallMethod( "*/photos/photo/orig-fname", "setOrigFname", 0 );
        digester.addCallMethod( "*/photos/photo/description", "setDesc", 0 );
        digester.addCallMethod( "*/photos/photo/tech-notes", "setTechNotes", 0 );
        digester.addCallMethod( "*/photos/photo/f-stop", "setFStop", 0, new Class[] {Double.class} );
        digester.addCallMethod( "*/photos/photo/shutter-speed", "setShutterSpeed", 0, new Class[] {Double.class} );
        digester.addCallMethod( "*/photos/photo/focal-length", "setFocalLength", 0, new Class[] {Double.class} );
        digester.addCallMethod( "*/photos/photo/quality", "setQuality", 0, new Class[] {Integer.class} );
        digester.addCallMethod( "*/photos/photo/film-speed", "setFilmSpeed", 0, new Class[] {Integer.class} );
        
        digester.addFactoryCreate( "*/photos/photo/shoot-time", new FuzzyDateFactory() );
        digester.addSetNext( "*/photos/photo/shoot-time", "setFuzzyShootTime" );
        
        // Crop settings
        digester.addCallMethod( "*/photos/photo/crop", "setPrefRotation", 1, new Class[] {Double.class} );
        digester.addCallParam( "*/photos/photo/crop", 0, "rot" );
        digester.addFactoryCreate( "*/photos/photo/crop", new RectangleFactory() );
        digester.addSetNext( "*/photos/photo/crop", "setCropBounds" );
        
        /* 
         Raw conversion setting mappings. All of these expect that a RawSettingsFactory
         is the topmost object in Digester stack. Note that in practice there must be 
         and explicit rule for each raw setting field since the rule that
         instantates the raw setting object & assign it to the parent object will 
         override this.
         */
        digester.addObjectCreate( "*/raw-conversion", RawSettingsFactory.class );
        digester.addCallMethod( "*/raw-conversion/whitepoint", "setWhite", 0, new Class[] {Integer.class} );
        digester.addCallMethod( "*/raw-conversion/blackpoint", "setBlack", 0, new Class[] {Integer.class} );
        digester.addCallMethod( "*/raw-conversion/ev-corr", "setEvCorr", 0, new Class[] {Double.class} );
        digester.addCallMethod( "*/raw-conversion/hlight-corr", "setHlightComp", 0, new Class[] {Double.class} );
        digester.addRule( "*/raw-conversion/color-balance", new Rule() {
            @Override
            public void begin( String namespace, String name, Attributes attrs ) {
                String rgStr = attrs.getValue( "red-green-ratio" );
                String bgStr = attrs.getValue( "blue-green-ratio" );
                double bg = 1.0;
                double rg = 1.0;
                try {
                    bg = Double.parseDouble( bgStr );
                    rg = Double.parseDouble(rgStr);
                } catch (NumberFormatException ex) {
                    digester.createSAXException( ex );
                }
                RawSettingsFactory f = (RawSettingsFactory) digester.peek();
                f.setRedGreenRation( rg );            
                f.setBlueGreenRatio( bg );            
            } 
        });
        digester.addRule( "*/raw-conversion/daylight-color-balance", new Rule() {
            @Override
            public void begin( String namespace, String name, Attributes attrs ) {
                String rgStr = attrs.getValue( "red-green-ratio" );
                String bgStr = attrs.getValue( "blue-green-ratio" );
                double bg = 1.0;
                double rg = 1.0;
                
                try {
                    bg = Double.parseDouble( bgStr );
                    rg = Double.parseDouble(rgStr);
                } catch (NumberFormatException ex) {
                    digester.createSAXException( ex );
                }
                RawSettingsFactory f = (RawSettingsFactory) digester.peek();
                f.setDaylightMultipliers( new double[] {rg, 1.0, bg} );
            } 
        });
        digester.addRuleSet( new ChannelMapRuleSet( "*/photo/") );
        digester.addRule( "*/photo/color-mapping", new Rule() {
            @Override
            public void end( String namespace, String name ) {
                PhotoInfo p = (PhotoInfo) digester.peek(1);
                ChannelMapOperationFactory f = 
                        (ChannelMapOperationFactory) digester.peek();
                p.setColorChannelMapping( f.create() );                
            }
        });
        
        digester.addObjectCreate( "*/photo/raw-conversion", RawSettingsFactory.class );
        digester.addRule( "*/photo/raw-conversion", new Rule() {
            @Override
            public void end( String namespace, String name ) {
                PhotoInfo p = (PhotoInfo)digester.peek(1);
                RawSettingsFactory f = (RawSettingsFactory) digester.peek();
                try {
                    p.setRawSettings( f.create() );
                } catch (PhotovaultException ex) {
                    digester.createSAXException( ex );
                }
            }
        });  
        
        // Instance mappings
        digester.addFactoryCreate( "*/photo/instances/instance", new InstanceFactory() );
        digester.addCallMethod( "*/instance/file-size", "setFileSize", 0, new Class[] {Long.class} );
        digester.addCallMethod( "*/instance/width", "setWidth", 0, new Class[] {Integer.class} );
        digester.addCallMethod( "*/instance/height", "setHeight", 0, new Class[] {Integer.class} );
        digester.addCallMethod( "*/instance/crop", "setRotated", 1, new Class[] {Double.class} );
        digester.addCallParam( "*/instance/crop", 0, "rot" );
        digester.addFactoryCreate( "*/instance/crop", new RectangleFactory() );
        digester.addSetNext( "*/instance/crop", "setCropBounds" );
        digester.addRule( "*/instance/hash", new Rule() {
            @Override
            public void body( String namespace, String name, String text ) {
/*                
                byte[] hash = Base64.decode( text );
                ImageInstance i = (ImageInstance) digester.peek();
                i.setHash( hash );
*/
            }
        } );
        digester.addRuleSet( new ChannelMapRuleSet( "*/instance/") );
        digester.addRule( "*/instance/color-mapping", new Rule() {
            @Override
            public void end( String namespace, String name ) {
/*
                ImageInstance i = (ImageInstance) digester.peek(1);
                ChannelMapOperationFactory f = 
                        (ChannelMapOperationFactory) digester.peek();
                i.setColorChannelMapping( f.create() );                
*/
            }
        });
        // Raw conversion parsing was already specified earlier. We just need a 
        // method for binding the RawConversionSettings object to instance
        digester.addObjectCreate( "*/instance/raw-conversion", RawSettingsFactory.class );
        digester.addRule( "*/instance/raw-conversion", new Rule() {
            @Override
            public void end( String namespace, String name ) {
/*
                ImageInstance i = (ImageInstance)digester.peek(1);
                RawSettingsFactory f = (RawSettingsFactory) digester.peek();
                try {
                    i.setRawSettings( f.create() );
                } catch (PhotovaultException ex) {
                    digester.createSAXException( ex );
                }
*/
            }
        });  
        /*
         TODO: import information about image locations. In first phase, this
         can be done by indexing images as an external volume...
        */
        

        digester.addSetNext( "*/photo/instances/instance", "addInstance" );
        
        // folder handling
        digester.addFactoryCreate( "*/photos/photo/folders/folder-ref", new FolderFactory( false ) );
        digester.addSetTop( "*/photos/photo/folders/folder-ref", "addPhoto" );
        
        fireStatusChangeEvent( IMPORTING_STARTED );
        try {
            digester.parse( reader );
        } catch (SAXException ex) {
            fireErrorEvent( ex.getMessage() );
            ex.printStackTrace();
        } catch (IOException ex) {
            fireErrorEvent( ex.getMessage() );
            ex.printStackTrace();
        }
        fireStatusChangeEvent( IMPORTING_COMPLETED );
    }  
}

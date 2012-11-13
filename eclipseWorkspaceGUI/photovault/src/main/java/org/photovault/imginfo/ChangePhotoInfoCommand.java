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

package org.photovault.imginfo;

import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandException;
import org.photovault.command.DataAccessCommand;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.dcraw.RawSettingsFactory;
import org.photovault.folder.FolderEditor;
import org.photovault.folder.FolderPhotoAssocDAO;
import org.photovault.folder.FolderPhotoAssociation;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.image.ChanMapOp;
import org.photovault.image.ChannelMapOperationFactory;
import org.photovault.image.ColorCurve;
import org.photovault.image.CropOp;
import org.photovault.image.DCRawMapOp;
import org.photovault.image.DCRawOp;
import org.photovault.image.ImageOpChain;
import org.photovault.replication.Change;
import org.photovault.replication.ChangeDTO;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.VersionedObjectEditor;

/**
  Command for changing the properties of {@link PhotoInfo}. This command provides 
 methods for changing all "simple properties of the photo and for adding or deleting
 it from folders. It can also be used for creating a new photo.
 
 */
public class ChangePhotoInfoCommand extends DataAccessCommand {
    
    static Log log = LogFactory.getLog( ChangePhotoInfoCommand.class );
    

    /**
     Construct a new command that creates a new PhotoInfo object.
     */
    public ChangePhotoInfoCommand() {
        
    }

    /** 
     Creates a new instance of ChangePhotoInfoCommand 
     @photoId UUID of the PhotoInfo to change 
     */
    public ChangePhotoInfoCommand( UUID photoUuid ) {
        if ( photoUuid != null ) {
            photoUuids.add( photoUuid );
        }
    }
    
    /** 
     Creates a new instance of ChangePhotoInfoCommand 
     @photoId Array of UUIDs of all PhotoInfo objects that will be changed.
     */
    public ChangePhotoInfoCommand( UUID[] photoUuids ) {
        for ( UUID id : photoUuids ) {
            this.photoUuids.add( id );
        }
    }

    /** 
     Creates a new instance of ChangePhotoInfoCommand 
     @photoId Collection of UUIDs of all PhotoInfo objects that will be changed.
     */
    public ChangePhotoInfoCommand( Collection<UUID> photoIds ) {
        this.photoUuids.addAll( photoIds );
    }
        /** 
     Creates a new instance of ChangePhotoInfoCommand 
     @photoId Collection of IDs of all PhotoInfo objects that will be changed.
     */
    
    /**
     Fields that have been changed by this command
     */
    Map<PhotoInfoFields, Object> changedFields = new HashMap<PhotoInfoFields, Object>();
    
    /**
     Folders the photos should be added to
     */
    Set<UUID> addedToFolders = new HashSet<UUID>();

    /**
     Folders the photos should be removed from
     */    
    Set<UUID> removedFromFolders = new HashSet<UUID>();
    
    /**
     UUIDs of all photos that will be changed by this command.
     */
    Set<UUID> photoUuids = new HashSet<UUID>();
    
    /**
     Photo instance with the changes applied (in command handler's persistence 
     context or later detached)
     */
    Set<PhotoInfo> changedPhotos = null;

    List<ChangeDTO> changes = new ArrayList();

    /**
     Get photo instance with the changes applied (in command handler's persistence 
     context or later detached)
     */
    public Set<PhotoInfo> getChangedPhotos() {
        return changedPhotos;
    }

    public List<ChangeDTO> getChanges() {
        return changes;
    }
    
    /**
     Set a field to be changed to new value
     @param field Foeld code for the field to change
     @param newValue New value for the field.
     */
    public void setField( PhotoInfoFields field, Object newValue ) {
        log.debug( "setField " + field + ": " + newValue );
        changedFields.put( field, newValue );
    }
    
    /**
     Get the ne wvalue for a field
     @return The value that will be changed to photos when this command is 
     executed or <code>null</code> if the field will be left unchanged.
     */
    public Object getField( PhotoInfoFields field ) {
        log.debug( "getField " + field  );
        return changedFields.get( field );
    }
    
    
    
    
    // Utility methods for setting fields
    
    public void setCamera( String newValue ) {
        setField( PhotoInfoFields.CAMERA, newValue );
    }
    
    public void setCropBounds( Rectangle2D newValue ) {
        setField( PhotoInfoFields.CROP_BOUNDS, newValue );
    }

    public void setDescription( String newValue ) {
        setField( PhotoInfoFields.DESCRIPTION, newValue );
    }

    public void setFStop( double newValue ) {
        setField( PhotoInfoFields.FSTOP, Double.valueOf( newValue ) );
    }

    public void setFilm( String newValue ) {
        setField( PhotoInfoFields.FILM, newValue );
    }

    public void setFilmSpeed( Integer newValue ) {
        setField( PhotoInfoFields.FILM_SPEED, newValue );
    }

    public void setFocalLength( Integer newValue ) {
        setField( PhotoInfoFields.FOCAL_LENGTH, newValue );
    }

    public void setLens( String newValue ) {
        setField( PhotoInfoFields.LENS, newValue );
    }

    public void setOrigFname( String newValue ) {
        setField( PhotoInfoFields.ORIG_FNAME, newValue );
    }

    public void setPhotographer( String newValue ) {
        setField( PhotoInfoFields.PHOTOGRAPHER, newValue );
    }

    public void setPrefRotation( double newValue ) {
        setField( PhotoInfoFields.PREF_ROTATION, Double.valueOf( newValue ) );
    }

    public void setQuality( Integer newValue ) {
        setField( PhotoInfoFields.QUALITY, newValue );
    }

    public void setRawSettings( RawConversionSettings newValue ) {
        setField( PhotoInfoFields.RAW_SETTINGS, newValue );
    }

    public void setShootTime( Date newValue ) {
        setField( PhotoInfoFields.SHOOT_TIME, newValue );
    }

    public void setShootingPlace( String newValue ) {
        setField( PhotoInfoFields.SHOOTING_PLACE, newValue );
    }

    public void setShutterSpeed( Double newValue ) {
        setField( PhotoInfoFields.SHUTTER_SPEED, newValue );
    }

    public void setTechNotes( String newValue ) {
        setField( PhotoInfoFields.TECH_NOTES, newValue );
    }

    public void setTimeAccuracy( Double newValue ) {
        setField( PhotoInfoFields.TIME_ACCURACY, newValue );
    }

    public void setUUID( UUID newValue ) {
        setField( PhotoInfoFields.UUID, newValue );
    }

    
    public enum FolderStates {
        UNMODIFIED,
        ADDED,
        REMOVED
    };
    
    /**
     Instruct command to add all photos to given folder
     @param folder The folder into which the photos will be added
     */
    public void addToFolder( PhotoFolder folder ) {
        removedFromFolders.remove( folder.getUuid() );
        addedToFolders.add( folder.getUuid() );
    }
    
    /**
     Instruct command to add all photos to given folder
     @param folder The folder into which the photos will be added
     */
    public void removeFromFolder( PhotoFolder folder ) {
        addedToFolders.remove( folder.getUuid() );
        removedFromFolders.add( folder.getUuid() );
    }
    
    
    public FolderStates getFolderState( PhotoFolder folder ) {
        if ( addedToFolders.contains( folder.getUuid() ) ) {
            return FolderStates.ADDED;        
        } else if ( removedFromFolders.contains( folder.getUuid() ) ) {
            return FolderStates.REMOVED;
        }
        return FolderStates.UNMODIFIED;
    }    
        
    private void setRawField( ProcGraphEditorHelper rawConvHelper,
            ProcGraphEditorHelper mapHelper,
            PhotoInfoFields field, Object newValue ) 
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        switch ( field ) {
            case RAW_BLACK_LEVEL:
                mapHelper.setProperty( "black", (Integer)newValue );
                break;
            case RAW_WHITE_LEVEL:
                mapHelper.setProperty( "white", (Integer)newValue );
                break;
            case RAW_CTEMP:
                rawConvHelper.setProperty( "colorTemp", (Double) newValue );
                break;
            case RAW_EV_CORR:
                mapHelper.setProperty("evCorr", (Double) newValue );
                break;
            case RAW_GREEN:
                rawConvHelper.setProperty( "greenGain", (Double) newValue );
                break;
            case RAW_HLIGHT_COMP:
                mapHelper.setProperty( "hlightCompr", newValue);
                break;
            case RAW_HLIGHT_RECOVERY:
                rawConvHelper.setProperty("hlightRecovery", (Integer) newValue );
                break;
            case RAW_WAVELET_DENOISE_THRESHOLD:
                rawConvHelper.setProperty("waveletThreshold", (Float) newValue);
                break;
        }
    }
    
    private void setColorMapField( ProcGraphEditorHelper chanMapHelper,
            PhotoInfoFields field, Object value ) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        switch ( field ) {
            case COLOR_CURVE_VALUE:
                chanMapHelper.setProperty( "channel(value)",
                        (ColorCurve) value );
                break;
            case COLOR_CURVE_RED:
                chanMapHelper.setProperty( "channel(red)",
                        (ColorCurve) value );
                break;
            case COLOR_CURVE_BLUE:
                chanMapHelper.setProperty( "channel(blue)",
                        (ColorCurve) value );
                break;
            case COLOR_CURVE_GREEN:
                chanMapHelper.setProperty( "channel(green)",
                        (ColorCurve) value );
                break;
            case COLOR_CURVE_SATURATION:
                chanMapHelper.setProperty( "channel(saturation)",
                        (ColorCurve) value );
                break;
        }
    }
    
    /**
     Execute the command.
     */
    public void execute() throws CommandException {
        StringBuffer debugMsg = null;
        if ( log.isDebugEnabled() ) {
            debugMsg = new StringBuffer();
            debugMsg.append( "execute()" );
            boolean isFirst = true;
            for ( UUID id : photoUuids ) { 
                debugMsg.append( isFirst ? "Photo ids: " : ", " );
                debugMsg.append( id );
            }
            debugMsg.append( "\n" );
            debugMsg.append( "Changed values:\n" );
            for ( Map.Entry<PhotoInfoFields, Object> e: changedFields.entrySet() ) {
                PhotoInfoFields field = e.getKey();
                Object value = e.getValue();
                debugMsg.append( field ).append( ": " ).append( value ).append( "\n" );
            }
            log.debug( debugMsg );
        }
        PhotoInfoDAO photoDAO = daoFactory.getPhotoInfoDAO();
        Set<PhotoInfo> photos = new HashSet<PhotoInfo>();
        if ( photoUuids.size() == 0 ) {
            PhotoInfo photo = photoDAO.create();
            photos.add( photo );
        } else {
            for ( UUID id : photoUuids ) {
                PhotoInfo photo = photoDAO.findByUUID( id );
                photos.add( photo );
            }
        }
        changedPhotos = new HashSet<PhotoInfo>();
        Set<PhotoInfoFields> rawSettingsFields = 
                EnumSet.range( PhotoInfoFields.RAW_BLACK_LEVEL, PhotoInfoFields.RAW_COLOR_PROFILE);
        Set<PhotoInfoFields> colorCurveFields = 
                EnumSet.range( PhotoInfoFields.COLOR_CURVE_VALUE, PhotoInfoFields.COLOR_CURVE_SATURATION );
        Set<PhotoInfoFields> cropFields = EnumSet.of(
                PhotoInfoFields.CROP_BOUNDS, PhotoInfoFields.PREF_ROTATION );
        
        DTOResolverFactory resolverFactory = daoFactory.getDTOResolverFactory();
        for ( PhotoInfo photo : photos ) {
            /*
            Ensure that this photo is persistence & the instance belongs to 
            current persistence context
             */
            changedPhotos.add( photo );
            VersionedObjectEditor<PhotoInfo> pe = new VersionedObjectEditor(
                    photo, resolverFactory );
            PhotoEditor pep = (PhotoEditor) pe.getProxy();
            RawSettingsFactory rawSettingsFactory = null;
            ChannelMapOperationFactory channelMapFactory = null;

            // New processing operations
            ImageOpChain procChain = photo.getProcessing();
            ProcGraphEditorHelper rawConvHelper = null;
            ProcGraphEditorHelper rawMapHelper = null;
            ProcGraphEditorHelper chanMapHelper = null;
            ProcGraphEditorHelper cropHelper = null;

            try {
            rawConvHelper =
                    new ProcGraphEditorHelper( pe, "dcraw", DCRawOp.class );
            rawMapHelper =
                    new ProcGraphEditorHelper( pe, "raw-map", DCRawMapOp.class );
            chanMapHelper =
                    new ProcGraphEditorHelper( pe, "chan-map", ChanMapOp.class );
            cropHelper =
                    new ProcGraphEditorHelper( pe, "crop", CropOp.class );
            } catch( IllegalAccessException ex ) {
                // Should not happen
                log.error( ex );
                throw new CommandException( 
                        "Unexpected problem instantiating processing grap helpers",
                        ex );
            } catch ( InstantiationException ex ) {
                // Should not happen
                log.error( ex );
                throw new CommandException(
                        "Unexpected problem instantiating processing grap helpers",
                        ex );
            }

            for ( Map.Entry<PhotoInfoFields, Object> e :
                    changedFields.entrySet() ) {
                PhotoInfoFields field = e.getKey();
                Object value = e.getValue();
                try {
                    if ( rawSettingsFields.contains( field ) ) {
                        this.setRawField( rawConvHelper, rawMapHelper, field,
                                value );
                    } else if ( colorCurveFields.contains( field ) ) {
                        setColorMapField( chanMapHelper, field, value );
                    } else if ( field == PhotoInfoFields.CROP_BOUNDS ) {
                        Rectangle2D cropRect = (Rectangle2D) e.getValue();
                        cropHelper.setProperty( "minX", cropRect.getMinX() );
                        cropHelper.setProperty( "maxX", cropRect.getMaxX() );
                        cropHelper.setProperty( "minY", cropRect.getMinY() );
                        cropHelper.setProperty( "maxY", cropRect.getMaxY() );
                    } else if ( field == PhotoInfoFields.PREF_ROTATION ) {
                        Double rot = (Double) e.getValue();
                        cropHelper.setProperty( "rot", rot );
                    } else {
                        pe.setField( e.getKey().getName(), e.getValue() );
                    }
                } catch ( IllegalAccessException ex ) {
                    log.error( "exception while setting field " + field, ex );
                    throw new CommandException(
                            "Illegal access while setting field " + field, ex );
                } catch ( InvocationTargetException ex ) {
                    log.error( "exception while setting field " + field, ex );
                    throw new CommandException(
                            "Invocation target exception while setting field " +
                            field, ex );
                } catch ( NoSuchMethodException ex ) {
                    log.error( "exception while setting field " + field, ex );
                    throw new CommandException(
                            "Non-existing method called while setting field " +
                            field, ex );
                }
            }

            ProcGraphEditorHelper[] chain = new ProcGraphEditorHelper[] {
                rawConvHelper, rawMapHelper, chanMapHelper, cropHelper
            };
            
            String nextNodeInput = null;
            for ( int n = chain.length-1 ; n >= 0 ; n-- ) {
                String sourcePort = null;
                // Find the node that should be connected to input of current node
                for ( int m = n-1 ; m >= 0 ; m-- ) {
                    if ( chain[m].isNodeAlreadyPresent() ) {
                        sourcePort = chain[m].getNodeName() + ".out";
                        break;
                    }
                }
                ProcGraphEditorHelper node = chain[n];
                if ( node.isNodeAlreadyPresent() || 
                        node.addNewNode( sourcePort, nextNodeInput ) ) {
                    nextNodeInput = node.getNodeName() + ".in";
                }
            }


            PhotoFolderDAO folderDAO = daoFactory.getPhotoFolderDAO();
            FolderPhotoAssocDAO assocDAO = daoFactory.getFolderPhotoAssocDAO();
            Set<PhotoFolder> af = new HashSet<PhotoFolder>();
            for ( UUID folderId : addedToFolders ) {
                log.debug( "Adding photo " + photo.getUuid() + " to folder " + folderId );
                PhotoFolder folder = folderDAO.findById( folderId, false );
                VersionedObjectEditor<PhotoFolder> fe =
                        new VersionedObjectEditor<PhotoFolder>( folder, resolverFactory );
                FolderEditor fep = (FolderEditor) fe.getProxy();
                FolderPhotoAssociation a = assocDAO.getAssociation( folder, photo );
                pep.addFolderAssociation( a );
                fep.addPhotoAssociation( a );
                Change<PhotoFolder> ch = fe.apply();
                changes.add( new ChangeDTO<PhotoFolder>( ch )) ;
                af.add(folder);
            }
            Set<PhotoFolder> rf = new HashSet<PhotoFolder>();
            Set<FolderPhotoAssociation> deletedAssocs =
                    new HashSet<FolderPhotoAssociation>();
            for ( UUID folderId : removedFromFolders ) {
                log.debug( "Removing photo " + photo.getUuid() + " from folder " + folderId );
                PhotoFolder folder = folderDAO.findById( folderId, false );
                VersionedObjectEditor<PhotoFolder> fe =
                        new VersionedObjectEditor<PhotoFolder>( folder, resolverFactory );
                FolderEditor fep = (FolderEditor) fe.getProxy();
                FolderPhotoAssociation a = assocDAO.getAssociation( folder, photo );
                fep.removePhotoAssociation( a );
                pep.removeFolderAssociation( a );
                deletedAssocs.add( a );
                Change<PhotoFolder> ch = fe.apply();
                changes.add( new ChangeDTO<PhotoFolder>( ch )) ;
            }
            Change<PhotoInfo> ch = pe.apply();
            changes.add( new ChangeDTO<PhotoInfo>( ch ) );
            for ( FolderPhotoAssociation a : deletedAssocs ) {
                assocDAO.makeTransient( a );
            }
        }
    }
}

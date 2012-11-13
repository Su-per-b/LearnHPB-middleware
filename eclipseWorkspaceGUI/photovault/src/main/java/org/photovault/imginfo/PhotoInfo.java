/*
  Copyright (c) 2006-2007 Harri Kaimio
 
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
import java.awt.image.renderable.ParameterBlock;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.*;
import com.sun.media.jai.codec.*;
import java.awt.Dimension;
import java.awt.image.*;
import java.awt.geom.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.Session;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.dcraw.RawImage;
import org.photovault.folder.*;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ColorCurve;
import org.photovault.image.ImageIOImage;
import org.photovault.image.PhotovaultImage;
import org.photovault.image.PhotovaultImageFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Type;
import org.photovault.image.DCRawMapOp;
import org.photovault.image.ImageOpChain;
import org.photovault.imginfo.dto.FolderRefResolver;
import org.photovault.imginfo.dto.OrigImageRefResolver;
import org.photovault.imginfo.dto.PhotoChangeSerializer;
import org.photovault.replication.ObjectHistory;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.History;
import org.photovault.replication.SetField;
import org.photovault.replication.ValueField;
import org.photovault.replication.Versioned;
import org.photovault.replication.VersionedObjectEditor;

/**
 PhotoInfo represents information about a single photograph
 TODO: write a decent doc!
 */
@Entity
@Table( name = "pv_photos" )
@Versioned( editor = PhotoEditor.class, changeSerializer=PhotoChangeSerializer.class )
public class PhotoInfo implements PhotoEditor {
    
    static Log log = LogFactory.getLog( PhotoInfo.class.getName() );
    
    // String field lengths
    /** Max length of camera field */
    final static int CAMERA_LENGTH = 30;
    /** Max length of shooting place field */
    final static int SHOOTING_PLACE_LENGTH = 30;
    /** Max length of photographer field */
    final static int PHOTOGRAPHER_LENGTH = 30;
    /** Max length of lens field */
    final static int LENS_LENGTH = 30;
    /** Max length of film field */
    final static int FILM_LENGTH = 30;
    /** Max length of origFname field */
    final static int ORIG_FNAME_LENGTH = 30;
    
    /**
     Create a new PhotoInfo. This constructor must be used by persistence layer 
     only. Otherwise, set original image in constructor.
     */
    public PhotoInfo() {
//         uuid = UUID.randomUUID();
        changeHistory = new ObjectHistory<PhotoInfo>( this );
        changeHistory.setTargetUuid( UUID.randomUUID() );                
        changeListeners = new HashSet();
    }
    
    public PhotoInfo( OriginalImageDescriptor original ) {
        changeListeners = new HashSet();
        this.original = original;
        original.photos.add( this );
        changeHistory = new ObjectHistory<PhotoInfo>( this );
    }
    
    
    ObjectHistory<PhotoInfo> changeHistory = null;
    
    @History
    @OneToOne( cascade=CascadeType.ALL )
    @org.hibernate.annotations.Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
    @PrimaryKeyJoinColumn
    public ObjectHistory<PhotoInfo> getHistory() {
        return changeHistory;
    }
    
    public void setHistory( ObjectHistory<PhotoInfo> h ) {
        changeHistory = h;
    }
     

    /**
     Creates an editor for this photo
     @param rf DTO resolver factory usedby the editor
     @return
     */
    public VersionedObjectEditor<PhotoInfo> editor( DTOResolverFactory rf ) {
        return new VersionedObjectEditor<PhotoInfo>(  this , rf );
    }
        
    
    /**
     Get a PhotoInfo object with given ID
     @param session the persistence context into which the object is requested
     @param uid UID of the requested object.
     @return The PhotoInfo with given UID or <code>null</code> if no such is 
     available.
     */
    public static PhotoInfo findPhotoInfo( Session session, Integer uid )
    throws PhotoNotFoundException {        
        PhotoInfo photo =  (PhotoInfo) session.get( PhotoInfo.class, uid );
        if ( photo == null ) {
            throw new PhotoNotFoundException();
        }
        return photo;
    }
    
    
    /**
     Retrieves the PhotoInfo objects whose original instance has a specific hash code
     @param hash The hash code we are looking for
     @return An array of matching PhotoInfo objects or <code>null</code>
     if none found.
     @deprecated Use PhotoInfoDAO#findPhotosWithOriginalHash instead.
     */
    static public PhotoInfo[] retrieveByOrigHash(byte[] hash) {
        throw new UnsupportedOperationException( 
                "retrieveByOrigHash not supported with Hibernate, " +
                "Use PhotoInfoDAO#findPhotosWithOriginalHash instead" );
//        PhotoInfo photos[] = null;
//        List result = new ArrayList();
//        if ( result.size() > 0 ) {
//                photos = (PhotoInfo[]) result.toArray( new PhotoInfo[result.size()] );
//            }
//        return photos;  
    }
    
    /**
     Creates a new persistent PhotoInfo object and stores it in database
     (just a dummy one with no meaningful field values)
     @return A new PhotoInfo object
     @deprecated Use the {@link PhotoInfoDAO#create()} instead
     */
    public static PhotoInfo create() {
        PhotoInfo photo = new PhotoInfo();
        // photo.uuid = UUID.randomUUID();
        // photo.setHistory( new PhotoInfoChangeSupport( photo ) );
        return photo;
    }
    
    /**
     Creates a new PhotoInfo object with a given UUID
     @param uuid UUID for the new object
     @return A new PhotoInfo object
     */
    public static PhotoInfo create(UUID uuid) {
        PhotoInfo photo = new PhotoInfo();
        // photo.uuid = uuid;
        // photo.setHistory( new PhotoInfoChangeSupport( photo ) );
        return photo;
    }
    
    /**
     Create a new Photo based on an image file. Unless the image resides in an external
     volume this method first copies a given image file to the default database
     volume. It then extracts the information it can from the image file and
     creates a PhotoInfo object based on this.
     
     @param imgFile File object that describes the image file that is to be added
     to the database
     @return The PhotoInfo object describing the new file.
     @throws PhotoNotFoundException if the file given as imgFile argument does
     not exist or is unaccessible. This includes a case in which imgFile is part
     of normal Volume.
     @deprecated Not supported anymore
     
     // TODO: Move this into a command object.
     */
    public static PhotoInfo addToDB( File imgFile )  throws PhotoNotFoundException {
        throw new UnsupportedOperationException( "addToDb is not supported anymore" );
    }
    
    /**
     Get the globally unique ID for this photo;
     */
    @Column( name = "photo_uuid" )
    @org.hibernate.annotations.Type( type = "org.photovault.persistence.UUIDUserType" )
    @Id
    public UUID getUuid() {
        return changeHistory.getTargetUuid();
    }    
    
    public void setUuid( UUID uuid ) {
	// this.uuid = uuid;
        changeHistory.setTargetUuid( uuid );
        modified();
    }
    

    /**
     This method reads the metadata from image file and updates the PhotoInfo record from it
     @param f The file to read
     */
    void updateFromFileMetadata( File f ) {
        ImageIOImage img = ImageIOImage.getImage( f, false, true );
        if ( img == null ) {
            return;
        }
        
        
        setShootTime( img.getTimestamp() );

        // Exposure
        setFStop( img.getAperture() );
        
        setShutterSpeed( img.getShutterSpeed() );
        setFocalLength( img.getFocalLength() );
        setFilmSpeed( img.getFilmSpeed() );
        String camera = img.getCamera();
        if ( camera.length() > CAMERA_LENGTH ) {
            camera = camera.substring( 0, CAMERA_LENGTH );
        }
        setCamera( camera );
        
    }

    /**
     Reads metadata from raw camera file (using dcraw) and updates PhotoInfo
     fields based on that
     @param f The raw file to read
     @return <code>true</code> if meta data was succesfully read, <code>false</code> 
     otherwise (e.g. if f was not a raw image file.
     */
    private boolean updateFromRawFileMetadata( File f ) {
        RawImage ri;
        try {
            ri = new RawImage(f);
        } catch (PhotovaultException ex) {
            return false;
        }
        if ( !ri.isValidRawFile() ) {
            return false;
        }
        setShootTime( ri.getTimestamp() );
        setFStop( ri.getAperture() );
        setShutterSpeed( ri.getShutterSpeed() );
        setFilmSpeed( ri.getFilmSpeed() );
        String camera = ri.getCamera();
        if ( camera.length() > CAMERA_LENGTH ) {
            camera = camera.substring( 0, CAMERA_LENGTH );
        }
        setCamera( camera );
        setFilm( "Digital" );
        setFocalLength( ri.getFocalLength() );
        
        ri.autoExpose();
        setRawSettings( ri.getRawSettings() );
        return true;
    }
    
    
    /**
     Deletes the PhotoInfo and all related instances from database. 
     
     @deprecated This method does not do any error checking whether the instances
     are actually deleted. This is sometimes useful for e.g. cleaning up a test
     environment but production code should use 
     {@link #delete( boolean deleteExternalInstances )} instead.
     */
    public void delete() {
        log.warn( "Calling PhotoInfo.delete()" );
        // First delete all instances
        
        // Then delete the photo from all folders it belongs to
//        if ( folders != null ) {
//            Object[] foldersArray = folders.toArray();
//            for ( int n = 0; n < foldersArray.length; n++ ) {
//                ((PhotoFolder)foldersArray[n]).removePhoto( this );
//            }
//        }
    }
    
    /**
     Tries to delete this photo, including all of its instances. If some
     instances cannot be deleted, other instances are deleted anyway but the actual
     PhotoInfo and its associations to folders are preserved.
     
     @deprecated TODO: This should be reimplemented according to new database schema
     
     @param deleteExternalInstances Tries to delete also instances on external 
     volumes
     
     @throws PhotovaultException if some instances of the photo cannot be deleted
     */
    public void delete( boolean deleteExternalInstances ) throws PhotovaultException {
        throw new UnsupportedOperationException( "delete() not implemented in Hibernate schema" );
    }
        
    
    /**
     Adds a new listener to the list that will be notified of modifications to this object
     @param l reference to the listener
     */
    public void addChangeListener( PhotoInfoChangeListener l ) {
        changeListeners.add( l );
    }
    
    /**
     Removes a listenre
     */
    public void removeChangeListener( PhotoInfoChangeListener l ) {
        changeListeners.remove( l );
    }
    
    private void notifyListeners( PhotoInfoChangeEvent e ) {
        Iterator iter = changeListeners.iterator();
        while ( iter.hasNext() ) {
            PhotoInfoChangeListener l = (PhotoInfoChangeListener) iter.next();
            l.photoInfoChanged( e );
        }
    }
    
    protected void modified() {
        lastModified = new java.util.Date();
        notifyListeners( new PhotoInfoChangeEvent( this ) );
    }
    
    /**
     set of the listeners that should be notified of any changes to this object
     */
    HashSet changeListeners = null;

    
    /**
     * Describe timeAccuracy here.
     */
    private double timeAccuracy;
    
    /**
     * Describe quality here.
     */
    private int quality;
    
    /**
     * Describe lastModified here.
     */
    private java.util.Date lastModified;
    
    /**
     * Describe techNotes here.
     */
    private String techNotes;
    
    /**
     * Describe origFname here.
     */
    private String origFname;


    private OriginalImageDescriptor original;

    /**
     Get image descriptor for original of this photo
     @return original's image descriptor.
     */
    @ValueField( dtoResolver=OrigImageRefResolver.class )
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @org.hibernate.annotations.Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE } )
    @JoinColumn( name = "original_id", nullable = true )    
    @org.hibernate.annotations.AccessType( "field" )
    public OriginalImageDescriptor getOriginal() {
        return original;
    }
    
    /**
     Set the original for this photo. Note that this method should be used only
     by persistence layer. Otherwise the original must be set in constructor.
     @param original image descriptor for the original
     */
    public void setOriginal( OriginalImageDescriptor original ) {
        this.original = original;
        if ( original != null ) {
            original.photos.add( this );
        }
    }
    
    
    /**
     Find instance that is preferred for use in particular situation. This function 
     seeks for an image that has at least a given resolution, has certain
     operations already applied and is available.
     @param requiredOpers Set of operations that must be applied correctly 
     in the returned image (but not that the operations need not be applied if 
     this photo does not specify some operation. So even if this is non-empty
     it is possible that the method returns original image!
     @param allowedOpers Set of operations that may be applied to the returned
     image
     @param minWidth Minimum width of the returned image in pixels
     @param minHeight Minimum height of the returned image in pixels
     @param maxWidth Maximum width of the returned image in pixels
     @param maxHeight Maximum height of the returned image in pixels
     @return Image that best matches the given criteria or <code>null</code>
     if no suct image exists or is not available.
     */
    public ImageDescriptorBase getPreferredImage( Set<ImageOperations> requiredOpers,
            Set<ImageOperations> allowedOpers, int minWidth, int minHeight,
            int maxWidth, int maxHeight ) {
        ImageDescriptorBase preferred = null;
        EnumSet<ImageOperations> appliedPreferred = null;
        
        // We are not interested in operations that are not specified for this photo
        EnumSet<ImageOperations> specifiedOpers = getAppliedOperations();
        requiredOpers = EnumSet.copyOf( requiredOpers );
        requiredOpers.removeAll( EnumSet.complementOf( specifiedOpers ) );
        
        /*
         Would the original be OK?
         */
        if ( requiredOpers.size() == 0 && 
                original.getWidth() <= maxWidth &&
                original.getHeight() <= maxHeight &&
                original.getFile().findAvailableCopy() != null ) {
            preferred = original;
            appliedPreferred = EnumSet.noneOf( ImageOperations.class );
        }

        // Calculate minimum & maimum scaling of resolution compared to original
        double minScale = ((double)minWidth) / ((double)original.getWidth());
        double maxScale = ((double)maxHeight) / ((double)original.getHeight());
        if ( allowedOpers.contains( ImageOperations.CROP ) ) {
            Dimension croppedSize = getCroppedSize();
            double aspectRatio = croppedSize.getWidth()/croppedSize.getHeight();
            double miw = minWidth;
            double mih = minHeight;
            double maw = maxWidth;
            double mah = maxHeight;
            if ( mih == 0.0 || (miw / mih) > aspectRatio ) {
                mih = miw / aspectRatio;
            }
            if ( mih > 0.0 && (miw / mih) < aspectRatio ) {
                miw = mih * aspectRatio;
            }
            if ( maw/mah > aspectRatio ) {
                maw = mah * aspectRatio;
            }
            if ( maw/mah < aspectRatio ) {
                mah = maw / aspectRatio;
            }
            minScale = ((double)miw) / ((double)croppedSize.getWidth());
            maxScale = ((double)maw) / ((double)croppedSize.getWidth());
        }

        // Check the copies
        Set<CopyImageDescriptor> copies = original.getCopies();
        for ( CopyImageDescriptor copy : copies ) {
            double scale = ((double)copy.getWidth()) / ((double)original.getWidth());
            if ( copy.getAppliedOperations().contains( ImageOperations.CROP ) ) {
                scale = ((double)copy.getWidth()) / ((double)getCroppedSize().getWidth());
            }
            if ( scale >= minScale && scale <= maxScale &&
                    copy.getFile().findAvailableCopy() != null ) {
                EnumSet<ImageOperations> applied = copy.getAppliedOperations();
                if ( applied.containsAll( requiredOpers ) && 
                        allowedOpers.containsAll( applied ) && 
                        isConsistentWithCurrentSettings( copy ) ) {
                    
                    // This is a potential one
                    if ( preferred == null || !appliedPreferred.containsAll( applied ) ) {
                        preferred = copy;
                        appliedPreferred = applied;                        
                    }
                }
            }
        }
        return preferred;
    }
    
    /**
     Get operations that have been applied to this photo.
     @return set of {@link ImageOperations} values for those operations that have 
     been applied.
     */
    @Transient
    public EnumSet<ImageOperations> getAppliedOperations() {
        EnumSet<ImageOperations> applied = EnumSet.noneOf( ImageOperations.class );
        
        if ( !this.getCropBounds().contains( 0.0, 0.0, 1.0, 1.0 ) ||
                this.getPrefRotation() != 0.0 ) {
            applied.add( ImageOperations.CROP );
        }
        // Check for raw conversion
        if ( getRawSettings() != null ) {
            applied.add( ImageOperations.RAW_CONVERSION );
        }
        // Check for color mapping
        ChannelMapOperation colorMap = getColorChannelMapping();
        if ( colorMap != null ) {
            applied.add( ImageOperations.COLOR_MAP );
        }
        return applied;
    }
    
    /**
     Returns a thumbnail of this image. If no thumbnail instance is yet available, 
     creates a new instance on the default volume. Otherwise loads an existing 
     thumbnail instance. <p>
     
     If thumbnail creation fails of if there is no image instances available at 
     all, returns a default thumbnail image.
     @return Thumbnail of this photo or default thumbnail if no photo instances 
     available
     */
    @Transient
    public Thumbnail getThumbnail() {
        log.debug( "getThumbnail: entry, Finding thumbnail for " + getUuid() );
        if ( thumbnail == null ) {
            thumbnail = getExistingThumbnail();
            if ( thumbnail == null ) {
                // Next try to create a new thumbnail instance
                log.debug( "No thumbnail found, creating" );
                createThumbnail();
            }
        }
        if ( thumbnail == null ) {
            // Thumbnail was not successful created, most probably because there
            // is no available instance
            thumbnail = Thumbnail.getErrorThumbnail();
            oldThumbnail = null;
        }
        
        log.debug( "getThumbnail: exit" );
        return thumbnail;
    }
    
    /**
     Returns an existing thumbnail for this photo but do not try to construct a 
     new one if there is no thumbnail already created.
     @return Thumbnail for this photo or null if none is found.
     */
    @Transient
    public Thumbnail getExistingThumbnail() {
        if ( thumbnail == null ) {
            log.debug( "Finding thumbnail from database" );
            ImageDescriptorBase img = getPreferredImage(
                    EnumSet.allOf( ImageOperations.class),
                    EnumSet.allOf( ImageOperations.class),
                    0, 0, 200, 200 );
            if ( img != null ) {
                log.debug( "Found thumbnail from database" );
                // TODO: This must take also locator.
                thumbnail = Thumbnail.createThumbnail( this,
                        img.getFile().findAvailableCopy() );
                oldThumbnail = null;
            }
        }
        return thumbnail;
    }
    
    /**
     Returns true if the photo has a Thumbnail already created,
     false otherwise
     */
    public boolean hasThumbnail() {
        log.debug( "hasThumbnail: entry, Finding thumbnail for " + getUuid() );
        if ( thumbnail == null ) {
            thumbnail = getExistingThumbnail();
        }
        log.debug( "hasThumbnail: exit" );
        return ( thumbnail != null && thumbnail != Thumbnail.getDefaultThumbnail() );
    }
    
    Thumbnail thumbnail = null;
    
    /**
     Reference to an outdated thumbnail image while a new thumbnail in being created
     */
    Thumbnail oldThumbnail = null;
    
    @Transient
    public Thumbnail getOldThumbnail() {
        return oldThumbnail;
    }
    
    /**
     Invalidates the current thumbnail:
     <ul>
     <li>Set thumbnail to null</li>
     <li>Set oldThumbnail to the previous thumbnail</li>
     </ul>
     */
    private void invalidateThumbnail() {
        if ( thumbnail != null ) {
            oldThumbnail = thumbnail;
            thumbnail = null;
        }
    }
    
    /**
     Delete all thumbnail/copy instance that do not match to the image settings.
     */
    private void purgeInvalidInstances() {
        log.debug( "entry: purgeInvalidInstances" );
        throw new UnsupportedOperationException( "ImageInstance has been deprecated!!!" );
//        List<ImageInstance> purgeList = new ArrayList<ImageInstance>();
//        for ( ImageInstance instance : instances ) {
//            if ( instance.getInstanceType() == ImageInstance.INSTANCE_TYPE_THUMBNAIL
//                    && !matchesCurrentSettings( instance ) ) {
//                purgeList.add( instance );
//            } else if ( instance.getInstanceType() == ImageInstance.INSTANCE_TYPE_MODIFIED
//                    && !isConsistentWithCurrentSettings( instance ) ) {
//                purgeList.add( instance );                
//            }
//        }
//        log.debug( "Deleting " + purgeList.size() + " instances" );
//        for ( ImageInstance i : purgeList ) {
//            ODMGXAWrapper txw = new ODMGXAWrapper();
//            txw.lock( this, Transaction.WRITE );
//            txw.lock( i, Transaction.WRITE );
//            instances.remove( i );
//            i.delete();
//            txw.commit();
//        }
//        log.debug( "exit: purgeInvalidInstances" );        
    }

    /**
     Helper function to calculate aspect ratio of an image
     @param width width of the image
     @param height height of the image
     @param pixelAspect Aspect ratio of a single pixel (width/height)
     @return aspect ratio (width/height)
     
     @deprecated Use {@link PhotoInstanceCreator} instead
     */
    private double getAspect( int width, int height, double pixelAspect ) {
        return height > 0
                ? pixelAspect*(((double) width) / ((double) height )) : -1.0;
    }
    
    /**
     Helper method to check if a image is ok for thumbnail creation, i.e. that
     it is large enough and that its aspect ration is same as the original has
     @param width width of the image to test
     @param height Height of the image to test
     @param minWidth Minimun width needed for creating a thumbnail
     @param minHeight Minimum height needed for creating a thumbnail
     @param origAspect Aspect ratio of the original image

     @deprecated Use {@link PhotoInstanceCreator} instead
     */
    private boolean isOkForThumbCreation( int width, int height,
            int minWidth, int minHeight, double origAspect, double aspectAccuracy ) {
        if ( width < minWidth ) return false;
        if ( height < minHeight ) return false;
        double aspect = getAspect( width, height, 1.0 );
        if ( Math.abs( aspect - origAspect) / origAspect > aspectAccuracy )  {
            return false;
        }
        return true;
    }
        
    
    private boolean isConsistentWithCurrentSettings( CopyImageDescriptor img ) {
        EnumSet<ImageOperations> applied = img.getAppliedOperations();
        if ( applied.contains( ImageOperations.CROP ) && 
                !(Math.abs(img.getRotation() - getPrefRotation( ) ) < 0.0001
                && img.getCropArea().equals( getCropBounds() ) ) ) {
            return false;
        }
        if ( applied.contains( ImageOperations.COLOR_MAP ) ) {
            ChannelMapOperation imgCm = img.getColorChannelMapping();
            ChannelMapOperation channelMap = getColorChannelMapping();
            if ( channelMap != null && !channelMap.isAlmostEqual( imgCm, 0.005 ) ) {
                return false;
            }
            if ( channelMap == null && imgCm != null && !imgCm.isAlmostEqual(channelMap, 0.005 ) ) {
                return false;
            }
        }
        RawConversionSettings rawSettings = getProcessing().getRawConvSettings();
        if ( applied.contains( ImageOperations.RAW_CONVERSION ) &&
                !( rawSettings == null || rawSettings.equals( img.getRawSettings() ) ) ) {
            return false;
        }
        return true;
                
    }
    
    /**
     Creates thumbnail & preview instances in given volume. The preview instance 
     is created only if one does not exist currently or it is out of date.
     TODO: Thiuis chould be refactored into a more generic and configurable 
     framework for creating needed instances.
     */
    protected void createThumbnail( VolumeBase volume ) {
        boolean recreatePreview = true;
        EnumSet<ImageOperations> previewOps = EnumSet.of( 
                ImageOperations.COLOR_MAP, 
                ImageOperations.RAW_CONVERSION );
        ImageDescriptorBase previewImage = this.getPreferredImage( EnumSet.noneOf( ImageOperations.class ),
                previewOps, 1024, 1024, 2048, 2048 );
        if ( previewImage != null ) {
            recreatePreview = false;
        }
        createThumbnail( volume, recreatePreview );
    }
    
    /** Creates new thumbnail and preview instances for this image on specific volume
     @param volume The volume in which the instance is to be created

     @deprecated Use {@link PhotoInstanceCreator} instead
     
     */
    protected void createThumbnail( VolumeBase volume, boolean createPreview ) {
        
        log.debug( "Creating thumbnail for " + getUuid() );
        
        // Maximum size of the thumbnail
        int maxThumbWidth = 100;
        int maxThumbHeight = 100;
        checkCropBounds();
        
        /*
         Determine the minimum size for the instance used for thumbnail creation
         to get decent image quality.
         The cropped portion of the image must be roughly the same
         resolution as the intended thumbnail.
         */
        double cropWidth = cropMaxX - cropMinX;
        cropWidth = ( cropWidth > 0.000001 ) ? cropWidth : 1.0;
        double cropHeight = cropMaxY - cropMinY;
        cropHeight = ( cropHeight > 0.000001 ) ? cropHeight : 1.0;
        int minInstanceWidth = (int)(((double)maxThumbWidth)/cropWidth);
        int minInstanceHeight = (int)(((double)maxThumbHeight)/cropHeight);
        int minInstanceSide = Math.max( minInstanceWidth, minInstanceHeight );
        
        
        // Find the original image to use as a staring point
        EnumSet<ImageOperations> allowedOps = EnumSet.allOf( ImageOperations.class );            
        if ( createPreview ) {
            // We need to create also the preview image, so we need original.
            allowedOps = EnumSet.noneOf( ImageOperations.class );
            minInstanceWidth = 1024;
            minInstanceHeight = 1024;
        }
        
        ImageDescriptorBase srcImage = this.getPreferredImage( EnumSet.noneOf( ImageOperations.class ),
                allowedOps, minInstanceWidth, minInstanceHeight, 
                Integer.MAX_VALUE, Integer.MAX_VALUE );
        
        if ( srcImage == null ) {
            // If there are no uncorrupted instances, no thumbnail can be created
            log.warn( "Error - no original image was found!!!" );
            return;
        }
        log.debug( "Found original, reading it..." );
        
        /*
         We try to ensure that the thumbnail is actually from the original image
         by comparing aspect ratio of it to original. This is not a perfect check
         but it will usually catch the most typical errors (like having a the original
         rotated by RAW conversion SW but still the original EXIF thumbnail.
         */
        double origAspect = this.getAspect(
                original.getWidth(),
                original.getHeight(), 1.0 );
        double aspectAccuracy = 0.01;
        
        // First, check if there is a thumbnail in image header
        RenderedImage origImage = null;
        
        // Read the image
        RenderedImage thumbImage = null;
        RenderedImage previewImage = null;
        
        try {
            File imageFile = srcImage.getFile().findAvailableCopy();
            PhotovaultImageFactory imgFactory = new PhotovaultImageFactory();
            PhotovaultImage img = imgFactory.create( imageFile, false, false );
            ChannelMapOperation channelMap = getColorChannelMapping();
            if ( channelMap != null ) {
                img.setColorAdjustment( channelMap );
            }
            if ( img instanceof RawImage ) {
                RawImage ri = (RawImage) img;
                ri.setRawSettings( getProcessing().getRawConvSettings() );
            }
            if ( createPreview ) {
                // Calculate preview image size
                int previewWidth = img.getWidth();
                int previewHeight = img.getHeight();
                while ( previewWidth > 2048 || previewHeight > 2048 ) {
                    previewWidth >>= 1;
                    previewHeight >>=1;
                }
                previewImage = img.getRenderedImage( previewWidth, previewHeight, false );
            }
            img.setCropBounds( this.getCropBounds() );
            double srcRotation = 0.0;
            if ( srcImage instanceof CopyImageDescriptor ) {
                srcRotation = ((CopyImageDescriptor)srcImage).getRotation();
            }
            img.setRotation( getPrefRotation() - srcRotation );
            thumbImage = img.getRenderedImage( maxThumbWidth, maxThumbHeight, true );
        } catch ( Exception e ) {
            log.warn( "Error reading image: " + e.getMessage() );
            // TODO: If we aborted here due to image writing problem we would have
            // problems later with non-existing transaction. We should really
            // rethink the error handling logic in the whole function. Anyway, we
            // haven't changed anything yet so we can safely commit the tx.
            return;
        }
        log.debug( "Done, finding name" );
        
        // Find where to store the file in the target volume
        File thumbnailFile = volume.getInstanceName( this, "jpg" );
        log.debug( "name = " + thumbnailFile.getName() );
        
        try {
            saveInstance( thumbnailFile, thumbImage );
            if ( thumbImage instanceof PlanarImage ) {
                ((PlanarImage)thumbImage).dispose();
                System.gc();
            }
        } catch (PhotovaultException ex) {
            log.error( "error writing thumbnail for " + 
                    srcImage.getFile().findAvailableCopy().getAbsolutePath() + 
                    ": " + ex.getMessage() );
            // TODO: If we abort here due to image writing problem we will have 
            // problems later with non-existing transaction. We should really 
            // rethink the error handling login in the whole function. Anyway, we 
            // haven't changed anything yet so we can safely commit the tx.
            return;
        }
        try {
            ImageFile thumbFile;
            thumbFile = new ImageFile(thumbnailFile);
            CopyImageDescriptor thumbImageDesc = new CopyImageDescriptor( thumbFile, "image#0", original );
            thumbImageDesc.setRotation( getPrefRotation() );
            thumbImageDesc.setCropArea( getCropBounds() );
            thumbImageDesc.setColorChannelMapping( getColorChannelMapping() );
            thumbImageDesc.setRawSettings( getProcessing().getRawConvSettings() );
            thumbFile.addLocation( new FileLocation( volume,
                    volume.mapFileToVolumeRelativeName( thumbnailFile ) ) );
        } catch ( Exception ex ) {
            log.error( "Error creating thumb instance: " + ex.getMessage() );
        } 
        
        log.debug( "Loading thumbnail..." );
        
        thumbnail = Thumbnail.createThumbnail( this, thumbnailFile );
        oldThumbnail = null;
        log.debug( "Thumbnail loaded" );
        
        /*
        if ( createPreview ) {
            File previewFile = volume.getInstanceName( this, "jpg" );
            try {
                saveInstance( previewFile, previewImage );
                if ( previewImage instanceof PlanarImage ) {
                    ((PlanarImage)previewImage).dispose();
                    System.gc();
                }
            } catch (PhotovaultException ex) {
                log.error( "error writing preview for " + srcImage.getFile().findAvailableCopy() +
                        ": " + ex.getMessage() );
                return;
            }
            ImageInstance previewInstance = addInstance( volume, previewFile,
                    ImageInstance.INSTANCE_TYPE_MODIFIED );
            previewInstance.setColorChannelMapping( channelMap );
            previewInstance.setRawSettings( rawSettings );
        }
        txw.commit();
         */
    }
    
    /**
     Helper function to save a rendered image to file
     @param instanceFile The file into which the image will be saved
     @param img Image that willb e saved
     @throws PhotovaultException if saving does not succeed
     */
    protected void saveInstance( File instanceFile, RenderedImage img ) throws PhotovaultException {
        OutputStream out = null;
        log.debug( "Entry: saveInstance, file = " + instanceFile.getAbsolutePath() );
        try {
            out = new FileOutputStream( instanceFile.getAbsolutePath());
        } catch(IOException e) {
            log.error( "Error writing thumbnail: " + e.getMessage() );
            throw new PhotovaultException( e.getMessage() );
        }
        if ( img.getSampleModel().getSampleSize( 0 ) == 16 ) {
            log.debug( "16 bit image, converting to 8 bits");
            double[] subtract = new double[1]; subtract[0] = 0;
            double[] divide   = new double[1]; divide[0]   = 1./256.;
            // Now we can rescale the pixels gray levels:
            ParameterBlock pbRescale = new ParameterBlock();
            pbRescale.add(divide);
            pbRescale.add(subtract);
            pbRescale.addSource( img );
            PlanarImage outputImage = (PlanarImage)JAI.create("rescale", pbRescale, null);
            // Make sure it is a byte image - force conversion.
            ParameterBlock pbConvert = new ParameterBlock();
            pbConvert.addSource(outputImage);
            pbConvert.add(DataBuffer.TYPE_BYTE);
            img = JAI.create("format", pbConvert);
        }
        JPEGEncodeParam encodeParam = new JPEGEncodeParam();
        ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", out,
                encodeParam);
        try {
            log.debug( "starting JPEG enconde" );
            encoder.encode( img );
            log.debug( "done JPEG encode" );
            out.close();
            // origImage.dispose();
        } catch (Exception e) {
            log.warn( "Exception while encoding" + e.getMessage() );
            throw new PhotovaultException( "Error writing instance " + 
                    instanceFile.getAbsolutePath()+ ": " + 
                    e.getMessage() );
        }
        log.debug( "Exit: saveInstance" );
    }
    
    /** Creates a new thumbnail on the default volume
     */
    protected void createThumbnail() {
        VolumeBase vol = VolumeBase.getDefaultVolume();
        createThumbnail( vol );
    }
    
    /**
     TODO: The exported image must be stored as ImafeFile in database (so that it 
     can be found later)
     
     Exports an image from database to a specified file with given resolution.
     The image aspect ratio is preserved and the image is scaled so that it fits
     to the given maximum resolution.
     @param file File in which the image will be saved
     @param width Width of the exported image in pixels. If negative the image is
     exported in its "natural" resolution (i.e. not scaled)
     @param height Height of the exported image in pixels
     @throws PhotovaultException if exporting the photo fails for some reason.
     */
    public void exportPhoto( File file, int width, int height ) throws PhotovaultException {        

        File imageFile = original.getFile().findAvailableCopy();
        
        if ( imageFile == null ) {
            // If there are no instances, nothing can be exported
            log.warn( "Error - no original image was found!!!" );
            throw new PhotovaultException( "No image file found to export photo" );
        }
        
        // Read the image
        RenderedImage exportImage = null;
        try {
            String fname = imageFile.getName();
            int lastDotPos = fname.lastIndexOf( "." );
            if ( lastDotPos <= 0 || lastDotPos >= fname.length()-1 ) {
                throw new IOException( "Cannot determine file type extension of " + imageFile.getAbsolutePath() );
            }
            PhotovaultImageFactory imageFactory = new PhotovaultImageFactory();
            PhotovaultImage img = null;
            /*
            Do not read the image yet since setting raw conversion
            parameters later may force a re-read.
             */
            img = imageFactory.create( imageFile, false, false );

            img.setCropBounds( this.getCropBounds() );
            img.setRotation( getPrefRotation() );
            ChannelMapOperation channelMap = getColorChannelMapping();
            if ( channelMap != null ) {
                img.setColorAdjustment( channelMap );
            }
            if ( img instanceof RawImage ) {
                RawImage ri = (RawImage) img;
                RawConversionSettings rawSettings = getProcessing().getRawConvSettings();
                if ( rawSettings != null ) {
                    ri.setRawSettings( rawSettings );
                } else if ( rawSettings == null ) {
                    // No raw settings for this photo yet, let's use
                    // the thumbnail settings
                    rawSettings = ri.getRawSettings();
                }
            }
            if ( width > 0 ) {
                exportImage =img.getRenderedImage( width, height, false );
            } else {
                exportImage =img.getRenderedImage( 1.0, false );
            }
        } catch ( Exception e ) {
            log.warn( "Error reading image: " + e.getMessage() );
            throw new PhotovaultException( "Error reading image: " + e.getMessage(), e );
        }
                
        // Reduce to 8 bit samples if we have more...
        if ( exportImage.getSampleModel().getSampleSize( 0 ) == 16 ) {
            double[] subtract = new double[1]; subtract[0] = 0;
            double[] divide   = new double[1]; divide[0]   = 1./256.;
            // Now we can rescale the pixels gray levels:
            ParameterBlock pbRescale = new ParameterBlock();
            pbRescale.add(divide);
            pbRescale.add(subtract);
            pbRescale.addSource( exportImage );
            PlanarImage outputImage = (PlanarImage)JAI.create("rescale", pbRescale, null);
            // Make sure it is a byte image - force conversion.
            ParameterBlock pbConvert = new ParameterBlock();
            pbConvert.addSource(outputImage);
            pbConvert.add(DataBuffer.TYPE_BYTE);
            exportImage = JAI.create("format", pbConvert);
        }
        
        // Try to determine the file type based on extension
        String ftype = "jpg";
        String imageFname = file.getName();
        int extIndex = imageFname.lastIndexOf( "." ) + 1;
        if ( extIndex > 0 ) {
            ftype = imageFname.substring( extIndex );
        }
        
        try {
            // Find a writer for that file extensions
            ImageWriter writer = null;
            Iterator iter = ImageIO.getImageWritersByFormatName( ftype );
            if (iter.hasNext()) writer = (ImageWriter)iter.next();
            if (writer != null) {
                ImageOutputStream ios = null;
                try {
                    // Prepare output file
                    ios = ImageIO.createImageOutputStream( file );
                    writer.setOutput(ios);
                    // Set some parameters
                    ImageWriteParam param = writer.getDefaultWriteParam();
                    // if bi has type ARGB and alpha is false, we have
                    // to tell the writer to not use the alpha
                    // channel: this is especially needed for jpeg
                    // files where imageio seems to produce wrong jpeg
                    // files right now...
//                    if (exportImage.getType() == BufferedImage.TYPE_INT_ARGB ) {
//                        // this is not so obvious: create a new
//                        // ColorModel without OPAQUE transparency and
//                        // no alpha channel.
//                        ColorModel cm = new ComponentColorModel(exportImage.getColorModel().getColorSpace(),
//                                false, false,
//                                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
//                        // tell the writer to only use the first 3 bands (skip alpha)
//                        int[] bands = {0, 1, 2};
//                        param.setSourceBands(bands);
//                        // although the java documentation says that
//                        // SampleModel can be null, an exception is
//                        // thrown in that case therefore a 1*1
//                        // SampleModel that is compatible to cm is
//                        // created:
//                        param.setDestinationType(new ImageTypeSpecifier(cm,
//                                cm.createCompatibleSampleModel(1, 1)));
//                    }
                    // Write the image
                    writer.write(null, new IIOImage(exportImage, null, null), param);
                    
                    // Cleanup
                    ios.flush();
                } finally {
                    if (ios != null) ios.close();
                    writer.dispose();
                    if ( exportImage != null && exportImage instanceof PlanarImage ) {
                        ((PlanarImage)exportImage).dispose();
                        System.gc();
                    }
                }
            }
            
        } catch ( IOException e ) {
            log.warn( "Error writing exported image: " + e.getMessage() );
            throw new PhotovaultException( "Error writing exported image: " + e.getMessage(), e );
        }
    }
    
    
    
    java.util.Date shootTime;
    
    /**
     * Get the value of shootTime. Note that shoot time can also be
     null (to mean that the time is unspecified)1
     @return value of shootTime.
     */
    @Column( name = "shoot_time" )
    @Temporal( value = TemporalType.TIMESTAMP )
    public java.util.Date getShootTime() {
        return shootTime != null ? (java.util.Date) shootTime.clone() : null;
    }
    
    /**
     * Set the value of shootTime.
     * @param v  Value to assign to shootTime.
     */
    public void setShootTime(java.util.Date  v) {
        this.shootTime = (v != null) ? (java.util.Date) v.clone()  : null;
        modified();
    }
    
    /**
     Set both shooting time & accuracy directly using a FuzzyTime object
     @param v FuzzyTime containing new values.
     */
    public void setFuzzyShootTime( FuzzyDate v ) {
        if ( v != null ) {
            java.util.Date d = v.getDate();
            this.shootTime = (d != null ) ? (java.util.Date) d.clone() : null;
            this.timeAccuracy = v.getAccuracy();
        } else {
            this.shootTime = null;
            this.timeAccuracy = 0.0;
        }
        modified();
    }
    
    @ValueField
    @Transient
    public FuzzyDate getFuzzyShootTime() {
        return new FuzzyDate( shootTime, timeAccuracy );
    }
    
    /**
     
     @return The timeAccuracty value
     */
    @Column( name = "time_accuracy")
    public double getTimeAccuracy() {
        return timeAccuracy;
    }
    
    /**
     
     Set the shooting time accuracy. The value is a +/- range from shootingTime
     parameter (i.e. shootingTime April 15 2000, timeAccuracy 15 means that the
     photo is taken in April 2000.
     
     * @param newTimeAccuracy The new TimeAccuracy value.
     */
    public void setTimeAccuracy(final double newTimeAccuracy) {
        this.timeAccuracy = newTimeAccuracy;
    }
    
    
 
    double FStop;
    
    /**
     * Get the value of FStop.
     * @return value of FStop.
     */
    @ValueField( field="FStop" )
    @Column( name = "f_stop" )
    public double getFStop() {
        return FStop;
    }
    
    /**
     * Set the value of FStop.
     * @param v  Value to assign to FStop.
     */
    public void setFStop(double  v) {
        this.FStop = v;
        modified();
    }
    double focalLength;
    
    /**
     * Get the value of focalLength.
     * @return value of focalLength.
     */
    @ValueField
    @Column(name = "focal_length")
    public double getFocalLength() {
        return focalLength;
    }
    
    /**
     * Set the value of focalLength.
     * @param v  Value to assign to focalLength.
     */
    public void setFocalLength(double  v) {
        this.focalLength = v;
        modified();
    }
    String shootingPlace;
    
    /**
     * Get the value of shootingPlace.
     * @return value of shootingPlace.
     */
    @ValueField
    @Column( name = "shooting_place" )
    public String getShootingPlace() {
        return shootingPlace;
    }
    
    /**
     * Set the value of shootingPlace.
     * @param v  Value to assign to shootingPlace.
     */
    public void setShootingPlace(String  v) {
        checkStringProperty( "Shooting place", v, SHOOTING_PLACE_LENGTH );
        this.shootingPlace = v;
        modified();
    }
    String photographer;
    
    /**
     * Get the value of photographer.
     * @return value of photographer.
     */
    @ValueField
    @Column( name = "photographer" )
    public String getPhotographer() {
        return photographer;
    }
    
    /**
     * Set the value of photographer.
     * @param v  Value to assign to photographer.
     */
    @SuppressWarnings("static-access")
    public void setPhotographer(String  v) {
        checkStringProperty( "Photographer", v, this.PHOTOGRAPHER_LENGTH );
        this.photographer = v;
        modified();
    }
    double shutterSpeed;
    
    /**
     * Get the value of shutterSpeed.
     * @return value of shutterSpeed.
     */
    @ValueField
    @Column( name = "shutter_speed" )
    public double getShutterSpeed() {
        return shutterSpeed;
    }
    
    /**
     * Set the value of shutterSpeed.
     * @param v  Value to assign to shutterSpeed.
     */
    public void setShutterSpeed(double  v) {
        this.shutterSpeed = v;
        modified();
    }
    String camera;
    
    /**
     * Get the value of camera.
     * @return value of camera.
     */
    @ValueField
    @Column( name = "camera" )
    public String getCamera() {
        return camera;
    }
    
    /**
     * Set the value of camera.
     * @param v  Value to assign to camera.
     */
    public void setCamera(String  v) {
        checkStringProperty( "Camera", v, CAMERA_LENGTH );
        this.camera = v;
        modified();
    }
    String lens;
    
    /**
     * Get the value of lens.
     * @return value of lens.
     */
    @ValueField
    @Column( name = "lens" )
    public String getLens() {
        return lens;
    }
    
    /**
     * Set the value of lens.
     * @param v  Value to assign to lens.
     */
    public void setLens(String  v) {
        checkStringProperty( "Lens", v, LENS_LENGTH );
        this.lens = v;
        modified();
    }
    String film;
    
    /**
     * Get the value of film.
     * @return value of film.
     */
    @ValueField
    @Column( name = "film" )
    public String getFilm() {
        return film;
    }
    
    /**
     * Set the value of film.
     * @param v  Value to assign to film.
     */
    public void setFilm(String  v) {
        checkStringProperty( "Film", v, FILM_LENGTH );
        this.film = v;
        modified();
    }
    int filmSpeed;
    
    /**
     * Get the value of filmSpeed.
     * @return value of filmSpeed.
     */
    @ValueField
    @Column( name = "film_speed" )
    public int getFilmSpeed() {
        return filmSpeed;
    }
    
    /**
     * Set the value of filmSpeed.
     * @param v  Value to assign to filmSpeed.
     */
    public void setFilmSpeed(int  v) {
        this.filmSpeed = v;
        modified();
    }
    
    /**
     Get the preferred rotation for this image in degrees. Positive values 
     indicate that the image should be rotated clockwise.
     @return value of prefRotation.
     */
    @ValueField
    @Transient
    @Deprecated
    public double getPrefRotation() {
        return getProcessing().getRotation();
    }
    
    /**
     Set the value of prefRotation.
     @param v  New preferred rotation in degrees. The value should be in range 
     0.0 <= v < 360, otherwise v is normalized to be between these values.
     */
    public void setPrefRotation(double  v) {
        // Normalize rotation
        while ( v < 0.0 ) {
            v += 360.0;
        }
        while ( v >= 360.0 ) {
            v -= 360.0;
        }
        
        if ( v != getProcessing().getRotation() ) {
            // Rotation changes, invalidate the thumbnail
            invalidateThumbnail();
            getProcessing().applyRotation( v );
            // purgeInvalidInstances();
        }
        
        modified();
    }
    
    /**
     Check that the e crop bounds are defined in consistent manner. This is needed
     since in old installations the max parameters can be larger than min ones.
     */
    
    private void checkCropBounds() {
        cropMinX = Math.min( 1.0, Math.max( 0.0, cropMinX ) );
        cropMinY = Math.min( 1.0, Math.max( 0.0, cropMinY ) );
        cropMaxX = Math.min( 1.0, Math.max( 0.0, cropMaxX ) );
        cropMaxY = Math.min( 1.0, Math.max( 0.0, cropMaxY ) );

        if ( cropMaxX - cropMinX <= 0.0) {
            cropMaxX = 1.0 - cropMinX;
        }
        if ( cropMaxY - cropMinY <= 0.0) {
            cropMaxY = 1.0 - cropMinY;
        }
    }
    
    /**
     Get the preferred crop bounds of the original image
     */
    @ValueField
    @Transient
    public Rectangle2D getCropBounds() {
        // checkCropBounds();
        return getProcessing().getCropping();
    }
    
    
    /**
     Set the preferred cropping operation
     @param cropBounds New crop bounds
     */
    public void setCropBounds( Rectangle2D cropBounds ) {
        getProcessing().applyCropping( cropBounds );
    }
    
    
    
    /**
     CropBounds describes the desired crop rectange from original image. It is
     defined as proportional coordinates that are applied after rotating the
     original image so that top left corner is (0.0, 0.0) and bottom right
     (1.0, 1.0)
     */
    
    double cropMinX;
    double cropMaxX;
    double cropMinY;
    double cropMaxY;
    
    /**
     Set the preferred color channel mapping
     @param cm the new color channel mapping
     */
    public void setColorChannelMapping( ChannelMapOperation cm ) {
        if ( cm != null ) {
            if ( !cm.equals( getColorChannelMapping() ) ) {
                // Rotation changes, invalidate the thumbnail
                invalidateThumbnail();
                // purgeInvalidInstances();
            }
        }
        getProcessing().applyChanMap( cm );
        modified();
    }

    /**
     Get currently preferred color channe?l mapping.
     @return The current color channel mapping
     */
    // TODO: Do mapping for these
    @ValueField
    @Transient
    public ChannelMapOperation getColorChannelMapping() {
        return getProcessing().getChanMap();
    }
    
    ImageOpChain processing = new ImageOpChain();
    
    @ValueField
    @Column( name="processing", length=1000000 )
    @Type(type="org.photovault.persistence.ImageOpChainUserType")
    public ImageOpChain getProcessing() {
        return processing;
    }
    
    public void setProcessing( ImageOpChain proc ) {
        processing = proc;
        invalidateThumbnail();
        modified();
    }
    
    @Transient
    public Dimension getCroppedSize() {
        double rot = processing.getRotation() * Math.PI / 180.0;
        double origWidth = original.getWidth();
        double origHeight = original.getHeight();
        double rotSin = Math.abs( Math.sin( rot ) );
        double rotCos = Math.abs( Math.cos( rot ) );
        double rotWidth = origWidth * rotCos + origHeight * rotSin;
        double rotHeight = origWidth * rotSin + origHeight * rotCos;
        Rectangle2D crop = processing.getCropping();
        Dimension ret = new Dimension((int) (rotWidth * crop.getWidth()),
                (int)(rotHeight*crop.getHeight() ) );
        return ret;
    }

    /**
     * Get the current raw conversion settings.
     * @return Current settings or <code>null</code> if this is not a raw image.
     * @deprecated Use {@link #getProcessing() } instead.
     */
    @ValueField
    @Transient
    @Deprecated
    public RawConversionSettings getRawSettings() {
        return getProcessing().getRawConvSettings();
    }
    
    /**
     Set the raw conversion settings for this photo
     @param s The new raw conversion settings to use. The method makes a clone of 
     the object.
     * @deprecated User {@link #getProcessing() } instead
     */
    @Deprecated
    public void setRawSettings( RawConversionSettings s ) {
        log.debug( "entry: setRawSettings()" );
        RawConversionSettings oldRawSettings = getProcessing().getRawConvSettings();
        if ( s != null ) {
            if ( !s.equals( oldRawSettings ) ) {
                invalidateThumbnail();
            }
        } else {
            // s is null so this should not be raw image
            if ( oldRawSettings != null ) {
                log.error( "Setting raw conversion settings of an raw image to null!!!" );
                invalidateThumbnail();
                // purgeInvalidInstances();                
            }
        }
        getProcessing().applyRawConvSetting( s );
        modified();
        log.debug( "exit: setRawSettings()" );
    }

               
               
    String description;
    
    /**
     * Get the value of description.
     * @return value of description.
     */
    @ValueField
    @Column( name = "description" )
    public String getDescription() {
        return description;
    }
    
    /**
     * Set the value of description.
     * @param v  Value to assign to description.
     */
    public void setDescription(String  v) {
        this.description = v;
        modified();
    }
    
    public static final int QUALITY_UNDEFINED = 0;
    public static final int QUALITY_TOP = 1;
    public static final int QUALITY_GOOD = 2;
    public static final int QUALITY_FAIR = 3;
    public static final int QUALITY_POOR = 4;
    public static final int QUALITY_UNUSABLE = 5;
    
    /**
     * Get the value of value attribute.
     *
     * @return an <code>int</code> value
     */
    @ValueField
    @Column( name = "photo_quality" )
    public int getQuality() {
        return quality;
    }
    
    /**
     * Set the "value attribute for the photo which tries to describe
     How good the pohot is. Possible values:
     <ul>
     <li>QUALITY_UNDEFINED - value of the photo has not been evaluated</li>
     <li>QUALITY_TOP - This frame is a top quality photo</li>
     <li>QUALITY_GOOD - This frame is good, one of the best available from the session</li>
     <li>QUALITY_FAIR - This frame is OK but probably not the 1st choice for use</li>
     <li>QUALITY_POOR - Unsuccesful picture</li>
     <li>QUALITY_UNUSABLE - Technical failure</li>
     </ul>
     
     *
     * @param newQuality The new Quality value.
     */
    public void setQuality(final int newQuality) {
        this.quality = newQuality;
        modified();
    }
    
    /**
     Returns the time when this photo (=metadata of it) was last modified
     * @return a <code>Date</code> value
     */
    @ValueField
    @Column( name = "last_modified" )
    @Temporal(value = TemporalType.TIMESTAMP )
    public java.util.Date getLastModified() {
        return lastModified != null ? (java.util.Date) lastModified.clone() : null;
    }
    
    public  void setLastModified(final java.util.Date newDate) {
        this.lastModified = (newDate != null) ? (java.util.Date) newDate.clone()  : null;
        modified();
    }
    
    /**
     * Get the <code>TechNotes</code> value.
     *
     * @return a <code>String</code> value
     */
    @ValueField
    @Column( name = "tech_notes" )
    public String getTechNotes() {
        return techNotes;
    }
    
    /**
     * Set the <code>TechNotes</code> value.
     *
     * @param newTechNotes The new TechNotes value.
     */
    public void setTechNotes( String newTechNotes ) {
        this.techNotes = newTechNotes;
        modified();
    }
    
    /**
     Get the original file name of this photo
     
     * @return a <code>String</code> value
     */
    @ValueField
    @Column( name = "orig_fname" )
    public String getOrigFname() {
        return origFname;
    }
    
    /**
     Set the original file name of this photo. This is set also by addToDB which is the
     preferred way of creating a new photo into the DB.
     @param newFname The original file name
     @throws IllegalArgumentException if the given file name is longer than
     {@link #ORIG_FNAME_LENGTH}
     */
    public void setOrigFname(final String newFname) {
        checkStringProperty( "OrigFname", newFname, ORIG_FNAME_LENGTH );
        this.origFname = newFname;
        modified();
    }
    
    /**
     Utility method to get the color curve assigned to red channel
     @return The curve or <code>null</code> if no curve is assigned
     */
    @Transient
    public ColorCurve getRedColorCurve() {
        ChannelMapOperation channelMap = getColorChannelMapping();
        return channelMap != null ? channelMap.getChannelCurve( "red" ) : null;
    }
    
    /**
     Utility method to get the color curve assigned to green channel
     @return The curve or <code>null</code> if no curve is assigned
     */
    @Transient
    public ColorCurve getGreenColorCurve() {
        ChannelMapOperation channelMap = getColorChannelMapping();
        return channelMap != null ? channelMap.getChannelCurve( "green" ) : null;
    }
    
    /**
     Utility method to get the color curve assigned to blue channel
     @return The curve or <code>null</code> if no curve is assigned
     */
    @Transient
    public ColorCurve getBlueColorCurve() {
        ChannelMapOperation channelMap = getColorChannelMapping();
        return channelMap != null ? channelMap.getChannelCurve( "blue" ) : null;
    }
    
    /**
     Utility method to get the color curve assigned to saturation adjustment.
     @return The curve or <code>null</code> if no curve is assigned
     */
    @Transient
    public ColorCurve getSaturationCurve() {
        ChannelMapOperation channelMap = getColorChannelMapping();
        return channelMap != null ? channelMap.getChannelCurve( "saturation" ) : null;
    }
    
    /**
     Utility method to get the color curve assigned to master value adjustment.
     @return The curve or <code>null</code> if no curve is assigned
     */
    @Transient
    public ColorCurve getMasterCurve() {
        ChannelMapOperation channelMap = getColorChannelMapping();
        return channelMap != null ? channelMap.getChannelCurve( "value" ) : null;
    }
    
    @Transient
    public Integer getRawBlack() {
        DCRawMapOp m = (DCRawMapOp) getProcessing().getOperation( "raw_map" );
        return ( m != null ) ? m.getBlack() : null;
    }
    
    @Transient
    public Integer getRawWhite() {
        DCRawMapOp m = (DCRawMapOp) getProcessing().getOperation( "raw_map" );
        return ( m != null ) ? m.getWhite() : null;
    }
    
    @Transient
    public Double getRawEvCorr() {
        DCRawMapOp m = (DCRawMapOp) getProcessing().getOperation( "raw_map" );
        return ( m != null ) ? m.getEvCorr() : null;
    }

    @Transient
    public Double getRawHlightComp() {
        DCRawMapOp m = (DCRawMapOp) getProcessing().getOperation( "raw_map" );
        return ( m != null ) ? m.getHlightCompr() : null;
    }
    
    @Transient
    public Double getRawColorTemp() {
        DCRawMapOp m = (DCRawMapOp) getProcessing().getOperation( "raw_map" );
        return null;
    }
    
    
    /**
     Returns a collection that contains all folders the photo belongs to
     */
    // TODO: implement mapping of folders
    @Transient
    public Set<PhotoFolder> getFolders() {
        Set<PhotoFolder> folders = new HashSet<PhotoFolder>();
        for ( FolderPhotoAssociation a : folderAssociations ) {
            PhotoFolder f = a.getFolder();
            if ( f != null ) {
                folders.add( f );
            }
        }
        return folders;
    }
    
    
    /**
     Folder associations this photo is part of.
     */
    Set<FolderPhotoAssociation> folderAssociations = 
            new HashSet<FolderPhotoAssociation>();
    
    /**
     Get all know associations from this photo to folders. Note that some of the
     folders may not be known in this database, just the association is known. In
     these cases the folder field in the association object in<code>null</code>
     @return
     */
    @SetField( elemClass=FolderPhotoAssociation.class, 
               dtoResolver=FolderRefResolver.class )
    @OneToMany( mappedBy = "photo" )
    public Set<FolderPhotoAssociation> getFolderAssociations() {
        return folderAssociations;
    }
    
    /**
     Set the folder associations for this object. For Hibernate use
     @param s Set of all known associations
     */
    public void setFolderAssociations( Set<FolderPhotoAssociation> s ) {
        folderAssociations = s;
    }
    
    /**
     Add an association to a folder
     @param a The associaton object
     @throws IllegalStateException if the association is really created to other 
     photo.
     */
    public void addFolderAssociation( FolderPhotoAssociation a ) {
        folderAssociations.add( a );
        a.setPhoto( this );
    }
    
    public void removeFolderAssociation( FolderPhotoAssociation a ) {
        folderAssociations.remove( a );
        a.setPhoto( null );
    }
    
    /**
     Helper method for comparing testing equality of 2 objects that can 
     potentially be null
     @param o1 First object to compare
     @param o2 The second object to compare
     @return <code>true</code> if o1 and o2 are both <code>null</code> or equal.
     <code>false</code> otherwise.     
     */
    static private boolean isEqual( Object o1, Object o2 ) {
        if ( o1 == null ) {
            if ( o2 == null ) {
                return true;
            } else {
                return false;
            }
        }
        return o1.equals( o2 );
    }
    
    /**
     Checks that a string is no longer that maximum length allowed for it
     @param propertyName The porperty name used in error message
     @param value the new value
     @param maxLength Maximum length for the string
     @throws IllegalArgumentException if value is longer than maxLength
     */
    void checkStringProperty( String propertyName, String value, int maxLength )
    throws IllegalArgumentException {
        if ( value != null && value.length() > maxLength ) {
            throw new IllegalArgumentException( propertyName
                    + " cannot be longer than " + maxLength + " characters" );
        }
    }
    
    @Override
    public boolean equals( Object obj ) {
        if ( obj == null || obj.getClass() != this.getClass() ) {
            return false;
        }
        PhotoInfo p = (PhotoInfo)obj;
        
        return ( isEqual( p.photographer, this.photographer )
        && isEqual( p.shootingPlace, this.shootingPlace )
        && isEqual( p.shootTime, this.shootTime )
        && isEqual(p.description, this.description )
        && isEqual( p.camera, this.camera )
        && isEqual( p.lens, this.lens )
        && isEqual( p.film, this.film )
        && isEqual( p.techNotes, this.techNotes )
        && isEqual( p.origFname, this.origFname )
        && isEqual( p.getUuid(), this.getUuid() )
        && p.shutterSpeed == this.shutterSpeed
                && p.filmSpeed == this.filmSpeed
                && p.focalLength == this.focalLength
                && p.FStop == this.FStop
                && p.quality == this.quality
                && p.getProcessing().equals( getProcessing() ));
    }
    
    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}

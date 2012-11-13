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

package org.photovault.imginfo.indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandException;
import org.photovault.common.PhotovaultException;
import org.photovault.folder.PhotoFolder;
import org.photovault.image.PhotovaultImage;
import org.photovault.image.PhotovaultImageFactory;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.CopyImageDescriptor;
import org.photovault.imginfo.CreateCopyImageCommand;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.FileLocation;
import org.photovault.imginfo.ImageDescriptorBase;
import org.photovault.imginfo.ImageFile;
import org.photovault.imginfo.ImageFileDAO;
import org.photovault.imginfo.ImageOperations;
import org.photovault.imginfo.ModifyImageFileCommand;
import org.photovault.imginfo.OriginalImageDescriptor;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.imginfo.Volume;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.taskscheduler.BackgroundTask;

/**
    Background task for indexing a single file.
    <ul>
    <li>First Photovault checks if the file is already indexed. If there is
    a file in this path according to database and its hash or both file size
    and last modification time match this is assumed to be an existing file.</li>
    <li>
    If the file is not found Photovault tries to find an existing file with
    the same hash. If such is found, Photovault assumes that this is another
    copy of the same file and adds a new location for that file. </li>
    <li>
    If no such file is found Photovault creates a new ImageFile and PhotoInfo
    with the image stored in location "image#0" in this file as original.
    After that it creates a thumbnail on default volume.
    </li>
    <li>
    Finally, Phootvault adds all photos associated with this file the given
    folder
    </li>
    </ul>
    @author Harri Kaimio
 
 */
public class IndexFileTask extends BackgroundTask {
    static Log log = LogFactory.getLog( IndexFileTask.class.getName() );
    
    /**
     The file that will be indexed
     */
    private File f;
    
    /**
     Folder corresponding to the directory in which the file is. Created photos
     will be added to this folder.
     */
    private PhotoFolder folder;
    
    /**
     Volume of the file
     */
    private ExternalVolume volume;
    
    /**
     ImageFile that describes the file.
     */
    private ImageFile ifile;
    
    private IndexingResult result;
    
    /**
     Photos that were found during the indexing operation
     */
    private Set<PhotoInfo> photosFound = new HashSet<PhotoInfo>();
    
    /**
     Get the result of idnexing operation
     */
    public IndexingResult getResult() {
        return result;
    }
    
    /**
     Get the file that is indexed by this task
     @return The file
     */
    public File getFile() {
        return f;
    }
    
    /**
     Get the ImageFile that corresponds to indexed file.
     @return The ImageFile that was found to correspond to indexed file or <code>
     null</code> if the task has not been executed or the file was not an image.
     */
    public ImageFile getImageFile() {
        return ifile;
    }
    
    /**
     Get the photos associated with the indexed file
     @return Detached instances of the photos that are associated with this 
     file or empty set if there are none or the task has not yet been executed
     */
    public Set<PhotoInfo> getPhotosFound() {
        return photosFound;
    }
    
    /**
     MD5 hash of the file
     */
    private byte[] hash = null;
    private ExtVolIndexerEvent currentEvent = new ExtVolIndexerEvent( this );
    
    /**
     Reference to the {@link DIrectoryIndexer} that created this instance. The 
     directory indexer is notified of the result of indexing operation.
     */
    private DirectoryIndexer dirIndexer;
    
    /**
     Creates a new IndexFileTask
     @param f The file that will be idnexed
     @param folder PhotoFolder corresponding to f's directory
     @param vol Volume of f.
     @param dir DirectoryIndexer that created this instance.
     */
    public IndexFileTask( File f, PhotoFolder folder, ExternalVolume vol, DirectoryIndexer dir ) {
        this.f = f;
        this.folder = folder;
        this.volume = vol;
        this.dirIndexer = dir;
    }
    
    /**
     Run the actual indexing operation
     */
    public void run( ) {
        indexFile();
        if ( dirIndexer != null ) {
            dirIndexer.fileIndexerCompleted( this );
        }
    }

    /**
     Run the actual idnexing operation (called by run())
     */
    private void indexFile() {
        log.debug( "entry: indexFile " + f.getAbsolutePath() );
        currentEvent.setIndexedFile( f );
        
        DAOFactory daoFactory = DAOFactory.instance(HibernateDAOFactory.class );
        ImageFileDAO ifDAO = daoFactory.getImageFileDAO();
        PhotoInfoDAO photoDAO = daoFactory.getPhotoInfoDAO();
        // Check if the instance already exists n database
        ifile = ifDAO.findFileInLocation( volume, volume.mapFileToVolumeRelativeName( f ) );
        if ( ifile != null ) {
            log.debug( "found existing file" );
            FileLocation fileLoc = null;
            for ( FileLocation loc : ifile.getLocations() ) {
                if ( loc.getFile().equals( f ) ) {
                    fileLoc = loc;
                    break;
                }
            }
            
            boolean matchesFile = true;
            if ( f.length() == ifile.getFileSize() ) {
                if ( f.lastModified() != fileLoc.getLastModified() ) {
                    hash = ImageFile.calcHash( f );
                    if ( !hash.equals( ifile.getHash()  ) ) {
                        matchesFile = false;
                    }
                }
            } else {
                matchesFile = false;
            }
            
            // There is an existing instance, check whether the data matches
            if ( matchesFile ) {
                log.debug( "File is consistent with DB" );
                result = IndexingResult.UNCHANGED;
                updatePhotosFound();
                return;
            } else {
                ModifyImageFileCommand deleteCmd = new ModifyImageFileCommand( ifile  );
                deleteCmd.removeLocation( fileLoc );
                log.debug( "File is not consistent with the one in DB, removing location" );
                try {
                    cmdHandler.executeCommand( deleteCmd );
                } catch (CommandException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        /*
         If we reach here, the file is new (or changed) after last indexing.
         Check first if it is a copy of an existing instance
         */
        PhotovaultImage img = null;
        try {
            PhotovaultImageFactory imgFactory = new PhotovaultImageFactory();
            img = imgFactory.create( f, false, false );
        } catch ( PhotovaultException e ) {
            result = IndexingResult.NOT_IMAGE;
            return;
        }
        // This is an image.
        
        if ( hash == null ) {
            // TODO: This should be moved to PhotovaultImage
            hash = ImageFile.calcHash( f );
        }
        
        ifile = ifDAO.findImageFileWithHash( hash );

        ModifyImageFileCommand cmd = null;
        if (ifile != null) {
            /**
            Yes, this is a known file. Just information about the new location
            to the database.
             */
            cmd = new ModifyImageFileCommand(ifile);
            result = IndexingResult.NEW_LOCATION;
        } else {
            /*
            The file is not known to Photovault. Create a new ImageFile
            object & PhotoInfo that regards it as its original.
             */
            try {
                cmd = new ModifyImageFileCommand( img, hash );
                result = IndexingResult.NEW_FILE;
            } catch ( Exception e ) {
                result = IndexingResult.ERROR;
                return;
            }
        }
        cmd.addLocation(
                new FileLocation( volume,
                volume.mapFileToVolumeRelativeName( f ) ) );
        try {
            cmdHandler.executeCommand( cmd );

            // Add all photos associated with this image to current folder
            ifile = ifDAO.findById( cmd.getImageFile().getId(), false );
            updatePhotosFound();
            // Create any missing preview images for the found photos
            for ( PhotoInfo p : photosFound ) {
                createPreviewInstances( img, p, daoFactory );
            }
            addPhotosToFolder(  );
        } catch ( CommandException ex ) {
            log.warn( "Exception in modifyImageFileCommand: " + ex.getMessage() );
            result = IndexingResult.NOT_IMAGE;
            return;
        }

        img.dispose();
        currentEvent.setPhoto( null );        
        log.debug( "exit: indexFile " + f.getAbsolutePath() );
        ifile = cmd.getImageFile();
    }

    /**
     Create the needed preview instances for a photo.
     The method cretes both a thumbnail (small, low quality, max 200x200 images)
     as well as preview that can be used for displaying if the volume with original is
     offline (high quality, max 1280x1280 JPEG image without cropping)

     @param img the loaded image that is used as a basis for preview
     @param p The photo
     @param f DAO factory used for database access
     */
    private void createPreviewInstances(
            PhotovaultImage img, PhotoInfo p, DAOFactory f )
            throws CommandException {
        ImageDescriptorBase thumbImage = p.getPreferredImage( 
                EnumSet.allOf( ImageOperations.class ), 
                EnumSet.allOf( ImageOperations.class ), 0, 0, 200, 200 );
        
        // Preview image with no cropping, longer side 1280 pixels
        int origWidth = p.getOriginal().getWidth();
        int origHeight = p.getOriginal().getHeight();
        
        int copyMinWidth = Math.min( origWidth, 1280 );
        int copyMinHeight = 0;
        if ( origHeight > origWidth ) {
            copyMinHeight = Math.min( origHeight, 1280 );
            copyMinWidth = 0;
        }
        EnumSet<ImageOperations> previewOps = 
                EnumSet.of( ImageOperations.RAW_CONVERSION, ImageOperations.COLOR_MAP );
        ImageDescriptorBase previewImage = p.getPreferredImage(
                previewOps, previewOps, 
                copyMinWidth, copyMinHeight, 1280, 1280 );


        Volume vol = f.getVolumeDAO().getDefaultVolume();
        if ( previewImage == null ) {
            CreateCopyImageCommand cmd =
                    new CreateCopyImageCommand( img, p, vol, 1280, 1280 );
            cmd.setLowQualityAllowed( false );
            cmd.setOperationsToApply( previewOps );
            cmdHandler.executeCommand( cmd );
        }
        if ( thumbImage == null ) {
            CreateCopyImageCommand cmd =
                    new CreateCopyImageCommand( img, p, vol, 200, 200 );
            cmd.setLowQualityAllowed( true );
            cmdHandler.executeCommand( cmd );
        }
    }    

    private void updatePhotosFound() {
        ImageDescriptorBase img = ifile.getImage( "image#0" );
        OriginalImageDescriptor origImage = 
                (img instanceof OriginalImageDescriptor) ? 
                    (OriginalImageDescriptor) img : 
                    ((CopyImageDescriptor) img).getOriginal();
        photosFound.addAll( origImage.getPhotos() );
    }
    
    /**
     Add the found photos to folder.
    
     @throws org.photovault.command.CommandException
     */
    private void addPhotosToFolder() throws CommandException {
        ImageDescriptorBase img = ifile.getImage( "image#0" );
        OriginalImageDescriptor origImage = 
                (img instanceof OriginalImageDescriptor) ? 
                    (OriginalImageDescriptor) img : 
                    ((CopyImageDescriptor) img).getOriginal();
        Set<PhotoInfo> photos = origImage.getPhotos();
        List<UUID> photoIds = new ArrayList<UUID>();
        for ( PhotoInfo p : photos ) {
            photoIds.add( p.getUuid() );
        }
        ChangePhotoInfoCommand addFolderCmd = new ChangePhotoInfoCommand( photoIds );
        addFolderCmd.addToFolder( folder );
        cmdHandler.executeCommand( addFolderCmd );
    }
}

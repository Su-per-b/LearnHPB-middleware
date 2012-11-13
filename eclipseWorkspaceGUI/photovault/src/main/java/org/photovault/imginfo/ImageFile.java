/*
  Copyright (c) 2007 Harri Kaimio
 
  This file is part of Photovault.
 
  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.
 
  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even therrore implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package org.photovault.imginfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawImage;

/**
 * ImageFile represents info about a certain image file that may or may not exist 
 * in some of Photovault volumes. Image files are regarded as immutable objects -
 * in practice it is of course possible to modify an image file but after the
 * modification it is regarded as a new one. Therefore, an image file in file system 
 * can be linked to an ImageFile object by its MD5 hash code.
 * <p>
 * ImageFile contains one or more {@link ImageInstance}s. There can be several (or 
 * no) copies of an ImageFile available in different volumes. The known locations 
 * are described bu {@link FileLocation} objects.
 * </p>
 * @author Harri Kaimio
 * @since 0.6.0
 */

@NamedQueries({
    /*
     Find the iamge file object that describes file in an external volume
     Parameters:
     volume - The external volume
     fname - Name of the file (i.e. relative path from volume base directory)
     */
    @NamedQuery(
     name = "findImageFileByLocation",
     query = "select f from ImageFile f join f.locations loc "+
            "where loc.volume = :volume and loc.fname = :fname"
    )
})
@Entity
@Table( name="pv_image_files" )
public class ImageFile implements java.io.Serializable {

    static Log log = LogFactory.getLog( ImageFile.class );
    
    /** Creates a new instance of ImageFile */
    public ImageFile() {
        id = UUID.randomUUID();
    }
    
    /**
     * Creates a new ImageFile from given file. Note that the file is not added
     * to the image as a new location, this must be done separately.
     * @param f The file from which the ImageFile fields are calculated.
     * @throws org.photovault.common.PhotovaultException If there is an error
     * @throws java.io.IOException If reading the image file failed.
     */
    public ImageFile( File f ) throws PhotovaultException, IOException {
        // Set lastChecked to a time that is certainly earlier than any data read 
        // from file system
        hash = calcHash( f );
        init( f );
    }
    
    /**
     Create a new image file when the MD5 hash of the file is already known.
     * @param f The file from which the ImageFile fields are calculated.
     * @param hash MD5 hash for file f.
     * @throws org.photovault.common.PhotovaultException If there is an error
     * @throws java.io.IOException If reading the image file failed.
     */
    public ImageFile( File f, byte[] hash ) throws PhotovaultException, IOException {
        if ( hash != null ) {
            this.hash = hash;
        } else {
            hash = calcHash( f );
        }
        init( f );
    }
    
    private void init( File f ) throws PhotovaultException, IOException {
        id = UUID.nameUUIDFromBytes( hash );
        size = f.length();
        readImageFile( f );        
    }
    
    private UUID id;
        
    /**
     * Get the UUID of this ImageFile
     * @return UUID of this object
     */
    @Id
    @Column( name = "id" )
    @org.hibernate.annotations.Type( type = "org.photovault.persistence.UUIDUserType" )
    public UUID getId() {
        return id;
    }
    
    
    /**
     * Set UUID of this object. Note that this should be called by persistence layer 
     * only.
     * @param id UUID for the ImageFile object.
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    long size;
    
    /**
     * Get size of the associated image file
     * @return Size of the file in bytes.
     */
    @Column( name = "file_size" )
    public long getFileSize() {
        return size;
    }
    
    /**
     * Set size of the associated file. Tgis method should be called only by persistence 
     * layer.
     * @param size Size of the associated file in bytes.
     */
    public void setFileSize( long size ) {
        this.size = size;
    }
    
    private byte[] hash;
    
    /**
     * Get MD5 hash of the associated image file.
     * @return Hash of the file
     */
    @Column( name = "md5_hash" )
    // @org.hibernate.annotations.Type( type = "varbinary" )
    public byte[] getHash() {
        return hash;
    }

    /**
     * Set the MD5 hash of associated file
     * @param hash Value of hash.
     */
    public void setHash(byte[] hash) {
        this.hash = hash;
    }
    
    Set<FileLocation> locations = new HashSet<FileLocation>();
    
    
    /**
     * Get the known locations in which this file is stored
     * @return Set of known locations
     */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(
    name = "pv_image_locations",
            joinColumns = @JoinColumn(name = "id")
    )            
    public Set<FileLocation> getLocations() {
        return locations;
    }
    
    /**
     * Set the known locations of the file. Used by persistence layer only.
     * @param locations New value for locations.
     */
    protected void setLocations( Set<FileLocation> locations ) {
        this.locations = locations;
    }
    
    /**
     * Add a new location for the image.
     * @param newLocation FileLocation object that describes the location.
     */
    public void addLocation( FileLocation newLocation ) {
        newLocation.file = this;
        locations.add( newLocation );
    }
    
    /**
     * remove a location from the file.
     * @param location Location to remove.
     */
    public void removeLocation( FileLocation location ) {
        locations.remove( location );
    }
    

    /**
     Find a copy of this file that can be used
     @return File that is described by this object or <code>null</code> if
     no copy is available.
     */
    public File findAvailableCopy() {
        for ( FileLocation loc : locations ) {
            if ( loc.getVolume().isAvailable() ) {
                try {
                    File f = loc.getVolume().mapFileName(loc.getFname());
                    if ( f.exists() ) {
                        return f;
                    }
                } catch (FileNotFoundException ex) {
                    log.warn( "Exception while looking for available file: " + 
                            ex.getMessage() );
                }
            }
        }
        return null;
    }
    
    /**
     Images contained in this file
     */
    Map<String,ImageDescriptorBase> images = new HashMap<String,ImageDescriptorBase>();
    
    /**
     * Get all images stored in this file
     * @return Map that maps image location descriptor to the image descriptor object
     */
    @MapKey( name = "locator" )
    @OneToMany(cascade  = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE },
               mappedBy = "file")
    @org.hibernate.annotations.Cascade({
               org.hibernate.annotations.CascadeType.SAVE_UPDATE, 
                       org.hibernate.annotations.CascadeType.DELETE })
    public Map<String,ImageDescriptorBase> getImages() {
        return images;
    }
    
    /**
     * Set the images stored in this file. USed by persistence layer only.
     * @param images New set of images.
     */
    protected void setImages( Map<String,ImageDescriptorBase> images ) {
        this.images = images;
    }
    
    /**
     *     Opens the image file specified by fname & dirname properties, read
     *     the rest of fields from that and create the instances
     * @param f File from which to read the information
     * @throws org.photovault.common.PhotovaultException If the file is not of recognized type
     * @throws IOException if the image cannot be read.
     */
    protected void readImageFile( File f ) throws PhotovaultException, IOException {
        
        String fname = f.getName();
        int lastDotPos = fname.lastIndexOf( "." );
        if ( lastDotPos <= 0 || lastDotPos >= fname.length()-1 ) {
            throw new PhotovaultException( "Cannot determine file type extension of " + f.getAbsolutePath() );
        }
        String suffix = fname.substring( lastDotPos+1 );
        Iterator readers = ImageIO.getImageReadersBySuffix( suffix );
        if ( readers.hasNext() ) {
            ImageReader reader = (ImageReader)readers.next();
            ImageInputStream iis = null;
            try {
                iis = ImageIO.createImageInputStream( f );
                if ( iis != null ) {
                    reader.setInput( iis, true );
                    // int imageCount = reader.getNumImages( true );
                    // for now...
                    int imageCount = 1;
                    for ( int n = 0 ; n < imageCount ; n++ ) {
                        ImageDescriptorBase img = new OriginalImageDescriptor( this, "image#" + n );
                        img.setWidth( reader.getWidth( n ) );
                        img.setHeight( reader.getHeight( n ) );
                        // img.setInstanceType( ImageInstance.INSTANCE_TYPE_ORIGINAL );
                        img.setFile( this );
                    }
                    reader.dispose();
                }
            } catch (IOException ex) {
                log.debug( "Exception in readImageFile: " + ex.getMessage() );
                throw ex;
            } finally {
                if ( iis != null ) {
                    try {
                        iis.close();
                    } catch (IOException ex) {
                        log.warn( "Cannot close image stream: " + ex.getMessage() );
                    }
                }
            }
        } else {
            RawImage ri = new RawImage( f );
            if ( ri.isValidRawFile() ) {
                // PlanarImage img = ri.getCorrectedImage();
                ImageDescriptorBase img = new OriginalImageDescriptor( this, "image#0" );
                img.setWidth( ri.getWidth() );
                img.setHeight( ri.getHeight() );
            } else {
                throw new PhotovaultException( "Unknown image file extension " + suffix +
                        "\nwhile reading " + f.getAbsolutePath() );
            }
        }
        
    }
    
    
    /**
     *     Utility function to calculate the hash of a specific file
     * @param f The file
     * @return Hash of f
     */
    public static byte[] calcHash( File f ) {
        FileInputStream is = null;
        byte hash[] = null;
        try {
            is = new FileInputStream( f );
            byte readBuffer[] = new byte[4096];
            MessageDigest md = MessageDigest.getInstance("MD5");
            int bytesRead = -1;
            while( ( bytesRead = is.read( readBuffer ) ) > 0 ) {
                md.update( readBuffer, 0, bytesRead );
            }
            hash = md.digest();
        } catch (NoSuchAlgorithmException ex) {
            log.error( "MD5 algorithm not found" );
        } catch (FileNotFoundException ex) {
            log.error( f.getAbsolutePath() + "not found" );
        } catch (IOException ex) {
            log.error( "IOException while calculating hash: " + ex.getMessage() );
        }  finally {
            try {
                if ( is != null ) {
                    is.close();
                }
            } catch (IOException ex) {
                log.error( "Cannot close stream after calculating hash" );
            }
        }
        return hash;
    }

    /**
     Utility method to get an imge by its location in file
     @param string locator string of the image
     */
    public ImageDescriptorBase getImage(String string) {
        return images.get( string );
    }

    
}

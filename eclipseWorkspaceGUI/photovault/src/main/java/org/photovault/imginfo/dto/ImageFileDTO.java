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

package org.photovault.imginfo.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import org.photovault.imginfo.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.media.jai.ImageLayout;

/**
 Data transfer object that is used to transfer information about {@link ImageFile}
 between Photovault instances. It contains only information that is relevant in 
 any context - for example, known locations of the file are omitted as these are 
 specific to certain Photovault installation. On the other hand, if the file 
 contains a {@link CopyImageDescriptor} based on original in different file, 
 information about that file is included.
 <p>
 Use {@link ImageFileDtoResolver} to convert ImageFileDTO into a persistent 
 {@link ImageFile} object in local context.
 
 @since 0.6.0
 @author Harri Kaimio
 */
@XStreamAlias( "image-file" )
@XStreamConverter( ImageFileXmlConverter.class )
public class ImageFileDTO implements Serializable {

    static final long serialVersionUID = -1744409450144341619L;

    ImageFileDTO() {
        images = new HashMap<String, ImageDescriptorDTO>();
        locations = new ArrayList<FileLocationDTO>();
    }

    /**
     Constructs an ImageFileDTO based on ImageFile object
     @param f The ImageFile used.
     */
    public ImageFileDTO( ImageFile f ) {
        this(f, new HashMap<UUID, ImageFileDTO>());
    }
    
    /**
     Constructor that is used internally while constructing ImageFile graph.
     @param f The file used
     @param createdFiles Collection of ImageFile objects that are already 
     included in the graph being processed.
     */
    ImageFileDTO( ImageFile f, Map<UUID, ImageFileDTO> createdFiles ) {
        uuid = f.getId();
        hash = f.getHash();
        size = f.getFileSize();        
        createdFiles.put( uuid, this );
        images = new HashMap<String, ImageDescriptorDTO>( f.getImages().size() );
        locations = new ArrayList<FileLocationDTO>();
        for ( ImageDescriptorBase img : f.getImages().values() ) {
            ImageDescriptorDTO idto = null;
            if ( img instanceof OriginalImageDescriptor ) {
                idto = new OrigImageDescriptorDTO( img, createdFiles );
            } else {
                idto = new CopyImageDescriptorDTO( (CopyImageDescriptor) img, createdFiles );
            }
            images.put( img.getLocator(), idto );
        }
        for (FileLocation l : f.getLocations() ) {
            FileLocationDTO ldto = new FileLocationDTO( l );
            addLocation( ldto );
        }
    }
    /**
     UUID of the image file
     @serialField 
     */
    @XStreamAsAttribute
    private UUID uuid;
    
    /**
     MD5 hash of the image file
     @serialField 
     */
    @XStreamAsAttribute
    private byte[] hash;
    
    /**
     Size of the image file (in bytes)
     @serialField 
     */
    @XStreamAsAttribute
    private long size;
    
    /**
     Images belonging to the file
     */
    @XStreamAlias( "images" )
    transient private Map<String, ImageDescriptorDTO> images;

    transient private List<FileLocationDTO> locations;

    /**
     * Add a new image to the file. USer by {@link ImageFileXmlConverter}
     * @param locator Location of the image in file
     * @param img The image
     */
    void addImage( String locator, ImageDescriptorDTO img ) {
        images.put( locator, img );
    }

    /**
     Returns UUID of the described image file
     */
    public UUID getUuid() {
        return uuid;
    }


    void setUuid( UUID uuid ) {
        this.uuid = uuid;
    }

    /**
     Returns hash of the described image file
     */
    public byte[] getHash() {
        return hash != null ? Arrays.copyOf( hash, hash.length ) : null;
    }

    /**
     * @param hash the hash to set
     */
    void setHash( byte[] hash ) {
        this.hash = hash;
    }
    /**
     Returns size of the described image file in bytes
     */
    public long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    void setSize( long size ) {
        this.size = size;
    }

    /**
     Returns all images contained in the file
     */
    public Map<String, ImageDescriptorDTO> getImages() {
        return Collections.unmodifiableMap( images );
    }
    
    
    public List<FileLocationDTO> getLocations() {
        return Collections.unmodifiableList( locations );
    }

    void addLocation( FileLocationDTO l ) {
        locations.add( l );
    }

    /**
     Write the obejct to stream
     @param is
     */
    private void writeObject( ObjectOutputStream os ) throws IOException {
        os.defaultWriteObject();
        os.writeInt( images.size() );
        for ( Map.Entry<String,ImageDescriptorDTO> e : images.entrySet() ) {
            os.writeObject( e.getKey() );
            os.writeObject( e.getValue() );
        }
    }
    
    private void readObject( ObjectInputStream is ) 
            throws IOException, ClassNotFoundException {
        is.defaultReadObject();
        int imageCount = is.readInt();
        images = new HashMap<String, ImageDescriptorDTO>( imageCount );
        
        for ( int n = 0 ; n < imageCount ; n++ ) {
            String locator = (String) is.readObject();
            ImageDescriptorDTO dto = (ImageDescriptorDTO) is.readObject();
            images.put( locator, dto );
        }
    }
}

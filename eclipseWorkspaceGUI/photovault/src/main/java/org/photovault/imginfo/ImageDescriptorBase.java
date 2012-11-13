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

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * Image descriptors describe meta data about a single image known to Photovaut.
 * The image can be either original or copy (i.e. Photovault has created the image
 * by applying some operation to original)
 * <p>
 * Image is always contained in {@link ImageFile}.
 * </p>
 * @author Harri Kaimio
 * @since 0.6.0
 */
@Entity
@Table( name = "pv_images" )
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn( name = "image_type", discriminatorType = DiscriminatorType.STRING )
public class ImageDescriptorBase implements java.io.Serializable {
    
    /** Creates a new instance of ImageDescriptorBase */
    public ImageDescriptorBase() {
    }
    
    /**
     * Create a new image
     * @param f File from which information about the image is read.
     * @param locator Location of this image in file.
     */
    public ImageDescriptorBase( ImageFile f, String locator ) {
        this.file = f;
        this.locator = locator;
        file.images.put( locator, this );
    }


    Long id;
    private UUID fileId;
    private String locator;
    private ImageFile file;
    private int height;
    private int width;

    /**
     * Get id of the image. This is purely Photovault internal ID used by persistence layer.
     * @return Id of the image
     */
    @Id
    @Column( name = "id" )
    @GeneratedValue( generator = "ImageIdGen", strategy = GenerationType.TABLE )
    @TableGenerator( name="ImageIdGen", table="unique_keys", pkColumnName="id_name", 
                     pkColumnValue="hibernate_seq", valueColumnName="next_val" )
    Long getId() {
        return id;
    }
    
    /**
     * Set ID of the image. USed by persistence layer.
     * @param id New ID for the image.
     */
    protected void setId( Long id ) {
        this.id = id;
    }
    
    /**
     * Get the file that contains this image
     * @return The file
     */
    @ManyToOne( targetEntity = org.photovault.imginfo.ImageFile.class )
    @JoinColumn( name = "file_id", nullable = false )    
    public ImageFile getFile() {
        return file;
    }
    
    /**
     * Set the file that owns this image.
     * @param file The file
     */
    public void setFile(ImageFile file) {
        this.file = file;
    }
    
    /**
     * Get location of this image inside file
     * @return Location descriptor for the image. See setLocator for syntax.
     */
    @Column( name = "location" )
    public String getLocator() {
        return locator;
    }

    /**
     * Set location of the image in file. Since some image files can contain multiple 
     * images, there needs to be a way to refer to any of them.
     * <p>
     * The string given as parameter is of form "<type>#num{,<num>}" where
     * </p>
     * <ul>
     *    <li>type is the type of the image access. Currently specified types are 
     *    <strong>image</strong> (normal image), <strong>exif</strong> (image stored
     *    in EXIF metadata fo an image) and <strong>thumb</strong> (image stored as a 
     *    thumbnail)</li>
     *    <li>num is the order number of the image, starting from zero. If there are 
     * more than 1 order numbers, the tirst refers to an image in file and the second
     * to nth element associated with the image.</li>
     * </ul>
     * <p>Examples:</p>
     * <ul>
     *    <li>"image#0" refers to firsy image in the file</li>
     *    <li>"exif#0" refers to thumbnail stored in EXIF data of the first image</li>
     *    <li> "thumb#1,2" refers to 3rd thumbnail associated with 2nd image</li>
     * </ul>
     * @param locator Image locator, see above
     */
    public void setLocator(String locator ) {
        this.locator = locator;
    }


    /**
     * Get height of the image.
     * @return Height of the image in pixels
     */
    @Column( name = "height" )
    public int getHeight() {
        return height;
    }

    /**
     * Set height of the image
     * @param height Height of the image in pixels
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get width of the image
     * @return Width of the image in pixels.
     */
    @Column( name = "width" )
    public int getWidth() {
        return width;
    }

    /**
     * Set width of the image.
     * @param width Width of the image in pixels.
     */
    public void setWidth(int width) {
        this.width = width;
    }
}

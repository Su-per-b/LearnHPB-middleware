/*
  Copyright (c) 2008 Harri Kaimio
 
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

import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.folder.FolderPhotoAssociation;
import org.photovault.folder.PhotoFolder;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ColorCurve;
import org.photovault.replication.ObjectHistory;

/**
 Interface that provides methods for editing {@link PhotoInfo} objects.
 */
public interface PhotoEditor {

    /**
     Associate the photo with a new folder
     @param a Association object representing link to a folder
     */
    public void addFolderAssociation( FolderPhotoAssociation a );

    /**
     Remove association between the photo and folder
     @param a
     */
    public void removeFolderAssociation( FolderPhotoAssociation a );

    /**
     * Utility method to get the color curve assigned to blue channel
     * @return The curve or <code>null</code> if no curve is assigned
     */

    ColorCurve getBlueColorCurve();

    /**
     * Get the value of camera.
     * @return value of camera.
     */

    String getCamera();

    /**
     * Get currently preferred color channe?l mapping.
     * @return The current color channel mapping
     */

    ChannelMapOperation getColorChannelMapping();

    /**
     * Get the preferred crop bounds of the original image
     */
    Rectangle2D getCropBounds();

    /**
     * Get the value of description.
     * @return value of description.
     */
    String getDescription();

    /**
     * Get the value of FStop.
     * @return value of FStop.
     */
    double getFStop();

    /**
     * Get the value of film.
     * @return value of film.
     */
    String getFilm();

    /**
     * Get the value of filmSpeed.
     * @return value of filmSpeed.
     */
    int getFilmSpeed();

    /**
     * Get the value of focalLength.
     * @return value of focalLength.
     */
   double getFocalLength();

    /**
     * Returns a collection that contains all folders the photo belongs to
     */
    Set<PhotoFolder> getFolders();

    FuzzyDate getFuzzyShootTime();

    /**
     * Utility method to get the color curve assigned to green channel
     * @return The curve or <code>null</code> if no curve is assigned
     */
    ColorCurve getGreenColorCurve();

    ObjectHistory<PhotoInfo> getHistory();

    /**
     * Returns the time when this photo (=metadata of it) was last modified
     * @return a <code>Date</code> value
     */
   Date getLastModified();

    /**
     * Get the value of lens.
     * @return value of lens.
     */
    String getLens();

    /**
     * Utility method to get the color curve assigned to master value adjustment.
     * @return The curve or <code>null</code> if no curve is assigned
     */
    ColorCurve getMasterCurve();

    /**
     * Get the original file name of this photo
     *
     * @return a <code>String</code> value
     */
    String getOrigFname();

    /**
     * Get the value of photographer.
     * @return value of photographer.
     */
    String getPhotographer();

    /**
     * Get the preferred rotation for this image in degrees. Positive values
     * indicate that the image should be rotated clockwise.
     * @return value of prefRotation.
     */
    double getPrefRotation();

    /**
     * Get the value of value attribute.
     *
     * @return an <code>int</code> value
     */
    int getQuality();

    Integer getRawBlack();

    Double getRawColorTemp();

    Double getRawEvCorr();

    Double getRawHlightComp();

    /**
     * Get the current raw conversion settings.
     * @return Current settings or <code>null</code> if this is not a raw image.
     */
    RawConversionSettings getRawSettings();

    Integer getRawWhite();

    /**
     * Utility method to get the color curve assigned to red channel
     * @return The curve or <code>null</code> if no curve is assigned
     */
    ColorCurve getRedColorCurve();

    /**
     * Utility method to get the color curve assigned to saturation adjustment.
     * @return The curve or <code>null</code> if no curve is assigned
     */
    ColorCurve getSaturationCurve();

    /**
     * Get the value of shootTime. Note that shoot time can also be
     * null (to mean that the time is unspecified)1
     * @return value of shootTime.
     */
    Date getShootTime();

    /**
     * Get the value of shootingPlace.
     * @return value of shootingPlace.
     */
    String getShootingPlace();

    /**
     * Get the value of shutterSpeed.
     * @return value of shutterSpeed.
     */
    double getShutterSpeed();

    /**
     * Get the <code>TechNotes</code> value.
     *
     * @return a <code>String</code> value
     */
    String getTechNotes();

    /**
     *
     * @return The timeAccuracty value
     */
    double getTimeAccuracy();

    /**
     * Get the globally unique ID for this photo;
     */
    UUID getUuid();

    /**
     * Set the value of camera.
     * @param v  Value to assign to camera.
     */
    void setCamera( String v );

    /**
     * Set the preferred color channel mapping
     * @param cm the new color channel mapping
     */
    void setColorChannelMapping( ChannelMapOperation cm );

    /**
     * Set the preferred cropping operation
     * @param cropBounds New crop bounds
     */
    void setCropBounds( Rectangle2D cropBounds );

    /**
     * Set the value of description.
     * @param v  Value to assign to description.
     */
    void setDescription( String v );

    /**
     * Set the value of FStop.
     * @param v  Value to assign to FStop.
     */
    void setFStop( double v );

    /**
     * Set the value of film.
     * @param v  Value to assign to film.
     */
    void setFilm( String v );

    /**
     * Set the value of filmSpeed.
     * @param v  Value to assign to filmSpeed.
     */
    void setFilmSpeed( int v );

    /**
     * Set the value of focalLength.
     * @param v  Value to assign to focalLength.
     */
    void setFocalLength( double v );

    /**
     * Set both shooting time & accuracy directly using a FuzzyTime object
     * @param v FuzzyTime containing new values.
     */
    void setFuzzyShootTime( FuzzyDate v );


    /**
     * Set the value of lens.
     * @param v  Value to assign to lens.
     */
    void setLens( String v );

    /**
     * Set the original file name of this photo. This is set also by addToDB which is the
     * preferred way of creating a new photo into the DB.
     * @param newFname The original file name
     * @throws IllegalArgumentException if the given file name is longer than
     * {@link #ORIG_FNAME_LENGTH}
     */
    void setOrigFname( final String newFname );

    /**
     * Set the value of photographer.
     * @param v  Value to assign to photographer.
     */
    void setPhotographer( String v );

    /**
     * Set the value of prefRotation.
     * @param v  New preferred rotation in degrees. The value should be in range
     * 0.0 <= v < 360, otherwise v is normalized to be between these values.
     */
    void setPrefRotation( double v );

    /**
     * Set the "value attribute for the photo which tries to describe
     * How good the pohot is. Possible values:
     * <ul>
     * <li>QUALITY_UNDEFINED - value of the photo has not been evaluated</li>
     * <li>QUALITY_TOP - This frame is a top quality photo</li>
     * <li>QUALITY_GOOD - This frame is good, one of the best available from the session</li>
     * <li>QUALITY_FAIR - This frame is OK but probably not the 1st choice for use</li>
     * <li>QUALITY_POOR - Unsuccesful picture</li>
     * <li>QUALITY_UNUSABLE - Technical failure</li>
     * </ul>
     *
     *
     * @param newQuality The new Quality value.
     */
    void setQuality( final int newQuality );

    /**
     * Set the raw conversion settings for this photo
     * @param s The new raw conversion settings to use. The method makes a clone of
     * the object.
     */
    void setRawSettings( RawConversionSettings s );


    /**
     * Set the value of shootingPlace.
     * @param v  Value to assign to shootingPlace.
     */
    void setShootingPlace( String v );

    /**
     * Set the value of shutterSpeed.
     * @param v  Value to assign to shutterSpeed.
     */
    void setShutterSpeed( double v );

    /**
     * Set the <code>TechNotes</code> value.
     *
     * @param newTechNotes The new TechNotes value.
     */
    void setTechNotes( String newTechNotes );

}

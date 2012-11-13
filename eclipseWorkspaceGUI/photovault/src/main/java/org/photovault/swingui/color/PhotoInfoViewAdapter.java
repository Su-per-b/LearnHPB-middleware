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

package org.photovault.swingui.color;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ColorCurve;
import org.photovault.image.PhotovaultImage;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.FuzzyDate;
import org.photovault.imginfo.PhotoInfoFields;
import org.photovault.swingui.selection.PhotoSelectionController;
import org.photovault.swingui.selection.PhotoSelectionView;
import org.photovault.swingui.PreviewImageView;

/**
 * Adapter class for getting notifications from some PhotoSelectionController generated 
 * events. Just implements {@link PhotoInfoView} with empty methods so that it is 
 * easier to use when only a few methods are needed.
 * <p> 
 * In future PhotoInfoView should be refactored, using this class is more future 
 * proof.
 */
public class PhotoInfoViewAdapter implements PhotoSelectionView, PreviewImageView {
    private static Log log = LogFactory.getLog( PhotoInfoViewAdapter.class );
    
    PhotoSelectionController c;
    
    /**
     * Creates a new instance of PhotoInfoViewAdapter
     */
    public PhotoInfoViewAdapter( PhotoSelectionController c ) {
        this.c = c;
        c.addView( this );
    }
    
    public PhotoSelectionController getController() {
        return c;
    }

    public void setPhotographer(String newValue) {
    }

    public String getPhotographer() {
        return null;
    }

    public void setPhotographerMultivalued(boolean mv) {
    }

    public void setFuzzyDate(FuzzyDate newValue) {
    }

    public FuzzyDate getFuzzyDate() {
        return null;
    }

    public void setFuzzyDateMultivalued(boolean mv) {
    }

    public void setQuality(Number quality) {
    }

    public Number getQuality() {
        return null;
    }

    public void setQualityMultivalued(boolean mv) {
    }

    public void setShootingPlace(String newValue) {
    }

    public String getShootingPlace() {
        return null;
    }

    public void setShootingPlaceMultivalued(boolean mv) {
    }

    public void setFocalLength(Number newValue) {
    }

    public Number getFocalLength() {
        return null;
    }

    public void setFocalLengthMultivalued(boolean mv) {
    }

    public void setFStop(Number newValue) {
    }

    public Number getFStop() {
        return null;
    }

    public void setFStopMultivalued(boolean mv) {
    }

    public void setCamera(String newValue) {
    }

    public String getCamera() {
        return null;
    }

    public void setCameraMultivalued(boolean mv) {
    }

    public void setFilm(String newValue) {
    }

    public String getFilm() {
        return null;
    }

    public void setFilmMultivalued(boolean mv) {
    }

    public void setLens(String newValue) {
    }

    public String getLens() {
        return null;
    }

    public void setLensMultivalued(boolean mv) {
    }

    public void setDescription(String newValue) {
    }

    public String getDescription() {
        return null;
    }

    public void setDescriptionMultivalued(boolean mv) {
    }

    public void setTechNotes(String newValue) {
    }

    public String getTechNotes() {
        return null;
    }

    public void setTechNotesMultivalued(boolean mv) {
    }

    public void setShutterSpeed(Number newValue) {
    }

    public Number getShutterSpeed() {
        return null;
    }

    public void setShutterSpeedMultivalued(boolean mv) {
    }

    public void setFilmSpeed(Number newValue) {
    }

    public Number getFilmSpeed() {
        return null;
    }

    public void setFilmSpeedMultivalued(boolean mv) {
    }

    public void setFolderTreeModel(TreeModel model) {
    }

    public void setRawSettings(RawConversionSettings rawConversionSettings) {
    }

    public void setRawSettingsMultivalued(boolean mv) {
    }

    public RawConversionSettings getRawSettings() {
        return null;
    }

    public void expandFolderTreePath(TreePath path) {
    }

    public void setColorChannelCurve(String name, ColorCurve curve) {
    }

    public void setColorChannelMultivalued(String name, boolean isMultivalued, ColorCurve[] values ) {
    }

    public ColorCurve getColorChannelCurve(String name) {
        return null;
    }
    
    public void setColorChannelMapping(ChannelMapOperation cm) {
    }

    public ChannelMapOperation getColorChannelMapping() {
        return null;
    }

    public void setColorChannelMappingMultivalued(boolean mv) {
    }

    public void modelPreviewImageChanged(PhotovaultImage preview) {
    }

    public PhotovaultImage getPreviewImage() {
        return null;
    }

    public Object getField(PhotoInfoFields field) {
        Object value = null;
        String propertyName = field.getName();
        try {
            value = PropertyUtils.getProperty( this, propertyName );
        } catch (NoSuchMethodException ex) {
            log.error( "Cannot get property " + propertyName );
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            log.error( ex.getMessage() );
        } catch (InvocationTargetException ex) {
            log.error( ex.getMessage() );
        }
        return value;
    }


    public void setField(PhotoInfoFields field, Object newValue) {
        String propertyName = field.getName();
        try {
            PropertyUtils.setProperty( this, propertyName, newValue );
        } catch (NoSuchMethodException ex) {
            log.error( "Cannot set property " + propertyName );
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            log.error( ex.getMessage() );
        } catch (InvocationTargetException ex) {
            log.error( ex.getMessage() );
        }
    }
    
    public void setFieldMultivalued(PhotoInfoFields field, boolean isMultivalued) {
        String propertyName = field.getName() + "Multivalued";
        try {
            PropertyUtils.setProperty( this, propertyName, isMultivalued );
        } catch (NoSuchMethodException ex) {
            log.error( "Cannot set property " + propertyName );
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            log.error( ex.getMessage() );
        } catch (InvocationTargetException ex) {
            log.error( ex.getMessage() );
        }
    }

    public void setField(PhotoInfoFields field, Object newValue, List refValues) {
    }

    public void setHistogram( String channel, int[] histData ) {
    }
        
}

/*
  Copyright (c) 2007, Harri Kaimio
  
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

import java.awt.Frame;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;
import javax.media.jai.Histogram;
import org.hibernate.Session;
import org.photovault.image.ImageRenderingListener;
import org.photovault.image.PhotovaultImage;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoFields;
import org.photovault.swingui.JAIPhotoViewer;
import org.photovault.swingui.PhotoViewChangeEvent;
import org.photovault.swingui.PhotoViewChangeListener;
import org.photovault.swingui.PhotoViewEvent;
import org.photovault.swingui.framework.AbstractController;
import org.photovault.swingui.selection.*;

/**
ColorSettingsDlgController manages the interaction between color settings
dialog, selected photos and preview in JAIPhotoViewer.
 
 */
public class ColorSettingsDlgController extends PhotoSelectionController
        implements PhotoViewChangeListener, ImageRenderingListener {

    /** Creates a new instance of ColorSettingsDlgController */
    public ColorSettingsDlgController( Frame parentFrame,
            AbstractController parentCtrl, Session session ) {
        super( parentCtrl, session );
        this.parentFrame = parentFrame;
        createUI();
    }
    
    
    Frame parentFrame;
    ColorSettingsDlg dlg = null;
    /**
     Image shown in preview 
     */
    ColorSettingsPreview preview = null;
    /**
     The image that is shown in preview control
     */
    PhotovaultImage previewImage = null;


    /**
    Create the color settings dialog
     */
    void createUI() {
        dlg = new ColorSettingsDlg( parentFrame, this, false, null );
        addView( dlg );
    }

    /**
    This method overrides the super type viewChanged method so that it also 
    updates the preview control.
    TODO: More elegant solution would be if also JAIPhotoViewer would a real 
    view for selection controller. However, this will require larger refactoring
    (e.g. to accommodate hierarchical selection controllers with different
    changes tied to different views.)
    @param view The view that initiated field change
    @param field The changed field
    @param value New value for field
     */
    @Override
    public void viewChanged( PhotoSelectionView view, PhotoInfoFields field,
            Object value ) {
        super.viewChanged( view, field, value );
        if ( preview != null && photos != null &&
                photos.length == 1 && photos[0] == preview.getPhoto() ) {
            updatePreview( field, value );
        }
    }

    @Override
    protected void photosChanged() {
        boolean allRaw = true;
        if ( photos != null ) {
            for ( PhotoInfo p : photos ) {
                if ( p.getRawSettings() == null ) {
                    allRaw = false;
                }
            }
        } else {
            allRaw = false;
        }
        dlg.setRawControlsEnabled( allRaw );
    }

    /**
    Discard all changes. OVerridden from superclass to ensure that preview image 
    is returned to original state.
     */
    @Override
    public void discard() {
        super.discard();
        if ( preview != null && photos != null &&
                photos.length == 1 && photos[0] == preview.getPhoto() ) {
            for ( PhotoInfoFields f : EnumSet.allOf( PhotoInfoFields.class ) ) {
                updatePreview( f, PhotoInfoFields.getFieldValue( photos[0], f ) );
            }
        }
    }

    /**
    Update the preview control to match change that has happened in other views
    @param field The changed field
    @param value new value for field
     */
    private void updatePreview( PhotoInfoFields field, Object value ) {
        Set fieldValues = this.getFieldValues( field );

        preview.setField( field, value, new ArrayList( fieldValues ) );
    }

    /**
    Set the preview control used by this controller.
    @param preview The control to use
     */
    public void setPreviewControl( ColorSettingsPreview preview ) {
        this.preview = preview;
        if ( preview != null && preview instanceof JAIPhotoViewer ) {
            ((JAIPhotoViewer) preview).addViewChangeListener( this );
        }
        previewImage = preview.getImage();
        if ( previewImage != null ) {
            previewImage.addRenderingListener( this );
        }
        updateViewHistograms();
        
    }
    /**
    Names of the color channels
     */
    static String[] colorCurveNames =
            {"value", "red", "green", "blue", "saturation"};
    /**
    What histogram is shown with each channel
     */
    static String[] channelHistType = {
        PhotovaultImage.HISTOGRAM_RGB_CHANNELS,
        PhotovaultImage.HISTOGRAM_RGB_CHANNELS,
        PhotovaultImage.HISTOGRAM_RGB_CHANNELS,
        PhotovaultImage.HISTOGRAM_RGB_CHANNELS,
        PhotovaultImage.HISTOGRAM_IHS_CHANNELS
    ,
       
            };
    /**
     The band of the histogram defined in @see channelHistTypes that is
     associated with each channel. -1 means that all histogram bands should be
     shown.
     */       
    static int[] channelHistBand = {
        -1, 0, 1, 2, 2};

    /**
    Get histogram for a specific color channel if data is available
    @param channel Name of channel
    @return Array of integers that contain histogram data for the selected image
    if the data is available. <code>Null</code> otherwise.
     */
    public int[] getHistogram( String channel ) {
        int chan = 0;
        while ( chan < colorCurveNames.length && !colorCurveNames[chan].equals( channel ) ) {
            chan++;
        }
        if ( chan == colorCurveNames.length ) {
            return null;
        }
        int[] histData = null;
        if ( preview != null ) {
            PhotovaultImage previewImage = preview.getImage();
            if ( previewImage != null && channelHistType[chan] != null ) {
                Histogram h = previewImage.getHistogram( channelHistType[chan] );
                if ( h != null ) {
                    if ( channelHistBand[chan] == -1 ) {
                        /*
                        TODO: All histogram bands should be shown. Currently
                        this is not supported by ColorCurvePanel. If the image is
                        black and white, show jus thte value.
                         */
                        if ( h.getNumBands() < 3 ) {
                            histData = h.getBins( 0 );
                        }
                    } else if ( channelHistBand[chan] < h.getNumBands() ) {
                        histData = h.getBins( channelHistBand[chan] );
                    }
                }
            }
        }
        return histData;
    }

    /**
     * Update all histograms in views from current preview image.
     */
    private void updateViewHistograms() {
        Histogram rgbHist = null;
        Histogram ihsHist = null;
        if ( previewImage != null ) {
            rgbHist = previewImage.getHistogram( PhotovaultImage.HISTOGRAM_RGB_CHANNELS );
            ihsHist = previewImage.getHistogram( PhotovaultImage.HISTOGRAM_IHS_CHANNELS );
        }
        for ( int n = 0 ; n < colorCurveNames.length ; n++ ) {
            Histogram h = ( channelHistType[n].equals(PhotovaultImage.HISTOGRAM_RGB_CHANNELS) ) ?
                rgbHist : ihsHist;
            int[] histData = null;
            if ( h != null ) {
                if ( channelHistBand[n] == -1 ) {
                    /*
                    TODO: All histogram bands should be shown. Currently
                    this is not supported by ColorCurvePanel. If the image is
                    black and white, show jus thte value.
                     */
                    if ( h.getNumBands() < 3 ) {
                        histData = h.getBins( 0 );
                    }
                } else if ( channelHistBand[n] < h.getNumBands() ) {
                    histData = h.getBins( channelHistBand[n] );
                }
            }
            for ( PhotoSelectionView view : views ) {
                view.setHistogram( colorCurveNames[n], histData );
            }
        }
    }

    public void showDialog() {
        dlg.showDialog();
    }


    /**
     * Callback that is called after a new rendering of preview iamge has been 
     * created, so that histograms in other views must be refreshed.
     * @param img The image that has been rendered.
     */
    public void newRenderingCreated( PhotovaultImage img ) {
        updateViewHistograms();
    }

    /**
     Callback when image shown in preview window changes 
     @param ev
     */
    public void photoViewChanged( PhotoViewChangeEvent ev ) {
        if ( previewImage != null ) {
            previewImage.removeRenderingListener( this );
        }
        previewImage = preview.getImage();
        if ( previewImage != null ) {
            previewImage.addRenderingListener( this );
        }
        updateViewHistograms();
    }
}

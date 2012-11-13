/*
  Copyright (c) 2006 Harri Kaimio
  
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
package org.photovault.dcraw;

import org.photovault.common.PhotovaultException;

/**
 * Factory class for creating RawConversionSettings instances. This is an utility
 * class that makes RawConversionSettings creation easier.
 * @author Harri Kaimio
 */
public class RawSettingsFactory {
    
    /**
     * Creates a new instance of RawSettingsFactory.
     * @param settings A {@link RawConversionSettings} object that will be used a s a template
     */
    public RawSettingsFactory( RawConversionSettings settings ) {
        if ( settings != null ) {
            ctemp = settings.getColorTemp();
            greenGain = settings.getGreenGain();
            redGreenRatio = settings.getRedGreenRatio();
            blueGreenRatio = settings.getBlueGreenRatio();
            dlBlueGreenRatio = settings.getDaylightBlueGreenRatio();
            dlRedGreenRatio = settings.getDaylightRedGreenRatio();
            evCorr = settings.getEvCorr();
            black = settings.getBlack();
            white = settings.getWhite();
            hlightComp = settings.getHighlightCompression();
            useEmbeddedProfile = settings.getUseEmbeddedICCProfile();
            colorProfile = settings.getColorProfile();
            medianPassCount = settings.getMedianPassCount();
            waveletThreshold = settings.getWaveletThreshold();
            hlightRecovery = settings.getHlightRecovery();
            colorBalanceSet = true;
            dlMultSet = true;
        }
    }
    
    /**
     Defatult constructor
     */
    public RawSettingsFactory() {
        
    }
    
    double ctemp;
    double greenGain = 1.0;
    double redGreenRatio;
    double blueGreenRatio;
    double dlBlueGreenRatio;
    double dlRedGreenRatio;
    double evCorr = 0.0f;
    int black = 0;
    int white = 0x10000;
    double hlightComp = 0.0;
    int hlightRecovery = 0;
    float waveletThreshold = 0.0f;
    int medianPassCount = 0;
    boolean useEmbeddedProfile = false;
    ColorProfileDesc colorProfile = null;
    boolean colorBalanceSet = false;
    boolean dlMultSet = false;
    
    /**
     * Set the color temperature for created objects
     * @param colorTemp The color temperature (in Kelvins) that will be used.
     */
    public void setColorTemp( double colorTemp ) {
        ctemp = colorTemp;
        colorBalanceSet = true;
        calcColorMultipliers();
    }
    
    /**
     * Set the green gain to be used
     * @param greenGain Green gain
     */
    public void setGreenGain( double greenGain ) {
        this.greenGain = greenGain;
        calcColorMultipliers();        
    }
    
    /**
     * Set the ratio of red and green channel multipliers. This is an alternate 
     * method for settings color balance to color temperature.
     * @param r Ration of red and green channels.
     */
    public void setRedGreenRation( double r ) {
        redGreenRatio = r;
        if ( blueGreenRatio > 0.0 ) {
            colorBalanceSet = true;
        }
        calcColorTemp();
    }
    
    /**
     * Set the ratio of blue and green channel multipliers. This is an alternate 
     * method for settings color balance to color temperature.
     * @param r Ratio of blue and green channels
     */
    public void setBlueGreenRatio( double r ) {
        blueGreenRatio = r;
        if ( redGreenRatio > 0.0 ) {
            colorBalanceSet = true;
        }
        calcColorTemp();
    }
    
    /**
     * Set the channel multipliers used in daylight (5500K)
     * @param m Array of 3 doubles that contain the channel multipliers for red, blue 
     * and green channels.
     */
    public void setDaylightMultipliers( double[] m ) {
        dlBlueGreenRatio = m[2]/m[1];
        dlRedGreenRatio = m[0]/m[1];  
        this.dlMultSet = true;
    }
    
    /**
     * Set the EV correction
     * @param evCorr EV correction that will be used (compared to {@link #white} setting)
     */
    public void setEvCorr( double evCorr ) {
        this.evCorr = evCorr;
    }
    
    /**
     * Set the black level
     * @param black Pixel value that will be considered black
     */
    public void setBlack( int black ) {
        this.black = black;
    }
    
    /**
     * Set the white level
     * @param white Pixel value that will be considered as white. If evCorrection or hightlight
     * compression is not zero it will affect the actula white point.
     */
    public void setWhite( int white ) {
        this.white = white;
    }
    
    /**
     * Set the hightlight compression that will be applied
     * @param compression New highlight compression value
     */
    public void setHlightComp( double compression ) {
        this.hlightComp = compression;
    }
    
    /**
     * Set the color profile
     * @param profile Color profile that will be applied for convertion from 
     * raw image color space
     */
    public void setColorProfile( ColorProfileDesc profile ) {
        this.colorProfile = profile;
    }
    
    /**
     * Set whether to use color profile embedded to raw file
     * @param useEmbedded <CODE>true</CODE> if embedded profile should be used, 
     * <CODE>false</CODE> otherwise.
     */
    public void setUseEmbeddedProfile( boolean useEmbedded ) {
        this.useEmbeddedProfile = useEmbedded;
    }

    /**
     * @param hlightRecovery the hlightRecovery to set
     */
    public void setHlightRecovery(int hlightRecovery) {
        this.hlightRecovery = hlightRecovery;
    }

    /**
     * @param waveletThreshold the waveletThreshold to set
     */
    public void setWaveletThreshold(float waveletThreshold) {
        this.waveletThreshold = waveletThreshold;
    }

    /**
     * @param medianPassCount the medianPassCount to set
     */
    public void setMedianPassCount(int medianPassCount) {
        this.medianPassCount = medianPassCount;
    }

    /**
     * Create a new {@link RawConversionSettings} object and initialize it with the
     * values set for this factory.
     * @throws org.photovault.common.PhotovaultException if some value(s) needed 
     * for object creation are not initialized. In practice,
     * at least daylightmultipliers and color balance must be set (using either 
     * channel multipliers or color temperature)
     * @return The created object.
     */
    public RawConversionSettings create()
        throws PhotovaultException {
        if ( !( colorBalanceSet && dlMultSet )  ) {
            throw new PhotovaultException( 
                    "Color balance or daylight multipliers not set" );
        }
        RawConversionSettings s = new RawConversionSettings();
        s.daylightRedGreenRatio = dlRedGreenRatio;
        s.daylightBlueGreenRatio = dlBlueGreenRatio;
        s.redGreenRatio = redGreenRatio;
        s.blueGreenRatio = blueGreenRatio;
        s.evCorr = evCorr;
        s.hlightComp = hlightComp;
        s.white = white;
        s.black = black;
        s.whiteBalanceType = RawConversionSettings.WB_MANUAL;
        s.useEmbeddedICCProfile = this.useEmbeddedProfile;
        s.colorProfile = this.colorProfile;
        s.hlightRecovery = hlightRecovery;
        s.waveletThreshold = waveletThreshold;
        s.medianPassCount = medianPassCount;

        return s;        
    }

    /**
     * Calculate color channel multipliers form color temperature.
     */
    private void calcColorMultipliers() {
        double[] rgb = RawConversionSettings.colorTempToRGB( ctemp );
        redGreenRatio = (dlRedGreenRatio*rgb[1]) / (rgb[0]*greenGain);
        blueGreenRatio = (dlBlueGreenRatio*rgb[1]) / (rgb[2]*greenGain);        
    }

    /**
     * Calculate color temperature and green gain from channel multipliers.
     */
    private void calcColorTemp() {
        double rgb[] = {
            dlRedGreenRatio/redGreenRatio,
            1.,
            dlBlueGreenRatio/blueGreenRatio
        };
        double ct[] = RawConversionSettings.rgbToColorTemp( rgb );
        ctemp = ct[0];
        greenGain = ct[1];
    }


}

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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Wrapper object for all settings related to raw conversion using dcraw.
 * This is an immutable object.
 * @author Harri Kaimio
 */
@Embeddable
public class RawConversionSettings implements Cloneable, Externalizable {
    
    static final long serialVersionUID = 1131589578728161524L;
    /**
    Version of the serialized for of this object
     */
    static final int version = 1;
    /**
    The white pixel value in raw image (unadjusted)
     */
    int white;
    
    /**
    EV correction of the image
     */
    double evCorr;
    
    /**
    Highlight compression (in f-stops)
     */
    double hlightComp;
    
    /**
    black level
     */
    int black;
    
    /**
    Whether the ICC profile embedded to raw file should be used
     */
    boolean useEmbeddedICCProfile;
    
    /**
    Database ID for the color profile (used by OJB)
     */
    int colorProfileId;
    
    /**
    ICC color profile used for raw conversion.
     */
    ColorProfileDesc colorProfile;
    
    /**
    Methodfor setting the white balance
     */
    int whiteBalanceType;
    
    /**
    Invalid default value
     */
    final public static int WB_INVALID = 0;

    /**
    Use camera WB settings
     */
    final public static int WB_CAMERA = 1;

    /**
    User dcraw's automatic WB algorithm
     */
    final public static int WB_AUTOMATIC = 2;
    
    /**
    Set white balance manually
     */
    final public static int WB_MANUAL = 3;
    
    /**
    Ratio of red & green channel multipliers
     */
    double redGreenRatio;

    /**
    Ratio of blue & green channel multipliers
     */
    double blueGreenRatio;
    
    /**
    Ratio of red & green channel multipliers in daylight
     */
    double daylightRedGreenRatio;

    /**
    Ratio of blue & green channel multipliers in daylight
     */
    double daylightBlueGreenRatio;

    /**
     * Highlight recovery method
     */
    int hlightRecovery;

    /**
     * Wavelet denoising threshold
     */
    float waveletThreshold;

    /**
     * Number of median filter passes
     */
    int medianPassCount;

    /** Creates a new instance of RawConversionSettings */
    public RawConversionSettings() {
    }

    /**
     *     Get the white point
     * @return See {@link #white}
     */
    @Column(name = "raw_whitepoint")
    public int getWhite() {
        return white;
    }

    protected void setWhite( int white ) {
        this.white = white;
    }

    /**
     *     Get the black point
     * 
     * @return See {@link #black}
     */
    @Column(name = "raw_blackpoint")
    public int getBlack() {
        return black;
    }

    protected void setBlack( int black ) {
        this.black = black;
    }

    /**
     *     Get exposure correction
     * @return See {@link #evCorr}
     */
    @Column(name = "raw_ev_corr")
    public double getEvCorr() {
        return evCorr;
    }

    protected void setEvCorr( double newEv ) {
        this.evCorr = newEv;
    }

    /**
     * Get the highlight compression used
     * @return Number of f-stops the highlights are compressed.
     */
    @Column( name = "raw_hlight_corr")
    public double getHighlightCompression() {
        return hlightComp;
    }

    protected void setHighlightCompression( double hlc ) {
        this.hlightComp = hlc;
    }
    
    /**
     * Get info whether to use embedded color profile
     * @return See {@link #useEmbeddedICCProfile}
     */
    @Column( name = "raw_embedded_profile" )
    public boolean getUseEmbeddedICCProfile() {
        return useEmbeddedICCProfile;
    }

    protected void setUseEmbeddedICCProfile( boolean isEmbedded ) {
        this.useEmbeddedICCProfile = isEmbedded;
    }
    
    /**
     Get the ICC color profile used for raw conversion
     @return The used profile or <code>null</code> if no non-embedded profile
     is assigned.
     */
    @Transient
    public ColorProfileDesc getColorProfile() {
        return colorProfile;
    }
    
    /**
     * Get the method for setting white balance
     * @return One of {@link #WB_AUTOMATIC}, {@link #WB_MANUAL} 
     * or {@link #WB_CAMERA}
     */
    @Column( name = "raw_wb_type" )
    public int getWhiteBalanceType() {
        return whiteBalanceType;
    }

    protected void setWhiteBalanceType( int wbType ) {
        this.whiteBalanceType = wbType;
    }
    
    /**
     * Get the red/green channel multiplier ratio
     * @return see {@link #redGreenRatio}
     */
    @Column( name = "raw_r_g_ratio" )
    public double getRedGreenRatio() {
        return redGreenRatio;
    }

    protected void setRedGreenRatio( double newRatio ) {
        this.redGreenRatio = newRatio;
    }
    
    /**
     * Get blue/green channel multiplier ratio
     * @return See {@link #blueGreenRatio}
     */
    @Column( name = "raw_b_g_ratio" )
    public double getBlueGreenRatio() {
        return blueGreenRatio;
    }

    protected void setBlueGreenRatio( double newRatio ) {
        this.blueGreenRatio = newRatio;
    }
    
    /**
     * Get daylight channel multipliers
     * @return See {@link #daylightRedGreenRatio}
     */
    @Column( name = "raw_dl_r_g_ratio" )
    public double getDaylightRedGreenRatio() {
        return daylightRedGreenRatio;
    }

    protected void setDaylightRedGreenRatio( double newRatio ) {
        this.daylightRedGreenRatio = newRatio;
    }

    /**
     * Get daylight channel multipliers
     * @return See {@link #daylightBlueGreenRatio}
     */
    @Column( name = "raw_dl_b_g_ratio" )
    public double getDaylightBlueGreenRatio() {
        return daylightBlueGreenRatio;
    }

    protected void setDaylightBlueGreenRatio( double newRatio ) {
        this.daylightBlueGreenRatio = newRatio;
    }
    
    final static double XYZ_to_RGB[][] = {
        { 3.24071,  -0.969258,  0.0556352 },
        {-1.53726,  1.87599,    -0.203996 },
        {-0.498571, 0.0415557,  1.05707 } };
    
    /**
     * Convert a color telmerature to RGB value of an white patch illuminated 
     * with light with that temperature
     * @param T The color temperature of illuminant (in Kelvin)
     * @return Patches RGB value as 3 doubles (RGB)
     */
    static public double[] colorTempToRGB( double T ) {
        /*
         This routine has been copied from Udi Fuchs' ufraw (and is originally from
         Bruce Lindbloom's web site)
         */
        
        int c;
        double xD, yD, X, Y, Z, max;
        double RGB[] = new double[3];
        // Fit for CIE Daylight illuminant
        if (T<= 4000) {
            xD = 0.27475e9/(T*T*T) - 0.98598e6/(T*T) + 1.17444e3/T + 0.145986;
        } else if (T<= 7000) {
            xD = -4.6070e9/(T*T*T) + 2.9678e6/(T*T) + 0.09911e3/T + 0.244063;
        } else {
            xD = -2.0064e9/(T*T*T) + 1.9018e6/(T*T) + 0.24748e3/T + 0.237040;
        }
        yD = -3*xD*xD + 2.87*xD - 0.275;
        
        X = xD/yD;
        Y = 1;
        Z = (1-xD-yD)/yD;
        max = 0;
        for (c=0; c<3; c++) {
            RGB[c] = X*XYZ_to_RGB[0][c] + Y*XYZ_to_RGB[1][c] + Z*XYZ_to_RGB[2][c];
            if (RGB[c]>max) max = RGB[c];
        }
        for (c=0; c<3; c++) RGB[c] = RGB[c]/max;
        return RGB;
    }
    
    /**
     Converts RGB multipliers to color balance
     @param rgb The color triplet of a white patch in the raw image
     @return Array of 2 doubles that contains temperature & green gain for the 
             light source that has illuminated the patch.    
     */
    static public double[] rgbToColorTemp( double rgb[] ) {
        double Tmax;
        double Tmin;
        double testRGB[] = null;
        Tmin = 2000;
        Tmax = 12000;
        double T;
        for (T=(Tmax+Tmin)/2; Tmax-Tmin>10; T=(Tmax+Tmin)/2) {
            testRGB = colorTempToRGB( T );
            if (testRGB[2]/testRGB[0] > rgb[2]/rgb[0])
                Tmax = T;
            else
                Tmin = T;
        }
        double green = (testRGB[1]/testRGB[0]) / (rgb[1]/rgb[0]);
        double result[] = {T, green};
        return result;
    }
    
    /**
     * Set the color temperature to use when converting the image
     * @param T Color temperature (in Kelvin)
     */
    public void setColorTemp( double T ) {
        double rgb[] = colorTempToRGB( T );
        
        redGreenRatio = daylightRedGreenRatio/(rgb[0]/rgb[1]);
        blueGreenRatio = daylightBlueGreenRatio/(rgb[2]/rgb[1]);
    }
    
    /**
     * Get color temperature of the image
     * @return Color temperature (in Kelvin)
     */
    @Transient
    public double getColorTemp() {
        double rgb[] = {
            daylightRedGreenRatio/redGreenRatio,
            1.,
            daylightBlueGreenRatio/blueGreenRatio
        };
        double ct[] = rgbToColorTemp( rgb );
        return ct[0];
    }
    
    /**
     * Get the green channel gain used
     * @return Ratio of green channel multiplier to the multiplier caused by 
     * illuminant with current comlor temperature.
     */
    @Transient
    public double getGreenGain() {
        double rgb[] = {
            daylightRedGreenRatio/redGreenRatio,
            1.,
            daylightBlueGreenRatio/blueGreenRatio
        };
        double ct[] = rgbToColorTemp( rgb );
        return ct[1];        
    }
    /**
     * @return The highlight recovery method
     */
    @Column( name = "raw_hlight_recovery" )
    public int getHlightRecovery() {
        return hlightRecovery;
    }

    void setHlightRecovery( int recovery ) {
        hlightRecovery = recovery;
    }
    /**
     * @return Threshold for wavelet denoising
     */
    @Column( name = "raw_wavelet_denoise_thr")
    public float getWaveletThreshold() {
        return waveletThreshold;
    }

    void setWaveletThreshold( float threshold ) {
        waveletThreshold = threshold;
    }

    /**
     * @return Number of passes median filter is applied to reduce artifacts
     */
    @Column( name = "raw_median_pass_count" )
    public int getMedianPassCount() {
        return medianPassCount;
    }

    void setMedianPassCount( int count ) {
        medianPassCount = count;
    }
    
    /**
     * Creates a new RawConversionSettings object
     * @param chanMult Channel multipliers (4 doubles)
     * @param daylightMult daylight channel multipliers (3 doubles)
     * @param white White pixel value
     * @param black Black pixel value
     * @param evCorr Exposure correction
     * @param hlightComp Highlight compression
     * @param wbType White balance setting method
     * @param embeddedProfile Should we use embedded ICC profile form raw file?
     * @return New RawConversionSettings object
     * @deprecated Use {@link RawSettingsFactory} instead.
     */
    static public RawConversionSettings create( 
            double chanMult[], double daylightMult[], 
            int white, int black, double evCorr, double hlightComp, int wbType, boolean embeddedProfile ) {
        RawConversionSettings s = new RawConversionSettings();
        s.blueGreenRatio = chanMult[2]/chanMult[3];
        s.redGreenRatio = chanMult[0]/chanMult[1];
        s.daylightRedGreenRatio = daylightMult[0]/daylightMult[1];
        s.daylightBlueGreenRatio = daylightMult[2]/daylightMult[1];
        s.evCorr = evCorr;
        s.hlightComp = hlightComp;
        s.white = white;
        s.black = black;
        s.whiteBalanceType = wbType;
        s.useEmbeddedICCProfile = embeddedProfile;
        return s;
    }
    
    /**
     * Creates a new RawConversionSettings object using color temperature
     * @param colorTemp Color temperature
     * @param greenGain Green gain to use
     * @param daylightMult daylight channel multipliers (3 doubles)
     * @param white White pixel value
     * @param black Black pixel value
     * @param evCorr Exposure correction
     * @param hlightComp Highlight compression
     * @param wbType White balance setting method
     * @param embeddedProfile Should we use embedded ICC profile form raw file?
     * @return New RawConversionSettings object
     * @deprecated Use {@link RawSettingsFactory} instead.
     */
    static public RawConversionSettings create( 
            double colorTemp, double greenGain, double daylightMult[], 
            int white, int black, double evCorr, double hlightComp, 
            int wbType, boolean embeddedProfile ) {
        RawConversionSettings s = new RawConversionSettings();
        s.daylightRedGreenRatio = daylightMult[0]/daylightMult[1];
        s.daylightBlueGreenRatio = daylightMult[2]/daylightMult[1];
        double[] rgb = RawConversionSettings.colorTempToRGB( colorTemp );
        s.redGreenRatio = (s.daylightRedGreenRatio/(rgb[0]/(rgb[1]))) / greenGain;
        s.blueGreenRatio = (s.daylightBlueGreenRatio/(rgb[2]/(rgb[1]))) / greenGain;
        s.evCorr = evCorr;
        s.hlightComp = hlightComp;
        s.white = white;
        s.black = black;
        s.whiteBalanceType = wbType;
        s.useEmbeddedICCProfile = embeddedProfile;
        return s;
    }
    
    /**
     * Create a copy of this object
     * @return Copy initialized with current field values
     */
    @Override
    public RawConversionSettings clone() {
        RawConversionSettings s;
        try {
            s = (RawConversionSettings) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
        return s;
    }
    
    /**
     * Compares 2 doubles and check whether the are equal to a given accuracy
     * @param d1 The 1st number to compare
     * @param d2 The 2nd number to compare
     * @param errorLimit Maximum absolute error allowed
     * @return <code>true</code> if d1 & d2 are equal within the given 
     * accuracy, <code>false</code> otherwise.
     */
    private boolean equalsDouble( double d1, double d2, double errorLimit ) {
        return Math.abs( d1-d2 ) < errorLimit;
    }
    /**
     * Compare this objetc to another
     * @param o The object to compare with
     * @return Whether the 2 objects are equal.
     */
    @Override
    public boolean equals( Object o ) {
        if ( o instanceof RawConversionSettings ) {
            RawConversionSettings s = (RawConversionSettings) o;
            return ( s.white == white &&
                    s.useEmbeddedICCProfile == this.useEmbeddedICCProfile &&
                    s.colorProfile == this.colorProfile && 
                    s.whiteBalanceType == this.whiteBalanceType &&
                    s.black == this.black &&
                    s.waveletThreshold == this.waveletThreshold &&
                    s.hlightRecovery == this.hlightRecovery &&
                    s.medianPassCount == this.medianPassCount &&
                    equalsDouble(s.waveletThreshold, waveletThreshold, 0.001 ) &&
                    equalsDouble( s.blueGreenRatio, this.blueGreenRatio, 0.0001 ) &&
                    equalsDouble( s.redGreenRatio, this.redGreenRatio, 0.0001 ) &&
                    equalsDouble( s.daylightBlueGreenRatio, this.daylightBlueGreenRatio, 0.0001 ) &&
                    equalsDouble( s.daylightRedGreenRatio, this.daylightRedGreenRatio, 0.0001 ) &&
                    equalsDouble( s.hlightComp, this.hlightComp, 0.0001 ) &&
                    equalsDouble( s.evCorr, this.evCorr, 0.0001 ) );
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return ( white + black + (int) (blueGreenRatio * 1000000.0) + 
                (int)( redGreenRatio * 1000000.0 ) );
    }
    

    /**
     Searializes the object.
     @param oo 
     @throws java.io.IOException
     
     @serialData
     RawConversionSettings serial format starts with version ID (which is 1 for
     current settings format. After that the fields are stored in the following 
     order:
     <ul>    
      <li>black point (int)</li>
      <li>white point (int)</li>
      <li>evCorr (double)</li>
      <li>hlightComp (double)</li>
      <li>daylightRedGreenRatio (double)</li>
      <li>daylightBlueGreenRatio (double)</li>
      <li>redGreenRatio (double)</li>
      <li>blueGreenRatio (double)</li>
      <li>whiteBalanceType (int)</li>
    </ul>
     
     */
    public void writeExternal( ObjectOutput oo ) throws IOException {
        oo.writeInt( version );
        oo.writeInt( black );
        oo.writeInt(  white );
        oo.writeDouble( evCorr );
        oo.writeDouble( hlightComp );
        oo.writeDouble( daylightRedGreenRatio );
        oo.writeDouble( daylightBlueGreenRatio );
        oo.writeDouble( redGreenRatio );
        oo.writeDouble( blueGreenRatio );
        oo.writeInt( whiteBalanceType );
    }

    public void readExternal( ObjectInput oi ) 
            throws IOException, ClassNotFoundException {
        int v = oi.readInt();
        if ( v > 1 ) { 
            throw new IOException( "Too new version " + v );
        }
        black = oi.readInt();
        white = oi.readInt();
        evCorr = oi.readDouble();
        hlightComp = oi.readDouble();
        daylightRedGreenRatio = oi.readDouble();
        daylightBlueGreenRatio = oi.readDouble();
        redGreenRatio = oi.readDouble();
        blueGreenRatio = oi.readDouble();
        whiteBalanceType = oi.readInt();
    }
}

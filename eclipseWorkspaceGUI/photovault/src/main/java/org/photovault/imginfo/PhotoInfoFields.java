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

package org.photovault.imginfo;

import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ColorCurve;

/**
 Enum type that lists all fields in {@link PhotoInfo}. The class also provides
 utility methodds to access photo by field.
 */
public enum PhotoInfoFields {                
    CAMERA ("camera", String.class ),         
    CROP_BOUNDS( "cropBounds", Rectangle2D.class ),         
    DESCRIPTION( "description", String.class ),         
    FSTOP( "FStop", Double.class ),       
    FILM( "film", String.class ),       
    FILM_SPEED( "filmSpeed", Integer.class ),         
    FOCAL_LENGTH( "focalLength", Integer.class ),         
    LENS( "lens", String.class ),         
    ORIG_FNAME( "origFname", String.class ),         
    PHOTOGRAPHER( "photographer", String.class ),         
    PREF_ROTATION( "prefRotation", Double.class ),         
    QUALITY( "quality", Integer.class ),         
    RAW_SETTINGS( "rawSettings", RawConversionSettings.class ),         
    SHOOT_TIME( "shootTime", Date.class ),  
    FUZZY_SHOOT_TIME( "fuzzyShootTime", FuzzyDate.class ),
    SHOOTING_PLACE( "shootingPlace", String.class ),         
    SHUTTER_SPEED( "shutterSpeed", Double.class ),         
    TECH_NOTES( "techNotes", String.class ),         
    TIME_ACCURACY( "timeAccuracy", Double.class ),         
    UUID( "uuid", UUID.class ),         
    RAW_BLACK_LEVEL( "rawBlack", Integer.class ),         
    RAW_WHITE_LEVEL( "rawWhite", Integer.class ),         
    RAW_EV_CORR( "rawEvCorr", Double.class ),         
    RAW_HLIGHT_COMP( "hlightComp", Double.class ),         
    RAW_CTEMP( "rawColorTemp", Double.class ),         
    RAW_GREEN( "rawGreenGain", Double.class ),
    RAW_HLIGHT_RECOVERY( "rawHlightRecovery", Integer.class ),
    RAW_WAVELET_DENOISE_THRESHOLD( "rawWaveletDenoiseThreshold", Float.class ),
    RAW_COLOR_PROFILE( "rawColorProfile", Double.class ),         
    COLOR_CURVE_VALUE( "masterCurve", ColorCurve.class ),         
    COLOR_CURVE_RED( "redColorCurve", ColorCurve.class ),         
    COLOR_CURVE_GREEN( "greenColorCurve", ColorCurve.class ),         
    COLOR_CURVE_BLUE( "blueColorCurve", ColorCurve.class ),         
    COLOR_CURVE_SATURATION( "saturationCurve", ColorCurve.class );


    PhotoInfoFields(String name, Class type) {
        this.name = name;
        this.type = type;
    }
    
    private final String name;
    private final Class type;
    
    public Class getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }

    private static Object getRawSettingFieldValue( PhotoInfo photo, PhotoInfoFields field ) {
        RawConversionSettings s = photo.getRawSettings();
        if ( s == null ) {
            return null;
        }
        switch ( field ) {
            case RAW_BLACK_LEVEL:
                return s.getBlack();
            case RAW_COLOR_PROFILE:
                return s.getColorProfile();
            case RAW_CTEMP:
                return s.getColorTemp();
            case RAW_EV_CORR:
                return s.getEvCorr();
            case RAW_GREEN:
                return s.getGreenGain();
            case RAW_HLIGHT_COMP:
                return s.getHighlightCompression();
            case RAW_WHITE_LEVEL:
                return s.getWhite();
            case RAW_HLIGHT_RECOVERY:
                return s.getHlightRecovery();
            case RAW_WAVELET_DENOISE_THRESHOLD:
                return s.getWaveletThreshold();
        }
        throw new IllegalArgumentException( "Unknown raw setting field " + field );
    }
    
    private static ColorCurve getColorCurve( PhotoInfo photo, PhotoInfoFields field ) {
        ChannelMapOperation cm = photo.getColorChannelMapping();
        if ( cm == null ) {
            return null;
        }
        switch( field ) {
            case COLOR_CURVE_VALUE:
                return cm.getChannelCurve( "value" );
            case COLOR_CURVE_RED:
                return cm.getChannelCurve( "red" );
            case COLOR_CURVE_GREEN:
                return cm.getChannelCurve( "green" );
            case COLOR_CURVE_BLUE:
                return cm.getChannelCurve( "blue" );
            case COLOR_CURVE_SATURATION:
                return cm.getChannelCurve( "saturation" );
        }
        throw new IllegalArgumentException( "Unknown raw setting field " + field );
    }
    
    /**
     Get value of a field in a photo
     @param photo The photo
     @param field Field to access
     @return Value of field in photo, or <code>null</code> if the field has not 
     been set.
     */
    public static Object getFieldValue( PhotoInfo photo, PhotoInfoFields field ) {
        Object retval = null;
        switch( field ) {
            case CAMERA:
                return photo.getCamera();
            case CROP_BOUNDS:
                return photo.getCropBounds();
            case DESCRIPTION:
                return photo.getDescription();
            case FILM:
                return photo.getFilm();
            case FILM_SPEED:
                return photo.getFilmSpeed();
            case FOCAL_LENGTH:
                return photo.getFocalLength();
            case FSTOP:
                return photo.getFStop();
            case LENS:
                return photo.getLens();
            case ORIG_FNAME:
                return photo.getOrigFname();
            case PHOTOGRAPHER:
                return photo.getPhotographer();
            case PREF_ROTATION:
                return photo.getPrefRotation();
            case QUALITY:
                return photo.getQuality();
            case RAW_SETTINGS:
                return photo.getRawSettings();
            case SHOOTING_PLACE:
                return photo.getShootingPlace();
            case SHOOT_TIME:
                return photo.getShootTime();
            case FUZZY_SHOOT_TIME:
                return photo.getFuzzyShootTime();
            case SHUTTER_SPEED:
                return photo.getShutterSpeed();
            case TECH_NOTES:
                return photo.getTechNotes();
            case TIME_ACCURACY:
                return photo.getTimeAccuracy();
            case UUID:
                return photo.getUuid();
        }
        if ( EnumSet.range( RAW_BLACK_LEVEL, RAW_COLOR_PROFILE).contains( field )  ) {
            return getRawSettingFieldValue( photo, field );
        }
        if ( EnumSet.range( COLOR_CURVE_VALUE, COLOR_CURVE_SATURATION ).contains( field ) ) {
            return getColorCurve( photo, field );
        }
        throw new IllegalArgumentException( "No support for field " + field );
    }
    
    /**
     Get a field by its name
     @param name Name of the field
     @return Field with the specified name or <code>null</code> if no such field 
     exists.
     */
    public static PhotoInfoFields getByName( String name ) {
        for ( PhotoInfoFields f : EnumSet.allOf( PhotoInfoFields.class ) ) {
            if ( f.getName().equals( name ) ) {
                return f;
            }
        }
        return null;
    }
    
    /**
     Set of all raw conversion parameter fields
     */
    public static final EnumSet<PhotoInfoFields> 
            RAW_FIELDS = EnumSet.range(RAW_BLACK_LEVEL, RAW_COLOR_PROFILE );
}
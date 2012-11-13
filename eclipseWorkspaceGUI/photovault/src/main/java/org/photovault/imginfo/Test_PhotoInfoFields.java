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
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawSettingsFactory;
import org.photovault.image.ChannelMapOperationFactory;
import org.photovault.image.ColorCurve;
import static org.photovault.imginfo.PhotoInfoFields.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static java.lang.Math.*;
/**
 *
 * @author harri
 */
public class Test_PhotoInfoFields {
    
    /** Creates a new instance of Test_PhotoInfoFields */
    public Test_PhotoInfoFields() {
    }
    
    PhotoInfo photo;
    
    @BeforeMethod
    public void setUp() throws PhotovaultException {
        photo = PhotoInfo.create();
        photo.setCamera("camera" );
        photo.setCropBounds( new Rectangle2D.Double( 0.1, 0.1, 0.9, 0.9 ) );
        photo.setDescription( "description" );
        photo.setFStop( 2.8 );
        photo.setFilm( "trix" );
        photo.setFilmSpeed( 320 );
        photo.setFocalLength( 52 );
        photo.setFuzzyShootTime( new FuzzyDate( new Date( 2000, 1, 1 ), 1.0 ) );
        photo.setLens( "50 mm" );
        photo.setPhotographer( "harri" );
        photo.setQuality( 1 );
        photo.setShootingPlace( "rantasalmi" );
        photo.setPrefRotation( 20.0 );
        photo.setShutterSpeed( 0.2 );
        photo.setTechNotes( "technote" );
        
        RawSettingsFactory f = new RawSettingsFactory();
        f.setBlack( 100 );
        f.setWhite( 40000 );
        f.setDaylightMultipliers( new double[] {1.0, 1.0, 1.0 } );
        f.setColorTemp( 5000.0 );
        f.setEvCorr( -1.0 );
        f.setHlightComp( 1.0 );
        f.setGreenGain( 1.1 );
        photo.setRawSettings( f.create() );

        valueCurve = new ColorCurve();
        valueCurve.addPoint( 0.0, 0.0 );
        valueCurve.addPoint( 0.1, 0.0 );
        redCurve = new ColorCurve();
        redCurve.addPoint( 0.1, 0.0 );
        redCurve.addPoint( 0.2, 0.0 );
        greenCurve = new ColorCurve();
        greenCurve.addPoint( 0.2, 0.0 );
        greenCurve.addPoint( 0.3, 0.0 );
        blueCurve = new ColorCurve();
        blueCurve.addPoint( 0.3, 0.0 );
        blueCurve.addPoint( 0.4, 0.0 );
        satCurve = new ColorCurve();
        satCurve.addPoint( 0.4, 0.0 );
        satCurve.addPoint( 0.5, 0.0 );
        
        ChannelMapOperationFactory cf = new ChannelMapOperationFactory( null );
        cf.setChannelCurve( "value", valueCurve );
        cf.setChannelCurve( "red", redCurve );
        cf.setChannelCurve( "green", greenCurve );
        cf.setChannelCurve( "blue", blueCurve );
        cf.setChannelCurve( "saturation", satCurve );
        
        photo.setColorChannelMapping( cf.create() );
    }
    
    ColorCurve redCurve;
    ColorCurve greenCurve;
    ColorCurve blueCurve;
    ColorCurve valueCurve;
    ColorCurve satCurve;
    
    @Test
    public void testFieldAccess() {
        assert getFieldValue( photo, CAMERA ).equals( "camera" );
        assert getFieldValue( photo, CROP_BOUNDS ).equals( new Rectangle2D.Double( 0.1, 0.1, 0.9, 0.9 ) );
        assert getFieldValue( photo, DESCRIPTION ).equals( "description" );
        assert getFieldValue( photo, FSTOP ).equals( 2.8 );
        assert getFieldValue( photo, FILM ).equals( "trix" );
        assert getFieldValue( photo, FILM_SPEED ).equals( 320 );
        assert getFieldValue( photo, FOCAL_LENGTH ).equals( 52.0 );
        assert getFieldValue( photo, SHOOT_TIME ).equals(  new Date( 2000, 1, 1 ) );
        assert getFieldValue( photo, LENS ).equals( "50 mm" );
        assert getFieldValue( photo, PHOTOGRAPHER ).equals( "harri" );
        assert getFieldValue( photo, QUALITY ).equals( 1 );
        assert getFieldValue( photo, SHOOTING_PLACE ).equals( "rantasalmi" );
        assert getFieldValue( photo, PREF_ROTATION ).equals( 20.0 );
        assert getFieldValue( photo, SHUTTER_SPEED ).equals( 0.2 );
        assert getFieldValue( photo, TECH_NOTES ).equals( "technote" );
    }
    
    @Test
    public void testRawSettingsAccess() {
        assert getFieldValue( photo, RAW_WHITE_LEVEL ).equals(  40000 );
        assert getFieldValue( photo, RAW_BLACK_LEVEL ).equals(  100 );
        assert abs( ((Double)getFieldValue( photo, RAW_CTEMP )) -5000.0) < 5.0;
        assert getFieldValue( photo, RAW_EV_CORR ).equals(  -1.0 );
        assert abs( ((Double)getFieldValue( photo, RAW_GREEN )) - 1.1 ) < 0.001;
        assert getFieldValue( photo, RAW_HLIGHT_COMP ).equals(  1.0 );
        assert getFieldValue( photo, RAW_COLOR_PROFILE ) == null;        
    }
    
    @Test
    public void testChannelMap() {
        assert getFieldValue( photo, COLOR_CURVE_VALUE ).equals( valueCurve );
        assert getFieldValue( photo, COLOR_CURVE_RED ).equals( redCurve );
        assert getFieldValue( photo, COLOR_CURVE_BLUE ).equals( blueCurve );
        assert getFieldValue( photo, COLOR_CURVE_GREEN ).equals( greenCurve );
        assert getFieldValue( photo, COLOR_CURVE_SATURATION ).equals( satCurve );
    }
    
    @Test
    public void testNullRawSettings() {
        photo.setRawSettings( null );
        assert getFieldValue( photo, RAW_SETTINGS ) == null;
        assert getFieldValue( photo, RAW_BLACK_LEVEL ) == null;
    }
    
    @Test
    public void testNullChannelMap() {
        photo.setColorChannelMapping( null );
        assert getFieldValue( photo, COLOR_CURVE_GREEN ) == null;
    }
    
    /**
     Verify that no field value generates an exception
     */
    @Test
    public void testAllFieldValues() {
        for ( PhotoInfoFields field : EnumSet.allOf( PhotoInfoFields.class ) ) {
            getFieldValue( photo, field );
        }
    }
}

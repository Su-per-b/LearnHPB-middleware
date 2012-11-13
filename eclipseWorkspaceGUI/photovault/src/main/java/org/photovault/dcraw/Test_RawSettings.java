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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.photovault.common.PhotovaultException;

/**
 * Test cases for RawConversionSettings and RawSettingsFactory classes
 * @author Harri Kaimio
 * @since 
 */
public class Test_RawSettings extends TestCase {
    
    /** Creates a new instance of Test_RawSettings */
    public Test_RawSettings() {
    }
    
    /**
     * Test "copying" using RawSettingsFactory (i.e. Initialize the factory with exisiting
     * settings and verify that created objects are equal to this.
     */
    public void testFactoryCopy() {
        RawConversionSettings s1 =
                RawConversionSettings.create(
                5000.0, 1.0,
                new double[] {1., 1., 1.},
                16000, 0, 0.0, 1.0, RawConversionSettings.WB_MANUAL,
                false
                );
        RawSettingsFactory f = new RawSettingsFactory( s1 );
        RawConversionSettings s2 = null;
        try {
            s2 = f.create();
        } catch (PhotovaultException ex) {
            fail( "Exception while creating raw settings: " + ex.getMessage() );
        }
        assertEquals( s1, s2 );
    }
    
    /**
     * Test creating settings with RawSettingsFactory
     */
    public void testRawFactoryCreate() {
        RawConversionSettings s1 =
                RawConversionSettings.create(
                5000.0, 1.0,
                new double[] {1., 1., 1.},
                16000, 0, 0.0, 1.0, RawConversionSettings.WB_MANUAL,
                false
                );
                
        RawSettingsFactory f = new RawSettingsFactory( s1 );
        f.setColorTemp( 6000 );
        f.setHlightComp( 2.0 );
        f.setWhite( 32000 );
        RawConversionSettings s2 = null;
        try {
            s2 = f.create();
        } catch (PhotovaultException ex) {
            fail( "Exception while creating raw settings: " + ex.getMessage() );
        }
        assertEquals( "Color temp incorrect", 6000.0, s2.getColorTemp(), 10.0 );
        assertEquals( "White incorrect", 32000, s2.getWhite() );
        assertEquals( "Hightlight compression incorrect", 2.0, s2.getHighlightCompression(), 0.01 );
        assertEquals( "Green gain incorrect", s1.getGreenGain(), s2.getGreenGain(), 0.001 );
    }
    
    /**
     * Test that RawSettingsFactory works correctly if no seed settings object has been
     * given: first create() method must throw exception, later when all necessary 
     * values have been set it must create a correct object.
     */
    public void testRawFactoryCreateNoSeedSettings() {
        RawSettingsFactory f = new RawSettingsFactory( null );
        boolean success = false;
        try {
            RawConversionSettings s = f.create();
        } catch (PhotovaultException ex) {
            success = true;
        }
        assertTrue( "No exception thrown when RawSettingsFactory not initialized",
                success );
        f.setDaylightMultipliers( new double[] {1.0, 1.0, 1.0} );
        f.setBlack( 0 );
        f.setWhite( 32000 );
        f.setBlueGreenRatio( 1.0 );
        f.setRedGreenRation( 1.0 );
        f.setEvCorr( 0.0 );
        f.setHlightComp( 0.0 );
        RawConversionSettings s2 = null;
        try {
            s2 = f.create();
        } catch (PhotovaultException ex) {
            fail( "Exception while creating raw settings: " + ex.getMessage() );
        }
        assertEquals( "Black not correct", 0, s2.getBlack() );
        assertEquals( "White not correct", 32000, s2.getWhite() );
        assertEquals( "B/G not correct", 1.0, s2.getBlueGreenRatio() );
        assertEquals( "R/G not correct", 1.0, s2.getRedGreenRatio() );
        assertEquals( "EV correction incorrect", 0.0, s2.getEvCorr() );
        assertEquals( "Hlight compression incorrect", 0.0, s2.getHighlightCompression() );
    }
    
    public static void main( String[] args ) {
        junit.textui.TestRunner.run( suite() );
    }

    public static Test suite() {
	return new TestSuite( Test_RawSettings.class );
    }
}

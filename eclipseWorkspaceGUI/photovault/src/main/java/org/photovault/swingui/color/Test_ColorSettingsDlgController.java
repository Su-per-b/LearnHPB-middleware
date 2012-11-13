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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.image.ChannelMapOperationFactory;
import org.photovault.image.ColorCurve;
import org.photovault.image.PhotovaultImage;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoFields;
import org.photovault.imginfo.PhotoNotFoundException;
import org.photovault.test.PhotovaultTestCase;

/**
  Test cases for color settings dialog. These also test selection framework in 
 general.
 
 */
public class Test_ColorSettingsDlgController extends PhotovaultTestCase {
    static Log log = LogFactory.getLog( Test_ColorSettingsDlgController.class.getName() );
    
    static String testImgDir = "testfiles";
    
    /** Creates a new instance of Test_ColorSettingsDlgController */
    public Test_ColorSettingsDlgController() {
    }

    JFrame frame;
    ColorSettingsDlgController ctrl;
    PhotoInfo photo;
    PhotoInfo photo2;
    
    public void setUp() {
        frame = new JFrame( getName() );
        ctrl = new ColorSettingsDlgController( frame, null, null );
        ctrl.showDialog();

        // Create a photo that can be used in testing
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
	try {
	    photo = PhotoInfo.addToDB( f );
	} catch ( PhotoNotFoundException e ) {
	    fail( "Could not find photo: " + e.getMessage() );
	}
        ColorCurve c = new ColorCurve();
        c.addPoint( 0.5, 0.5 );
        c.addPoint( 0.5, 0.7 );
        ChannelMapOperationFactory cmf = new ChannelMapOperationFactory();
        cmf.setChannelCurve( "blue", c );
        photo.setColorChannelMapping( cmf.create() );
        photo = ctrl.getDAOFactory().getPhotoInfoDAO().makePersistent( photo );
        
        // Create another photo that can be used in testing
	fname = "test2.jpg";
	f = new File( testImgDir, fname );
	try {
	    photo2 = PhotoInfo.addToDB( f );
	} catch ( PhotoNotFoundException e ) {
	    fail( "Could not find photo: " + e.getMessage() );
        }
        c = new ColorCurve();
        c.addPoint( 0.5, 0.5 );
        c.addPoint( 0.5, 0.7 );
        cmf.setChannelCurve( "blue", c );
        photo2.setColorChannelMapping( cmf.create() );        
        photo2 = ctrl.getDAOFactory().getPhotoInfoDAO().makePersistent( photo2 );        
    }
    
    public void tearDown() {
        frame.dispose();
        ctrl.getDAOFactory().getPhotoInfoDAO().makeTransient( photo );
        ctrl.getDAOFactory().getPhotoInfoDAO().makeTransient( photo2 );
    }
    

    // handy abbreviation for displaying our test frame
    private void showFrame() {
        try {
            // Always do direct component manipulation on the event thread
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() { frame.pack(); frame.show(); }
            });
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }    
    
    
    /**
     Test view to use
     */
    static class TestPreview implements ColorSettingsPreview {
        public void setField(PhotoInfoFields field, Object value, List refValues) {
            values.put( field, value );
            if ( refValues != null ) {
                this.refValues.put( field, refValues );
            }
        }

        public PhotoInfo getPhoto() {
            return photo;
        }
        
        PhotoInfo photo;
        Map<PhotoInfoFields, Object> values = 
                new HashMap<PhotoInfoFields, Object>();
        Map<PhotoInfoFields, List> refValues = 
                new HashMap<PhotoInfoFields, List>();

        public PhotovaultImage getImage() {
            return null;
        }
    }
        
    
    /**
     Test that change in color curve is passed to other views and preview
     */
    public void testColorCurvePassing() {
        TestPreview preview = new TestPreview();
        ctrl.setPreviewControl( preview );
        ctrl.setPhoto( photo );
        preview.photo = photo;
        ColorCurve curve = new ColorCurve();
        curve.addPoint( 0.1, 0.2 );
        curve.addPoint( 0.9, 0.8 );
        
        ctrl.viewChanged( ctrl.dlg, PhotoInfoFields.COLOR_CURVE_BLUE, curve );
        assertTrue( preview.values.containsKey( PhotoInfoFields.COLOR_CURVE_BLUE ) );
        ColorCurve curve2 = (ColorCurve) preview.values.get( PhotoInfoFields.COLOR_CURVE_BLUE );
        assertEquals( curve, curve2 );
    }
    
    /**
     Test that change in raw settings is passed to other views and preview
     */
    public void testRawSettingPassing() {
        
    }
    
    /**
     Test that preview is updated in correct situations (and not updated if it 
     is not showing the selection.
     */
    public void testPreviewUpdates() {
        // Assert that preview is updated if it is showing the correct photo
        
        TestPreview preview = new TestPreview();
        ctrl.setPreviewControl( preview );
        ctrl.setPhoto( photo );
        preview.photo = photo;
        ColorCurve curve = new ColorCurve();
        curve.addPoint( 0.1, 0.2 );
        curve.addPoint( 0.9, 0.8 );
        
        ctrl.viewChanged( ctrl.dlg, PhotoInfoFields.COLOR_CURVE_BLUE, curve );
        assertTrue( preview.values.containsKey( PhotoInfoFields.COLOR_CURVE_BLUE ) );
        ColorCurve res = (ColorCurve) preview.values.get( PhotoInfoFields.COLOR_CURVE_BLUE );
        assertEquals( curve, res );
        
        // Assert that preview is NOT updated if it shows a different photo
        preview.photo = photo2;
        ColorCurve curve2 = new ColorCurve();
        curve2.addPoint( 0.2, 0.1 );
        curve2.addPoint( 0.8, 0.9 );        
        preview.values.clear();
        ctrl.viewChanged( ctrl.dlg, PhotoInfoFields.COLOR_CURVE_BLUE, curve );
        assertFalse( preview.values.containsKey( PhotoInfoFields.COLOR_CURVE_BLUE ) );
        
        // Assert that reference values are updated and curve cleared when 
        // multiple photos are selected
        preview.values.clear();
        preview.refValues.clear();
        ctrl.setPhotos( new PhotoInfo[] {photo, photo2} );
        assertNull(preview.values.get( PhotoInfoFields.COLOR_CURVE_BLUE ) );
        assertNull( ctrl.dlg.getColorChannelCurve( "blue" ) );
        assertEquals( 2, ctrl.dlg.getRefCurves( "blue" ).size() );
        ctrl.viewChanged( null, PhotoInfoFields.COLOR_CURVE_BLUE, curve );
        assertEquals( curve, ctrl.dlg.getColorChannelCurve( "blue" ) );
        assertEquals( 2, ctrl.dlg.getRefCurves( "blue" ).size()  );
    }
    
    /**
     Test that saving changes work.
     */
    public void testSave() {
        TestPreview preview = new TestPreview();
        ctrl.setPreviewControl( preview );
        ctrl.setPhoto( photo );
        preview.photo = photo;
        ColorCurve curve = new ColorCurve();
        curve.addPoint( 0.1, 0.2 );
        curve.addPoint( 0.9, 0.8 );
        
        ctrl.viewChanged( ctrl.dlg, PhotoInfoFields.COLOR_CURVE_BLUE, curve );
        ChangePhotoInfoCommand cmd = ctrl.getChangeCommand();
        assertEquals( curve, cmd.getField( PhotoInfoFields.COLOR_CURVE_BLUE ) );
    }
    
    /**
     Test that after discarding changes all views are returned to initial state
     */
    public void testDiscard() {
        TestPreview preview = new TestPreview();
        ctrl.setPreviewControl( preview );
        ctrl.setPhoto( photo );
        preview.photo = photo;
        ColorCurve curve = new ColorCurve();
        curve.addPoint( 0.1, 0.2 );
        curve.addPoint( 0.9, 0.8 );
        
        ctrl.viewChanged( ctrl.dlg, PhotoInfoFields.COLOR_CURVE_BLUE, curve );
        ctrl.discard();
        /*
         After discard, the photos should have their original curves again
         */
        assertEquals( photo.getBlueColorCurve(), ctrl.dlg.getColorChannelCurve( "blue" ) );
        assertEquals( 1, ctrl.dlg.getRefCurves( "blue" ).size() );
        
    }
    
    public static Test suite() {
	return new TestSuite( Test_ColorSettingsDlgController.class );
    }
    
    public static void main( String[] args ) {
	junit.textui.TestRunner.run( suite() );
    }	    
}

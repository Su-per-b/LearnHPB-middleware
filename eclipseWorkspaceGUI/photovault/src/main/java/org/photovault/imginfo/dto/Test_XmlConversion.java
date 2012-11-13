/*
  Copyright (c) 2008 Harri Kaimio

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

package org.photovault.imginfo.dto;

import com.thoughtworks.xstream.XStream;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.UUID;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.dcraw.RawSettingsFactory;
import org.photovault.image.ChannelMapOperationFactory;
import org.photovault.image.ColorCurve;
import org.photovault.image.ImageOpChain;
import org.photovault.imginfo.CopyImageDescriptor;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.FileLocation;
import org.photovault.imginfo.ImageFile;
import org.photovault.imginfo.OriginalImageDescriptor;
import org.photovault.imginfo.VolumeManager;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 * Test code for serializing image files to XML
 *
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class Test_XmlConversion {

    XStream xstream;

    @BeforeClass
    public void setup() {
        xstream = new XStream();
        ImageFileXmlConverter c = new ImageFileXmlConverter( xstream.getMapper(), true );
        xstream.registerConverter( c, XStream.PRIORITY_VERY_HIGH );
        xstream.processAnnotations( ImageFileDTO.class );
        xstream.processAnnotations( FileLocationDTO.class );
        xstream.processAnnotations( ImageDescriptorDTO.class );
        xstream.processAnnotations( OrigImageDescriptorDTO.class );
        xstream.processAnnotations( CopyImageDescriptorDTO.class );
        xstream.processAnnotations( OrigImageRefResolver.class );
        ImageOpChain.initXStream( xstream );
    }

    @Test
    public void testImageConversion() throws PhotovaultException {
        ImageFile f = new ImageFile();
        f.setFileSize( 1000000 );
        f.setHash( new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16} );
        f.setId( UUID.randomUUID() );

        OriginalImageDescriptor img = new OriginalImageDescriptor( f, "image#1" );
        img.setWidth( 2000 );
        img.setHeight( 1000 );

        CopyImageDescriptor copy1 = new CopyImageDescriptor( f, "image#2", img );
        copy1.setCropArea( new Rectangle2D.Double( 0.1, 0.1, 0.8, 0.8 ) );
        copy1.setRotation( 27.0 );
        copy1.setWidth( 200 );
        copy1.setHeight( 100 );
        ChannelMapOperationFactory cmf = new ChannelMapOperationFactory();
        ColorCurve b1 = new ColorCurve();
        b1.addPoint( 0.1, 0.1 );
        b1.addPoint( 0.8, 0.8 );
        cmf.setChannelCurve( "blue", b1 );
        copy1.setColorChannelMapping( cmf.create() );
        RawSettingsFactory rf = new RawSettingsFactory();
        rf.setBlack( 20 );
        rf.setWhite( 50000 );
        rf.setColorTemp( 5500 );
        rf.setEvCorr( -0.1 );
        rf.setHlightComp( 0.2 );
        rf.setDaylightMultipliers( new double[]{1.0, 1.1, 1.2} );
        RawConversionSettings rs = rf.create();
        copy1.setRawSettings( rs );
        ExternalVolume v = new ExternalVolume( "testvol", "/tmp/testvol" );
        FileLocation l = new FileLocation( v, "dir/file.jpg" );
        l.setLastModifiled( new Date() );
        f.addLocation( l );

        ImageFileDTO dto = new ImageFileDTO( f );

        String xml = xstream.toXML( dto );
        
        ImageFileDTO dto2 = (ImageFileDTO) xstream.fromXML( xml );

        // Verify that the deserialized object is OK
        assertEquals( dto.getSize(), dto2.getSize() );
        assertEquals( dto.getUuid(), dto2.getUuid() );
        assertEquals( dto.getHash(), dto2.getHash() );
        assertEquals( dto.getImages().size(), dto2.getImages().size() );
        CopyImageDescriptorDTO copy2 = (CopyImageDescriptorDTO) dto2.getImages().get( "image#2" );
        assertEquals( copy1.getProcessing(), copy2.getProcessing() );
        assertTrue( dto2 == copy2.getOrigImageFile() );
        assertEquals( "image#1", copy2.getOrigLocator() );
    }

}

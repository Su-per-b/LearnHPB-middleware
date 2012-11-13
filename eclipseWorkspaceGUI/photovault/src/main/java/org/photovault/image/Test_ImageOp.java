/*
  Copyright (c) 2009 Harri Kaimio

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
package org.photovault.image;

import com.thoughtworks.xstream.XStream;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.dcraw.RawSettingsFactory;
import org.photovault.image.ImageOp.Sink;
import org.photovault.image.ImageOp.Source;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 * Test cases for {@link ImageOpChain} and {@link ImageOp}
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class Test_ImageOp {

    static class TestOp extends ImageOp {

        TestOp( ImageOpChain chain, String name ) {
            super();
            setName( name );
            setChain( chain );
            initPorts();
        }

        @Override
        protected void initPorts() {
            addInputPort( "in" );
            addOutputPort( "out" );
        }

        @Override
        public ImageOp createCopy() {
            throw new UnsupportedOperationException( "Not supported yet." );
        }
    }

    @Test
    public void testXml() {
        ImageOpChain chain = new ImageOpChain();
        DCRawOp op1 = new DCRawOp( chain, "dcraw" );
        op1.setWhite( 32000 );
        op1.setBlack( 30 );
        Source op1out = op1.getOutputPort( "out" );
        DCRawMapOp op2 = new DCRawMapOp( chain, "op2" );
        op2.setBlack( 25 );
        op2.setWhite( 10000 );
        op2.setEvCorr( -1.0  );
        op2.setHlightCompr( 0.1 );
        Sink op2in = op2.getInputPort( "in" );
        op2in.setSource( op1out );
        Source op2out = op2.getOutputPort( "out" );
        ChanMapOp op3 = new ChanMapOp( chain, "map" );
        ColorCurve sat = new ColorCurve();
        sat.addPoint( 0.0, 0.0 );
        sat.addPoint( 1.0, 0.5 );
        op3.setChannel( "sat", sat );
        Sink op3in = op3.getInputPort( "in" );
        op3in.setSource( op2out );
        Source op3out = op3.getOutputPort( "out" );
        CropOp op4 = new CropOp( chain, "crop" );
        op4.setRot( 2.0 );
        op4.setMaxX( 0.9 );
        op4.setMaxY( 0.8 );
        op4.setMinX( 0.1 );
        op4.setMinY( 0.2 );
        Sink op4in = op4.getInputPort( "in" );
        op4in.setSource( op3out );
        Source op4out = op4.getOutputPort( "out" );
        chain.addOperation( op1 );
        chain.addOperation( op2 );
        chain.addOperation( op3 );
        chain.addOperation( op4 );

        chain.setHead( op4out.getPortName() );

        XStream xs = new XStream();
        xs.processAnnotations( ImageOp.class );
        xs.processAnnotations( DCRawOp.class );
        xs.processAnnotations( DCRawMapOp.class );
        xs.processAnnotations( CropOp.class );
        xs.processAnnotations( ChanMapOp.class );
        xs.processAnnotations( ImageOpChain.class );
        xs.processAnnotations( ColorCurve.class );
        xs.registerConverter( new ImageOpChainXmlConverter( xs.getMapper() ) );
        String xml = xs.toXML( chain );
        ImageOpChain chain2 = (ImageOpChain) xs.fromXML( xml );
        String xml2 = xs.toXML( chain2 );
        assertEquals( xml, xml2 );
    }

    @Test
    public void testXml2() {
        String xml= "<crop rot=\"0.0\" minx=\"0.0584958217270195\" maxx=\"0.807799442896936\" miny=\"0.17130919220055713\" maxy=\"0.9066852367688023\"/>";
        XStream xs = new XStream();
        xs.processAnnotations( ImageOp.class );
        xs.processAnnotations( DCRawOp.class );
        xs.processAnnotations( DCRawMapOp.class );
        xs.processAnnotations( CropOp.class );
        xs.processAnnotations( ChanMapOp.class );
        xs.processAnnotations( ImageOpChain.class );
        xs.processAnnotations( ColorCurve.class );
        xs.registerConverter( new ImageOpChainXmlConverter( xs.getMapper() ) );
        CropOp crop = (CropOp) xs.fromXML( xml );
        assertNotNull( crop.getInputPorts() );

    }


    @Test
    public void testLegacyHelpers() throws PhotovaultException {
        ImageOpChain chain = new ImageOpChain();
        RawSettingsFactory rf = new RawSettingsFactory();
        rf.setDaylightMultipliers( new double[] {0.5, 1.0,0.5});
        rf.setBlack( 12 );
        rf.setColorTemp( 5500 );
        rf.setGreenGain( 1.0 );
        rf.setHlightComp( 1.0 );
        RawConversionSettings rs = rf.create();
        chain.applyRawConvSetting( rs );
        RawConversionSettings rs2 = chain.getRawConvSettings();
        assertEquals( rs, rs2 );
        DCRawOp dcraw = (DCRawOp) chain.getOperation( "dcraw" );
        DCRawMapOp rawmap = (DCRawMapOp) chain.getOperation( "raw-map" );
        assertTrue( rawmap.getInputPort( "in" ).getSource() == dcraw.getOutputPort( "out" ) );
        verifyChain( chain );

        assertEquals( "raw-map.out", chain.getHead() );

        chain.applyCropping( new Rectangle2D.Double( 0.1, 0.2, 0.7, 0.6 ) );
        chain.applyRotation( 5.0 );

        CropOp crop = (CropOp) chain.getOperation( "crop" );
        assertEquals( 5.0, crop.getRot() );
        assertEquals( 0.2, crop.getMinY() );
        assertEquals( "crop.out", chain.getHead() );
        assertTrue( crop.getInputPort( "in" ).getSource() == rawmap.getOutputPort( "out" ) );
        verifyChain( chain );

        ChannelMapOperationFactory cf = new ChannelMapOperationFactory();
        ColorCurve val = new ColorCurve();
        val.addPoint( 0.0, 1.0 );
        val.addPoint( 1.0, 0.0 );
        cf.setChannelCurve( "value", val );
        chain.applyChanMap( cf.create() );
        verifyChain( chain );
    }

    @Test
    public void testBeanAccess() throws IllegalAccessException, InvocationTargetException, InvocationTargetException, NoSuchMethodException {
        ImageOpChain chain = new ImageOpChain();
        DCRawOp dcrawOp = new DCRawOp();
        dcrawOp.setBlack( 20 );
        PropertyUtils.setProperty( chain, "operation(dcraw)", dcrawOp );
        PropertyUtils.setProperty( chain, "operation(dcraw).white", 10000 );
        DCRawMapOp mapOp = new DCRawMapOp();
        mapOp.setEvCorr( -1.0 );
        PropertyUtils.setProperty( chain, "operation(raw-map)", mapOp );
        PropertyUtils.setProperty( chain, "connection(raw-map.in)", "dcraw.out" );
        assertEquals( 10000, dcrawOp.getWhite() );
        assertEquals( dcrawOp.getOutputPort( "out" ), mapOp.getInputPort( "in" ).getSource() );
    }

    private void verifyChain( ImageOpChain chain ) {
        Set<String> namesNotFound = new HashSet<String>( chain.operations.keySet() );
        String src = chain.getHead();
        while ( src != null ) {
            ImageOp nextOp = chain.getOutputPort( src ).op;
            namesNotFound.remove( nextOp.getName() );
            ImageOp.Sink sink = nextOp.getInputPort( "in" );
            src = ( sink != null ) ? sink.getSource().getPortName() : null;
        }

        if ( namesNotFound.size() > 0 ) {
            fail( "Did not encounter operations: " + namesNotFound );
        }
    }
}

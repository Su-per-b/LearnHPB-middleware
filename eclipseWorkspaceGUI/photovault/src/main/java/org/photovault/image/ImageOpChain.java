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
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.dcraw.RawSettingsFactory;

/**
 * ImageOpChain describes the image processing operations that are applied to
 * original iamge to achieve desired result.
 * @author Harri Kaimio
 * @since 0.6.0
 */
@XStreamAlias( "processing" )
public class ImageOpChain {

    public ImageOpChain() {
    }

    /**
     * Copy constructor. Makes deep copy of the given chain
     * @param chain
     */
    public ImageOpChain( ImageOpChain chain ) {
        // Copy operations
        for ( Map.Entry<String, ImageOp> e : chain.operations.entrySet() ) {
            ImageOp op = e.getValue().createCopy();
            setOperation( e.getKey(), op );
        }
        head = chain.head;
        for ( Map.Entry<String,String> e : chain.sources.entrySet() ) {
            setConnection( e.getKey(), e.getValue() );
        }
    }

    /**
     * Operations that are part of this chain, keyed by their name
     */
    @XStreamOmitField
    Map<String, ImageOp> operations = new HashMap<String, ImageOp>();

    /**
     * Map from name of input port to name of the output it is connected
     */
    Map<String, String> sources = new HashMap<String, String>();

    /**
     * Map from name of output port to set of input port names it is connected
     * to.
     */
    Map<String, Set<String>> sinks = new HashMap<String, Set<String>>();
    public String head;

    /**
     * Get name of head of this chain
     * @return
     */
    public String getHead() {
        return head;
    }

    /**
     * Set head of the chain
     * @param outputName Name of the output port that is to be used as head
     */
    public void setHead( String outputName ) {
        head = outputName;
    }

    /**
     * Get name of output prot that is connected to given input
     * @param sinkName Name of the input port
     * @return Name of output connected to sinkName, or null if none.
     */
    public String getConnection( String sinkName ) {
        ImageOp.Sink sink = getInputPort( sinkName );
        return (sink == null ) ? null : sink.getSourceName();
    }

    /**
     * Connect given sink and source together
     * @param sinkName Name of the sink
     * @param sourceName Name of the soruce
     */
    public void setConnection( String sinkName, String sourceName ) {
        String oldSource = sources.get( sinkName );
        sources.put( sinkName, sourceName );
        Set<String> sinksOfSource = sinks.get( sourceName );
        if ( sinksOfSource == null ) {
            sinksOfSource = new HashSet<String>();
            sinks.put( sourceName, sinksOfSource );
        }
        sinksOfSource.add( sinkName );

        if ( oldSource != null ) {
            sinks.get( oldSource ).remove( sinkName );
        }

    }

    ImageOp.Source getSource( ImageOp.Sink sink ) {
        ImageOp.Source ret = null;
        String sourceName = sources.get( sink.getPortName() );
        if ( sourceName != null ) {
            ret = getOutputPort( sourceName );
        }
        return ret;
    }

    Set<ImageOp.Sink> getSinks( ImageOp.Source source ) {
        Set<ImageOp.Sink> ret = new HashSet<ImageOp.Sink>();
        Set<String> sinkNames = sinks.get( source.getPortName() );
        if ( sinkNames != null ) {
            for ( String s : sinkNames ) {
                ret.add( getInputPort( s ) );
            }
        }
        return ret;
    }

    /**
     * Disconnect sink and soruce
     * @param sourceName Name of the source
     * @param sinkNameName of the sink
     * @throws IllegalArgumentException if either of the ports is not known
     * @throws IllegalStateException if the ports are not connected
     */
    public void removeConnection( String sourceName, String sinkName ) {
        ImageOp.Source src = getOutputPort( sourceName );
        if ( src == null ) {
            throw new IllegalArgumentException( sourceName + " not known!" );
        }
        ImageOp.Sink sink = getInputPort( sinkName );
        if ( sink == null ) {
            throw new IllegalArgumentException( sinkName + " not known!" );
        }
        if ( sink.getSource() != src ) {
            throw new IllegalStateException(
                    sourceName + " is not connected to " + sinkName );
        }
        sink.setSource( null );
    }

    /**
     * Get operation by its name
     * @param name Name of the operation
     * @return Operation with given name or <code>null</code> if it does not
     * exist
     */
    public ImageOp getOperation( String name ) {
        return operations.get( name );
    }


    public void addOperation( ImageOp op ) {
        operations.put( op.getName(), op );
        op.chain = this;
    }

    /**
     * Add operation to the chain
     * @param name Name of the operation
     * @param op The operation to add
     */
    public void setOperation( String name, ImageOp op ) {
        op.setName( name );
        operations.put( name, op );
        op.chain = this;
    }

    public ImageOp.Sink getInputPort( String name ) {
        String parts[] = name.split( "\\." );
        if ( parts.length != 2 ) {
            throw new IllegalArgumentException( name + " is not a valid sink name" );
        }
        ImageOp op = getOperation( parts[0] );
        ImageOp.Sink sink = op.getInputPort( parts[1] );
        return sink;
    }

    public ImageOp.Source getOutputPort( String name ) {
        String parts[] = name.split( "\\." );
        if ( parts.length != 2 ) {
            throw new IllegalArgumentException( name + " is not a valid sink name" );
        }
        ImageOp op = getOperation( parts[0] );
        ImageOp.Source src = op.getOutputPort( parts[1] );
        return src;
    }

    static XStream xs = null;

    private synchronized static XStream getXStream() {
        if ( xs == null ) {
            xs = new XStream();
            initXStream( xs );
      }
        return xs;
    }

    /**
     * Initialize an XStream XML converter to recognize element types & conerters
     * associated with ImageOpChain.
     * @param xstream
     */
    public static void initXStream( XStream xstream ) {
        xstream.processAnnotations( ImageOp.class );
        xstream.processAnnotations( DCRawOp.class );
        xstream.processAnnotations( DCRawMapOp.class );
        xstream.processAnnotations( CropOp.class );
        xstream.processAnnotations( ImageOpChain.class );
        xstream.processAnnotations( ChanMapOp.class );
        xstream.processAnnotations( ColorCurve.class );
        xstream.registerConverter( new ImageOpChainXmlConverter( xstream.getMapper() ) );
    }

    public String getAsXml() {
        return getXStream().toXML( this );
    }

    public static ImageOpChain fromXml( String xml ) {
        return (ImageOpChain) getXStream().fromXML( xml );
    }

    /**
     * Helper function to convert {@link RawConversionSettings} into operations
     * in this chain.
     * @param rawSettings
     */
    public void applyRawConvSetting( RawConversionSettings rawSettings ) {
        if ( rawSettings != null ) {
            // Copy state to processing graph
            DCRawOp rawConv = (DCRawOp) getOperation( "dcraw" );
            if ( rawConv == null ) {
                rawConv = new DCRawOp( this, "dcraw" );
            }
            rawConv.setBlueGreenRatio( rawSettings.getBlueGreenRatio() );
            rawConv.setDaylightBlueGreenRatio( rawSettings.
                    getDaylightBlueGreenRatio() );
            rawConv.setDaylightRedGreenRatio( rawSettings.
                    getDaylightRedGreenRatio() );
            rawConv.setHlightRecovery( rawSettings.getHlightRecovery() );
            rawConv.setMedianFilterPassCount( rawSettings.getMedianPassCount() );
            rawConv.setRedGreenRatio( rawSettings.getRedGreenRatio() );
            rawConv.setWaveletThreshold( rawSettings.getWaveletThreshold() );

            DCRawMapOp mapping = (DCRawMapOp) getOperation(
                    "raw-map" );
            if ( mapping == null ) {
                mapping = new DCRawMapOp();
                mapping.setName( "raw-map" );
                addOperation( mapping );
                mapping.setBlack( rawSettings.getBlack() );
                mapping.setWhite( rawSettings.getWhite() );
                mapping.setEvCorr( rawSettings.getEvCorr() );
                mapping.setHlightCompr( rawSettings.getHighlightCompression() );
                setConnection( "raw-map.in", "dcraw.out"  );
            }
//            mapping.getInputPort( "in" ).setSource(
//                    rawConv.getOutputPort( "out" ) );
            ImageOp cropOp = getOperation( "crop" );
            ImageOp chanOp = getOperation( "chan-map" );
            if ( chanOp != null ) {
                setConnection( "chan-map.in", "raw-map.out" );
//                chanOp.getInputPort( "in" ).setSource(
//                        mapping.getOutputPort( "out" ) );
            } else if ( cropOp != null ) {
                setConnection( "crop.in", "raw-map.out" );
//                cropOp.getInputPort( "in" ).setSource(
//                        mapping.getOutputPort( "out" ) );
            } else {
                setHead( "raw-map.out" );
            }
        }
    }


    /**
     * Helper functions that converts raw conversion realted nodes in this chain
     * into {@link RawConversionSettings}
     * @return
     */
    public RawConversionSettings getRawConvSettings() {
        DCRawOp dcraw = (DCRawOp) getOperation( "dcraw" );
        DCRawMapOp mapping = (DCRawMapOp) getOperation( "raw-map" );
        if ( dcraw == null || mapping == null ) {
            return null;
        }
        RawSettingsFactory rf = new RawSettingsFactory();
        rf.setBlack( mapping.getBlack() );
        rf.setWhite( mapping.getWhite() );
        rf.setDaylightMultipliers( new double[] {
            dcraw.getDaylightRedGreenRatio(), 1.0,
            dcraw.getDaylightBlueGreenRatio() } );
        rf.setBlueGreenRatio( dcraw.getBlueGreenRatio() );
        rf.setEvCorr( mapping.getEvCorr() );
        rf.setHlightComp( mapping.getHlightCompr() );
        rf.setHlightRecovery( dcraw.getHlightRecovery() );
        rf.setMedianPassCount( dcraw.getMedianFilterPassCount() );
        rf.setRedGreenRation( dcraw.getRedGreenRatio() );
        rf.setWaveletThreshold( (float) dcraw.getWaveletThreshold() );
        try {
            return rf.create();
        } catch ( PhotovaultException ex ) {
            return null;
        }
    }

    /**
     * Helper function to apply Photovault style cropping to this chain
     * @param cropBounds Crop bounds.
     */
    public void applyCropping( Rectangle2D cropBounds ) {
        CropOp crop = (CropOp) getOperation( "crop" );
        if ( crop == null ) {
            crop = createCropOp();
        }
        crop.setMinX( cropBounds.getMinX() );
        crop.setMaxX( cropBounds.getMaxX() );
        crop.setMinY( cropBounds.getMinY() );
        crop.setMaxY( cropBounds.getMaxY() );
    }

    public void applyRotation( double rot ) {
        CropOp crop = (CropOp) getOperation( "crop" );
        if ( crop == null ) {
            crop = createCropOp();
        }
        crop.setRot( rot );
    }

    public Rectangle2D getCropping() {
        CropOp crop = (CropOp) getOperation( "crop" );
        if ( crop == null ) {
            return new Rectangle2D.Double( 0.0, 0.0, 1.0, 1.0 );
        }
        return new Rectangle2D.Double( crop.getMinX(), crop.getMinY(),
                crop.getMaxX() - crop.getMinX(),
                crop.getMaxY() - crop.getMinY() );
    }

    public double getRotation() {
        CropOp crop = (CropOp) getOperation( "crop" );
        return ( crop != null ) ? crop.getRot() : 0.0;
    }


    private CropOp createCropOp() {
        CropOp crop = new CropOp();
        crop.setName( "crop" );
        addOperation( crop );
        String oldHead = getHead();
        if ( oldHead != null ) {
            setConnection( "crop.in", oldHead );
//             crop.getInputPort( "in" ).setSource( oldHead );
        }
        setHead( "crop.out" );
        return crop;
    }

    /**
     * Heper function to create {@link ChanMapOp} node based on
     * {@link ChannelMapOperation}
     * @param map
     */
    public void applyChanMap( ChannelMapOperation map ) {
        if ( map == null ) {
            return;
        }
        ChannelMapOperationFactory cf = new ChannelMapOperationFactory();
        ChanMapOp mapOp = (ChanMapOp) getOperation( "chan-map" );
        if ( mapOp != null ) {
            mapOp.channels.clear();
        } else {
            mapOp = new ChanMapOp( this, "chan-map" );
            ImageOp dmapOp = (DCRawMapOp) getOperation( "raw-map" );
            Set<String> nextOps = new HashSet<String>();
            if ( dmapOp != null ) {
                nextOps = dmapOp.getOutputPort( "out" ).getSinkNames();
                setConnection( "chan-map.in", "raw-map.out" );
//                mapOp.getInputPort( "in" ).setSource( dmapOp.getOutputPort( "out") );
            }
            if ( nextOps.size() > 0 ) {
                for ( String sink : nextOps ) {
                    setConnection(  sink, "chan-map.out" );
//                    sink.setSource( mapOp.getOutputPort( "out" ) );
                }
            } else {
                setHead( "chan-map.out");
            }
        }
        for ( String chan : map.getChannelNames() ) {
            mapOp.setChannel( chan, map.getChannelCurve( chan ) );
        }
    }


    public ChannelMapOperation getChanMap() {
        ChanMapOp map = (ChanMapOp) getOperation( "chan-map" );
        if ( map == null ) {
            return null;
        }

        ChannelMapOperationFactory cf = new ChannelMapOperationFactory();
        for ( Map.Entry<String,ColorCurve> e : map.channels.entrySet() ) {
            cf.setChannelCurve( e.getKey(), e.getValue() );
        }
        return cf.create();
    }

    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( !obj.getClass().equals( ImageOpChain.class ) ) {
            return false;
        }
        ImageOpChain other = (ImageOpChain) obj;
        if ( !operations.equals( other.operations ) ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return operations.hashCode();
    }
    
}


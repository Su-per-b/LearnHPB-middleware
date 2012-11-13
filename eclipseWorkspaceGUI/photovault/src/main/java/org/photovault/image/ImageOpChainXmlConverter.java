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

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import org.photovault.image.ImageOp.Sink;
import org.photovault.image.ImageOp.Source;

/**
 * XStream converter for {@link ImageOpChain} objects to XML and back
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class ImageOpChainXmlConverter extends AbstractCollectionConverter {

    public ImageOpChainXmlConverter( Mapper mapper ) {
        super( mapper );
    }

    @Override
    public boolean canConvert( Class clazz ) {
        return clazz.equals( ImageOpChain.class );
    }

    @Override
    public void marshal( Object obj, HierarchicalStreamWriter writer,
            MarshallingContext ctx ) {
        ImageOpChain chain = (ImageOpChain) obj;
        if ( chain.head != null ) {
            writer.addAttribute( "output",
                    chain.head );
        }
        for ( ImageOp op : chain.operations.values() ) {
            writer.startNode( "op" );
            writer.addAttribute(
                    "class", mapper().serializedClass( op.getClass() ) );
            writer.addAttribute( "name", op.getName() );
            ctx.convertAnother( op );
            writer.endNode();
        }
        // Convert the connetions
        for ( ImageOp op : chain.operations.values() ) {
            for ( ImageOp.Sink sink : op.getInputPorts().values() ) {
                ImageOp.Source source = sink.getSource();
                if ( source != null ) {
                    writer.startNode( "connection" );
                    writer.addAttribute( "source",
                            source.op.getName() + "." + source.name );
                    writer.addAttribute( "sink",
                            sink.op.getName() + "." + sink.name );
                    writer.endNode();
                }
            }
        }
    }

    @Override
    public Object unmarshal( HierarchicalStreamReader reader,
            UnmarshallingContext ctx ) {
        ImageOpChain chain = new ImageOpChain();
        String outputName = reader.getAttribute( "output" );
        while ( reader.hasMoreChildren() ) {
            reader.moveDown();
            String nodeName = reader.getNodeName();
            if ( "op".equals( nodeName ) ) {
                Class clazz =
                        mapper().realClass( reader.getAttribute( "class" ) );
                if ( !ImageOp.class.isAssignableFrom( clazz ) ) {
                    throw new IllegalStateException(
                            clazz.getCanonicalName() + " is not an ImageOp" );
                }
                String name = reader.getAttribute( "name" );
                ImageOp op = (ImageOp) ctx.convertAnother( ctx, clazz );
                op.initPortMaps();
                chain.setOperation( name, op );
            } else if ( "connection".equals( nodeName ) ) {
                String source = reader.getAttribute( "source" );
                String sinkName = reader.getAttribute( "sink" );
                String[] sourcePath = source.split( "\\." );
                String[] sinkPath = sinkName.split( "\\." );
                if ( sourcePath.length != 2 ) {
                    throw new IllegalStateException(
                            source + "is not valid port name" );
                }
                ImageOp sourceOp = chain.getOperation( sourcePath[0] );
                Source sourcePort = sourceOp.getOutputPort( sourcePath[1] );
                if ( sinkPath.length != 2 ) {
                    throw new IllegalStateException(
                            sinkName + "is not valid port name" );
                }
                ImageOp sinkOp = chain.getOperation( sinkPath[0] );
                Sink sinkPort = sinkOp.getInputPort( sinkPath[1] );
                sinkPort.setSource( sourcePort );
            }
            reader.moveUp();
        }
        chain.head = outputName;

        return chain;
    }
}

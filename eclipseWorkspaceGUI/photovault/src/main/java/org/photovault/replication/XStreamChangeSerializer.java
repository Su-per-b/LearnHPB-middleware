/*
  Copyright (c) 2008 Harri Kaimio

  This file is part of Photovault.

  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even therrore implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package org.photovault.replication;

import com.thoughtworks.xstream.XStream;
import java.io.UnsupportedEncodingException;

/**
 * ChangeSerializer that uses XStream to serialize a change as UTF-8 coded XML.
 * The default implementation uses custom XML converter for classes in this
 * package. Everything else is serialized using XStream defaults.<p>
 * Derived classes can add custom serialization code by configuring the xstream
 * instance in their constructor. After construction, the xstream instance must
 * not be changed as it is global to all application threads and it is not thread
 * when other methods than toXML/fromXML are called.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class XStreamChangeSerializer implements ChangeSerializer {

    /**
     * XStream isntance used for serializing.
     */
    private XStream xstream;

    /**
     * Constructor. Derived classes should override this and configure xstream
     * in their constructors to add any custom XML conversion.
     */
    public XStreamChangeSerializer() {
        xstream = new XStream();
        xstream.alias( "change", ChangeDTO.class );
        xstream.registerConverter( new ChangeDtoXmlConverter( xstream.getMapper() ) );
        xstream.alias( "value-change", ValueChange.class );
        xstream.registerConverter( new ValueChangeXmlConverter( xstream.getMapper() ) );
        xstream.alias( "set-change", SetChange.class );
        xstream.registerConverter( new SetChangeXmlConverter( xstream.getMapper() ) );
        xstream.addImplicitCollection( SetChange.class, "addedItems" );
        xstream.addImplicitCollection( SetChange.class, "removedItems" );
    }

    /**
     * Get the xstream instance
     * @return
     */
    final protected XStream getXStream() {
        return xstream;
    }

    final public byte[] serializeChange( ChangeDTO dto ) {
        try {
            return getXStream().toXML( dto ).getBytes( "utf-8" );
        } catch ( UnsupportedEncodingException ex ) {
            throw new RuntimeException( "Cannot find utf-8 character set", ex );
        }
    }

    final public ChangeDTO deserializeChange( byte[] serialized ) {
        try {
            String xml = new String( serialized, "utf-8" );
            return (ChangeDTO) getXStream().fromXML( xml );
        } catch ( UnsupportedEncodingException ex ) {
            throw new RuntimeException( "Cannot find utf-8 character set", ex );
        }
    }

}

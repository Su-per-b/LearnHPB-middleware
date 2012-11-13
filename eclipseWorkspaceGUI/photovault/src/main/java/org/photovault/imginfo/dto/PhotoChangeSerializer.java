/*
  Copyright (c) 2008-2009 Harri Kaimio

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


package org.photovault.imginfo.dto;

import com.thoughtworks.xstream.XStream;
import org.photovault.image.ImageOpChain;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.replication.XStreamChangeSerializer;

/**
 * Serializer for converting {@link ImageFileDTO} objects to XML. In addition
 * to the basic converters defined by the base class, it defines additional
 * converters for {@link ImageFileDTO}, {@link ImageDescriptorDTO} and its
 * subclasses, {@link OrigImageRefDTO} and some composite types used in
 * {@link PhotoInfo} fields.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class PhotoChangeSerializer extends XStreamChangeSerializer {
    public PhotoChangeSerializer() {
        super();
        XStream xstream = getXStream();
        xstream.processAnnotations( ImageFileDTO.class );
        xstream.processAnnotations( ImageDescriptorDTO.class );
        xstream.processAnnotations( OrigImageDescriptorDTO.class );
        xstream.processAnnotations( CopyImageDescriptorDTO.class );
        xstream.processAnnotations( OrigImageRefDTO.class );
        ImageOpChain.initXStream( xstream );
    }

}

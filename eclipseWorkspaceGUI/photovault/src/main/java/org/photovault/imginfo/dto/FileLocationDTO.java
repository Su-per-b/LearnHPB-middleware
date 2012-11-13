/*
  Copyright (c) 2010 Harri Kaimio

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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.photovault.imginfo.FileLocation;
import org.photovault.imginfo.VolumeBase;

/**
 * Data transfer object that describes {@link FileLocation}
 * @author Harri Kaimio
 * @since 0.6.0
 */
@XStreamAlias( "location" )
public class FileLocationDTO {
    @XStreamAsAttribute
    private UUID volumeId;
    @XStreamAsAttribute
    private String volumeType;
    @XStreamAsAttribute
    private String location;
    @XStreamAsAttribute
    private String lastModified;
    static private DateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );

    public FileLocationDTO( FileLocation l ) {
        VolumeBase v = l.getVolume();
        volumeId = v.getId();
        volumeType= v.getClass().getName();
        location = l.getFname();
        long lastModifiedMsec = l.getLastModified();
        lastModified = df.format( new Date( lastModifiedMsec ) );
    }

    /**
     * @return the volumeId
     */
    public UUID getVolumeId() {
        return volumeId;
    }

    /**
     * @return the volumeType
     */
    public String getVolumeType() {
        return volumeType;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the lastModified
     */
    public String getLastModified() {
        return lastModified;
    }


}

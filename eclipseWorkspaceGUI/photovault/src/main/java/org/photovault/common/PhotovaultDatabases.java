/*
  Copyright (c) 2006-2007 Harri Kaimio
  
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


package org.photovault.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Collection;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Iterator;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.Volume;

/**
 * This is a collection of available photovault databases.
 * @author Harri Kaimio
 */
public class PhotovaultDatabases {
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( PhotovaultDatabases.class.getName() );

    HashMap<String, PVDatabase> databases;
    
    /** Creates a new instance of PhotovaultDatabases */
    public PhotovaultDatabases() {
        databases = new HashMap<String, PVDatabase>();
    }
    
    public void addDatabase( PVDatabase db ) throws PhotovaultException {
        if ( databases.containsKey( db.getName() ) ) {
            throw new PhotovaultException( "Database " + db.getName() + " already exists!" );
        }
        databases.put( db.getName(), db );
    }
    
    public PVDatabase getDatabase( String dbName ) {
        return (PVDatabase) databases.get( dbName );
    }
    
    public Collection<PVDatabase> getDatabases() {
        return databases.values();
    }

    void save(BufferedWriter outputWriter ) throws IOException {
        String indent = "  ";
        outputWriter.write( indent + "<databases>\n" );
        Iterator iter = databases.values().iterator();
        while( iter.hasNext() ) {
            PVDatabase db = (PVDatabase) iter.next();
            db.writeXml( outputWriter, 4 );
        }
        outputWriter.write( indent + "</databases>\n" );
    }
}

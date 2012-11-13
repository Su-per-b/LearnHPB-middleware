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

package org.photovault.imginfo.xml;

/**
    This interface can receive status information about XML import progress.
 */
public interface XmlImportListener {
    
    /**
     Called when XML importing status changes, i.e.
     <ul>
     <li>Importing is started</li>
     <li>Importing is completed</li>
     <li>Importing is moving to different phase</li>
     </ul>
     @param importer The XmlImporter object that initiated the event
     @param status New status code, see {@link XmlImporter} documentation for 
     details.
     */
    public void xmlImportStatus( XmlImporter importer, int status );

    /**
     Called if an error occurs during importing.
     @param exporter The XmlImporter object that initiated the event
     @param message Error message
     */
    public void xmlImportError( XmlImporter exporter, String message );
    
    /**
     Called when a object has been read from XML file
     @param xmlImporter The XmlImporter object that initiated the event
     @param obj The object just importer
     */
    void xmlImportObjectImported(XmlImporter xmlImporter, Object obj);


}

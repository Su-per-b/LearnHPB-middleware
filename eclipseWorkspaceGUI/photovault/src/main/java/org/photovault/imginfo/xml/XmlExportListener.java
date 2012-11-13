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
    This interface can receive status information about XML export progress.
 */
public interface XmlExportListener {
    
    /**
     Called when XML exporting status changes, i.e.
     <ul>
     <li>Exporting is started</li>
     <li>Exporting is completed</li>
     <li>Exporting is moving to different phase</li>
     </ul>
     @param exporter The XmlExporter object that initiated the event
     @param status New status code, see {@link XmlExporter} documentation for 
     details.
     */
    public void xmlExportStatus( XmlExporter exporter, int status );

    /**
     Called if an error occurs during exporting.
     @param exporter The XmlExporter object that initiated the event
     @param message Error message
     */
    public void xmlExportError( XmlExporter exporter, String message );
    
    /**
     Called when a object is written to XML file
     @param exporter The XmlExporter object that initiated the event
     @param obj The object exported
     */
    public void xmlExportObjectExported( XmlExporter exporter, Object obj );
}

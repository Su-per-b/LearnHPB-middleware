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

import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import org.photovault.common.PhotovaultSettings;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.folder.PhotoFolder;
import org.photovault.image.ChannelMapOperation;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoQuery;

/**
 This class exports metadata from the whole database or part of it into a XML
 file.
 */
public class XmlExporter {
    
    /** Creates a new instance of XmlExporter */
    public XmlExporter( BufferedWriter writer ) {
        this.writer = writer;
    }
    
    /**
     Indentation currently used
     */
    int indent = 0;
    
    /**
     Wtire that is used for writing the XML data.
     */
    BufferedWriter writer = null;
    
    
    /**
     Number of photos exported
     */
    int photoCount = 0;

    /**
     Number of folders exported
     */
    int folderCount = 0;

    /**
     Number of instances exported
     */
    int instanceCount = 0;
    
    int totalPhotoCount = 0;
    
    /**
     Get the number of exported folders
     @return Number of folders exported during the ongoing operation
     */
    public int getExportedFolderCount() {
        return folderCount;
    }
    
    /**
     Get the number of exported photos
     @return Number of photos exported during the ongoing operation
     */
    public int getExportedPhotoCount() {
        return photoCount;
    }
    
    /**
     Get the total number of photos to export
     @return While export operation is ongoing, the total number of photos to 
     export. In other situations, return value is undefined.
     */
    
    public int getTotalPhotoCount() {
        return totalPhotoCount;
    }
    
    Set listeners = new HashSet();
    
    // State values
    
    /**
     Invalid state
     */
    public final static int EXPORTER_STATE_INVALID = 0;

    /**
     This object is currently exporting folder hierarchy
     */
    public final static int EXPORTER_STATE_EXPORTING_FOLDERS = 1;

    /**
     This object is currently exporting photos
     */
    public final static int EXPORTER_STATE_EXPORTING_PHOTOS = 2;

    /**
     The export operation has completed
     */
    public final static int EXPORTER_STATE_COMPLETED = 3;

    /**
     No export operation has started.
     */
    public final static int EXPORTER_STATE_NOT_STARTED = 4;
        
    /**
     Ask that a {@link XmlExportListener} should be notified of events in this 
     exporter.
     @param l The object that should be notified of events
     */
    public void addListener( XmlExportListener l ) {
        listeners.add( l );
    }
    
    /**
     Ask that a {@link XmlExportListener} should not anymore be notified of events in this 
     exporter.
     @param l The object that should be removed form listeners.
     */    
    public void removeListener( XmlExportListener l ) {
        listeners.remove( l );
    }
    
    /**
     Inform listeners about status change to this listener
     */
    private void fireStatusChangeEvent( int status ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            XmlExportListener l = ( XmlExportListener ) iter.next();
            l.xmlExportStatus( this, status );
        }
    }
    
    /**
     Inform listeners about error in exporting
     @param errorMsg The error message that is sent to listeners
     */
    private void fireErrorEvent( String errorMsg ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            XmlExportListener l = ( XmlExportListener ) iter.next();
            l.xmlExportError( this, errorMsg );
        }
    }
    
    /**
     Inform listeners that an object has been exported
     @param obj the exported object
     */
    private void fireObjectExportedEvent( Object obj ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            XmlExportListener l = ( XmlExportListener ) iter.next();
            l.xmlExportObjectExported( this, obj );
        }
    }

    /**
     Get the number of exported instances
     @return Number of instances exported during the ongoing operation
     */
    public int getExportedInstanceCount() {
        return instanceCount;
    }
    
    /**
     Get the indentation that should be used
     @return String consisting of {@link indent} spaces
     */
    private String getIndent() {
        String spaces = "                                                                             ";
        return spaces.substring( 0, indent );
    }
    
    /**
     Write selected subset of folder hierarchy to file
     @param folders Set of folders to write.
     @throws IOException if error occurs during writing
     */
    public void writeFolders( Set folders ) throws IOException {
        fireStatusChangeEvent( EXPORTER_STATE_EXPORTING_FOLDERS );
        Set remainingFolders = new HashSet( folders );
        PhotoFolder rootFolder = PhotoFolder.getRoot();
        writer.write( getIndent() + "<folders root-uuid=\"" + 
                rootFolder.getUuid() + "\">" );
        writer.newLine();
        indent += 2;
        writeFolder( rootFolder, remainingFolders, true );
        indent -= 2;
        writer.write( getIndent() + "</folders>" );
        writer.newLine();
    }

    /**
     Write the database as XML
     @throws IOException if error occurs during writing
     */
    public void write( ) throws IOException {
        writer.write( "<?xml version='1.0' ?>\n" );
        writer.write( "<!--\n  This data was exported from Photovault database\n-->");
        writer.newLine();
        writer.write( "<photovault-data version=\"0.5.0\">" );
        writer.newLine();
        indent += 2;
        PhotovaultSettings settings = PhotovaultSettings.getSettings();
        writer.write( getIndent() + "<originator export-time=\"" + new Date().getTime() + "\"/>" );
        writer.newLine();
        writeFolders( new HashSet() );
        
        PhotoQuery query = new PhotoQuery();
        Vector photos = new Vector();
        for ( int n = 0; n < query.getPhotoCount(); n++ ) {
            photos.add( query.getPhoto( n ) );
        }
        totalPhotoCount = query.getPhotoCount();
        writePhotos( photos );
        indent -= 2;
        writer.write( getIndent() + "</photovault-data>" );
        writer.newLine();
        fireStatusChangeEvent( EXPORTER_STATE_COMPLETED );
    }
    
    /**
     Create a XML cdata section from a string. If there is an end marker (]]> in 
     the string, replace it with ] ]>
     @param text the original string
     @return text wrapped inside CDATA markers
     */
    private String cdata( java.lang.String text ) {
        int endmarkerStart = 0;
        if ( text == null ) {
            text = "";
        }
        while ( ( endmarkerStart = text.indexOf( "]]>") ) >= 0 ) {
            text = text.substring( 0, endmarkerStart ) + "] ]>" 
                    + text.substring( endmarkerStart+3 );
        }
        return "<![CDATA[" + text + "]]>";
        
    }
    
    /**
     Writes XML element describing a single folder and its subfolders
     @param folder Folder to write
     @param remainingFolders Set of remaining folders, removes the written 
     folders from the set.
     @param writeParentId Write parent id for the topmost folder. Parent ID
     is NOT written for other folders (it should be obvious from the XML
     structure.
     @throws IOException if error occurs during writing
     */
    private void writeFolder(PhotoFolder folder, Set remainingFolders, boolean writeParentId) throws IOException {
        UUID folderUUID = folder.getUuid();
        writer.write( getIndent() + "<folder id=\"" + folderUUID + "\"" );
        if ( writeParentId ) {
            PhotoFolder parent = folder.getParentFolder();
            if ( parent != null ) {
                writer.write( " parent-id=\"" + parent.getUuid() + "\"" );
            }
        }
        Date createTime = folder.getCreationDate();
        if ( createTime != null ) {
            writer.write( " created=\"" + createTime.getTime() + "\"" );
        }
        writer.write( ">\n" );
        indent += 2;
        writer.write( getIndent() + "<name>" + cdata( folder.getName() ) + "</name>\n" );
        writer.write( getIndent() + "<description>" + cdata( folder.getDescription() ) + "</description>\n" );
        remainingFolders.remove( folder );
        for ( int n = 0 ; n < folder.getSubfolderCount(); n++ ) {
            writeFolder( folder.getSubfolder( n ), remainingFolders, false );
        }
        indent -= 2;
        writer.write( getIndent() + "</folder>\n" );
        folderCount++;
        fireObjectExportedEvent( folder );
    }
    
    private void writePhotos( Collection photos ) throws IOException {
        fireStatusChangeEvent( EXPORTER_STATE_EXPORTING_PHOTOS );
        Iterator iter = photos.iterator();
        writer.write( getIndent() + "<photos>" );
        writer.newLine();
        indent += 2;
        while ( iter.hasNext() ) {
            PhotoInfo p = ( PhotoInfo ) iter.next();
            writePhoto( p );
        }
        indent -= 2;
        writer.write( getIndent() + "</photos>" );
        writer.newLine();
    }
    
    /**
     Writes an XML element that describes a single photo
     @param p The photo to write
     @throws IOException if error occurs during writing     
     */
    private void writePhoto( PhotoInfo p ) throws IOException {
        writer.write( getIndent() + "<photo id=\"" + p.getUuid() + "\"" );
        writer.write( " modified=\"" + p.getLastModified() + "\">" );
        writer.newLine();
        indent += 2;
        Date shootTime = p.getShootTime();
        if ( shootTime != null ) {
            double timeAccuracy = p.getTimeAccuracy();
            long timeAccuracyMsecs = (long)( 24*3600000*timeAccuracy );
            writer.write( getIndent() + "<shoot-time time=\"" + shootTime.getTime() +
                    "\" accuracy=\"" + timeAccuracyMsecs + "\"/>");
            writer.newLine();
        }
        writer.write( getIndent() + "<shooting-place>" + cdata( p.getShootingPlace() ) + 
                "</shooting-place>" );
        writer.newLine();
        writer.write( getIndent() + "<photographer>" + cdata( p.getPhotographer() ) + "</photographer>" );
        writer.newLine();
        writer.write( getIndent() + "<camera>" + cdata( p.getCamera() ) + "</camera>" );
        writer.newLine();
        writer.write( getIndent() + "<lens>" + cdata( p.getLens() ) + "</lens>" );
        writer.newLine();
        writer.write( getIndent() + "<f-stop>" + p.getFStop() + "</f-stop>" );
        writer.newLine();
        writer.write( getIndent() + "<shutter-speed>" + p.getShutterSpeed() + "</shutter-speed>" );
        writer.newLine();
        writer.write( getIndent() + "<focal-length>" + p.getFocalLength() + "</focal-length>" );
        writer.newLine();
        writer.write( getIndent() + "<film>" + cdata( p.getFilm() ) + "</film>" );
        writer.newLine();
        writer.write( getIndent() + "<film-speed>" + p.getFilmSpeed() + "</film-speed>" );
        writer.newLine();
        writer.write( getIndent() + "<orig-fname>" + cdata( p.getOrigFname() ) + "</orig-fname>" );
        writer.newLine();
        writer.write( getIndent() + "<description>" + cdata( p.getDescription() ) + "</description>" );
        writer.newLine();
        writer.write( getIndent() + "<tech-notes>" + cdata( p.getTechNotes() ) + "</tech-notes>" );
        writer.newLine();
        writer.write( getIndent() + "<quality>" + p.getQuality() + "</quality>" );
        writer.newLine();
        writer.write( getIndent() + "<crop rot=\"" + p.getPrefRotation() + "\" " );
        Rectangle2D c = p.getCropBounds();
        writer.write( "xmin=\"" + c.getMinX() + "\" " );
        writer.write( "xmax=\"" + c.getMaxX() + "\" " );
        writer.write( "ymin=\"" + c.getMinY() + "\" " );
        writer.write( "ymax=\"" + c.getMaxY() + "\"/>" );
        writer.newLine();
        ChannelMapOperation cm = p.getColorChannelMapping();
        if ( cm != null ) {
            String chanMapXml = cm.getAsXml( indent );
            writer.write( chanMapXml );
        }
        RawConversionSettings rs = p.getRawSettings();
        if ( rs != null ) {
            writeRawSettings( rs );
        }
        // Write instances
        writer.write( getIndent() + "<instances>" );
        writer.newLine();
        indent += 2;
/*
 TODO: Store information about images
        for ( ImageInstance i : p.getInstances() ) {
            writeInstance( i );
        }
*/
        indent -= 2;
        writer.write( getIndent() + "</instances>" );
        writer.newLine();
        Collection folders = p.getFolders();
        if ( folders.size() > 0 ) {
            writer.write( getIndent() + "<folders>" );
            writer.newLine();
            indent += 2;
            Iterator iter = folders.iterator();
            while ( iter.hasNext() ) {
                PhotoFolder f = (PhotoFolder) iter.next();
                writer.write( getIndent() + "<folder-ref id=\"" + f.getUuid() + "\"/>" );
                writer.newLine();
            }
            indent -= 2;
            writer.write( getIndent() + "</folders>" );
            writer.newLine();
        }
        indent -= 2;
        writer.write( getIndent() + "</photo>" );
        writer.newLine();
        photoCount++;
        fireObjectExportedEvent( p );
    }
    
    /**
     Writes a RawConversionSettings object into XML file
     @param rs The raw settings object to write
     @throws IOException if an error occurs during writing.
     */
    private void writeRawSettings( RawConversionSettings rs ) throws IOException {
        writer.write( getIndent() + "<raw-conversion> " );
        writer.newLine();
        indent += 2;
        writer.write( getIndent() + "<whitepoint>" + rs.getWhite() + "</whitepoint>" );
        writer.newLine();
        writer.write( getIndent() + "<blackpoint>" + rs.getBlack() + "</blackpoint>" );
        writer.newLine();
        writer.write( getIndent() + "<ev-corr>" + rs.getEvCorr() + "</ev-corr>" );
        writer.newLine();
        writer.write( getIndent() + "<hlight-corr>" + rs.getHighlightCompression() + "</hlight-corr>" );
        writer.newLine();
        writer.write( getIndent() + "<wb-type>" + rs.getWhiteBalanceType() + "</wb-type>" );
        writer.newLine();
        writer.write( getIndent() + "<color-balance red-green-ratio=\"" + rs.getRedGreenRatio() );
        writer.write( "\" blue-green-ratio=\"" + rs.getBlueGreenRatio() + "\"/>" );
        writer.newLine();
        writer.write( getIndent() + "<daylight-color-balance red-green-ratio=\"" + rs.getDaylightRedGreenRatio() );
        writer.write( "\" blue-green-ratio=\"" + rs.getDaylightBlueGreenRatio() + "\"/>" );
        writer.newLine();
        indent -= 2;
        writer.write( getIndent() + "</raw-conversion>" );
        writer.newLine();
    }
}

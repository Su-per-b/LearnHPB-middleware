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

package org.photovault.imginfo;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.common.PVDatabase;
import org.photovault.common.PhotovaultSettings;

/**
   The class Volume presents a single volume. e.g. storage area for image files
*/

@Entity
@DiscriminatorValue( "volume" )
public class Volume extends VolumeBase {

    static Log log = LogFactory.getLog( Volume.class.getName() );


    /**
     * Creates a new volume.
     */
    public Volume() {
        super();
    }
    
    /**
     * Creates a new volume
     * @param volName Name of the volume
     * @param volBaseDir Root directory of the volume
     */
    public Volume( String volName, String volBaseDir ) {
        super( volName, volBaseDir );
    }
	

	
	
    /**
     * This function provides a filing name for a certain image. The name given will be 
     * 	based on last modified date of the photograph, which is supposed to match the 
     * 	shooting time in most cases.
     * @param imgFile File in the file system
     * @return Filing name for the file or <CODE>null</CODE> if it is not in the volume directory
     */

    public File getFilingFname( File imgFile ) {

	// Use the "last nodified" date  as the basis for the file name
	long mt = imgFile.lastModified();
	java.util.Date modTime = new java.util.Date( mt );

	// Find the file extension
	int extStart = imgFile.getName().indexOf( "." );
	String strExtension = imgFile.getName().substring( extStart+1 );
	return getNewFname( modTime, strExtension );
    }

    /**
     *       Constructs a file name that can be used as a name for an instance for a given photo
     * @param photo the photo whose isntance is to be created
     * @param strExtension String to use as extension for the file name
     * @return File name for the photo.
     */
    public File getInstanceName( PhotoInfo photo, String strExtension ) {
	java.util.Date d = photo.getShootTime();
	// shooting time can in practice be null, use current date in this case (Date is used just for readability)
	if ( d == null ) {
	    d = new java.util.Date();
	}
	return getNewFname( d, strExtension );
	
    }
    
    private File getNewFname( java.util.Date date, String strExtension ) {
	log.debug( "getNewFname " + date + " " + strExtension );
        SimpleDateFormat fmt = new SimpleDateFormat( "yyyy" );
	String strYear = fmt.format( date );
	fmt.applyPattern( "yyyyMM" );
	String strMonth = fmt.format( date );
	fmt.applyPattern( "yyyyMMdd" );
	String strDate = fmt.format( date );

	File yearDir = new File( getBaseDir(), strYear );
        log.debug( "YearDir: " + yearDir );
	if ( !yearDir.exists() ) {
	    log.debug( "making yeardir" );
            if ( !yearDir.mkdir() ) {
                log.error( "Failed to create directory " + yearDir.getAbsoluteFile() );
            }
	}

	// Create the month directeory if it does not exist yet
	File monthDir = new File ( yearDir, strMonth );
        log.debug( "MontDir: " + monthDir );
	if ( !monthDir.exists() ) {
	    log.debug( "making yeardir" );
	    if ( !monthDir.mkdir() ) {
                log.error( "Failed to create " + monthDir.getAbsolutePath() );
            }
	}

	// Find a free order num for this file
	String monthFiles[] = monthDir.list();
	int orderNum = 1;
	for ( int n = 0; n < monthFiles.length; n++ ) {
	    if ( monthFiles[n].startsWith( strDate ) ) {
		int delimiterLoc = monthFiles[n].indexOf( "." );
		String strFileNum = monthFiles[n].substring( strDate.length()+1, delimiterLoc );
		int i = 0;
		try {
		    i = Integer.parseInt( strFileNum );
		} catch ( NumberFormatException e ) {}
		if ( i >= orderNum ) {
		    orderNum = i+1;
		}
	    }
	}

	String strOrderNum = String.valueOf( orderNum );

	// Find the file extension
	String fname = strDate + "_"+
	    "00000".substring( 0, 5-strOrderNum.length())+ strOrderNum + "." + strExtension;
	File archiveFile = new File( monthDir, fname );
	return archiveFile;
	
    }

    /**
     * Maps a file name to a path in this volume. File names in a volume are 
     * unique but for performance reasons they are divided to several directories 
     * based on year and month of shooting
     * @return Path to the actual file
     * @param fname The file name to be mapped
     * @throws java.io.FileNotFoundException 
     */
    public File mapFileName( String fname ) throws FileNotFoundException {
	File yearDir = new File( getBaseDir(), fname.substring( 0, 4 ) );
	File monthDir = new File ( yearDir, fname.substring( 0, 6 ) );
	File archiveFile = new File( monthDir, fname );
        log.debug( "Mapped " + fname + " to " + archiveFile );
	if ( !archiveFile.exists() ) {
	    throw new FileNotFoundException( archiveFile.getPath() + " does not exist in volume" );
	}
	return archiveFile;
    }
    
    /**
     Normal volume is conceptually flat - all file names are unique so we will 
     just strip the path from file name.
     */
    public String mapFileToVolumeRelativeName( File f ) {
       return f.getName(); 
    }

    /**
     * Write the volume as XML
     * @param outputWriter Writer to use
     * @param indent indentation of the outermost element.
     * @throws java.io.IOException if writing fails.
     */
    public void writeXml(BufferedWriter outputWriter, int indent ) throws IOException {
        String s = "                                ".substring( 0, indent );
        outputWriter.write( s + "<volume name=\"" + getName() +
               "\" basedir=\"" + getBaseDir() + "\"/>\n" );
    }


}

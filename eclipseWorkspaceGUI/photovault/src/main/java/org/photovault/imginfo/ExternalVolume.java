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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.photovault.folder.PhotoFolder;

/**
 Extenal volume is a volume that resides outside Photovault repository, i.e. a
 normal user-created subdirectory of photos. Since it is not under control of 
 Photovault some of its characterestics differ from normal volumes:
 <ul>
 <li> Photovault will not create new instances it external volume
 <li> Photovault does not set or expect restrictions on how the directories
 and files are organized.
 </ul>
 @author Harri Kaimio
 */
@Entity
@DiscriminatorValue( "external_volume")
public class ExternalVolume extends VolumeBase {
    
    /**
     Default constructor
     */
    public ExternalVolume() {
        super();
    }
    
    /** Creates a new instance of. The ExternalVolume 
     @param volName Name of the volume
     @param baseDir The base directory of the external volume
     */
    public ExternalVolume( String volName, String baseDir ) {
        super( volName, baseDir );
    }
    
    /**
     *     getFilingFname should return that can be used for an instance of a certain file.
     *     However, Photovault cannot store anything in external volume so return null.
     * @param imageFile File whose filing name is requested
     * @return returns always <CODE>null</CODE>
     */
     
    public File getFilingFname(File imageFile) {
        return null;
    }

    /**
     *     Photovault cannot create new instances in external volume so return null
     * @param photo The photo
     * @param strExtension File type extension
     * @return always <CODE>null</CODE>
     */
    public File getInstanceName(PhotoInfo photo, String strExtension) {
        return null;
    }
    
    UUID folderId = null;
    
    /**
     *     Returns the ID of the folder that is used to represent this external volume. 
     *     If this external volume is not associated withny folder return <code>null</code>.
     * @return Id of he associated {@link PhotoFolder}
     * @deprecated In the new persistent framework, this should be replaced by nethod 
     * that returns the folder itself.
     */
    @Transient
    public UUID getFolderId() {
        return folderId;
    }
    
    /**
     Sets the id of the folder that is used to represent this volume.
     @param id The id of new folder of -1 to disassociate this volume from any 
     folder.
     */
    public void setFolderId( UUID id ) {
        folderId = id;
    }
    
    private PhotoFolder folder;
    
    @OneToOne
    @JoinColumn( name="folder_uuid", nullable = true )
    public PhotoFolder getFolder() {
        return folder;
    }
    
    public void setFolder( PhotoFolder f ) {
        folder = f;
    }

    /**
     * Write the object as XML
     * @param outputWriter The writer into which the object is written
     * @param indent Number of spaces to indent the outermost element.
     * @throws java.io.IOException If writing fails.
     */
    public void writeXml(BufferedWriter outputWriter, int indent ) throws IOException {
        String s = "                                ".substring( 0, indent );
        outputWriter.write( s+ "<external-volume name=\"" + getName() +
                "\" basedir=\"" + getBaseDir() +
                "\" folder=\"" + getFolderId().toString() + "\"/>\n" );
    }
    
}

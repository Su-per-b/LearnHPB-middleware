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

package org.photovault.imginfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.common.PhotovaultException;
import org.photovault.imginfo.VolumeBase;

/**
 <p>
 VolumeManager is a singleton class that manages information about volumes 
 that are currently available at some mount point. 
 </p>
 <p>
 As this is application wide information, the instance is not specific to any
 persistence context. Use methods {@link VolumeDAO} for getting instances of 
 volume that are bound to certain persistence context.
 
 @author Harri Kaimio
 @since 0.6
 */
public class VolumeManager {
    
    static private Log log = LogFactory.getLog( VolumeManager.class.getName() );
    
    /**
     Key for volume UUID in volume identification file     
     */
    static private String KEY_VOLUME_ID = "volume.id";
    /**
     Key for volume name in volume identification file     
     */
    static private String KEY_VOLUME_NAME = "volume.name";
    /**
     Key for volume class in volume identification file     
     */
    static private String KEY_VOLUME_CLASS = "volume.class";
    
    static private String VOL_INFO_DIR = ".photovault_volume";
    
    /**
     Mount points that are checked for volume.
     */
    Set<File> mountPoints = new HashSet<File>();

    /**
     Volumes known to be available and their locations.
     */
    Map<UUID, File> availableVolumes = new HashMap<UUID, File>();
    
    /**
     Constructs a new VolumeManager. As this class is singleton, use instance() 
     method to get the instance.
     */
    private VolumeManager() {
    
    }
    
    /**
     Singleton instance
     */
    static VolumeManager inst = new VolumeManager();
    
    /**
     Get the VolumeManager
     @return Instance of VolumeManager class
     */
    public static VolumeManager instance() {
        return inst;
    }
    
    /**
     Add a new mount piint that will be searched for volumes.
     @param mountPoint The new mount point
     */
    public void addMountPoint( File mountPoint ) {
        mountPoints.add( mountPoint );
    }
    
    /**
     Remove an existing volume mount point
     @param mountPoint The mount point that will be removed
     */
    public void removeMountPoint( File mountPoint ) {
        mountPoints.remove( mountPoint );
    }
    
    /**
     Get all mount points that are searched for volumes
     
     @return Collection of mount points
     */
    public Collection<File> getMountPoints() {
        return mountPoints;
    }
    
    /**
     Get a volume that is currently mounted in given directory. 
     <p>
     The method reads the identification file in the directory and returns a 
     {@link VolumeBase} derived object that matches it. If volDAO parameter is 
     not <code>null</code>, Photovault queries database in that persistence 
     context to find existing persistent instance, and if not found, creates 
     and persists a new object. If volDAO is <code>null</code>, the method 
     returns a transient instance and makes no check whether the volume is 
     already known.
     
     @param dir Base directory for the volume
     @param volDAO Volume DAO object that is used for querying database and 
     persisting created volume object.
     @return VolumeBase object if basedir is a volume or <code>null</code> 
     otherwise. 
     @throws java.io.FileNotFoundException If volume identification file cannot
     be opened
     @throws java.io.IOException If read error occurs
     */
    public VolumeBase getVolumeAt( File dir, VolumeDAO volDAO ) 
            throws FileNotFoundException, IOException {
        VolumeBase vol = null;
        File volInfoDir = new File( dir, VOL_INFO_DIR );
        if ( volInfoDir.exists() )  {
            for ( File f: volInfoDir.listFiles() ) {
                if ( f.getName().startsWith( "volume" ) && f.getName().endsWith(".id" ) )  {
                    InputStream propStream = new FileInputStream( f );
                    Properties volProps = new Properties();
                    volProps.load( propStream );
                    String strId = volProps.getProperty( KEY_VOLUME_ID );
                    if ( strId != null ) {
                        UUID id = UUID.fromString( strId );
                        if ( volDAO != null )  {
                            vol = volDAO.getVolume( id );
                        }
                        if ( vol == null ) {
                            /*
                             The volume was not found previously for this database.
                             Create a new instance.
                             */
                            String volName = volProps.getProperty( KEY_VOLUME_NAME );
                            String volType = volProps.getProperty( KEY_VOLUME_CLASS );
                            
                            if ( volType.equals( "org.photovault.imginfo.Volume") ) {
                                vol = new Volume();
                            } else if ( volType.equals("org.photovault.imginfo.ExternalVolume") ) {
                                vol = new ExternalVolume();
                            } else {
                                log.error( "Unknown volume type: " + volType );
                                return null;
                            }
                            vol.setId( id );
                            vol.setName(volName);
                            if ( volDAO != null ) {
                                volDAO.makePersistent( vol );
                                volDAO.flush();
                            }
                        }        
                    }
                    break;
                }
            }
        }
        if ( vol != null ) {
            registerVolumeMount( vol, dir );
        }
        return vol;
    }
    
    /**
     Find out the volume in which a given file belongs
     @param f The file
     @param volDAO VOlumeDAO that is used to fetch/create the returned volume 
     object
     @return Volume in which the file belongs. If volDAO is non-null, the returned 
     volume instance belongs to the same persistence context as volDAO. Otherwise 
     the isntance in non-persistent.
     */
    public VolumeBase getVolumeOfFile( File f, VolumeDAO volDAO )
            throws FileNotFoundException, IOException  {
        if ( f.isDirectory() ) {
            VolumeBase vol = getVolumeAt( f, volDAO );
            if ( vol != null ) {
                return vol;
            }
        }
        File parent = f.getAbsoluteFile().getParentFile();
        if ( parent != null ) {
            return getVolumeOfFile( parent, volDAO );
        }
        return null;
    }
    
    /**
     Initializes a volume directory by storing photovault identification 
     information to it.
     
     @param vol Volume instance that represents the directory
     @param basedir Base directory of initialized volume
     @throws org.photovault.common.PhotovaultException If the directory is already
     initialized as Photovault volume or the identification file cannot be written.
     */
    public void initVolume( VolumeBase vol, File basedir ) throws PhotovaultException {
        if ( !basedir.exists() ) {
            basedir.mkdir();
        }
        File confDir = new File( basedir, VOL_INFO_DIR );
        if (confDir.exists()) {
            // This seems to be an existing volume
            log.warn("Attempting to create a new volume in " + basedir +
                    "but volume exists there already");
            try {
                VolumeBase v = getVolumeAt(basedir, null );
                if ( v != null ) {
                    throw new PhotovaultException( basedir.getAbsolutePath() + 
                            " is already initialized for volume " + vol.getId() );
                }
            } catch ( Exception e ) {
                throw new PhotovaultException( 
                        "Error fecthing existing volume configuration: " + 
                        e.getMessage(), e );
            } 
         
        } else {
            confDir.mkdir();
        }
        
        Properties volProps = new Properties();
        volProps.setProperty( KEY_VOLUME_ID, vol.getId().toString() );
        volProps.setProperty( KEY_VOLUME_NAME, vol.getName() );
        volProps.setProperty(KEY_VOLUME_CLASS, vol.getClass().getName() );
        File propFile = new File( confDir, 
                String.format( "volume_%s.id", vol.getId().toString() ) );
        OutputStream propStream = null;
        try  {
            propStream = new FileOutputStream( propFile );
            volProps.store(propStream, 
                "This file identifies a photo directory for Photovault. Do not remove!" );
        } catch ( Exception e ) {
            throw new PhotovaultException( "Error writing volume configuration: " +
                    e.getMessage(), e );            
        } finally {
            if ( propStream != null ) {
                try {
                    propStream.close();
                } catch (Exception e) {
                    throw new PhotovaultException(
                            "Error closing volume configuration file: " +
                            e.getMessage(), e);
                }
            }
        }
        registerVolumeMount( vol, basedir );
    }
    
    /**
     Register that a volume is mounted in certain directory.
     @param vol The mounted volume
     @param mountDir The root directory of the volume
     */
    private void registerVolumeMount( VolumeBase vol, File mountDir ) {
        availableVolumes.put( vol.getId(), mountDir );        
    }
    
    /**
     Remove volume from the collection of mounted volumes
     @param vol The volume that is no longer accessible.
     */
    private void unregisterVolumeMount( VolumeBase vol ) {
        unregisterVolumeMount( vol.getId());
    }
        
    
    /**
     Remove volume from the collection of mounted volumes
     @param volId UUID of the volume that is no longer accessible.
     */
    private void unregisterVolumeMount( UUID volId ) {
        availableVolumes.remove( volId );
    }
    
    
    /**
     Get root directory of a volume
     @param vol The volume
     @return Directory where the volume is available of <code>null</code> if it 
     is not.
     */
    public File getVolumeMountPoint( VolumeBase vol ) {
        return availableVolumes.get( vol.getId() );
    }

    
    public void updateVolumeMounts() {
        updateVolumeMounts( null );
    }

    public void updateVolumeMounts( VolumeDAO volDao ) {
        Set<UUID> oldVolumes = new HashSet<UUID>( availableVolumes.keySet() );
        for ( File dir : mountPoints ) {
            try {
                VolumeBase v = getVolumeAt( dir, volDao );
                oldVolumes.remove( v.getId() );
            } catch ( Exception e ) {
                log.warn( "Exception in updateVolumeMounts: " + e.getMessage() );
            }
        }

        // Unregister volumes that were not seen
        for ( UUID volId : oldVolumes ) {
            unregisterVolumeMount( volId );
        }
    }
}

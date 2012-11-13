/*
  Copyright (c) 2006 Harri Kaimio
  
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

package org.photovault.dcraw;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class describes a color profile used by Photovault raw conversion.
 * The color profiles are stored in Photovault volumes, and a profile described
 * by an object of this class can be present in many volumes. Each instance is 
 * described by an object of class {@link ColorProfileInstance}.
 
 * @author Harri Kaimio
 * @since 0.4
 */
public class ColorProfileDesc {
    static private final Log log = LogFactory.getLog( ColorProfileDesc.class.getName() );
    
    /**
     * Creates a new instance of ColorProfileDesc
     */
    public ColorProfileDesc() {
    }
    
    /**
     * Name of this ICC profile
     */
    private String name = null;
    
    /**
     * Free-text description if  this profile
     */
    private String description = null;
    
    /**
     * Cource color space of this profile
     */
    private int srcCS = 0;
    
    /**
     * Target color space of this profile
     */
    private int targetCS = 0;
    
    /**
     * MD5 hash of this profile
     */
    private byte[] hash = null;
    
    Vector instances = null;
    
    int id;
    
    UUID uuid;

    /**
     * Get the name
     * @return Name of this profile
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this profile
     * @param name New name of the profile
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description
     * @return Description of this profile
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of this profile
     * @param description New description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public int getSrcCS() {
        return srcCS;
    }

    public void setSrcCS(int srcCS) {
        this.srcCS = srcCS;
    }

    public int getTargetCS() {
        return targetCS;
    }

    public void setTargetCS(int targetCS) {
        this.targetCS = targetCS;
    }

    /**
     * Get the hash of this profile
     * @return MD hash of the ICC profile hash
     */
    public byte[] getHash() {
        return (hash != null) ? hash.clone() : null;
    }

    /**
     * Set the hash
     * @param hash New MD5 hash
     */
    private void setHash(byte[] hash) {
        this.hash = hash;
    }
    
    /**
     * Get an existing color profile with a given ID
     * @param id ID of hte color profile to search
     * @return The color profile with a given ID or <CODE>null</CODE> if no such profile 
     * is found.
     */
    static public ColorProfileDesc getProfileById( int id ) {
        throw new UnsupportedOperationException( " ColorProfileDesc#getProfileById not supported in Hibernate!!" );
    }
    
    /**
     * Get all known ICC prodiles
     * @return Collection containing all known profiles
     @todo Implement with Hibernate
     */
    static public Collection getAllProfiles() {
//        log.debug( "Fetching all color profiles" );
//        String oql = "select colorProfiles from " + ColorProfileDesc.class.getName() + 
//                " where id > 0";
       List profiles = null;
//        
//        // Get transaction context
//        ODMGXAWrapper txw = new ODMGXAWrapper();
//        Implementation odmg = ODMG.getODMGImplementation();
//        
//        try {
//            OQLQuery query = odmg.newOQLQuery();
//            query.create( oql );
//            txw.flush();
//            profiles = (List) query.execute();
//            txw.commit();
//        } catch (Exception e ) {
//            log.warn( "Error fetching records: " + e.getMessage() );
//            e.printStackTrace();
//            txw.abort();
//            return null;
//        }
        return profiles;
    }

    private void addInstance(ColorProfileInstance i) {
        if ( instances == null ) {
            instances = new Vector();
        }
        instances.add( i );
    }
    
    /**
     Get an ICC file with this color profile
     @return An accessible file with the correct ICC profile of <code>null</code>
     if no such file is available.
     */
    public File getInstanceFile() {
        File ret = null;
        Iterator iter = instances.iterator();
        while ( iter.hasNext() ) {
            ColorProfileInstance i = (ColorProfileInstance) iter.next();
            File cand = i.getProfileFile();
            if ( cand.exists() ) {
                ret = cand;
                break;
            }
        }
        return ret;
    }
    
    /**
     * An action used for creating a new ICC profile. Profiles should be created 
     * with this action since it is anticipated that after distributed database
     * support is added this will be the only allowed method.
     */
    public  static class CreateProfile {
        
        File profileFile;
        String name;
        String description;
        
        /**
         * Create a new CreateProfile action
         * @param f File containing the ICC profile.
         * @param name Name of the profile.
         * @param desc Description of the profile.
         */
        public CreateProfile( File f, String name, String desc ) {
            profileFile = f;
            this.name = name;
            this.description = desc;
        }
        
        /**
         * Creates the profile
         * @return The created profile
         */
        public ColorProfileDesc execute() {
            throw new UnsupportedOperationException( "Color profiles not supported in Hibernate" );
        }
    }
    
    /**
     * Action to change a color profile
     */
    public static class ChangeProfile {
        ColorProfileDesc p;
        String newName = null;
        String newDesc = null;
        
        /**
         * Create a new ChangeProfile action.
         * @param p Profile that will be changed
         */
        public ChangeProfile( ColorProfileDesc p ) {
            this.p = p;
        }
        
        /**
         * Set new name for the profile
         * @param name New name for the profile
         */
        public void setName( String name ) {
            newName = name;            
        }
        
        /**
         * Set new description for the profile
         * @param desc The new description
         */
        public void setDesc( String desc ) {
            newDesc = desc;
        }
        
        /**
         * Do the changes to profile.
         */
        public void execute() {
            throw new UnsupportedOperationException( "Color profiles not supported in Hibernate" );
        }
    }
    
}

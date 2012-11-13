/*
  Copyright (c) 2008 Harri Kaimio
 
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

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPSchemaRegistry;
import com.adobe.xmp.options.PropertyOptions;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.folder.PhotoFolder;
import org.photovault.image.ChannelMapOperation;
import org.photovault.imginfo.xml.Base64;
import org.photovault.persistence.DAOFactory;
import org.photovault.replication.ObjectHistory;
import org.photovault.replication.ObjectHistoryDTO;

/**
 Utility class for converting metadata between Photovault database format and 
 XMP.
 
 @author Harri Kaimio
 @since 0.6.0
 */
public class XMPConverter {

    private static Log log = LogFactory.getLog( XMPConverter.class.getName() );
    
    // XMP namespaces
    
    /**
     XMP Basic namespace
     */
    static private final String NS_XMP_BASIC = "http://ns.adobe.com/xap/1.0/";

    /**
     Dublin Core namespace
     */
    static private final String NS_DC = "http://purl.org/dc/elements/1.1/";

    /**
     XMP Media Management namespace
     */
    static private final String NS_MM = "http://ns.adobe.com/xap/1.0/mm/";

    /**
     XMP EXIF tag namespace
     */
    static private final String NS_EXIF = "http://ns.adobe.com/exif/1.0/";

    /**
     XMP auxiliary EXIF namespace
     */
    static private final String NS_EXIF_AUX = "http://ns.adobe.com/exif/1.0/aux/";

    /**
     XMP EXIF TIFF specific tag namespace
     */
    static private final String NS_TIFF = "http://ns.adobe.com/tiff/1.0/";
    
    /**
     Photovault XMP extensions namespace
     */
    static private final String NS_PV = "http://ns.photovault.org/xmp/1.0/";

    /**
     * Photoshop XMP schema namespace
     */
    static private final String NS_PHOTOSHOP = "http://ns.adobe.com/photoshop/1.0";

    private DAOFactory df;
    
    /**
     Constructor
     @param df DAOFactory to fetch connected objects from database
     */
    public XMPConverter( DAOFactory df ) {
        this.df = df;
    }

    /**
     Create XMP metadata based on given photo and image file
     @param f The image file, used to initialize media management information
     @param p The photo used to initialize descriptive fields
     @return An XMP metadata object initialized based on the given information
     @throws com.adobe.xmp.XMPException If an error occurs while creating the 
     metadata
     */
    public XMPMeta getXMPMetadata( ImageFile f, PhotoInfo p ) throws XMPException {
        XMPMeta meta = XMPMetaFactory.create();
        XMPSchemaRegistry reg = XMPMetaFactory.getSchemaRegistry();

        // Check for Photovault schemas
        if ( reg.getNamespacePrefix( NS_PV ) == null ) {
            try {
                reg.registerNamespace( NS_PV, "pv" );
            } catch ( XMPException e ) {
                log.error( "CMPException: " + e.getMessage() );
            }
        }

        if ( reg.getNamespacePrefix( NS_PHOTOSHOP ) == null ) {
            try {
                reg.registerNamespace( NS_PHOTOSHOP, "photoshop" );
            } catch ( XMPException e ) {
                log.error( "CMPException: " + e.getMessage() );
            }

        }

        byte[] data = null;
        try {
            URI ifileURI = new URI( "uuid", f.getId().toString(), null );
            meta.setProperty( NS_MM, "InstanceID", ifileURI.toString() );
            meta.setProperty( NS_MM, "Manager", "Photovault 0.5.0dev" );
            meta.setProperty( NS_MM, "ManageTo", ifileURI.toString() );
        } catch ( URISyntaxException ex ) {
            log.error( ex );
        }
        CopyImageDescriptor firstImage = (CopyImageDescriptor) f.getImage( "image#0" );
        OriginalImageDescriptor orig = firstImage.getOriginal();
        String rrNS = reg.getNamespaceURI( "stRef" );
        try {
            URI origURI =
                    new URI( "uuid", orig.getFile().getId().toString(), orig.getLocator() );
            meta.setStructField( NS_MM, "DerivedFrom",
                    rrNS, "InstanceID", origURI.toString() );
        } catch ( URISyntaxException ex ) {
            log.error( ex );
        }
        meta.setStructField( NS_MM, "DerivedFrom", NS_PV, "Rotation",
                Double.toString( firstImage.getRotation() ) );
        Rectangle2D cropArea = firstImage.getCropArea();
        meta.setStructField( NS_MM, "DerivedFrom", NS_PV, "XMin",
                Double.toString( cropArea.getMinX() ) );
        meta.setStructField( NS_MM, "DerivedFrom", NS_PV, "XMax",
                Double.toString( cropArea.getMaxX() ) );
        meta.setStructField( NS_MM, "DerivedFrom", NS_PV, "YMin",
                Double.toString( cropArea.getMinY() ) );
        meta.setStructField( NS_MM, "DerivedFrom", NS_PV, "YMax",
                Double.toString( cropArea.getMaxY() ) );
        ChannelMapOperation cm = firstImage.getColorChannelMapping();
        if ( cm != null ) {
            try {
                ByteArrayOutputStream cms = new ByteArrayOutputStream();
                ObjectOutputStream cmos = new ObjectOutputStream( cms );
                cmos.writeObject( cm );
                String cmBase64 = Base64.encodeBytes( cms.toByteArray(),
                        Base64.GZIP | Base64.DONT_BREAK_LINES );
                meta.setStructField( NS_MM, "DerivedFrom", NS_PV, "ChannelMap",
                        cmBase64 );
            } catch ( IOException e ) {
                log.error( "Error serializing channel map", e );
            }
        }
        RawConversionSettings rs = firstImage.getRawSettings();
        if ( rs != null ) {
            try {
                ByteArrayOutputStream rss = new ByteArrayOutputStream();
                ObjectOutputStream rsos = new ObjectOutputStream( rss );
                rsos.writeObject( rs );
                String rsBase64 = Base64.encodeBytes( rss.toByteArray(),
                        Base64.GZIP | Base64.DONT_BREAK_LINES );
                meta.setStructField( NS_MM, "DerivedFrom", NS_PV, "RawConversion",
                        rsBase64 );
            } catch ( IOException e ) {
                log.error( "Error serializing raw settings", e );
            }
        }

        /*
        Set the image metadata based the photo we are creating this copy.
        There may be other photos associated with the origial image file,
        so we should store information about these in some proprietary part 
        of metadata.
         */
        meta.appendArrayItem( NS_DC, "creator",
                new PropertyOptions().setArrayOrdered( true ),
                p.getPhotographer(), null );
        meta.setProperty( NS_DC, "description", p.getDescription() );

        double fstop = p.getFStop();
        if ( fstop > 0.0 ) {
            String aperture = floatToRational( p.getFStop() );
            meta.setProperty( NS_EXIF, "ApertureValue", aperture );
            meta.setProperty( NS_EXIF, "FNumber", aperture );
        }
//        String film = photo.getFilm();

          int isoSpeed = p.getFilmSpeed();
          if ( isoSpeed > 0 ) {
              meta.appendArrayItem( NS_EXIF, "ISOSpeedRatings",
                      new PropertyOptions().setArrayOrdered( true ),
                      String.valueOf( isoSpeed ), null );
          }

          double focalLength = p.getFocalLength();
          if ( focalLength > 0.0 ) {
              meta.setProperty( NS_EXIF, "FocalLength", floatToRational( focalLength ) );
          }

          int quality = p.getQuality();
          double rating = 0.0;
          switch ( quality ) {
              case PhotoInfo.QUALITY_TOP :
                  rating = 5.0;
                  break;
              case PhotoInfo.QUALITY_GOOD :
                  rating = 4.0;
                  break;
              case PhotoInfo.QUALITY_FAIR :
                  rating = 3.0;
                  break;
              case PhotoInfo.QUALITY_POOR :
                  rating = 1.0;
                  break;
              case PhotoInfo.QUALITY_UNUSABLE :
                  rating = -1.0;
                  break;

          }
          meta.setPropertyDouble( NS_XMP_BASIC, "Rating", rating );

          /* XMP location needs to be formal hierachical place, so we will store
           * this as a keyword.
           */
          PropertyOptions subjectOptions = new PropertyOptions( PropertyOptions.ARRAY );
          String shootingPlace = p.getShootingPlace();
          if ( shootingPlace != null ) {
              meta.appendArrayItem( NS_DC, "subject",
                      subjectOptions, shootingPlace, null );
          }

          double expTime = p.getShutterSpeed();
          if ( expTime > 0.0 ) {
              String shutterSpeed = expTimeAsRational( expTime );
              meta.setProperty( NS_EXIF, "ExposureTme", shutterSpeed );
          }
//            photo.getTechNotes();


        Date shootDate = p.getShootTime();
        if ( shootDate != null ) {
            DateFormat dfmt = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
            String xmpShootDate = dfmt.format( shootDate );
            meta.setProperty( NS_XMP_BASIC, "CreateDate", xmpShootDate );
            meta.setProperty( NS_PHOTOSHOP, "DateCreated", xmpShootDate );
        }

        // Save technical data
        meta.setProperty( NS_TIFF, "Model", p.getCamera() );
        meta.setProperty( NS_EXIF_AUX, "Lens", p.getLens() );
        // TODO: add other photo attributes as well

        // Add names of the folders the photo belongs to as keywords
        for ( PhotoFolder folder : p.getFolders() ) {
            if ( folder.getExternalDir() == null ) {
                meta.appendArrayItem( NS_DC, "subject",
                        subjectOptions, folder.getName(), null );
            }
        }


        // Save the history of the image
        ObjectHistory<PhotoInfo> h = p.getHistory();
        ObjectHistoryDTO<PhotoInfo> hdto = new ObjectHistoryDTO<PhotoInfo>( h );
        ByteArrayOutputStream histStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream histoStream = new ObjectOutputStream( histStream );
            histoStream.writeObject( hdto );
            histoStream.flush();
            histStream.flush();
            byte histData[] = histStream.toByteArray();
            String histBase64 = Base64.encodeBytes( histData,
                    Base64.GZIP | Base64.DONT_BREAK_LINES );
            meta.setProperty( NS_PV, "History", histBase64 );
        } catch ( IOException e ) {
            log.warn( "Error serializing history", e );
        }
        return meta;
    }
    
    /**
     Updates a {@link PhotoInfo} object based on XMP metadata. 
     @param metadata The XMP metadata used to initialize or update the PhotoInfo
     @param pe editor for changing the PhotoInfo
     @throws com.adobe.xmp.XMPException If an error occurs while reading metadata
     */
    public void updatePhoto( XMPMeta metadata, PhotoEditor pe ) throws XMPException {
        String creator = (String) metadata.getArrayItem( NS_DC, "creator", 0 ).getValue();
        pe.setPhotographer( creator.trim() );
        String cameraMark = (String) metadata.getPropertyString( NS_TIFF, "Mark" );
        String cameraModel = (String) metadata.getPropertyString( NS_TIFF, "Model" );
        StringBuffer camera = new StringBuffer();
        if ( cameraMark != null ) {
            camera.append( cameraMark.trim() );
        }
        if ( cameraModel != null ) {
            if ( cameraMark != null ) {
                camera.append( " " );
            }
            camera.append( cameraModel );
        }        
        /*
         Use the default description
         */
        
        String description = (String) metadata.getArrayItem( NS_DC, "description", 0 ).getValue();
        if ( description != null ) {
            pe.setDescription( description.trim() );
        }
        Calendar c = metadata.getPropertyCalendar( NS_XMP_BASIC, "CreateDate" );
        Date d = c.getTime();
        pe.setFuzzyShootTime( new FuzzyDate( d, 0.0 ) );
    }

    /**
     * Simple method to convert aperture value in floating point into rational
     * number
     * @param f The floating point number
     * @return f as rational representation in string format.
     * E.g. floatToRational( 0.5 ) returns "1/2"
     */
    private String floatToRational( double f ) {
        long times10 = Math.round( f*10 );
        if ( times10 % 10 == 0 ) {
            return String.valueOf( f );
        }
        return String.valueOf( Math.round(f*10) ) + "/10";
    }
    private static int shutterSpeeds[] = {
            1, 2, 3, 4, 6, 8, 10, 15, 20, 30, 50, 60, 90, 125, 200, 250, 350,
            500, 1000, 2000, 4000, 8000 };

    private String expTimeAsRational( double expTime ) {
        for ( int sp : shutterSpeeds ) {
            double time = 1.0 / sp;
            if ( Math.abs( expTime - time )/time < 0.01 ) {
                return "1/" + sp;
            }
            if ( time < expTime ) {
                break;
            }
        }
        double denomin = 1.0;
        while ( Math.abs( Math.round( expTime * denomin ) / denomin - expTime ) >
                0.01 * expTime ) {
            denomin *= 10.0;
        }
        return String.valueOf( Math.round( expTime * denomin ) ) + "/" 
                + Math.round(denomin);

    }

}

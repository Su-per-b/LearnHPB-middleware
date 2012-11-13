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

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandException;
import org.photovault.command.DataAccessCommand;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawImage;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.PhotovaultImage;
import org.photovault.image.PhotovaultImageFactory;
import org.w3c.dom.NodeList;

/**
 Create a new copy image that matches the settings of a given photo.
 @author Harri Kaimio <harri@kaimio.fi>
 @since 0.6.0
 */
public class CreateCopyImageCommand  extends DataAccessCommand {
    
    static Log log = LogFactory.getLog( CreateCopyImageCommand.class );
    private PhotovaultImage img;
    
    /** 
     Creates a CreateCopyImageCommand that stores the image in a Photovault 
     volume.
     @param photo The photo from which the image is created
     @param vol Volume the image is stored in
     @param maxWidth Maximum width for the created image
     @param maxHeight Maximum height for the created image     
     */
    public CreateCopyImageCommand( PhotoInfo photo, Volume vol, 
            int maxWidth, int maxHeight ) {
        this( null, photo, vol, maxWidth, maxHeight );
           
    }

    /**
     Create a copy from already loaded image
     @todo This is somewhat dangerous since it gives public access to internal
     database structures (it is possible to give an erronneus image as a basis 
     for a copy) However, the performance gain is too big to be wasted.
     @param img The laoded PhotovaultImage
     @param photo The photo from which the image is created
     @param vol Volume the image is stored in
     @param maxWidth Maximum width for the created image
     @param maxHeight Maximum height for the created image     
     */
    public CreateCopyImageCommand( PhotovaultImage img, PhotoInfo photo, Volume vol, 
            int maxWidth, int maxHeight ) {
        this.img = img;
        if ( img != null ) {
            createFromOriginal = true;
        }
        photoUuid = photo.getUuid();
        volumeUuid = vol.getId();
        this.setVolume(vol);
        this.setMaxWidth(maxWidth);
        this.setMaxHeight(maxHeight);
    }
    
    /** 
     Creates a CreateCopyImageCommand that stores the image in a file outside of
     Photovault volume.
     @param photo The photo from which the image is created
     @param vol Volume the image is stored in
     @param maxWidth Maximum width for the created image
     @param maxHeight Maximum height for the created image     
     */
    public CreateCopyImageCommand( PhotoInfo photo, File dstFile, 
            int maxWidth, int maxHeight ) {
        photoUuid = photo.getUuid();
        this.dstFile = dstFile;
        this.setMaxWidth(maxWidth);
        this.setMaxHeight(maxHeight);
    }

    /**
     The volume into which the created file is stored.
     */
    private Volume volume;
    
    /**
    UUID of the volume in which the created image will be stored.
    */
    private UUID volumeUuid;
    
    /**
     Where the image is stored
     */
    private File dstFile;
    
    /**
     Photo descriptor that defines the settings used when creating the image
     */
    private PhotoInfo photo;
    
    /**
     UUID of the photo
     */
    private UUID photoUuid;
    
    /**
     Maximum width for the created image
     */
    private int maxWidth;
    /**
     Maximum height for the created image
     */
    private int maxHeight;
    
    /**
     If true, the image will be created from original. If false, also an existing 
     copy can be used.
     */
    private boolean createFromOriginal = true;
    
    /**
     If true, allow optimizations that trade image quality to processing time
     (e.g. subsampling when loading the image)
     */
    private boolean lowQualityAllowed = false;
    
    
    /**
     Operations that will be applied in the created image.
     */
    private Set<ImageOperations> operationsToApply = EnumSet.allOf( ImageOperations.class );

    /**
     Execute the command. 
     @throws CommandException If no image suitable for using as a source can be 
     found or if saving the created image does not succeed.
     */
    public void execute() throws CommandException {
        // Find the image used as source for the new instance
        PhotoInfoDAO photoDAO = daoFactory.getPhotoInfoDAO();
        VolumeDAO volDAO = daoFactory.getVolumeDAO();
        photo = photoDAO.findByUUID( photoUuid );
        
        Set<ImageOperations> operationsNotApplied = EnumSet.copyOf( operationsToApply );
        ImageDescriptorBase srcImageDesc = photo.getOriginal();
        // Find a suitable image for using as source if the original has not
        // yet been loaded.
        if ( img == null ) {
            ImageFile srcImageFile = srcImageDesc.getFile();
            File src = srcImageFile.findAvailableCopy();
            if ( src == null && !createFromOriginal ) {
                srcImageDesc = photo.getPreferredImage( EnumSet.noneOf( ImageOperations.class ),
                        operationsToApply, maxWidth, maxHeight,
                        Integer.MAX_VALUE, Integer.MAX_VALUE );
                if ( srcImageDesc != null ) {
                    srcImageFile = srcImageDesc.getFile();
                    src = srcImageFile.findAvailableCopy();
                    operationsNotApplied.removeAll( ((CopyImageDescriptor) srcImageDesc).getAppliedOperations() );
                }
            }
            if ( src == null ) {
                throw new CommandException( "No suitable image file found" );
            }

            // Create the image for the instance
            PhotovaultImageFactory imgFactory = new PhotovaultImageFactory();
            try {
                img = imgFactory.create( src, false, false );
            } catch ( PhotovaultException ex ) {
                throw new CommandException( ex.getMessage() );
            }
        }
        if ( operationsNotApplied.contains( ImageOperations.CROP ) ) {
            img.setCropBounds( photo.getCropBounds() );
            img.setRotation( photo.getPrefRotation() );
        }
        if ( operationsNotApplied.contains( ImageOperations.COLOR_MAP ) ) {
            ChannelMapOperation channelMap = photo.getColorChannelMapping();
            if ( channelMap != null ) {
                img.setColorAdjustment( channelMap );
            }
        }
        if ( operationsNotApplied.contains( ImageOperations.COLOR_MAP ) &&
                img instanceof RawImage ) {
            RawImage ri = (RawImage) img;
            ri.setRawSettings( photo.getRawSettings() );
        }

        RenderedImage renderedDst = 
                img.getRenderedImage( maxWidth, maxHeight, lowQualityAllowed );
        
        // Determine correct file name for the image & save it

        if ( volumeUuid != null ) {
            VolumeBase vol = volDAO.findById(volumeUuid, false);
            dstFile = vol.getInstanceName( photo, "jpg" );
        }
        if ( dstFile == null ) {
            throw new CommandException( "Either destination file or volume must be specified" );
        }

        ImageFileDAO ifDAO = daoFactory.getImageFileDAO();
        ImageFile dstImageFile = new ImageFile();
        ifDAO.makePersistent( dstImageFile );
        CopyImageDescriptor dstImage = new CopyImageDescriptor( dstImageFile, "image#0", photo.getOriginal() );
        ImageDescriptorDAO idDAO = daoFactory.getImageDescriptorDAO();
        idDAO.makePersistent( dstImage );
        if ( operationsToApply.contains( ImageOperations.COLOR_MAP ) ) {
            dstImage.setColorChannelMapping( photo.getColorChannelMapping() );
        }
        if ( operationsToApply.contains( ImageOperations.CROP ) ) {
            dstImage.setCropArea( photo.getCropBounds() );
            dstImage.setRotation( photo.getPrefRotation() );
        }
        if ( operationsToApply.contains( ImageOperations.RAW_CONVERSION ) ) {
            dstImage.setRawSettings( photo.getRawSettings() );
        }
        dstImage.setWidth( renderedDst.getWidth() );
        dstImage.setHeight( renderedDst.getHeight() );
        ((CopyImageDescriptor) dstImageFile.getImages().get( "image#0" )).setOriginal( photo.getOriginal() );
        byte[] xpmData = createXMPMetadata( dstImageFile );

        
        try {
            saveImage( dstFile, renderedDst, xpmData );
        } catch (PhotovaultException ex) {
            throw new CommandException( ex.getMessage() );
        } finally {
            img.dispose();
        } 

        /*
         Check if the resulting image file is already known & create a new one
         if not
         */
        byte[] hash = ImageFile.calcHash( dstFile );
        dstImageFile.setHash( hash );
        
        /*
         Store location of created file in database
         */
        if ( volume != null ) {
            dstImageFile.addLocation( new FileLocation( volume, 
                    volume.mapFileToVolumeRelativeName( dstFile ) ) );
        }
        
        /*
         Ensure that the photo is initialized in memory as it is used as a 
         detached object after closing our persistence context.         
         */
        if ( !photo.hasThumbnail() ) {
            log.error( "No valid thumbnail available!!!" );
        }
    }

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
    
    static private final String NS_PV = "http://ns.photovault.org/xmp/1.0/";
    
    /**
     Creates an XMP packet from associated data that can be added to saved copy 
     file. The data is currently mostly intended for informational purposes: no 
     strict sematics are defined. In future, it should be possible to transfer 
     files from one Photovault database to another without loss of information.
     
     @param ifile The ImageFile that is saved
     @return Binary XMP packet
     */
    private byte[] createXMPMetadata( ImageFile ifile ) {
        XMPConverter xmpconv = new XMPConverter( null );
        byte[] data = null;
        try {
            XMPMeta meta = xmpconv.getXMPMetadata( ifile, photo );
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            outStream.write( "http://ns.adobe.com/xap/1.0/".getBytes("utf-8" ) );
            outStream.write( 0 );
            XMPMetaFactory.serialize( meta, outStream );
            outStream.write( "<?xpacket end=\"w\"?>".getBytes( "utf-8" ) );
            data = outStream.toByteArray();
            log.debug( "XMP metadata:\n" + new String( data ) );
        
        
        } catch ( XMPException e ) {
            log.error( e );
        } catch ( IOException e ) {
            log.error( e );
        }

        return data;
    }

    /**
     Helper function to save a rendered image to file
     @param instanceFile The file into which the image will be saved
     @param img Image that willb e saved
     @param xmpData XPM metadata packet that should be saved with the image
     @throws PhotovaultException if saving does not succeed
     */
    protected void saveImage( File instanceFile, RenderedImage img, byte[] xmpData ) throws PhotovaultException {
        ImageOutputStream out = null;
        log.debug( "Entry: saveImage, file = " + instanceFile.getAbsolutePath() );
        try {
            out = new FileImageOutputStream( instanceFile );
        } catch(IOException e) {
            log.error( "Error writing image: " + e.getMessage() );
            throw new PhotovaultException( e.getMessage() );
        }
        if ( img.getSampleModel().getSampleSize( 0 ) == 16 ) {
            log.debug( "16 bit image, converting to 8 bits");
            double[] subtract = new double[1]; subtract[0] = 0;
            double[] divide   = new double[1]; divide[0]   = 1./256.;
            // Now we can rescale the pixels gray levels:
            ParameterBlock pbRescale = new ParameterBlock();
            pbRescale.add(divide);
            pbRescale.add(subtract);
            pbRescale.addSource( img );
            PlanarImage outputImage = (PlanarImage)JAI.create("rescale", pbRescale, null);
            // Make sure it is a byte image - force conversion.
            ParameterBlock pbConvert = new ParameterBlock();
            pbConvert.addSource(outputImage);
            pbConvert.add(DataBuffer.TYPE_BYTE);
            img = JAI.create("format", pbConvert);
        }
        
        IIOImage iioimg = new IIOImage( img, null, null );

        /*
         Not all encoders support metadata handling
         */
        Iterator writers = ImageIO.getImageWritersByFormatName( "jpeg" );
        ImageWriter imgwriter = null;
        while ( writers.hasNext() ) {
            imgwriter = (ImageWriter) writers.next();
            if ( imgwriter.getClass().getName().endsWith( "JPEGImageEncoder" ) ) {
                // Break on finding the core provider.
                break;
            }
        }
        if ( imgwriter == null ) {
            System.err.println( "Cannot find core JPEG writer!" );
        }
        imgwriter.addIIOWriteWarningListener( new IIOWriteWarningListener() {

            public void warningOccurred( ImageWriter arg0, int arg1, String arg2 ) {
                log.warn( "Warning from ImageWriter: " + arg2 );
            }
        });
        ImageWriteParam params = imgwriter.getDefaultWriteParam();
        ImageTypeSpecifier its =
            ImageTypeSpecifier.createFromRenderedImage(img);
        IIOMetadata metadata = imgwriter.getDefaultImageMetadata( its, null );

        IIOMetadataNode metatop =
                (IIOMetadataNode) metadata.getAsTree( "javax_imageio_jpeg_image_1.0" );
        NodeList markerSeqNodes = metatop.getElementsByTagName( "markerSequence" );
        if ( markerSeqNodes.getLength() > 0 ) {
            IIOMetadataNode xmpNode = new IIOMetadataNode( "unknown" );
            xmpNode.setAttribute("MarkerTag", "225" );
            xmpNode.setUserObject( xmpData );
            markerSeqNodes.item( 0 ).appendChild( xmpNode );
        }
        
        try {
            metadata.setFromTree( "javax_imageio_jpeg_image_1.0", metatop );
        } catch ( Exception e ) {
            log.warn( "error editing metadata: " + e.getMessage() );
            e.printStackTrace();
            throw new PhotovaultException( "error setting image metadata: \n" + e.getMessage() );
        }
        
        iioimg.setMetadata( metadata );
        
        try {
            imgwriter.setOutput( out );
            imgwriter.write( iioimg );
        } catch ( IOException e ) {
            log.warn( "Exception while encoding" + e.getMessage() );
            throw new PhotovaultException( "Error writing instance " +
                    instanceFile.getAbsolutePath() + ": " +
                    e.getMessage() );
        } finally {
            try {
                out.close();
            } catch ( IOException e ) {
                log.warn( "Exception while closing file: " + e.getMessage() );
                imgwriter.dispose();
                throw new PhotovaultException( "Error writing instance " +
                        instanceFile.getAbsolutePath() + ": " +
                        e.getMessage() );

            }
            imgwriter.dispose();
        }
        log.debug( "Exit: saveImage" );
    }
    
    /**
     Get the volume in which the image will be created.
     @return Volume in owning persistence context's scope.
     */
    public Volume getVolume() {
        return volume;
    }

    /**
     Set the volume in which the image will be created.
     @param The volume. This can be either detached instance or associated with
     a persistence context. if <code>null</code> the image is saved outside volume
     in file set by setDstFile()
     */
    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    /**
     Get the destination file in which the image will be saved.
     @return The file
     */
    public File getDstFile() {
        return dstFile;
    }

    /**
     The the file in which the image will be saved. Note that if both dstFile and 
     volume are set, the volume will take precedence.
     @param dstFile The file.
     */
    public void setDstFile(File dstFile) {
        this.dstFile = dstFile;
    }

    /**
     Get the photo whose image will be created.
     */
    public PhotoInfo getPhoto() {
        return photo;
    }
    
    /**
     Set the photo whose image willb e created.
     @param photo The photo. This can be either detached instance or associated with
     a persistence context.
     */
    public void setPhoto(PhotoInfo photo) {
        this.photo = photo;
    }

    /**
     Get maximum width of the created instance
     @return maximum width in pixels
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     Set the maximum width of the crated instance
     @param maxWidth Maximum width in pixels
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     Get maximum height of the created instance
     @return height width in pixels
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     Set the maximum height of the crated instance
     @param maxWidth Maximum height in pixels
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     Return whether the image must be created from original
     @return <code>true</code> if the iamge must be created from original, <code>
     false</code> if creation from suitable copy image is allowed. Default value is 
     <code>true</code>.
     */
    public boolean isCreateFromOriginal() {
        return createFromOriginal;
    }

    /**
     Set whether the image must be created from original
     @param crateFromOriginal <code>true</code> if the image must be created 
     from original, <code>false</code> if creation from suitable copy image is 
     allowed. Default value is <code>true</code>.
     */
    public void setCreateFromOriginal(boolean createFromOriginal) {
        this.createFromOriginal = createFromOriginal;
    }

    /**
     Get the operations that will be applied to the created image.
     @return set of operations that will be applied.
     */
    public Set<ImageOperations> getOperationsToApply() {
        return operationsToApply;
    }

    /**
     Set the operations that will be applied to the image
     @param operationsToApply Set op operations that will be applied to the 
     created image.
     */
    public void setOperationsToApply(Set<ImageOperations> operationsToApply) {
        this.operationsToApply = operationsToApply;
    }
    
    /**
     Are optimizations that trade image quality for performance allowed?
     @return <code>true</code> if yes, <code>false</code> otherwise.
     */
    public boolean isLowQualityAllowed() {
        return lowQualityAllowed;
    }
    
    /**
     Set whether optimizations that trade image quality for performance (e.g. 
     subsampling while loading image) are allowed or not
     @param b The new value for flag
     */
    public void setLowQualityAllowed( boolean b ) {
        lowQualityAllowed = b;
    }
}

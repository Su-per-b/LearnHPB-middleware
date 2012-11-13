/*
  Copyright (c) 2006 Harri Kaimio
  
  This file is part of Photovault.

  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even therrore implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
*/

package org.photovault.image;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.sun.media.imageio.plugins.tiff.BaselineTIFFTagSet;
import com.sun.media.imageio.plugins.tiff.EXIFParentTIFFTagSet;
import com.sun.media.imageio.plugins.tiff.EXIFTIFFTagSet;
import com.sun.media.imageio.plugins.tiff.TIFFDirectory;
import com.sun.media.imageio.plugins.tiff.TIFFField;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.BorderExtender;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderableOp;
import javax.media.jai.RenderedImageAdapter;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.operator.RenderableDescriptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Wrapper class for imaging pipeline for images that are read using JAI ImageIO
 */
public class ImageIOImage extends PhotovaultImage {
    static final private Log log = LogFactory.getLog( ImageIOImage.class.getName() );

    private int width = 0;

    private int height = 0;
    
    /**
     * Creates a new instance of ImageIOImage. Note that you should not normally use
     * this constructor directly but use {@link PhotovaultImageFactory} instead.
     */
    public ImageIOImage( File f ) {
        this.f = f;
    }
    
    public static ImageIOImage getImage( File f, boolean loadImage, boolean loadMetadata ) {
        if ( getImageReader( f ) == null ) {
            return null;
        }
        ImageIOImage i = new ImageIOImage( f );
        i.load( loadImage, loadMetadata, Integer.MAX_VALUE, Integer.MAX_VALUE, false );
        return i;
    }
    
    /**
     The loaded image file
     */
    PlanarImage image = null;
    RenderableOp renderableImage = null;
    
    /**
     Sample model of the loaded file
     */
    SampleModel originalSampleModel = null;
    /**
     Color model of the loaded file
     */
    ColorModel originalColorModel = null;
    
    /**
     If true, the loaded image stored in {@see image} is loaded with low quality.
     */
    boolean imageIsLowQuality = false;
    
    /**
     Get the image pixel data. If the iamge has not been read earlier, this method
     reads it from disk.
     @param minWidth The minimum size for the iamge to be loaded
     @param minHeight The minimum height for the loaded image
     @param isLowQualityAllowed if <code>true</code> the method may use shortcuts 
     that make tradeoff in image quality for improved performance or memory consumption
     (like increase subsampling)
     @return The image data as an RenderedImage.
     */
    public RenderableOp getCorrectedImage( int minWidth, int minHeight, boolean isLowQualityAllowed ) {
        if ( image == null ||
                (minWidth > image.getWidth() || minHeight > image.getHeight() ) ||
                ( imageIsLowQuality && !isLowQualityAllowed ) ) {
            load( true, (metadata == null), minWidth, minHeight, isLowQualityAllowed );
        }
        return renderableImage;
    }
    
    /**
     Get the sample model of the loaded image
     */
    public SampleModel getCorrectedImageSampleModel() {
        return originalSampleModel;
    }

    /**
     Get the color model of the loaded image
     */    
    public ColorModel getCorrectedImageColorModel() {
        return originalColorModel;
    }
    

    /**
     * Get the shooting time of the image
     * @return Shooting time as reported by dcraw or <CODE>null</CODE> if
     * not available
     */
    public Date getTimestamp() {
        Date ret = null;

        
        String origDateStr = getEXIFTagAsString( EXIFTIFFTagSet.TAG_DATE_TIME_ORIGINAL );
        if ( origDateStr == null ) {
            origDateStr = getEXIFTagAsString( BaselineTIFFTagSet.TAG_DATE_TIME );
        }
        if ( origDateStr != null ) {
            SimpleDateFormat df = new SimpleDateFormat( "yyyy:MM:dd HH:mm:ss");
            try {
                ret = df.parse( origDateStr );
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return ret;        
    }
    
    /**
     * Get the camera mode used to shoot the image
     * @return Camera model reported by dcraw
     */
    public String getCamera() {
        // Put here both camera manufacturer and model
        String maker = getEXIFTagAsString( BaselineTIFFTagSet.TAG_MAKE );        
        String model = getEXIFTagAsString( BaselineTIFFTagSet.TAG_MODEL );
        StringBuffer cameraBuf = new StringBuffer( maker != null ? maker : "" );
        if ( model != null ) {
            cameraBuf.append( " "). append( model );
        }
        return cameraBuf.toString();
    }
    
    /**
     * Get the film speed setting used when shooting the image
     * @return Film speed (in ISO) as reported by dcraw
     */
    public int getFilmSpeed() {
        return getEXIFTagAsInt( EXIFTIFFTagSet.TAG_ISO_SPEED_RATINGS );
    }
    
    /**
     * Get the shutter speed used when shooting the image
     * @return Exposure time (in seconds) as reported by dcraw
     */
    public double getShutterSpeed() {
        return getEXIFTagAsDouble( EXIFTIFFTagSet.TAG_EXPOSURE_TIME );
    }
    
    /**
     * Get aperture (f-stop) used when shooting the image
     * @return F-stop number reported by dcraw
     */
    public double getAperture() {
        return getEXIFTagAsDouble( EXIFTIFFTagSet.TAG_F_NUMBER );
    }
    
    /**
     * Get the focal length from image file meta data.
     * @return Focal length used when taking the picture (in millimetres)
     */
    public double getFocalLength() {
        return getEXIFTagAsDouble( EXIFTIFFTagSet.TAG_FOCAL_LENGTH );
    }
    
    
    TIFFDirectory metadata = null;
    TIFFDirectory exifData = null;

    /**
     * Get a TIFF metadata field
     * @param tag Numeric ID of the tag
     * @return TIFFField object describing the tag or <CODE>null</CODE> if the tag does not 
     * exist in the image.
     */
    private TIFFField getMetadataField( int tag ) {
        if ( metadata == null ) {
            load( false, true, Integer.MAX_VALUE, Integer.MAX_VALUE, false );
        }
        
        TIFFField ret =  null;
        if ( exifData != null ) {
            ret = exifData.getTIFFField( tag );
        }
        if ( ret == null && metadata != null ) {
            ret = metadata.getTIFFField( tag );
        }
        return ret;        
    }
    
    XMPMeta xmpMetadata = null;
    
    private XMPMeta getXMPMetadata() {
        if ( xmpMetadata == null ) {
            load( false, true, Integer.MAX_VALUE, Integer.MAX_VALUE, false );
        }
        return xmpMetadata;
    }
    
    /**
     * Get an EXIF tag as string
     * @param tag ID of the tag.
     * @return The tag value as String or <CODE>null</CODE> if the tag does not exist in the image
     */
    public String getEXIFTagAsString( int tag ) {
        String ret = null;
        TIFFField fld = getMetadataField( tag );
        if ( fld != null ) {
            ret = fld.getAsString( 0 );
        }
        return ret;
    }
    
    public double getEXIFTagAsDouble( int tag ) {
        double ret = 0.0;
        TIFFField fld = getMetadataField( tag );
        if ( fld != null ) {
            ret = fld.getAsDouble( 0 );
        }
        return ret;
    }

    public int getEXIFTagAsInt( int tag ) {
        int ret = 0;
        TIFFField fld = getMetadataField( tag );
        if ( fld != null ) {
            ret = fld.getAsInt( 0 );
        }
        return ret;
    }
    
    public RenderedImage getImage() {
        return null;
    }
    
    public static interface ScalingOp {
        
    };
    
    public static class MaxResolutionScalingOp implements ScalingOp {
        int width;
        int height;
        
        public MaxResolutionScalingOp( int width, int height ) {
            this.width = width;
            this.height = height;
        }
        
        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }        
    };
    
    public static class RelativeScalingOp implements ScalingOp {
        double scale = 1.0;
        
        public RelativeScalingOp( double scale ) {
            this.scale = scale;
        }
        
        public double getScale() {
            return scale;
        }
    };
    
    ScalingOp scalingOp = null;
    
    public void setScale( ScalingOp scalingOp ) {
        this.scalingOp = scalingOp;
    }
    
    public ScalingOp getScalingOp() {
        return scalingOp;
    }

    
    /**
     * Parse JPEG metadata structure and store the data in metadata and exifData fields
     * @param top The metadata object tree in format "javax_imageio_jpeg_image_1.0"
     */
    private void parseJPEGMetadata( IIOMetadataNode top ) {
        NodeList candidates = top.getElementsByTagName( "unknown" );
        for ( int n = 0; n < candidates.getLength(); n++ ) {
            Node node = candidates.item( n );
            if ( node instanceof IIOMetadataNode ) {
                IIOMetadataNode m = (IIOMetadataNode) node;
                Object obj = m.getUserObject();
                if ( obj instanceof byte[] ) {
                    byte[] data = (byte[]) obj;
                    if ( data[0] == 'E' && data[1] == 'x' && data[2] == 'i' && data[3] == 'f' ) {
                        log.debug( "exif data found" );
                        InputStream is = new ByteArrayInputStream( data, 6, data.length - 6 );
                        try {
                            ImageInputStream metadataStream = ImageIO.createImageInputStream( is );
                            Iterator readers = ImageIO.getImageReadersByFormatName( "TIFF" );
                            if ( readers.hasNext() ) {
                                ImageReader reader = (ImageReader) readers.next();
                                reader.setInput( metadataStream );
                                IIOMetadata iioMetadata = reader.getImageMetadata( 0 );
                                this.metadata = TIFFDirectory.createFromMetadata( iioMetadata );
                                TIFFField exifField = this.metadata.getTIFFField( EXIFParentTIFFTagSet.TAG_EXIF_IFD_POINTER );
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else if ( data.length > 28 && data[28] == 0 ) {
                        String id = null;
                        try {
                            id = new String( data, 0, 28, "utf-8" );
                        } catch ( UnsupportedEncodingException e ) {
                            log.error( e );
                        }
                        if ( "http://ns.adobe.com/xap/1.0/".equals( id ) ) {

                            // XMP metadata
                            try {
                                String xmpPacket = new String( data, 29, data.length-29, "utf-8" );
                                XMPMeta xmp = XMPMetaFactory.parseFromString( xmpPacket );
                                log.debug( "Found XMP metadata" );
                            } catch ( XMPException e ) {
                                log.warn( "caught XMP exception while parsing metadata", e );
                            } catch( UnsupportedEncodingException e ) {
                                log.error( e );
                            }
                    }
                    }
                }
            }
        }
    }
    
    /**
     * Get a proper image reader for a file based on file name extension.
     * @param f The file
     * @return Correct Reader or <CODE>null</CODE> if no proper reader is found.
     */
    static private ImageReader getImageReader( File f ) {
        ImageReader ret = null;
        if ( f != null ) {
            String fname = f.getName();
            int lastDotPos = fname.lastIndexOf( "." );
            if ( lastDotPos > 0 && lastDotPos < fname.length()-1 ) {
                String suffix = fname.substring( lastDotPos+1 );
                Iterator readers = ImageIO.getImageReadersBySuffix( suffix );
                if ( readers.hasNext() ) {
                    ret = (ImageReader)readers.next();
                }
            }
        }
        return ret;
    }
    
    /**
     Load the image and/or metadata
     @param loadImage Load the image pixel data if <CODE>true</CODE>
     @param loadMetadata Load image metadata if <CODE>true</CODE>.
     @param minWidth Minimum width of the loaded image
     @param minHeight Minimum height of the loaded image
     @param isLowQualityAllowed If <code>true</code>, use larger subsampling 
     to speed up loading.
     */
    private void load( boolean loadImage, boolean loadMetadata, 
            int minWidth, int minHeight, boolean isLowQualityAllowed ) {
        if ( f != null && f.canRead() ) {
            ImageReader reader = getImageReader( f );
            if ( reader != null ) {
                log.debug( "Creating stream" );
                ImageInputStream iis = null;
                try {
                    iis = ImageIO.createImageInputStream( f );
                    reader.setInput( iis, false, false );
                    width = reader.getWidth( 0 );
                    height = reader.getHeight( 0 );
                    if ( loadImage ) {
                        RenderedImage ri = null;
                        if ( isLowQualityAllowed ) {
                            ri = readExifThumbnail( f );
                            if ( ri == null || !isOkForThumbCreation( ri.getWidth(),
                                ri.getHeight(), minWidth, minHeight,
                                    reader.getAspectRatio( 0 ), 0.01 ) ) {
                                /*
                                 EXIF thumbnail either did not exist or was unusable,
                                 try to read subsampled version of original
                                 */
                                ri = readSubsampled( reader, minWidth, minHeight );
                            }
                        } else {
                            /*
                             High quality image is requested.
                             
                             If the image is very large, use subsampling anyway
                             to decrease memory consumption & speed up interactive 
                             operations. Anyway, most often user just views image 
                             at screen resolution
                             */
                            ImageReadParam param = reader.getDefaultReadParam();
                            

                            if ( minWidth * 2 < width && minHeight * 2 < height ) {
                                param.setSourceSubsampling( 2, 2, 0, 0 );
                            }
                                ri = reader.read( 0, param );
                            
                        }
                        if ( ri != null ) {                            
                            /*
                             TODO: JAI seems to have problems in doing convolutions
                             for large image tiles. Split image to reasonably sized
                             tiles as a workaround for this.
                             */
                            ri = new TiledImage( ri, 256, 256 );
                            image =  new RenderedImageAdapter( ri );
                            originalSampleModel = image.getSampleModel();
                            originalColorModel = image.getColorModel();
                            final float[] DEFAULT_KERNEL_1D = {0.25f,0.5f,0.25f};
                            ParameterBlock pb = new ParameterBlock();
                            KernelJAI kernel = new KernelJAI(DEFAULT_KERNEL_1D.length,
                                    DEFAULT_KERNEL_1D.length,
                                    DEFAULT_KERNEL_1D.length/2,
                                    DEFAULT_KERNEL_1D.length/2,
                                    DEFAULT_KERNEL_1D,
                                    DEFAULT_KERNEL_1D);
                            pb.add(kernel);
                            BorderExtender extender =
                                    BorderExtender.createInstance(BorderExtender.BORDER_COPY);
                            RenderingHints hints =
                                    JAI.getDefaultInstance().getRenderingHints();
                            if(hints == null) {
                                hints = new RenderingHints(JAI.KEY_BORDER_EXTENDER, extender);
                            } else {
                                hints.put(JAI.KEY_BORDER_EXTENDER, extender);
                            }
                            
                            RenderedOp filter = new RenderedOp("convolve", pb, hints);
                            // javax.media.jai.operator.BoxFilterDescriptor.create( null, new Integer(2), new Integer(2), new Integer(0), new Integer(0), null );
                            
                            // Add the subsampling operation.
                            pb = new ParameterBlock();
                            pb.addSource(filter);
                            pb.add(new Float(0.5F)).add(new Float(0.5F));
                            pb.add(new Float(0.0F)).add(new Float(0.0F));
                            pb.add(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
                            RenderedOp downSampler = new RenderedOp("scale", pb, null);
                            
                            renderableImage =
                                    RenderableDescriptor.createRenderable(
                                    image, downSampler, null, null, null, null, null );
                        } else {
                            image = null;
                            renderableImage = null;
                        }
                        imageIsLowQuality = isLowQualityAllowed;
                    }
                    if (loadMetadata ) {
                        readImageMetadata( reader );
                    }
                } catch (Exception ex) {
                    log.warn( ex.getMessage() );
                    ex.printStackTrace();
                    return;
                }
            }
        }
    }

    private void readImageMetadata( ImageReader reader ) throws IOException {
        Set<String> nodes = new HashSet<String>();
        nodes.add( "unknown" );
        IIOMetadata iioMetadata =
                reader.getImageMetadata( 0, "javax_imageio_jpeg_image_1.0",
                nodes );
        if ( iioMetadata != null ) {
            Node tree = iioMetadata.getAsTree( "javax_imageio_jpeg_image_1.0" );
            log.debug( "read metadata: " + iioMetadata.toString() );
            this.parseJPEGMetadata( (IIOMetadataNode) tree );
        }
    }
    
    /**
     Read the image (either original or proper thumbnail in the same file and subsample 
     it to save memory & time. The image is subsampled so that its reasolution is the
     smallest possible that is bigger than given limits. 
     
     @param reader The image reader that is used for reading the image
     @param minWidth Minimum width of the subsampled image
     @param minHeight Minimum height of the subsampled iamge
     
     @return Subsampled image.
     */
    
    private RenderedImage readSubsampled( ImageReader reader, int minWidth, 
            int minHeight )
            throws IOException {
        /*
         We try to ensure that the thumbnail is actually from the original image
         by comparing aspect ratio of it to original. This is not a perfect check
         but it will usually catch the most typical errors (like having a the original
         rotated by RAW conversion SW but still the original EXIF thumbnail.
         */
        double origAspect = reader.getAspectRatio( 0 );        
        double aspectAccuracy = 0.01;
        int minInstanceSide = Math.max( minWidth, minHeight );
        
        int numThumbs = 0;
        RenderedImage image = null;
        try {
            int numImages = reader.getNumImages( true );
            if ( numImages > 0 ) {
                numThumbs = reader.getNumThumbnails(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if ( numThumbs > 0
                && isOkForThumbCreation( reader.getThumbnailWidth( 0, 0 ),
                reader.getThumbnailHeight( 0, 0 ) , minWidth, minHeight, origAspect, aspectAccuracy )   ) {
            // There is a thumbanil that is big enough - use it
            
            log.debug( "Original has thumbnail, size "
                    + reader.getThumbnailWidth( 0, 0 ) + " x "
                    + reader.getThumbnailHeight( 0, 0 ) );
            image = reader.readThumbnail( 0, 0 );
            log.debug( "Read thumbnail" );
        } else {
            log.debug( "No thumbnail in original" );
            ImageReadParam param = reader.getDefaultReadParam();
            
            // Find the maximum subsampling rate we can still use for creating
            // a quality thumbnail. Some image format readers seem to have
            // problems with subsampling values (e.g. PNG sometimes crashed
            // the whole virtual machine, to for now let's do this only
            // with JPG.
            int subsampling = 1;
            if ( reader.getFormatName().equals( "JPEG" ) ) {
                int minDim = Math.min( reader.getWidth( 0 ),reader.getHeight( 0 ) );
                while ( 2 * minInstanceSide * subsampling < minDim ) {
                    subsampling *= 2;
                }
            }
            param.setSourceSubsampling( subsampling, subsampling, 0, 0 );
            image = reader.read( 0, param );
        }
        return image;
    }   
    
    
    /**
     Attemps to read a thumbnail from EXIF headers
     @return The thumbnail image or null if none available
     */
    private BufferedImage readExifThumbnail( File f ) {
        BufferedImage bi = null;
        try {
        IImageMetadata sanselanMetadata = Sanselan.getMetadata( f );
        if ( sanselanMetadata instanceof JpegImageMetadata ) {
            bi = ((JpegImageMetadata)sanselanMetadata).getEXIFThumbnail();
        }
        } catch ( IOException ex ) {
            log.error( ex );
        } catch ( ImageReadException ex ) {
            log.error( ex );
        }
        return bi;
    }
    
    /**
     Helper method to check if a image is ok for thumbnail creation, i.e. that
     it is large enough and that its aspect ration is same as the original has
     @param width width of the image to test
     @param height Height of the image to test
     @param minWidth Minimun width needed for creating a thumbnail
     @param minHeight Minimum height needed for creating a thumbnail
     @param origAspect Aspect ratio of the original image
     */
    private boolean isOkForThumbCreation( int width, int height,
            int minWidth, int minHeight, double origAspect, double aspectAccuracy ) {
        if ( width < minWidth ) return false;
        if ( height < minHeight ) return false;
        double aspect = getAspect( width, height, 1.0 );
        if ( Math.abs( aspect - origAspect) / origAspect > aspectAccuracy )  {
            return false;
        }
        return true;
    }
        

    /**
     Helper function to calculate aspect ratio of an image
     @param width width of the image
     @param height height of the image
     @param pixelAspect Aspect ratio of a single pixel (width/height)
     @return aspect ratio (width/height)
     */
    private double getAspect( int width, int height, double pixelAspect ) {
        return height > 0
                ? pixelAspect*(((double) width) / ((double) height )) : -1.0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
        
}
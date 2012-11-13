/*
  Copyright (c) 2006-2010 Harri Kaimio
 
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

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.media.jai.BorderExtender;
import javax.media.jai.Histogram;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderableOp;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.operator.BandCombineDescriptor;
import javax.media.jai.operator.HistogramDescriptor;
import javax.media.jai.operator.LookupDescriptor;
import javax.media.jai.operator.RenderableDescriptor;
import javax.media.jai.operator.TransposeDescriptor;
import javax.media.jai.operator.TransposeType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.common.PhotovaultException;
import org.photovault.image.PhotovaultImage;
import org.photovault.image.RawConvDescriptor;

/**
 Class to represent a raw camera image and set the parameters related to
 processing it.
 <p>
 <strong>Image processing steps</strong>
 <p>
 The actual raw image loading and processing up to demosaicing is done with LibRaw,
 which is based on dcraw.
 Since dcraw pipeline may have some nonlinear processing steps (at least when 
 camera ICC profile is used) that are done <em>after</em> color correction the
 resulting image can have color effects that depend in non-desirable way from actual 
 white balance settings.
 <p>
 To avoid this I load image from dcraw always using the daylight color correction
 recommended in the image file (stored to daylightMultipliers). I then do the actual white 
 balance correction here. This is certainly non-optimal and at least in theory 
 can lead to lost accuracy or even color clipping. But at least it behaves
 predictably.
 <p>
 As a summary, the processing steps done for raw image are
 <ul>
 <li>libraw loads raw data</li>
 <li>libraw scales raw channels using daylight multipliers</li>
 <li>libraw does demosaicing (using 2x2 box filter if small resolution is enough
 or AHD interpolation</li>
 <li>libraw converts to linear sRGB color space using either camera specific color
 matrix or ICC profile</li>
 <li>This class copies the iamge data to Raster, doing box filtering to reduce
 image size if only low resolution image is needed</li>
 <li>This class finalizes color correction to desired white balance</li>
 <li>This class does exposure & contrast adjustments and converts the image from 
 linear to gamma corrected sRGB color space.</li>
 <li>Base class ({@link PhotovaultImage}) applies desired lookup tables in gamma 
 corrected RGB & IHS color spaces if needed and instructs JAI to render final 
 image.
 </ul>
 
 */
public class RawImage extends PhotovaultImage {
    static private Log log = LogFactory.getLog( RawImage.class.getName() );
    
    
    /**
     ICC profile to use for raw conversion
     */
    ColorProfileDesc colorProfile = null;
    
    /**
     16 bit linear image returned by dcraw or <code>null</code> if the image
     has not been read or the conversion settings have been changed after
     reading.
     */
    PlanarImage rawImage = null;

    /**
     Raw image with WB correction applied
     */              
    RenderableOp wbAdjustedRawImage = null;
    
    /**
     Is the raw image loaded only half of the actual resolution?
     */
    boolean rawIsHalfSized = true;

    /**
     Raw image with desired exposure & contrast conversion done
     */
    RenderableOp rawConverter = null;

    /**
     8 bit gamma corrected version of the image or <code>null</code> if the image
     has not been converted or the conversion settings have been changed after
     reading.
     */
    RenderableOp correctedImage = null;
    
    /**
     Is this file really a raw image?
     */
    boolean validRawFile = false;
    
    /**
     Pixel value that is considered white in the linear image.
     */
    int white = 0;
    
    /**
     EV correction in F-stops. Positive values make the image brighter,
     negative darker. 0 sets white point to 99%
     */
    double evCorr = 0;
    
    /**
     Lookup table from 16 bit linear to 8 bit gamma corrected image
     */
    byte[] gammaLut = new byte[0x10000];
    
    
    /**
     Timestamp of the raw image
     */
    private Date timestamp = null;
    
    /**
     Camera model that was used to take the picture
     */
    private String camera = null;
    
    /**
     Film speed in ISO units or -1 if not known
     */
    private int filmSpeed = -1;
    
    /**
     Shutter speed in seconds
     */
    private double shutterSpeed = 0;
    
    /**
     Aperture of the camera (in f stops)
     */
    private double aperture = 0;
    
    /**
     Focal length of the camera in millimeters
     */
    private double focalLength = 0;
    
    /**
     Does the raw file have an embedded ICC profile?
     */
    private boolean hasICCProfile = false;
    
    /**
     Channel multipliers recommended by camera
     */
    private double cameraMultipliers[] = null;
    
    /**
     Channel multipliers for image shot at daylight as read by dcraw.
     3 doubles: R, G, B.
     */
    private double daylightMultipliers[] = null;
    
    /**
     Channel multipliers used for the conversion from raw image to sRGB color 
     space.
     */
    private double chanMultipliers[] = null;
    
    /**
     Color temperature (in Kelvin)
     */
    private double ctemp =0.0;
    
    /**
     Green channel gain compared to blackbody radiator color temperature
     */
    private double greenGain = 1.0;
    
    /**
     Level set as black
     */
    private int black = 0;
    
    /**
     Width of the raw image in pixels
     */
    private int width;
    
    /**
     Height of the raw image in pixels
     */
    private int height;
    
    private int histBins[][];

    /**
     * Earler dcraw based implementation rotated the raw images according to
     * camera recommendation during the conversion. These enums adapt the dcraw
     * rotation to Photovault & JAI data structures.
     */
    private static enum PreRotation {
        NONE( false, null ),
        ROTATE_90( true, TransposeDescriptor.ROTATE_90 ),
        ROTATE_180( false, TransposeDescriptor.ROTATE_180 ),
        ROTATE_270( true, TransposeDescriptor.ROTATE_270 );


        private final boolean swithcAxes;
        private final TransposeType jaiTranspose;

        PreRotation( boolean doSwitch, TransposeType jaiTranspose ) {
            this.swithcAxes = doSwitch;
            this.jaiTranspose = jaiTranspose;
        }

        boolean doSwitchAxes() {
            return swithcAxes;
        }

        TransposeType getJaiTransposeType() {
            return jaiTranspose;
        }

    };

    PreRotation preRotation = PreRotation.NONE;
    /**
     Returns true if this file is really a raw image file that can be decoded.
     */
    public boolean isValidRawFile() {
        return validRawFile;
    }
    
    /**
     * Get the shooting time of the image
     * @return Shooting time as reported by dcraw or <CODE>null</CODE> if
     * not available
     */
    public Date getTimestamp() {
        return (timestamp!= null) ? (Date) timestamp.clone()  : null;
    }
    
    /**
     * Get the camera mode used to shoot the image
     * @return Camera model reported by dcraw
     */
    public String getCamera() {
        return camera;
    }
    
    /**
     * Set the camera model.
     * @param camera The new camera model
     */
    public void setCamera(String camera) {
        this.camera = camera;
    }
    
    /**
     * Get the film speed setting used when shooting the image
     * @return Film speed (in ISO) as reported by dcraw
     */
    public int getFilmSpeed() {
        return filmSpeed;
    }
    
    /**
     * Get the shutter speed used when shooting the image
     * @return Exposure time (in seconds) as reported by dcraw
     */
    public double getShutterSpeed() {
        return shutterSpeed;
    }
    
    /**
     * Get aperture (f-stop) used when shooting the image
     * @return F-stop number reported by dcraw
     */
    public double getAperture() {
        return aperture;
    }
    
    /**
     * Get the focal length from image file meta data.
     * @return Focal length used when taking the picture (in millimetres)
     */
    public double getFocalLength() {
        return focalLength;
    }
    
    /**
     * Does the raw file have an embedded ICC color profile
     * @return <CODE>true</CODE> if the file has an embedded ICC color profile that dcraw
     * can read, <CODE>false</CODE> otherwise
     */
    public boolean isHasICCProfile() {
        return hasICCProfile;
    }
    
    /**
     * Get the color channel multipliers recommended by camera
     * @return The multipliers (4 doubles, RGBG)
     */
    public double[] getCameraMultipliers() {
        return (cameraMultipliers != null) ? cameraMultipliers.clone() : null;
    }
    
    /**
     * Get the color channel multipliers that should be used for pictures
     * taken in daylight.
     * @return The multipliers (3 doubles, RGB)
     */
    public double[] getDaylightMultipliers() {
        return (daylightMultipliers != null) ? daylightMultipliers.clone() : null;
    }
    
    
    /**
     * Set the exposure correction. Photovault sets the default exposure so that
     * 99% of raw image pixels fall under 99% gray.
     * @param evCorr The correction in f-stops.
     */
    public void setEvCorr( double evCorr ) {
        this.evCorr = evCorr;
        int white = (int) (this.white * Math.pow(  2, evCorr ) );
        rawConverter.setParameter( white, 0 );
        applyExposureSettings();
        fireChangeEvent( new RawImageChangeEvent( this ) );
    }
    
    /**
     * Get the current exposure correction
     * @return Correction in F-stops
     */
    public double getEvCorr() {
        return evCorr;
    }
    
    /**
     Amount of highlight compression/expansion that will be applied (in f-stops)
     Value 0 will cause linear tone mapping. Positive values set the produced white
     to map to this many f-stops higher luminance.
     */
    double highlightCompression = 0.0;
    
    /**
     * Set the amount of highlight compression/expansion that will be applied in raw
     * conversion
     * @param c New highlight compression
     */
    public void setHighlightCompression( double c ) {
        highlightCompression = c;
        rawConverter.setParameter( highlightCompression, 2 );
        applyExposureSettings();
        fireChangeEvent( new RawImageChangeEvent( this ) );
    }
    
    /**
     * Get current highlight compression/expansion
     * @return The current value
     */
    public double getHighlightCompression() {
        return highlightCompression;
    }

    float waveletThreshold = 0.0f;

    int medianPassCount = 0;

    int hlightRecovery = 0;
    
    public int[][] getHistogramBins() {
        return (histBins!=null) ? histBins.clone() : null;
    }
    
    /** Creates a new instance of RawImage
     @param f The raw file to open
     @throws PhotovaultException if dcraw has not been initialized properly
     */
    public RawImage( File f ) throws PhotovaultException {
        this.f = f;
        openRaw();
        closeRaw();
        // XXX debug
    }

    /**
     * Free any native meory reserved by this object.
     */
    @Override
    public void dispose() {
        if ( lrd != null ) {
            closeRaw();
        }
        if ( rawImage != null ) {
            rawImage.dispose();
        }
        super.dispose();
    }

    /**
     List of {@linkto RawImageChangeListener}s that should be notified about
     changes to this image.
     */
    ArrayList listeners = new ArrayList();
    
    /**
     Register an object to be notified about changes to this image.
     @param l The new listener.
     */
    public void addChangeListener( RawImageChangeListener l ) {
        listeners.add( l );
    }
    
    /**
     Remove a listener previpously registered with addChangeListener.
     @param l The listener that shouldno longer be notified.
     */
    public void removeChangeListener( RawImageChangeListener l ) {
        listeners.remove( l );
    }
    
    /**
     Send {@linkto RawImageChangeEvent} to all listeners
     @param ev The event that will be sent.
     */
    private void fireChangeEvent( RawImageChangeEvent ev ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            RawImageChangeListener l = (RawImageChangeListener) iter.next();
            l.rawImageSettingsChanged( ev );
        }
    }
    

    public RenderedImage getImage() {
        return null;
    }    
    
    /**
     * Subsampling of the loaded original image
     */
    int subsample = 1;

    /**
     *     Get a 8 bit gamma corrected version of the image.
     * @param minWidth Minimum width for the image that will be rendered
     * @param minHeight Minimum height for the image that will be rendered
     * @param isLowQualityAcceptable If true, renderer may use optimizations that
     * trade off image quality for speed.
     * @return The corrected image
     */
    public RenderableOp getCorrectedImage( int minWidth, int minHeight, 
            boolean isLowQualityAcceptable ) {


        int maxSubsample = 1;
        if ( minWidth > 0 && minHeight > 0 ) {
            while ( width >= minWidth * 2 * maxSubsample &&
                    height >= minHeight * 2 * maxSubsample ) {
                maxSubsample *= 2;
            }
        }
        if ( rawImage == null || maxSubsample < subsample ) {
            // dcraw.setHalfSize( isHalfSizeEnough );
            if ( maxSubsample == 1 && subsample > 1 ) {
                // The image has been loaded with 1/2 resolution so reloading
                // cannot be avoided
                closeRaw();
            }
            subsample = maxSubsample;
            loadRawImage();
            correctedImage = null;
        }
        if ( correctedImage == null ) {
            RenderingHints nonCachedHints = new RenderingHints( JAI.KEY_TILE_CACHE, null );

            // TODO: Why setting color model as a rendering hint produces black image???
            RawConvDescriptor.register();
            ParameterBlock pb = new ParameterBlockJAI( "RawConv" );
            pb.setSource( wbAdjustedRawImage, 0 );
            pb.set( white, 0 );
            pb.set( black, 1 );
            pb.set( highlightCompression, 2 );
            rawConverter = JAI.createRenderable( "RawConv", pb, nonCachedHints );
            applyExposureSettings();

            // Convert from linear to gamma corrected
            createGammaLut();
            LookupTableJAI jailut = new LookupTableJAI( gammaLut );
            correctedImage = LookupDescriptor.createRenderable( rawConverter, jailut, null );
            
            // Store the color model of the image
            ColorSpace cs = ColorSpace.getInstance( ColorSpace.CS_sRGB );
            cm = new ComponentColorModel( cs, new int[]{8,8,8},
                    false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE );

        }
        return correctedImage;
    }
    
    /**
     Set the preferred minimum size for the resulting image. Raw converter can use
     optimizations (e.g. use 2x2 block filter instead of true demosaicing) if 
     the actual image is larger than this
     @param minWidth The preferred minimum image width.
     @param minHeight The preferred minimum image height.
     @return <code>True</code> if given minimum size was larger than the already 
     loaded version. In this case the caller should reload image using 
     {@link #getCorrectedImage()}.
     @deprecated Use scaling paremeters in getRenderedImage instead.
     */
    public boolean setMinimumPreferredSize( int minWidth, int minHeight ) {
        boolean needsReload = ( correctedImage == null );
        if ( minWidth*2 > width || minHeight*2 > height ) {
            //dcraw.setHalfSize( false );
            if ( rawIsHalfSized ) {
                needsReload = true;
                if ( rawImage != null ) {
                    rawImage.dispose();
                }
                rawImage = null;
                correctedImage = null;
            }
        } else {
            //dcraw.setHalfSize( true );
        }
        return needsReload;
    }
    
    /**
     Color model of the image after raw conversion
     */
    ColorModel cm = null;

    /**
     Sample model of the image after raw conversion
     */
    SampleModel sm = null;


    /**
     Get the sample model of the image after raw conversion.
     */
    public SampleModel getCorrectedImageSampleModel() {
        return sm;
    }

    /**
     Get the color model of the image after raw conversion.
     */    
    public ColorModel getCorrectedImageColorModel() {
        return cm;
    }    


    /**
     * Load the raw image using dcraw. No processing is yet done for the image,
     * however, the histogram & white point is calculated.
     */
    private void loadRawImage() {
        long startTime = System.currentTimeMillis();
        log.debug( "begin:loadRawImage" );
        if ( lrd == null ) {
            openRaw();
            log.debug( "openRaw() " + (System.currentTimeMillis() - startTime) );
            if ( lrd == null ) {
                throw new IllegalStateException(
                        "Called loadRawImage before opening file" );
            }
            lr.libraw_unpack( lrd );
            log.debug( "unpacked " + (System.currentTimeMillis() - startTime) );
        }
        /*
         * Copy the unprocessed data to temporary array so that we can restore 
         * lrd to the state it had after unpack()
         */

        int oldFlags = lrd.progress_flags;
        int oldFilters = lrd.idata.filters;
        int rawImageSize = lrd.sizes.iwidth * lrd.sizes.iheight * 4;
        short rawWidth = lrd.sizes.width;
        short rawHeight = lrd.sizes.height;
        this.width = lrd.sizes.width;
        this.height = lrd.sizes.height;
        short[] rawData = lrd.image.getShortArray( 0, rawImageSize );

        lr.libraw_dcraw_process( lrd );
        log.debug(  "processed " + (System.currentTimeMillis()-startTime) );
        int procWidth = lrd.sizes.width;
        int procHeight = lrd.sizes.height;

        int postSubsample = (lrd.output_params.half_size > 0 ) ? 
            subsample/2 : subsample;
        /*
         * Copy the raw image to Java raster, using box filter to subsample
         */
        int scaledW = procWidth / postSubsample;
        int scaledH = procHeight / postSubsample;
        short[] buf = new short[scaledW*scaledH*3];
        int pos = 0;
        for ( int row = 0 ; row < scaledH; row++ ) {
            for ( int col = 0; col < scaledW; col++ ) {
                int rsum = 0;
                int gsum = 0;
                int bsum = 0;
                for ( int or = row * postSubsample ; or < (row+1)*postSubsample ; or++ ) {
                    for ( int oc = col * postSubsample ; oc < (col+1)*postSubsample ; oc++ ) {
                        int r = lrd.image.getShort( 8 * ( oc + procWidth * or) );
                        rsum += (r & 0xffff);
                        int g = lrd.image.getShort( 8 * ( oc + procWidth * or) + 2 );
                        gsum += (g & 0xffff);
                        int b = lrd.image.getShort( 8 * ( oc + procWidth * or) + 4 );
                        bsum += (b & 0xffff);
                    }
                }
                buf[pos++] = (short) (rsum / (postSubsample * postSubsample));
                buf[pos++] = (short) (gsum / (postSubsample * postSubsample));
                buf[pos++] = (short) (bsum / (postSubsample * postSubsample));
            }
        }
        log.debug(  "subsampled " + (System.currentTimeMillis()-startTime) );

        // Restore LibRaw state to what it was before dcraw_process
        lrd.image.write( 0, rawData, 0, rawImageSize );
        lrd.progress_flags = oldFlags;
        lrd.sizes.width = rawWidth;
        lrd.sizes.height = rawHeight;
        lrd.idata.filters = oldFilters;
        rawData = null;
        
        // closeRaw();

        DataBuffer db = new DataBufferUShort( buf, buf.length );
         SampleModel sampleModel =
                RasterFactory.createPixelInterleavedSampleModel(
                DataBuffer.TYPE_USHORT,
                scaledW, scaledH,
                3, 3 * scaledW, new int[]{0, 1, 2} );
         WritableRaster r = Raster.createWritableRaster( sampleModel, db, new Point( 0, 0 )  );
        log.debug(  "raster created " + (System.currentTimeMillis()-startTime) );

        if ( this.chanMultipliers == null ) {
            chanMultipliers = cameraMultipliers.clone();
            calcCTemp();
        }

            ColorSpace cs = ColorSpace.getInstance( ColorSpace.CS_LINEAR_RGB );
            ColorModel targetCM = new ComponentColorModel( cs, new int[]{16,16,16},
                    false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT );
            rawImage = new TiledImage( new BufferedImage( targetCM, r, 
                    true, null ), 256, 256 );
            
            if ( preRotation.getJaiTransposeType() != null ) {
                rawImage = TransposeDescriptor.create(
                        rawImage, preRotation.getJaiTransposeType(), null );
            }

            
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
            // downSampler = javax.media.jai.operator.BoxFilterDescriptor.create( null, new Integer(2), new Integer(2), new Integer(0), new Integer(0), null );
            
            RenderableOp rawImageRenderable = 
                    RenderableDescriptor.createRenderable( rawImage, 
                    downSampler, null, null, null, null, null );
            double colorCorrMat[][] = new double[][] {
                {colorCorr[0], 0.0, 0.0, 0.0 },
                {0.0, colorCorr[1], 0.0, 0.0 },
                {0.0, 0.0, colorCorr[2], 0.0 }
            };

            RenderingHints nonCachedHints = new RenderingHints( JAI.KEY_TILE_CACHE, null );
            wbAdjustedRawImage = 
                    BandCombineDescriptor.createRenderable( rawImageRenderable, 
                    colorCorrMat, nonCachedHints );
            
//            reader.getImageMetadata( 0 );
//            rawIsHalfSized = dcraw.ishalfSize();
//
//            createHistogram();
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (PhotovaultException ex) {
//            ex.printStackTrace();
//        }
        log.debug(  "image ready " + (System.currentTimeMillis()-startTime) );
        
        if ( autoExposeRequested ) {
            doAutoExpose();
        }
        log.debug(  "exit: loadRawImage " + (System.currentTimeMillis()-startTime) );

    }
    
    /**
     Recalculate image histogram (histBin structure).
     */
    private void createHistogram() {
        int numBins[] = {65536};
        double lowVal[] = {0.};
        double highVal[] = {65535.};
        RenderedOp histOp = 
                HistogramDescriptor.create( rawImage, null,
                Integer.valueOf( 1 ), Integer.valueOf( 1 ),
                numBins, lowVal, highVal, null );
        
        Histogram hist = (Histogram) histOp.getProperty( "histogram" );
        histBins = hist.getBins();        
    }
    
    /**
     * Logarithmic average of image luminance
     */
    private double logAvg = 0;
    
    /**
     * <CODE>true</CODE> if auto exposure calculation should be performed before accessing
     * image data next time
     */
    private boolean autoExposeRequested = true;
    
    /**
     * Recalculate the exposure values with autoexposure algorithm:
     * <ul>
     *    <li>Exposure is set so that log average brightness will map to 18% white</li>
     *    <li>Highlight compression is adjusted so that 1 % of pixels will be white.</li>
     * </ul>
     */
    public void autoExpose() {
        autoExposeRequested = true;
        if ( rawImage != null ) {
            doAutoExpose();
        } else {
            loadRawImage();
        }
        fireChangeEvent( new RawImageChangeEvent( this ) );
    }
    
    static double MAX_AUTO_HLIGHT_COMP = 1.0;
    static double MIN_AUTO_HLIGHT_COMP = 0.0;

    /**
     * Calculate the auto exposure settings
     */
    private void doAutoExpose() {
        autoExposeRequested = false;
        // Create a histogram if image luminance
        double lumMat[][] = {{ 0.27, 0.67, 0.06, 0.0 }};
        RenderingHints nonCachedHints = new RenderingHints( JAI.KEY_TILE_CACHE, null );
        RenderedOp lumImg = BandCombineDescriptor.create( rawImage, lumMat, nonCachedHints );
        
        int numBins[] = {65536};
        double lowVal[] = {0.};
        double highVal[] = {65535.};
        RenderedOp histOp = HistogramDescriptor.create( lumImg, null,
                Integer.valueOf( 1 ), Integer.valueOf( 1 ),
                numBins, lowVal, highVal, null );
        
        Histogram hist = (Histogram) histOp.getProperty( "histogram" );
        int[][] histBins = hist.getBins();
        
        double logSum = 0.0;
        int pixelCount = 0;
        for ( int n = 0; n < histBins[0].length; n++ ) {
            double l = Math.log1p( n );
            logSum += l * histBins[0][n];
            pixelCount += histBins[0][n];
        }
        double dw = 65536.;
        if ( pixelCount > 0 ) {
            logAvg = Math.exp( logSum / pixelCount );
            // Set the average to 18% grey
            dw = logAvg / 0.18;
        }
        // Set the white point  so that 1 % of pixels will be white
        int whitePixels = pixelCount/100;
        int brighterPixels = 0;
        int bin = 0xffff;
        for ( ; bin > 0; bin-- ) {
            brighterPixels += histBins[0][bin];
            if ( brighterPixels >= whitePixels ) break;
        }
        double hcRatio = ((double)bin)/dw;
        white = (int) dw;
        highlightCompression = Math.log( hcRatio ) / Math.log( 2.0 );
        highlightCompression = Math.min( MAX_AUTO_HLIGHT_COMP, 
                Math.max( MIN_AUTO_HLIGHT_COMP, highlightCompression ) );
    }

    LibRawData lrd;
    LibRaw lr = LibRaw.INSTANCE;

    private void openRaw() {
        lrd = lr.libraw_init( 0 );
        if ( subsample > 1 ) {
            lrd.output_params.half_size = 1;
        }
        lrd.output_params.highlight = hlightRecovery == 0 ? 0 : hlightRecovery+2;
        lrd.output_params.thereshold = waveletThreshold;
        lrd.output_params.med_passes = medianPassCount;
        
        if ( subsample == 2 ) {
            log.debug( "subsample 2" );
        }
        if ( lr.libraw_open_file( lrd, f.getAbsolutePath() ) != 0 ) {
            this.validRawFile = false;
            return;
        }
        validRawFile = true;
        this.aperture = lrd.other.getAperture();
        camera = lrd.idata.getMake() + " " + lrd.idata.getModel();
        filmSpeed = (int) lrd.other.getIsoSpeed();
        shutterSpeed = lrd.other.getShutterSpeed();
        width = lrd.sizes.width;
        height = lrd.sizes.height;
        timestamp = lrd.other.getTimestamp();
        focalLength = lrd.other.getFocalLen();
        daylightMultipliers = new double[4];
        for ( int n = 0; n< 4; n++ ) {
            daylightMultipliers[n] = lrd.color.pre_mul[n];
        }
        cameraMultipliers = new double[4];
        for ( int n = 0; n< 4; n++ ) {
            cameraMultipliers[n] = lrd.color.cam_mul[n];
        }
        switch( lrd.sizes.flip ) {
            case 0:
                preRotation = PreRotation.NONE;
                break;
            case 3:
                preRotation = PreRotation.ROTATE_180;
                break;
            case 5:
                preRotation = PreRotation.ROTATE_270;
                break;
            case 6:
                preRotation = PreRotation.ROTATE_90;
                break;
            default:
                log.error( "Unknown flip value " + lrd.sizes.flip );
                break;
        }
    }

    private void closeRaw() {
        lr.libraw_close( lrd );
        lrd = null;
    }

    /**
     Calculate the gamma correction lookup table using current exposure & white
     point settings.
     */
    private void createGammaLut() {
        double dw = 65535;
        double exposureMult = Math.pow( 2, 0 );
        for ( int n = 0; n < gammaLut.length; n++ ) {
            double r = exposureMult*((double)(n-black))/dw;
            double whiteLum = Math.pow( 2, 0 );
            // compress highlights
            r = (r*(1+(r/(whiteLum*whiteLum))))/(1+r);
            double val = (r <= 0.018) ? r*4.5 : Math.pow(r,0.45)*1.099-0.099;
            if ( val > 1. ) {
                val = 1.;
            } else if ( val < 0. ) {
                val = 0.;
            }
            int intVal = (int)( val * 256. );
            if ( intVal > 255 ) {
                intVal = 255;
            }
            gammaLut[n] = (byte)(intVal);
        }
    }

    /**
     * Apply the exposure settings (black level, white level, highlight
     * compression to rawConverter
     */
    private void applyExposureSettings() {

        if ( rawConverter != null ) {
            int w = (int)(white * Math.pow( 2, -evCorr ) );
            if ( w < 0 ) {
                w = 0;
            }
            if ( w > 0xffff ) {
                w = 0xffff;
            }
            rawConverter.setParameter( w, 0 );
            rawConverter.setParameter( black, 1 );
            rawConverter.setParameter( highlightCompression, 2 );
        }
    }
    
    /**
     * Get the width of corrected image
     * @return Width in pixels
     */
    public int getWidth() {
        return preRotation.doSwitchAxes() ? height : width;
    }
    
    /**
     * Get height of the converted image
     * @return Height in pixels
     */
    public int getHeight() {
        return preRotation.doSwitchAxes() ? width:  height;
    }
    
    public byte[] getGammaLut() {
        if ( gammaLut == null ) {
            createGammaLut();
        }
        return (gammaLut != null) ? gammaLut.clone() : null;
    }
    
    final static double XYZ_to_RGB[][] = {
        { 3.24071,  -0.969258,  0.0556352 },
        {-1.53726,  1.87599,    -0.203996 },
        {-0.498571, 0.0415557,  1.05707 } };
    
    /**
     * Convert a color temperature to RGB value of an white patch illuminated
     * with light with that temperature
     * @param T The color temperature of illuminant (in Kelvin)
     * @return Patches RGB value as 3 doubles (RGB)
     */
    public double[] colorTempToRGB( double T ) {
        /*
         This routine has been copied from Udi Fuchs' ufraw (and is originally from
         Bruce Lindbloom's web site)
         */
        
        int c;
        double xD, yD, X, Y, Z, max;
        double RGB[] = new double[3];
        // Fit for CIE Daylight illuminant
        if (T<= 4000) {
            xD = 0.27475e9/(T*T*T) - 0.98598e6/(T*T) + 1.17444e3/T + 0.145986;
        } else if (T<= 7000) {
            xD = -4.6070e9/(T*T*T) + 2.9678e6/(T*T) + 0.09911e3/T + 0.244063;
        } else {
            xD = -2.0064e9/(T*T*T) + 1.9018e6/(T*T) + 0.24748e3/T + 0.237040;
        }
        yD = -3*xD*xD + 2.87*xD - 0.275;
        
        X = xD/yD;
        Y = 1;
        Z = (1-xD-yD)/yD;
        max = 0;
        for (c=0; c<3; c++) {
            RGB[c] = X*XYZ_to_RGB[0][c] + Y*XYZ_to_RGB[1][c] + Z*XYZ_to_RGB[2][c];
            if (RGB[c]>max) max = RGB[c];
        }
        for (c=0; c<3; c++) RGB[c] = RGB[c]/max;
        return RGB;
    }
    
    /**
     Converts RGB multipliers to color balance
     @param rgb The color triplet of a white patch in the raw image
     @return Array of 2 doubles that contains temperature & green gain for the
     light source that has illuminated the patch.
     */
    double[] rgbToColorTemp( double rgb[] ) {
        double Tmax;
        double Tmin;
        double testRGB[] = null;
        Tmin = 2000;
        Tmax = 12000;
        double T;
        for (T=(Tmax+Tmin)/2; Tmax-Tmin>10; T=(Tmax+Tmin)/2) {
            testRGB = colorTempToRGB( T );
            if (testRGB[2]/testRGB[0] > rgb[2]/rgb[0])
                Tmax = T;
            else
                Tmin = T;
        }
        double green = (testRGB[1]/testRGB[0]) / (rgb[1]/rgb[0]);
        double result[] = {T, green};
        return result;
    }
    
    /**
     Multipliers used to correct colors from the raw image loaded with dcraw to
     the colors specified by chanMultipliers. Currently, dcraw is instructed to 
     load the image always with channel multipliers recommended for daylight
     (5500K). So is should always be so that chanMultipliers = colorCorr * daylightMultipliers
     
     */
    double colorCorr[] = new double[] {1.0, 1.0, 1.0};
    
    /**
     * Set the color temperature to use when converting the image
     * @param T Color temperature (in Kelvin)
     */
    public void setColorTemp( double T ) {
        ctemp = T;

        applyWbCorrection();
        double rgb[] = colorTempToRGB( T );
        
        // Update the multipliers
        chanMultipliers = new double[4];
        chanMultipliers[0] = daylightMultipliers[0]/rgb[0];
        chanMultipliers[1] = daylightMultipliers[1]/rgb[1] * greenGain;
        chanMultipliers[2] = daylightMultipliers[2]/rgb[2];
        chanMultipliers[3] = chanMultipliers[1];

        fireChangeEvent( new RawImageChangeEvent( this ) );
    }
    
    /**
     * Set the green gain to use when converting the image
     * @param g Green gain
     */
    public void setGreenGain( double g ) {
        greenGain = g;
        applyWbCorrection();        
        rawSettings = null;
        fireChangeEvent( new RawImageChangeEvent( this ) );
    }
    
    private void applyWbCorrection() {
        double rgb[] = colorTempToRGB( ctemp );
        colorCorr = new double[3];
        colorCorr[0] = 1.0/rgb[0];
        colorCorr[1] = greenGain/rgb[1];
        colorCorr[2] = 1.0/rgb[2];
        double colorCorrMat[][] = new double[][] {
            {colorCorr[0], 0.0, 0.0, 0.0 },
            {0.0, colorCorr[1], 0.0, 0.0 },
            {0.0, 0.0, colorCorr[2], 0.0 }
        };
        if ( wbAdjustedRawImage != null ) {
            wbAdjustedRawImage.setParameter( colorCorrMat, 0 );
        }
    }
    
    /**
     * Get color temperature of the image
     * @return Color temperature (in Kelvin)
     */
    public double getColorTemp() {
        return ctemp;
    }
    
    public double getGreenGain() {
        return greenGain;
    }
    
    void calcCTemp() {
        if ( chanMultipliers == null ) {
            chanMultipliers = cameraMultipliers.clone();
        }
        double rgb[] = {
            daylightMultipliers[0]/chanMultipliers[0],
            daylightMultipliers[1]/chanMultipliers[1],
            daylightMultipliers[2]/chanMultipliers[2]
        };
        double ct[] = rgbToColorTemp( rgb );
        ctemp = ct[0];
        greenGain = ct[1];
    }
    
    /**
     Get the current conversion settings
     @return The conversion settings
     */
    public RawConversionSettings getRawSettings() {
        if ( rawImage == null && rawSettings == null ) {
            subsample = 4;
            loadRawImage();
            doAutoExpose();
        }

        RawSettingsFactory rsf = new RawSettingsFactory( null );
        rsf.setDaylightMultipliers( daylightMultipliers );
        rsf.setRedGreenRation( chanMultipliers[0]/chanMultipliers[1] );
        rsf.setBlueGreenRatio( chanMultipliers[2]/chanMultipliers[1] );
        rsf.setBlack( black );
        rsf.setWhite( white );
        rsf.setEvCorr( evCorr );
        rsf.setHlightComp( highlightCompression );
        rsf.setUseEmbeddedProfile( hasICCProfile );
        rsf.setColorProfile( colorProfile );
        rsf.setHlightRecovery(hlightRecovery);
        rsf.setWaveletThreshold(waveletThreshold);
        rsf.setMedianPassCount(medianPassCount);
        RawConversionSettings s = null;
        try {
            s = rsf.create();
        } catch (PhotovaultException ex) {
            log.error( "Error while reacting raw settings object: " + 
                    ex.getMessage() );
        }
        return s;        
    }
    
    /**
     The last raw settings that were set to this image or <code>null</code> if they
     are no longer valid
     */
    RawConversionSettings rawSettings = null;
    
    /**
     Set the conversion settings to use
     @param s The new conversion settings
     */
    public void setRawSettings( RawConversionSettings s ) {
        if ( s == null ) {
            log.error( "null raw settings" );
            return;
        }
        if ( rawSettings != null && rawSettings.equals( s ) ) {
            return;
        }
        rawSettings = s;

        if ( hlightRecovery != s.getHlightRecovery() ) {
             hlightRecovery = s.getHlightRecovery();
            correctedImage = null;
            if ( rawImage != null ) {
                rawImage.dispose();
            }
            rawImage = null;
            wbAdjustedRawImage = null;
            rawConverter = null;
        }

        if ( waveletThreshold != s.getWaveletThreshold() ) {
            waveletThreshold = s.getWaveletThreshold();
            correctedImage = null;
            if ( rawImage != null ) {
                rawImage.dispose();
            }
            rawImage = null;
            wbAdjustedRawImage = null;
            rawConverter = null;
        }

        if ( chanMultipliers == null ||
                Math.abs( daylightMultipliers[0] - s.getDaylightRedGreenRatio() ) > 0.001 ||
                Math.abs( daylightMultipliers[2] - s.getDaylightBlueGreenRatio() ) > 0.001 ) {
            // Daylight settings have changed,  image must be reloaded
            daylightMultipliers = new double[3];
            daylightMultipliers[0] = s.getDaylightRedGreenRatio();
            daylightMultipliers[1] = 1.;
            daylightMultipliers[2] = s.getDaylightBlueGreenRatio();
            correctedImage = null;
            if ( rawImage != null ) {
                rawImage.dispose();
            }
            rawImage = null;
        }
        
        chanMultipliers = new double[4];
        chanMultipliers[0] = s.getRedGreenRatio();
        chanMultipliers[1] = 1.;
        chanMultipliers[2] = s.getBlueGreenRatio();
        chanMultipliers[3] = 1.;
        calcCTemp();
        applyWbCorrection();
        
        evCorr = s.getEvCorr();
        highlightCompression = s.getHighlightCompression();
        white = s.getWhite();
        black = s.getBlack();
        applyExposureSettings();
        hasICCProfile = s.getUseEmbeddedICCProfile();
        colorProfile = s.getColorProfile();        
        
        autoExposeRequested = false;
        fireChangeEvent( new RawImageChangeEvent( this ) );
    }


    private void printLibRawDebugInfo() {
        System.out.println( "Colors: " + lrd.idata.colors );
        System.out.println( "Filter pattern :" );
        char[] colors = new String( lrd.idata.cdesc ).toCharArray();
        for ( int n = 0 ; n < 16 ; n++ ) {
            int color = (lrd.idata.filters >> (2*n)) & 0x3;
            System.out.print( colors[color] );
            if ( n % 2 != 0 ) {
                System.out.println();
            }
        }
    }
}

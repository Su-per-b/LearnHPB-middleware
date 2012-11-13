/*
  Copyright (c) 2007 Harri Kaimio
 
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

    import java.awt.RenderingHints;
    import java.awt.geom.AffineTransform;
    import java.awt.geom.Rectangle2D;
    import java.awt.image.RenderedImage;
    import javax.media.jai.Interpolation;
    import javax.media.jai.InterpolationBilinear;
    import javax.media.jai.JAI;
    import javax.media.jai.ParameterBlockJAI;
    import javax.media.jai.RenderableOp;
    import javax.media.jai.RenderedOp;
    import javax.media.jai.operator.ConstantDescriptor;
    import javax.media.jai.operator.CropDescriptor;
    import javax.media.jai.operator.RenderableDescriptor;
    import org.testng.annotations.Test;

    /**
     This is a test case for problem in crop boundary value rounding in JAI
     renderable image chain. See #224 for more information.
     */
    public class Test_RenderableCrop {

        /** Creates a new instance of Test_RenderableCrop */
        public Test_RenderableCrop() {
        }

        @Test
        public void testRenderableCropBug() {
            RenderedOp src = ConstantDescriptor.create( 2560.0f, 1920.0f, 
                    new Byte[] {(byte)0x80, (byte)0x80, (byte)0x80}, null );
            RenderableOp rSrc = RenderableDescriptor.createRenderable( 
                    src, null, 128, 0.0f, 0.0f, 1.0f, null );

            // Rotate the image
            AffineTransform xform = getRotateXform(
                    20.0f, rSrc.getWidth(), rSrc.getHeight() );

            ParameterBlockJAI rotParams = new ParameterBlockJAI( "affine" );
            rotParams.addSource( rSrc  );
            rotParams.setParameter( "transform", xform );
            rotParams.setParameter( "interpolation",
                    Interpolation.getInstance( Interpolation.INTERP_NEAREST ) );
            RenderingHints hints = new RenderingHints( null );
            hints.put( JAI.KEY_INTERPOLATION, new InterpolationBilinear() );
            RenderableOp rotated = JAI.createRenderable( "affine", rotParams, hints );

            // Crop
            ParameterBlockJAI cropParams = new ParameterBlockJAI( "crop" );
            cropParams.addSource( rotated );
            float cropX = 0.2f;
            float cropW = rotated.getMinX() + rotated.getWidth() - cropX;
            float cropY = 0.3f;
            float cropH = rotated.getMinY() + rotated.getHeight() - cropY;

            // Verify that the crop rectangle is indeed inside source image
            Rectangle2D.Float cropRect = 
                    new Rectangle2D.Float( cropX, cropY, cropW, cropH );
            Rectangle2D.Float srcRect = 
                    new Rectangle2D.Float( rotated.getMinX(), rotated.getMinY(),
                    rotated.getWidth(), rotated.getHeight() );
            if ( !srcRect.contains( cropRect ) ) {
                // No, it doesn't (there can be rounding error in the previous float math
                Rectangle2D.intersect( srcRect, cropRect, cropRect );
                if ( !srcRect.contains( cropRect ) ) {
                    // Still rounding error, add some safety margin
                    final float margin = 5E-3f;
                    if ( srcRect.getMinX() > cropRect.getMinX() ) {
                        cropX += margin;
                    }
                    if ( srcRect.getMinY() > cropRect.getMinY() ) {
                        cropY += margin;
                    }
                    if ( srcRect.getMaxX() < cropRect.getMaxX() ) {
                        cropW -= margin;
                    }
                    if ( srcRect.getMaxY() < cropRect.getMaxY() ) {
                        cropH -= margin;
                    }
                }
            }

            cropParams.setParameter( "x", cropX );
            cropParams.setParameter( "y", cropY );
            cropParams.setParameter( "width", cropW );
            cropParams.setParameter( "height", cropH );
            CropDescriptor cdesc = new CropDescriptor();
            StringBuffer msg = new StringBuffer();        
            RenderableOp croppedImage = 
                    JAI.createRenderable("crop", cropParams, hints );
            // Translate the image so that it begins in origo
            ParameterBlockJAI pbXlate = new ParameterBlockJAI( "translate" );
            pbXlate.addSource( croppedImage );
            pbXlate.setParameter( "xTrans",(-croppedImage.getMinX()) );
            pbXlate.setParameter( "yTrans",(-croppedImage.getMinY()) );
            RenderableOp xformCroppedImage = 
                    JAI.createRenderable( "translate", pbXlate );
            // Create rendering
            RenderedImage rendered = 
                    xformCroppedImage.createScaledRendering( 200, 175, null );
        }

        public static AffineTransform getRotateXform( double rot, double curWidth, double curHeight ) {
            AffineTransform at = new AffineTransform();
            at.rotate( rot*Math.PI/180.0 );

            Rectangle2D bounds = getBounds( at, curWidth, curHeight );

            at.preConcatenate( AffineTransform.getTranslateInstance( 
                    -bounds.getMinX(), -bounds.getMinY() )  );
            return at;        
        }


        private static Rectangle2D getBounds( AffineTransform xform, double w, double h ) {
            double[] corners = {0.0f, 0.0f,
                               0.0f,     h,
                               w,        h,
                               w,     0.0f                
            };
            xform.transform( corners, 0, corners, 0, 4 );
            double minX = corners[0];
            double maxX = corners[0];
            double minY = corners[1];
            double maxY = corners[1];
            for ( int n = 2; n < corners.length; n += 2 ) {
                if ( corners[n+1] < minY ) {
                    minY = corners[n+1];
                }
                if ( corners[n+1] > maxY ) {
                    maxY = corners[n+1];
                }
                if ( corners[n] < minX ) {
                    minX = corners[n];
                }
                if ( corners[n] > maxX ) {
                    maxX = corners[n];
                }
            }

            return new Rectangle2D.Double( minX, minY, maxX-minX, maxY-minY );
        }

    }

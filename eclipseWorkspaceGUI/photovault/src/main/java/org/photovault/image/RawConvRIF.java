/*
  Copyright (c) 2009 Harri Kaimio

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


package org.photovault.image;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.CRIFImpl;
import javax.media.jai.ImageLayout;

/**
 * Factory for creating {@link RawConvOpImage}
 */
class RawConvRIF extends CRIFImpl {

    public RenderedImage create( ParameterBlock paramBlock, RenderingHints hints ) {
        RenderedImage src = paramBlock.getRenderedSource( 0 );
        int white = paramBlock.getIntParameter( 0 );
        int black = paramBlock.getIntParameter( 1 );
        double hlightComp = paramBlock.getDoubleParameter( 2 );
        ImageLayout layout = new ImageLayout( src );
        return new RawConvOpImage( src, white, black, hlightComp, layout, hints, false );
    }

}

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


package org.photovault.dcraw;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.CRIFImpl;

/**
 * Factory for creating {@link AHDInterpolateOp}
 * @author Harri Kaimio
 * @since 0.6.0
 */
class AHDInterpolateRIF extends CRIFImpl {

    public AHDInterpolateRIF() {
    }

    @Override
    public RenderedImage create( ParameterBlock params, RenderingHints hints ) {
        RenderedImage src = params.getRenderedSource( 0 );
        double rmult = params.getDoubleParameter( 0 );
        double bmult = params.getDoubleParameter( 1 );
        double gmult = params.getDoubleParameter( 2 );
        int downsample = params.getIntParameter( 3 );
        return new AHDInterpolateOp( src, rmult, bmult, gmult, downsample, hints );
    }

}

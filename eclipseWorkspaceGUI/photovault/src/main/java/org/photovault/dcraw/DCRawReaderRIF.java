/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.photovault.dcraw;

import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import javax.media.jai.CRIFImpl;

/**
 *
 * @author harri
 */
class DCRawReaderRIF extends CRIFImpl {

    @Override
    public RenderedImage create( ParameterBlock params, RenderingHints hints ) {
        String fname = (String) params.getObjectParameter( 0 );
        File f = new File( fname );
        return new DCRawReaderOp( f, hints );
    }

}

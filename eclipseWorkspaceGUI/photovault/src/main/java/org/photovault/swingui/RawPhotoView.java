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


package org.photovault.swingui;

import org.photovault.dcraw.ColorProfileDesc;
import org.photovault.swingui.selection.PhotoSelectionView;

/**
 * Extension of PhotoInfoView that supports also raw settings parameters.
 * @author Harri Kaimio
 */
public interface RawPhotoView extends PhotoSelectionView {
    void setRawBlack( int black );
    void setRawBlackMultivalued( boolean multivalued, Object[] values );    
    int getRawBlack();
    void setRawEvCorr( double evCorr );
    void setRawEvCorrMultivalued( boolean multivalued, Object[] values  );
    double getRawEvCorr();
    void setRawHlightComp( double comp );
    void setRawHlightCompMultivalued( boolean multivalued, Object[] values  );
    double getRawHlightComp();
    void setRawColorTemp( double ct );
    void setRawColorTempMultivalued( boolean multivalued, Object[] values  );
    double getRawColorTemp();
    void setRawGreenGain( double g );
    void setRawGreenGainMultivalued( boolean multivalued, Object[] values  );
    double getRawGreenGain();
    void setRawProfile( ColorProfileDesc p );
    void setRawProfileMultivalued( boolean multivalued, Object[] values  );
    ColorProfileDesc getRawProfile();
}

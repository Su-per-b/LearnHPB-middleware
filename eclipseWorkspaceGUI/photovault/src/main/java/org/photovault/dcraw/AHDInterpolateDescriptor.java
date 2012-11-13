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

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.OperationRegistry;
import javax.media.jai.registry.RIFRegistry;
import javax.media.jai.util.Range;

/**
 * Operator that interpolates a 3 channel RGB image from raw bayer data produced
 * by {@link DCRawReaderOp}.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class AHDInterpolateDescriptor extends OperationDescriptorImpl {

    // A map-like array of strings with resources information.
  private static final String[][] resources =
    {
      {"GlobalName",   "AHDInterpolate"},
      {"LocalName",    "AHDInterpolate"},
      {"Vendor",       "org.photovault"},
      {"Description",  "Do AHD demosaicing for bayer pattern image"},
      {"DocURL",       "http://www.photovault.org"},
      {"Version",      "1.0"}
    };
  // An array of strings with the supported modes for this operator.
  private static final String[] supportedModes = {"rendered" };
  // An array of strings with the parameter names for this operator.
  private static final String[] paramNames = {
      "rmult",
      "gmult",
      "bmult",
      "downsample"
  };
  // An array of Classes with the parameters' classes for this operator.
  private static final Class[] paramClasses = {
    Double.class,
    Double.class,
    Double.class,
    Integer.class
  };
  // An array of Objects with the parameters' default values.
  private static final Object[] paramDefaults =  {
    1.0, 1.0, 1.0, 1
  };
  // An array of Ranges with ranges of valid parameter values.
  private static final Range[] validParamValues = {
        new Range( Double.class, Double.valueOf( 0.0 ), Double.valueOf( 100.0 ) ),
        new Range( Double.class, Double.valueOf( 0.0 ), Double.valueOf( 100.0 ) ),
        new Range( Double.class, Double.valueOf( 0.0 ), Double.valueOf( 100.0 ) ),
        new Range( Integer.class, 1, Integer.MAX_VALUE )
    };

  // The number of sources required for this operator.
  private static final int numSources = 1;
  // A flag that indicates whether the operator is already registered.
  private static boolean registered = false;

 /**
  * The constructor for this descriptor, which just calls the constructor
  * for its ancestral class (OperationDescriptorImpl).
  */
  public AHDInterpolateDescriptor()
    {
    super(resources,supportedModes,numSources,paramNames,
          paramClasses,paramDefaults,validParamValues);
    }

 /**
  * A method to register this operator with the OperationRegistry and
  * RIFRegistry.
  */
  public static void register()
    {
    if (!registered)
      {
      // Get the OperationRegistry.
      OperationRegistry op = JAI.getDefaultInstance().getOperationRegistry();
      // Register the operator's descriptor.
      AHDInterpolateDescriptor desc =
        new AHDInterpolateDescriptor();
      op.registerDescriptor(desc);
      // Register the operators's RIF.
      AHDInterpolateRIF rif = new AHDInterpolateRIF();
      RIFRegistry.register(op,"AHDInterpolate","org.photovault",rif);
//      CRIFRegistry.register( op, "DCRawReader", rif );
      registered = true;
      }
    }


}

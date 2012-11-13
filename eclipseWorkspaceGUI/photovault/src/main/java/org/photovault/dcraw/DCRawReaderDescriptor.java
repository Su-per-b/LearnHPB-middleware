/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.photovault.dcraw;

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.OperationRegistry;
import javax.media.jai.registry.CRIFRegistry;
import javax.media.jai.registry.RIFRegistry;
import javax.media.jai.util.Range;

/**
 *
 * @author harri
 */
public class DCRawReaderDescriptor extends OperationDescriptorImpl {

    // A map-like array of strings with resources information.
  private static final String[][] resources =
    {
      {"GlobalName",   "DCRawReader"},
      {"LocalName",    "DCRawReader"},
      {"Vendor",       "org.photovault"},
      {"Description",  "Read raw iamge using LibRaw"},
      {"DocURL",       "http://www.photovault.org"},
      {"Version",      "1.0"}
    };
  // An array of strings with the supported modes for this operator.
  private static final String[] supportedModes = {"rendered" };
  // An array of strings with the parameter names for this operator.
  private static final String[] paramNames = {
    "fname"
  };
  // An array of Classes with the parameters' classes for this operator.
  private static final Class[] paramClasses = {
    String.class
  };
  // An array of Objects with the parameters' default values.
  private static final Object[] paramDefaults =  {
    null
  };
  // An array of Ranges with ranges of valid parameter values.
  private static final Range[] validParamValues =
    {
      null
    };

  // The number of sources required for this operator.
  private static final int numSources = 0;
  // A flag that indicates whether the operator is already registered.
  private static boolean registered = false;

 /**
  * The constructor for this descriptor, which just calls the constructor
  * for its ancestral class (OperationDescriptorImpl).
  */
  public DCRawReaderDescriptor()
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
      DCRawReaderDescriptor desc =
        new DCRawReaderDescriptor();
      op.registerDescriptor(desc);
      // Register the operators's RIF.
      DCRawReaderRIF rif = new DCRawReaderRIF();
      RIFRegistry.register(op,"DCRawReader","org.photovault",rif);
//      CRIFRegistry.register( op, "DCRawReader", rif );
      registered = true;
      }
    }


}


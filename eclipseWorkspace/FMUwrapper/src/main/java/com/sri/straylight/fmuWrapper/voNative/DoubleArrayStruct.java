package com.sri.straylight.fmuWrapper.voNative;




import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.DoubleByReference;


// TODO: Auto-generated Javadoc
/**
 * The Class DoubleArrayStruct.
 */
public class DoubleArrayStruct extends Structure {

	  /** The length. */
  	public int length;
	  
  	/** The double array. */
  	public DoubleByReference doubleArray;
	//  public static class ByReference extends DoubleArrayStruct implements Structure.ByReference { }
	  

	/**
	 * Gets the array.
	 *
	 * @return the array
	 */
	public double[] getArray() {
		
		
		Pointer p = doubleArray.getPointer();
		double[] convertedDoubleArray = p.getDoubleArray(0, length);
		
		return convertedDoubleArray;
	} 


	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "length", "doubleArray"});
	}
	
}

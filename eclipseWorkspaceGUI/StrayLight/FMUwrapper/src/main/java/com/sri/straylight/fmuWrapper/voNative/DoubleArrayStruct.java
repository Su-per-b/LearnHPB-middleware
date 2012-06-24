package com.sri.straylight.fmuWrapper.voNative;




import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.DoubleByReference;


public class DoubleArrayStruct extends Structure {

	  public int length;
	  public DoubleByReference doubleArray;
	//  public static class ByReference extends DoubleArrayStruct implements Structure.ByReference { }
	  

	public double[] getArray() {
		
		
		Pointer p = doubleArray.getPointer();
		double[] convertedDoubleArray = p.getDoubleArray(0, length);
		
		return convertedDoubleArray;
	} 



    
    
	
}

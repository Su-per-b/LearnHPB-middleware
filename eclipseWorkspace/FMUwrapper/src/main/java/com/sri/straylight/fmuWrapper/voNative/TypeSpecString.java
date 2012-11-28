
package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSpecString.
 */
public class TypeSpecString  extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends TypeSpecString implements Structure.ByReference { }
	
	public String start;
	public String declaredType;
	public int startValueStatus;

}

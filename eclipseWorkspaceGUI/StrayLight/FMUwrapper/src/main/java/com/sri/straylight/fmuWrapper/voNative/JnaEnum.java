package com.sri.straylight.fmuWrapper.voNative;

// TODO: Auto-generated Javadoc
/**
 * The Interface JnaEnum.
 *
 * @param <T> the generic type
 */
public interface JnaEnum<T> {
	   
   	/**
   	 * Gets the int value.
   	 *
   	 * @return the int value
   	 */
   	public int getIntValue();
	   
   	/**
   	 * Gets the for value.
   	 *
   	 * @param i the i
   	 * @return the for value
   	 */
   	public T getForValue(int i);
}

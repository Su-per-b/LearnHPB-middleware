package com.sri.straylight.fmuWrapper.voNative;

import java.text.DecimalFormat;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableStructBase.
 */
public class ScalarVariableStructBase extends Structure {
	
	/** The name. */
	public String name;
	
	/** The idx. */
	public int idx;
	
	/** The causality. */
	public int causality;
	
	/** The variability. */
	public int variability;
	
	/** The description. */
	public String description;
	
	/** The value reference. */
	public int valueReference;
	
	
	/**
	 * Gets the causality enum.
	 *
	 * @return the causality enum
	 */
	public Enu getCausalityEnum() {

		Enu p = Enu.enu_constant;
		Enu theEnum  = p.getForValue (causality);
		
		return theEnum;
	}
	
	/**
	 * Gets the variability enum.
	 *
	 * @return the variability enum
	 */
	public Enu getVariabilityEnum() {

		Enu p = Enu.enu_constant;
		Enu theEnum  = p.getForValue (variability);
		
		return theEnum;
	}
	
	public String doubleToString(double inValue){
		DecimalFormat threeDec = new DecimalFormat("0.000");
		String shortString = (threeDec.format(inValue));
		return shortString;
	}
	
}

package com.sri.straylight.fmuWrapper.voNative;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableStructBase.
 */
public class ScalarVariableStructBase extends Structure {
	
	public String name;
	public int idx;
	public int causality;
	public int variability;
	public String description;
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
	
	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "name", "idx", "causality", "variability", "description", "valueReference"});
	}
	
}

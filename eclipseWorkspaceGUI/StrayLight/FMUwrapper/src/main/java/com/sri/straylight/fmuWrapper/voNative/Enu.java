package com.sri.straylight.fmuWrapper.voNative;



// TODO: Auto-generated Javadoc
/**
 * The Enum Enu.
 */
public enum Enu implements JnaEnum<Enu>  {
	
    /** The enu_flat. */
    enu_flat,/** The enu_structured. */
enu_structured,/** The enu_constant. */
enu_constant,/** The enu_parameter. */
enu_parameter,/** The enu_discrete. */
enu_discrete,/** The enu_continuous. */
enu_continuous,
    
    /** The enu_input. */
    enu_input,
/** The enu_output. */
enu_output,
/** The enu_internal. */
enu_internal,
/** The enu_none. */
enu_none,
/** The enu_no alias. */
enu_noAlias,
/** The enu_alias. */
enu_alias,
/** The enu_negated alias. */
enu_negatedAlias;
	     
	/** The start. */
	private static int start = 0;
	

	
	/* (non-Javadoc)
	 * @see com.sri.straylight.fmuWrapper.voNative.JnaEnum#getIntValue()
	 */
	public int getIntValue() {
	    return this.ordinal() + start;
	}

	
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.fmuWrapper.voNative.JnaEnum#getForValue(int)
	 */
	public Enu getForValue(int i) {
	    for (Enu o : Enu.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	
	

	
}


package com.sri.straylight.fmuWrapper.voNative;



// TODO: Auto-generated Javadoc
/**
 * The Enum fmiStatus.
 */
public enum fmiStatus implements JnaEnum<fmiStatus>  {
	
	/** The fmi ok. */
	fmiOK,
    
    /** The fmi warning. */
    fmiWarning,
    
    /** The fmi discard. */
    fmiDiscard,
    
    /** The fmi error. */
    fmiError,
    
    /** The fmi fatal. */
    fmiFatal,
    
    /** The fmi pending. */
    fmiPending;
	     
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
	public fmiStatus getForValue(int i) {
	    for (fmiStatus o : fmiStatus.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	
}


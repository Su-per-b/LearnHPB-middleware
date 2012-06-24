package com.sri.straylight.fmuWrapper.voNative;



public enum fmiStatus implements JnaEnum<fmiStatus>  {
	
	fmiOK,
    fmiWarning,
    fmiDiscard,
    fmiError,
    fmiFatal,
    fmiPending;
	     
	private static int start = 0;
	

	
	public int getIntValue() {
	    return this.ordinal() + start;
	}

	
	
	public fmiStatus getForValue(int i) {
	    for (fmiStatus o : fmiStatus.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	
}


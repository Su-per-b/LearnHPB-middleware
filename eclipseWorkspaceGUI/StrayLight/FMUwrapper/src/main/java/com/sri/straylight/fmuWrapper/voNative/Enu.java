package com.sri.straylight.fmuWrapper.voNative;



public enum Enu implements JnaEnum<Enu>  {
	
    enu_flat,enu_structured,enu_constant,enu_parameter,enu_discrete,enu_continuous,
    enu_input,enu_output,enu_internal,enu_none,enu_noAlias,enu_alias,enu_negatedAlias;
	     
	private static int start = 0;
	

	
	public int getIntValue() {
	    return this.ordinal() + start;
	}

	
	
	public Enu getForValue(int i) {
	    for (Enu o : Enu.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	
	

	
}


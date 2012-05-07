package com.sri.straylight.SwingGUI;


import com.sri.straylight.fmuWrapper.voNative.JnaEnum;


public enum ConnectTo implements JnaEnum<ConnectTo>  {
	
    connecTo_file,
    connecTo_localhost,
    connecTo_straylightsim_com;
	     
	private static int start = 0;
	

	
	public int getIntValue() {
	    return this.ordinal() + start;
	}

	
	
	public ConnectTo getForValue(int i) {
	    for (ConnectTo o : ConnectTo.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	
	

	
}


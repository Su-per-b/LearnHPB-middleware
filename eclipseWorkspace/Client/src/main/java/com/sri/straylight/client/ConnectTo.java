package com.sri.straylight.client;


import com.sri.straylight.fmuWrapper.voNative.JnaEnum;


// TODO: Auto-generated Javadoc
/**
 * The Enum ConnectTo.
 */
public enum ConnectTo implements JnaEnum<ConnectTo>  {
	
    connectTo_file,
    connectTo_localhost,
    connectTo_wintermute_straylightsim_com,
    connectTo_dev_straylightsim_com;
	     
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
	public ConnectTo getForValue(int i) {
	    for (ConnectTo o : ConnectTo.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	
	

	
}


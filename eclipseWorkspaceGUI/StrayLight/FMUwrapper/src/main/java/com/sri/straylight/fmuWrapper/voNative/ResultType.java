
package com.sri.straylight.fmuWrapper.voNative;



// TODO: Auto-generated Javadoc
/**
 * The Enum ResultType.
 */
public enum ResultType implements JnaEnum<ResultType>  {
	
    /** The result type_state change. */
    resultType_stateChange, /** The result type_new results. */
 resultType_newResults, /** The result type_debug_message. */
 resultType_debug_message;
	     
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
	public ResultType getForValue(int i) {
	    for (ResultType o : ResultType.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}

}


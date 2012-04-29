
package com.sri.straylight.fmuWrapper;


public enum ResultType implements JnaEnum<ResultType>  {
	
    resultType_stateChange, resultType_newResults, resultType_debug_message;
	     
	private static int start = 0;
	

	
	public int getIntValue() {
	    return this.ordinal() + start;
	}

	
	
	public ResultType getForValue(int i) {
	    for (ResultType o : ResultType.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}

}


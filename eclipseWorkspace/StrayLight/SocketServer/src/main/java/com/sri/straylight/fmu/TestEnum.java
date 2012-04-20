package com.sri.straylight.fmu;

public enum TestEnum implements JnaEnum<TestEnum>{
	    NONE,
	    FILE,
	    GCF,
	    MAPPING,
	    MEMORY,
	    PROC,
	    NULL;
	     
	private static int start = 0;
	
	public int getIntValue() {
	    return this.ordinal() + start;
	}
	
	public TestEnum getForValue(int i) {
	    for (TestEnum o : this.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
}


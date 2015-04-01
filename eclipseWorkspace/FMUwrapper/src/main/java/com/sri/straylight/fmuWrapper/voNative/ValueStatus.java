package com.sri.straylight.fmuWrapper.voNative;


// TODO: Auto-generated Javadoc
/**
 * The Enum Elm.
 */
public enum ValueStatus implements JnaEnum<ValueStatus> {

	valueMissing,
	valueDefined, 
	valueIllegal;

	
	private static int start = 0;


	public int getIntValue() {
		return this.ordinal() + start;
	}


	public ValueStatus getForValue(int i) {
		for (ValueStatus o : ValueStatus.values()) {
			if (o.getIntValue() == i) {
				return o;
			}
		}
		return null;
	}

}

package com.sri.straylight.fmuWrapper.voNative;

// TODO: Auto-generated Javadoc
/**
 * The Enum Enu.
 */
public enum Enu implements JnaEnum<Enu> {

	enu_flat,  // 0
	enu_structured, 
	enu_constant, 
	enu_parameter, 
	enu_discrete, 
	enu_continuous, //5
	enu_input,
	enu_output,
	enu_internal,
	enu_none,
	enu_noAlias,
	enu_alias,
	enu_negatedAlias;


	private static int start = 0;
	
	private static String toStringMap_[] = new String[] {
		"flat",
		"structured",
		"constant",
		"parameter",
		"discrete",
		"continuous",
		"input",
		"output",
		"internal",
		"none",
		"noAlias",
		"alias",
		"negatedAlias"
	};
	
	
	
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
	
	public String toString() {
		int idx = getIntValue();
		
		return  toStringMap_[idx];
		
	}
	
	

}

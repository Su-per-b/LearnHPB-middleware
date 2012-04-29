package com.sri.straylight.fmuWrapper;



public enum State implements JnaEnum<State>  {
	
	fmuState_level_0_uninitialized, 
	fmuState_level_1_xmlParsed, 
	fmuState_level_2_dllLoaded,
	fmuState_level_3_instantiatedSlaves,
	fmuState_level_4_initializedSlaves,
	fmuState_level_5_initializedFMU,
	fmuState_runningSimulation,
	fmuState_completedSimulation,
	fmuState_error;
	     
	private static int start = 0;
	

	public int getIntValue() {
	    return this.ordinal() + start;
	}

	
	
	public State getForValue(int i) {
	    for (State o : State.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	
	

	
}


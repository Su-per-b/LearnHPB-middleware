package com.sri.straylight.fmuWrapper.voNative;




public enum SimStateNative implements JnaEnum<SimStateNative>  {
	

	simStateNative_0_uninitialized,
	simStateNative_1_connect_requested,
	simStateNative_1_connect_completed,
	simStateNative_2_xmlParse_requested,
	simStateNative_2_xmlParse_completed,
	simStateNative_3_init_requested,
	simStateNative_3_init_dllLoaded,
	simStateNative_3_init_instantiatedSlaves,
	simStateNative_3_init_initializedSlaves,
	simStateNative_3_ready,
	simStateNative_4_run_requested,
	simStateNative_4_run_started,
	simStateNative_4_run_completed,
	simStateNative_4_run_cleanedup,
	simStateNative_5_stop_requested,
	simStateNative_5_step_requested,
	simStateNative_6_pause_requested,
	simStateNative_6_pause_completed,
	simStateNative_7_reset_requested,
	simStateNative_7_reset_completed,
	simStateNative_e_error;
	
	private static int start = 0;
	

	public int getIntValue() {
	    return this.ordinal() + start;
	}

	
	
	public SimStateNative getForValue(int i) {
	    for (SimStateNative o : SimStateNative.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	

}


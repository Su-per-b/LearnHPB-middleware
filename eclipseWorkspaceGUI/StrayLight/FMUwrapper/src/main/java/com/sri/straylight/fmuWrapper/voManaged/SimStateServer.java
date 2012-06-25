package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.JnaEnum;




public enum SimStateServer implements JnaEnum<SimStateServer>  {
	
	simStateServer_0_uninitialized,
	simStateServer_1_connect_requested,
	simStateServer_1_connect_completed,
	simStateServer_2_xmlParse_requested,
	simStateServer_2_xmlParse_completed,
	simStateServer_3_init_requested,
	simStateServer_3_init_dllLoaded,
	simStateServer_3_init_instantiatedSlaves,
	simStateServer_3_init_initializedSlaves,
	simStateServer_3_ready,
	simStateServer_4_run_requested,
	simStateServer_4_run_started,
	simStateServer_4_run_completed,
	simStateServer_4_run_cleanedup,
	simStateServer_5_stop_requested,
	simStateServer_5_stop_completed,
	simStateServer_5_step_requested,
	simStateServer_5_step_completed,
	simStateServer_6_pause_requested,
	simStateServer_6_pause_completed,
	simStateServer_7_resume_requested,
	simStateServer_7_resume_completed,
	simStateServer_e_error;
	     
	private static int start = 0;
	

	public int getIntValue() {
	    return this.ordinal() + start;
	}

	
	
	public SimStateServer getForValue(int i) {
	    for (SimStateServer o : SimStateServer.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	

}


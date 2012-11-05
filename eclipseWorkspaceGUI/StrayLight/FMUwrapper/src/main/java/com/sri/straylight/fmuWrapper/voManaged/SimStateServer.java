package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.JnaEnum;




// TODO: Auto-generated Javadoc
/**
 * The Enum SimStateServer.
 */
public enum SimStateServer implements JnaEnum<SimStateServer>  {
	
	/** uninitialized. */
	simStateServer_0_uninitialized,
	
	/** connect */
	simStateServer_1_connect_requested,
	simStateServer_1_connect_completed,
	
	/** XML parse */
	simStateServer_2_xmlParse_requested,
	simStateServer_2_xmlParse_completed,
	
	/** init */
	simStateServer_3_init_requested,
	simStateServer_3_init_dllLoaded,
	simStateServer_3_init_instantiatedSlaves,
	simStateServer_3_init_initializedSlaves,
	
	/** ready */
	simStateServer_3_ready,
	
	/** run */
	simStateServer_4_run_requested,
	simStateServer_4_run_started,
	simStateServer_4_run_completed,
	simStateServer_4_run_cleanedup,
	
	/** stop. */
	simStateServer_5_stop_requested,
	simStateServer_5_stop_completed,
	
	/** step */
	simStateServer_5_step_requested,
	simStateServer_5_step_completed,
	
	/** pause */
	simStateServer_6_pause_requested,
	simStateServer_6_pause_completed,
	
	/** terminate */
	simStateServer_7_terminate_requested,
	simStateServer_7_terminate_completed,
	
	/** reset */
	simStateServer_7_reset_requested,
	simStateServer_7_reset_completed,
	
	/** The sim state server_7_resume_requested. */
	simStateServer_7_resume_requested,
	simStateServer_7_resume_completed,
	
	/** The sim state server_e_error. */
	simStateServer_e_error;
	     
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
	public SimStateServer getForValue(int i) {
	    for (SimStateServer o : SimStateServer.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	

}


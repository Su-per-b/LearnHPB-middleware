package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.JnaEnum;




// TODO: Auto-generated Javadoc
/**
 * The Enum SimStateServer.
 */
public enum SimStateServer implements JnaEnum<SimStateServer>  {
	
	/** The sim state server_0_uninitialized. */
	simStateServer_0_uninitialized,
	
	/** The sim state server_1_connect_requested. */
	simStateServer_1_connect_requested,
	
	/** The sim state server_1_connect_completed. */
	simStateServer_1_connect_completed,
	
	/** The sim state server_2_xml parse_requested. */
	simStateServer_2_xmlParse_requested,
	
	/** The sim state server_2_xml parse_completed. */
	simStateServer_2_xmlParse_completed,
	
	/** The sim state server_3_init_requested. */
	simStateServer_3_init_requested,
	
	/** The sim state server_3_init_dll loaded. */
	simStateServer_3_init_dllLoaded,
	
	/** The sim state server_3_init_instantiated slaves. */
	simStateServer_3_init_instantiatedSlaves,
	
	/** The sim state server_3_init_initialized slaves. */
	simStateServer_3_init_initializedSlaves,
	
	/** The sim state server_3_ready. */
	simStateServer_3_ready,
	
	/** The sim state server_4_run_requested. */
	simStateServer_4_run_requested,
	
	/** The sim state server_4_run_started. */
	simStateServer_4_run_started,
	
	/** The sim state server_4_run_completed. */
	simStateServer_4_run_completed,
	
	/** The sim state server_4_run_cleanedup. */
	simStateServer_4_run_cleanedup,
	
	/** The sim state server_5_stop_requested. */
	simStateServer_5_stop_requested,
	
	/** The sim state server_5_stop_completed. */
	simStateServer_5_stop_completed,
	
	/** The sim state server_5_step_requested. */
	simStateServer_5_step_requested,
	
	/** The sim state server_5_step_completed. */
	simStateServer_5_step_completed,
	
	/** The sim state server_6_pause_requested. */
	simStateServer_6_pause_requested,
	
	/** The sim state server_6_pause_completed. */
	simStateServer_6_pause_completed,
	
	/** The sim state server_6_reset_requested. */
	simStateServer_6_reset_requested,
	
	/** The sim state server_6_reset_completed. */
	simStateServer_6_reset_completed,
	
	/** The sim state server_7_resume_requested. */
	simStateServer_7_resume_requested,
	
	/** The sim state server_7_resume_completed. */
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


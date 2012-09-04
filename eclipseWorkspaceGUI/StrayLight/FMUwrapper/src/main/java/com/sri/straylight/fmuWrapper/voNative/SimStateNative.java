package com.sri.straylight.fmuWrapper.voNative;




// TODO: Auto-generated Javadoc
/**
 * The Enum SimStateNative.
 */
public enum SimStateNative implements JnaEnum<SimStateNative>  {
	

	/** The sim state native_0_uninitialized. */
	simStateNative_0_uninitialized,
	
	/** The sim state native_1_connect_requested. */
	simStateNative_1_connect_requested,
	
	/** The sim state native_1_connect_completed. */
	simStateNative_1_connect_completed,
	
	/** The sim state native_2_xml parse_requested. */
	simStateNative_2_xmlParse_requested,
	
	/** The sim state native_2_xml parse_completed. */
	simStateNative_2_xmlParse_completed,
	
	/** The sim state native_3_init_requested. */
	simStateNative_3_init_requested,
	
	/** The sim state native_3_init_dll loaded. */
	simStateNative_3_init_dllLoaded,
	
	/** The sim state native_3_init_instantiated slaves. */
	simStateNative_3_init_instantiatedSlaves,
	
	/** The sim state native_3_init_initialized slaves. */
	simStateNative_3_init_initializedSlaves,
	
	/** The sim state native_3_ready. */
	simStateNative_3_ready,
	
	/** The sim state native_4_run_requested. */
	simStateNative_4_run_requested,
	
	/** The sim state native_4_run_started. */
	simStateNative_4_run_started,
	
	/** The sim state native_4_run_completed. */
	simStateNative_4_run_completed,
	
	/** The sim state native_4_run_cleanedup. */
	simStateNative_4_run_cleanedup,
	
	/** The sim state native_5_stop_requested. */
	simStateNative_5_stop_requested,
	
	/** The sim state native_5_step_requested. */
	simStateNative_5_step_requested,
	
	/** The sim state native_6_pause_requested. */
	simStateNative_6_pause_requested,
	
	/** The sim state native_6_pause_completed. */
	simStateNative_6_pause_completed,
	
	/** The sim state native_7_reset_requested. */
	simStateNative_7_reset_requested,
	
	/** The sim state native_7_reset_completed. */
	simStateNative_7_reset_completed,
	
	/** The sim state native_7_resume_requested. */
	simStateNative_7_resume_requested,
	
	/** The sim state native_7_resume_completed. */
	simStateNative_7_resume_completed,
	
	/** The sim state native_e_error. */
	simStateNative_e_error;
	
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
	public SimStateNative getForValue(int i) {
	    for (SimStateNative o : SimStateNative.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	

}


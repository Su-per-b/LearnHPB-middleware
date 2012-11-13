package com.sri.straylight.client.model;

import com.sri.straylight.fmuWrapper.voNative.JnaEnum;




// TODO: Auto-generated Javadoc
/**
 * The Enum SimStateClient.
 */
public enum SimStateClient implements JnaEnum<SimStateClient>  {
	
	/** uninitialized */
	level_0_uninitialized,
	
	/** connect */
	level_1_connect_requested,
	level_1_connect_completed,
	
	/** XML parse */
	level_2_xmlParse_requested,
	level_2_xmlParse_completed,
	
	/** init. */
	level_3_init_requested,
	level_3_init_completed,
	
	/** The level_3_ready. */
	level_3_ready,
	
	/** run */
	level_4_run_requested,
	level_4_run_started,
	level_4_run_completed,
	level_4_run_cleanedup,
	
	/** stop */
	level_5_stop_requested,
	level_5_stop_completed,
	
	/** step */
	level_5_step_requested,
	level_5_step_completed,
	
	/** terminate */
	level_7_terminate_requested,
	level_7_terminate_completed,
	
	/** reset */
	level_7_reset_requested,
	level_7_reset_completed,
	
	/** resume */
	level_7_resume_requested,
	level_7_resume_completed,
	
	/** The level_e_error. */
	level_e_error;
	
	
	/** The start. */
	private static int start = 0;
	

	/* (non-Javadoc)
	 * @see com.sri.straylight.fmuWrapper.voNative.JnaEnum#getIntValue()
	 */
	public int getIntValue() {
	    return this.ordinal() + start;
	}

	/**
	 * Checks if is processing request.
	 *
	 * @return true, if is processing request
	 */
	public boolean isProcessingRequest() {
		
		return  (this == level_1_connect_requested ||
				this == level_2_xmlParse_requested ||
				this == level_3_init_requested ||
					this == level_4_run_requested
				);

	}
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.fmuWrapper.voNative.JnaEnum#getForValue(int)
	 */
	public SimStateClient getForValue(int i) {
	    for (SimStateClient o : SimStateClient.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	

}


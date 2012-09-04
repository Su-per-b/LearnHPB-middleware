package com.sri.straylight.client.model;

import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;
import com.sri.straylight.fmuWrapper.voNative.JnaEnum;




// TODO: Auto-generated Javadoc
/**
 * The Enum SimStateClient.
 */
public enum SimStateClient implements JnaEnum<SimStateClient>  {
	
	/** The level_0_uninitialized. */
	level_0_uninitialized,
	
	/** The level_1_connect_requested. */
	level_1_connect_requested,
	
	/** The level_1_connect_completed. */
	level_1_connect_completed,
	
	/** The level_2_xml parse_requested. */
	level_2_xmlParse_requested,
	
	/** The level_2_xml parse_completed. */
	level_2_xmlParse_completed,
	
	/** The level_3_init_requested. */
	level_3_init_requested,
	
	/** The level_3_ready. */
	level_3_ready,
	
	/** The level_4_run_requested. */
	level_4_run_requested,
	
	/** The level_4_run_started. */
	level_4_run_started,
	
	/** The level_4_run_completed. */
	level_4_run_completed,
	
	/** The level_4_run_cleanedup. */
	level_4_run_cleanedup,
	
	/** The level_5_stop_requested. */
	level_5_stop_requested,
	
	/** The level_5_step_requested. */
	level_5_step_requested,
	
	/** The level_6_reset_requested. */
	level_6_reset_requested,
	
	/** The level_6_reset_completed. */
	level_6_reset_completed,
	
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


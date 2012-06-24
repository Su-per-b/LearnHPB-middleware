package com.sri.straylight.client.model;

import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;
import com.sri.straylight.fmuWrapper.voNative.JnaEnum;




public enum SimStateClient implements JnaEnum<SimStateClient>  {
	
	level_0_uninitialized,
	level_1_connect_requested,
	level_1_connect_completed,
	level_2_xmlParse_requested,
	level_2_xmlParse_completed,
	level_3_init_requested,
	level_3_init_completed,
	level_4_run_requested,
	level_4_run_started,
	level_4_run_completed,
	level_4_run_cleanedup,
	level_e_error, 
	level_5_stop_requested,
	level_5_stop_completed,
	level_6_pause_requested,
	level_6_pause_completed,
	level_7_resume_requested,
	level_7_resume_completed;
	     
	private static int start = 0;
	

	public int getIntValue() {
	    return this.ordinal() + start;
	}

	public boolean isProcessingRequest() {
		
		return  (this == level_1_connect_requested ||
				this == level_2_xmlParse_requested ||
				this == level_3_init_requested ||
					this == level_4_run_requested
				);

	}
	
	public SimStateClient getForValue(int i) {
	    for (SimStateClient o : SimStateClient.values()) {
	        if (o.getIntValue() == i) {
	            return o;
	        }
	    }
	    return null;
	}
	

}


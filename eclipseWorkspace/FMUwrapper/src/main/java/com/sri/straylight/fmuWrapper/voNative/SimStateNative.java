package com.sri.straylight.fmuWrapper.voNative;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;




// TODO: Auto-generated Javadoc
/**
 * The Enum SimStateNative.
 */
public enum SimStateNative implements JnaEnum<SimStateNative>, Iserializable  {
	

	
	/** uninitialized. */
	simStateNative_0_uninitialized,
	
	/** connect */
	simStateNative_1_connect_requested,
	simStateNative_1_connect_completed,
	
	/** XML parse */
	simStateNative_2_xmlParse_requested,
	simStateNative_2_xmlParse_completed,
	
	/** init */
	simStateNative_3_init_requested,
	simStateNative_3_init_dllLoaded,
	simStateNative_3_init_instantiatedSlaves,
	simStateNative_3_init_initializedSlaves,
	simStateNative_3_init_completed,
	
	/** ready */
	simStateNative_3_ready,
	
	/** The sim state native_4_run_requested. */
	simStateNative_4_run_requested,
	simStateNative_4_run_started,
	simStateNative_4_run_completed,
	simStateNative_4_run_cleanedup,
	
	/** stop. */
	simStateNative_5_stop_requested,
	simStateNative_5_stop_completed,
	
	/** step */
	simStateNative_5_step_requested,
	simStateNative_5_step_started,
	simStateNative_5_step_completed,
	
	/** pause */
	simStateNative_6_pause_requested,
	simStateNative_6_pause_completed,
	
	/** terminate */
	simStateNative_7_terminate_requested,
	simStateNative_7_terminate_completed,
	
	/** reset */
	simStateNative_7_reset_requested,
	simStateNative_7_reset_completed,
	
	/** resume */
	simStateNative_7_resume_requested,
	simStateNative_7_resume_completed,
	
	/** error */
	simStateNative_e_error,
	
	/** tear down */
	simStateNative_8_tearDown_requested,
	simStateNative_8_tearDown_completed;
	
	
	/** The start. */
	private static int start = 0;
	
	private boolean serializeType_ = true;
	
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
	
	@Override
	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	

	public boolean getSerializeType() {
		return serializeType_;
	}
	
	public void setSerializeType(boolean serializeType) {
		serializeType_ = serializeType;
	}
    
    
}


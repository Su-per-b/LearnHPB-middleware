package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class SimStateNativeRequest extends BaseEvent<SimStateNative> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimStateNativeRequest(Object source, SimStateNative newState) {
        super(source, newState);
    }

}

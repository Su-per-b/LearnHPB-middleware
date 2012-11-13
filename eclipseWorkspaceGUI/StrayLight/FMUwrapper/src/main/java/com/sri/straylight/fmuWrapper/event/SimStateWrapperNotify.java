package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;


public class SimStateWrapperNotify extends BaseEvent<SimStateWrapper> {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimStateWrapperNotify(Object source, SimStateWrapper newState) {
        super(source,newState);
    }

}

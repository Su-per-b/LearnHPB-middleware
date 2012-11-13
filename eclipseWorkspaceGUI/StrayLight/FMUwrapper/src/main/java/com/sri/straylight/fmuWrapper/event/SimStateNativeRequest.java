package com.sri.straylight.fmuWrapper.event;

import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

public class SimStateNativeRequest extends BaseEvent<SimStateNative> {

    public SimStateNativeRequest(Object source, SimStateNative newState) {
        super(source, newState);
    }

}

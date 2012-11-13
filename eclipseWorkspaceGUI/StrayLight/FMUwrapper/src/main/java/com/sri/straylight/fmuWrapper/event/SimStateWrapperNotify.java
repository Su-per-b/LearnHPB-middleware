package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;


import com.sri.straylight.fmuWrapper.voManaged.SimStateWrapper;


public class SimStateWrapperNotify extends BaseEvent<SimStateWrapper> {


    public SimStateWrapperNotify(Object source, SimStateWrapper newState) {
        super(source,newState);
    }

}

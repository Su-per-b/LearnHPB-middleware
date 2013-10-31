package com.sri.straylight.fmuWrapper.Controller;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;

public class FMUcontrollerGlobal extends FMUcontroller {
	
	public FMUcontrollerGlobal(AbstractController parent) {
		super(parent);
	}

	public FMUcontrollerGlobal(AbstractController parent,
			FMUwrapperConfig fmuWrapperConfig) {
		
		super(parent, fmuWrapperConfig);

	}

	public FMUcontrollerGlobal() {
		super();

	}
	
	
	@Override
    public void fireEvent(BaseEvent<?> event) {
		event.fire();
    }
    
    
}

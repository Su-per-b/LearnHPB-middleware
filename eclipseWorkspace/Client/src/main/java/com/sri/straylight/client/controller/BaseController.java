package com.sri.straylight.client.controller;

import com.sri.straylight.client.view.BaseView;
import com.sri.straylight.fmuWrapper.framework.AbstractController;


public abstract class BaseController extends AbstractController {
	
	public BaseController(AbstractController parentController) {
		super(parentController);
	}
	
		
	
    /**
     * Gets the view.
     *
     * @return the view
     */
    public BaseView getView() {
    	
    	if (view_ == null) {
    		return null;
    	} else {
    		return (BaseView) view_;
    	}
    }






    
    
	
}

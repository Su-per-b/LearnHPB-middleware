package com.sri.straylight.client.controller;

import com.sri.straylight.client.model.FMUInfoModel;
import com.sri.straylight.client.view.FMUInfoView;
import com.sri.straylight.fmuWrapper.framework.AbstractController;



// TODO: Auto-generated Javadoc
/**
 * The Class DebugConsoleController.
 */
public class FMUInfoController extends BaseController {


	/**
	 * Instantiates a new debug console controller.
	 *
	 * @param parentController the parent controller
	 */
	public FMUInfoController (AbstractController parentController) {
		super(parentController);

		FMUInfoModel dataModel = new FMUInfoModel("FMUInfo");
		
		FMUInfoView theView = new FMUInfoView(dataModel, this);
		
		setView_(theView);
		
	}
	
	

	



}

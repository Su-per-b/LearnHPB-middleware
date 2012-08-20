package com.sri.straylight.client.controller;

import java.util.Vector;

import javax.swing.JOptionPane;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ScalarValueChangeRequest;
import com.sri.straylight.client.event.menu.About_Help;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.model.ClientConfigXML;
import com.sri.straylight.client.model.InputFormDataModel;
import com.sri.straylight.client.view.InputFormView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;

public class InputFormController extends AbstractController {



    private InputFormDataModel inputFormDataModel_;
    
	public InputFormController(AbstractController parentController) {
		super(parentController);
	}
	


	public void init(XMLparsed xmlParsed) {  
		
		
		inputFormDataModel_ = new InputFormDataModel();
		inputFormDataModel_.xmlParsed = xmlParsed;
		
		

		InputFormView theView = new InputFormView(this, inputFormDataModel_);
	    setView_(theView);

    }
	
	
	public void reset(XMLparsed xmlParsed) {  
		//	xmlParsed_ = xmlParsed;
		
		//InputFormView theView = (InputFormView) this.getView();
		//theView.reset(xmlParsed_);
	}
	
	

	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {

		Vector<String> resultInput = event.resultOfStep.getInputList();
		int len = resultInput.size();
		
		 double[] resultInputAry_ = event.resultOfStep.getInput();
		 InputFormView theView = (InputFormView) this.getView();
		 
		 theView.newResult(resultInput);
		 
	}
	





	public void onDataModelUpdateRequest(ScalarValueChangeRequest event) {
		EventBus.publish(event);
	}
	
	

    
}

package com.sri.straylight.client.view;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JPanel;

import com.sri.straylight.client.controller.BaseController;
import com.sri.straylight.fmuWrapper.event.BaseEvent;

public class BaseView extends JPanel {

		private static final long serialVersionUID = 1L;
		
		protected String title_;
		
		protected BaseController parentController_;
		
		
		
		public String getTitle() {
			return title_;
		}
		
		public BaseView(String title, BaseController parentController) {
			
			parentController_ = parentController;
			title_ = title;
			
		}
		
		
		/**
		 * Left justify.
		 *
		 * @param panel the panel
		 * @return the component
		 */
		protected Component leftJustify( JPanel panel )  {
		    Box  b = Box.createHorizontalBox();
		    b.add( panel );
		    b.add( Box.createHorizontalGlue() );
		    // (Note that you could throw a lot more components
		    // and struts and glue in here.)
		    return b;
		}


		

	    
	    public void fireEvent(BaseEvent<?> event) {
	    	parentController_.fireEvent(event);
	    }
	    
	    
		
}

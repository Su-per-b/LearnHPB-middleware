package com.sri.straylight.client.view;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JPanel;

public class BaseView extends JPanel {

		private static final long serialVersionUID = 1L;
		private String title_;
		

		
		public String getTitle() {
			return title_;
		}
		
		public BaseView(String title) {
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


		
		
		
}

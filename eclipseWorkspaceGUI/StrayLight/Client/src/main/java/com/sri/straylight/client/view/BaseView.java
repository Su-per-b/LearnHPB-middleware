package com.sri.straylight.client.view;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JPanel;

public class BaseView extends JPanel {

	
		private String title_;
		
		private int idx_;
		
		

		/**
		 * @return the idx_
		 */
		public int getIdx() {
			return idx_;
		}

		
		public String getTitle() {
			return title_;
		}
		
		public BaseView(String title, int idx) {
			title_ = title;
			idx_ = idx;
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

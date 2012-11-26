package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.client.event.ViewInitialized;
import com.sri.straylight.client.view.BaseView;
import com.sri.straylight.client.view.JTableEx;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class InternalTableController.
 */
public class InternalTableController  extends BaseController {
	
	
    /** The table_. */
    private  JTableEx table_;
    
    /** The data model_. */
    private DefaultTableModel dataModel_;
    
	private static final String TITLE = "Internal";
	
	/**
	 * Instantiates a new internal table controller.
	 *
	 * @param parentController the parent controller
	 */
	public InternalTableController(AbstractController parentController) {
		super(parentController);	
	}
	

	
	
	protected void init_( XMLparsedInfo xmlParsed) {  

	    BaseView theView = new BaseView(TITLE);
	    
	    theView.setPreferredSize(new Dimension(704, 500));
	    theView.setLayout(new GridLayout(1, 1, 0, 0));
	    
		dataModel_ = new DefaultTableModel (
				xmlParsed.getInternalData(),
				xmlParsed.getInternalColumnNames()
		);
		
		table_ = new JTableEx(dataModel_);
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);
		
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(table_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    theView.add(scrollPaneTable);

	    setView_(theView);
	    
	    table_.updateLayout();
	    
	    ViewInitialized e = new ViewInitialized(this, theView);
	    EventBus.publish(e);

    }

	
}

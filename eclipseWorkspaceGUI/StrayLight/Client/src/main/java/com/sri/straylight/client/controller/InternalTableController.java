package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;

// TODO: Auto-generated Javadoc
/**
 * The Class InternalTableController.
 */
public class InternalTableController  extends AbstractController {
	
	
    /** The table_. */
    private  JTable table_;
    
    /** The data model_. */
    private DefaultTableModel dataModel_;
    
    
	/**
	 * Instantiates a new internal table controller.
	 *
	 * @param parentController the parent controller
	 */
	public InternalTableController(AbstractController parentController) {
		super(parentController);	
	}
	
	
	/**
	 * Inits the.
	 *
	 * @param initializedStruct the initialized struct
	 */
	public void init(XMLparsed initializedStruct) {  
		
	    JPanel panel = new JPanel();
	    
	    panel.setPreferredSize(new Dimension(704, 500));
	    panel.setLayout(new GridLayout(1, 1, 0, 0));
	    
		dataModel_ = new DefaultTableModel (
			initializedStruct.getInternalData(),
			initializedStruct.getInternalColumnNames()
		);
		
		table_ = new JTable(dataModel_);
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);
		
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(table_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    panel.add(scrollPaneTable);

	    setView_(panel);
    }
    
}

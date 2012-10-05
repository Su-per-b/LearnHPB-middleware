package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.view.JTableEx;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import java.awt.Component;


// TODO: Auto-generated Javadoc
/**
 * The Class ResultsTableController.
 */
public class ResultsTableController extends AbstractController {
	
    /** The table_. */
    private  JTableEx table_;
    
    /** The data model_. */
    private DefaultTableModel dataModel_;
    
	/**
	 * Instantiates a new results table controller.
	 *
	 * @param parentController the parent controller
	 */
	public ResultsTableController(AbstractController parentController) {
		super(parentController);
	}
	
	/**
	 * Inits the.
	 *
	 * @param xmlParsed the xml parsed
	 */
	public void init(XMLparsed xmlParsed) {  
		

	    JPanel panel = new JPanel();
	    
	    panel.setPreferredSize(new Dimension(704, 500));
	    panel.setLayout(new GridLayout(1, 1, 0, 0));
	    
		Object[][] data = {{}};
		
		dataModel_ = new DefaultTableModel(data,xmlParsed.getOutputColumnNames());
		
		table_ = new JTableEx(dataModel_);
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);
		
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(table_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    panel.add(scrollPaneTable);
	    
	    setView_(panel);
	    table_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
    }
	
	
	/**
	 * Reset.
	 */
	public void reset() {
		
		int count = dataModel_.getRowCount();
		for (int i = 0; i < count; i++) {
			dataModel_.removeRow(0);
		}
		
	}
	
	
	
	
	
	/**
	 * On result event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {

		
		ScalarValueResults  scalarValueResults = event.getScalarValueResults();
		
		Vector<String> resultOuput = scalarValueResults.output.getStringList();
		double time = event.getScalarValueResults().getTime();
		
		resultOuput.insertElementAt(Double.toString(time), 0);
		dataModel_.insertRow(0,resultOuput);
		
		table_.updateLayout();
	}
	
	
	

	
	
}
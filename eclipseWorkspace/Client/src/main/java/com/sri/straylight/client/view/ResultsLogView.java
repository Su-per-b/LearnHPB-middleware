package com.sri.straylight.client.view;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import com.sri.straylight.client.controller.ResultsLogController;
import com.sri.straylight.client.model.ResultsLogModel;

// TODO: Auto-generated Javadoc
/**
 * The Class OutputView.
 */
public class ResultsLogView  extends BaseView  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The table model_. */
	private DefaultTableModel tableModel_;
	
	/** The table_. */
	private  JTableEx table_;

	/** The result input ary_. */
	private Vector<String> resultOutputAry_;

	/** The input form controller_. */
	@SuppressWarnings("unused")
	private ResultsLogController resultsLogController_;
	
	/** The input form data model_. */
	private ResultsLogModel variableDataModel_;

	 
	private static final String TITLE = "Results Log";
	
	
	/**
	 * Instantiates a new input form view.
	 *
	 * @param inputFormController the input form controller
	 * @param inputFormDataModel the input form data model
	 */
	public ResultsLogView(ResultsLogController parentController, ResultsLogModel variableDataModel) {
		
		
		super(TITLE, parentController);
		
		variableDataModel_ = variableDataModel;
		

	    DefaultTableModel tableModel = variableDataModel_.getTableModel();
		table_ = new JTableEx(tableModel);
		
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);
	    table_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(table_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    add(scrollPaneTable);

	}
	
	
	public void setModel(ResultsLogModel resultsLogModel) {
		
		variableDataModel_ = resultsLogModel;
	    DefaultTableModel tableModel = resultsLogModel.getTableModel();
	    
		table_.setModel(tableModel);
		
	}

	
	/**
	 * New result.
	 *
	 * @param resultInput the result input
	 */
	public void newResult(Vector<String> resultOutput) {

		resultOutputAry_ = resultOutput;
		int len = resultOutputAry_.size();

		for (int i = 0; i < len; i++) {
			String str = resultOutputAry_.get(i);

			tableModel_.setValueAt(str, i, 1);
		}

		table_.updateLayout();

	}
	

	public void update() {
		table_.validate();
		table_.updateUI();
		table_.updateLayout();
	}


	public void clear() {
		setModel(variableDataModel_);
		
	}



}

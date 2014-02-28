package com.sri.straylight.client.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import com.sri.straylight.client.controller.OutputVariablesController;
import com.sri.straylight.client.model.OutputDataModel;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;

// TODO: Auto-generated Javadoc
/**
 * The Class OutputView.
 */
public class OutputView extends BaseView {

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
	private OutputVariablesController outputController_;
	
	/** The input form data model_. */
	@SuppressWarnings("unused")
	private OutputDataModel outputDataModel_;

	private ScalarValueCollection latestInput_;
	
	protected JSplitPane splitPane_;

	private JScrollPane scrollPaneTable_;
	 
    
	private static final String TITLE = "Output";
	/**
	 * Instantiates a new input form view.
	 *
	 * @param inputFormController the input form controller
	 * @param inputFormDataModel the input form data model
	 */
	public OutputView(OutputVariablesController outputController, OutputDataModel outputDataModel) {
		
		super(TITLE);
		
		outputDataModel_ = outputDataModel;
		outputController_ = outputController;

		tableModel_ = outputDataModel.getTableModel();
		
		this.setLayout(new GridLayout(1, 1, 0, 0));
		this.setAlignmentY(Component.LEFT_ALIGNMENT);
		
		table_ = new JTableEx(tableModel_);
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);

        ListSelectionModel selectionModel = table_.getSelectionModel(); 
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  

		scrollPaneTable_ = new JScrollPane(table_);
		scrollPaneTable_.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneTable_.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		this.add (scrollPaneTable_);
		setVisible(true);
	}

	
	/**
	 * New result.
	 *
	 * @param resultInput the result input
	 */
	public void newResult(Vector<String> resultOutput) {

		resultOutputAry_ = resultOutput;
		int len = resultOutputAry_.size();


		//populate the 'value' column
		for (int i = 0; i < len; i++) {
			String str = resultOutputAry_.get(i);

			tableModel_.setValueAt(str, i, 1);
		}

		table_.updateLayout();

	}
	

	/**
	 * Sets the result.
	 *
	 * @param scalarValueResults the new result
	 */
	public void addResult(ScalarValueResults scalarValueResults) {
		
		latestInput_ = scalarValueResults.getInput();
		
		Vector<String> list = latestInput_.getStringList();
		int len = list.size();

		for (int i = 0; i < len; i++) {
			String str = list.get(i);
			tableModel_.setValueAt(str, i, 1);
		}

		table_.updateLayout();

	}
	
	
	public JTableEx getTable() {
		return table_;
	}



	public void updateResults(ScalarValueResults scalarValueResults) {
		
		Vector<String> resultOutput = scalarValueResults.getOutput().getStringList();
		
		int len = resultOutput.size();
		

		//populate the 'value' column
		for (int i = 0; i < len; i++) {
			String str = resultOutput.get(i);
			
			try {
				tableModel_.setValueAt(str, i, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		table_.updateLayout();
		
	}



}

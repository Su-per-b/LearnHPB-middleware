package com.sri.straylight.client.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import com.sri.straylight.client.controller.BaseController;
import com.sri.straylight.client.model.VariableDataModel;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;

// TODO: Auto-generated Javadoc
/**
 * The Class InputFormView.
 */
public class TableOfVariablesView extends BaseView   {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The table model_. */
	private DefaultTableModel tableModel_;
	
	/** The table_. */
	private  JTableEx table_;

	
	/** The input form data model_. */
	private VariableDataModel variableDataModel_;

	private JPanel bottomPanel_;
	
	protected JSplitPane splitPane_;

	private JScrollPane scrollPaneTable_;
	 
	private String title_ = "{none}";
	/**
	 * Instantiates a new input form view.
	 * @param title 
	 *
	 * @param inputFormController the input form controller
	 * @param inputFormDataModel the input form data model
	 */
	public TableOfVariablesView(BaseController parentController, VariableDataModel variableDataModel, String title) {
		
		super(title, parentController);
		
		title_ = title;
		variableDataModel_ = variableDataModel;
		
		tableModel_ = variableDataModel.getTableModel();
		
		
		this.setLayout(new GridLayout(1, 1, 0, 0));
		this.setAlignmentY(Component.LEFT_ALIGNMENT);
		
		table_ = new JTableEx(tableModel_);
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);

        ListSelectionModel selectionModel = table_.getSelectionModel(); 
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
        selectionModel.addListSelectionListener(new RowListener(this));  
        
		scrollPaneTable_ = new JScrollPane(table_);
		scrollPaneTable_.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneTable_.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		bottomPanel_ = new JPanel();
		bottomPanel_.setPreferredSize(new Dimension(700, 500));
		bottomPanel_.setLayout(new GridLayout(1, 1, 0, 0));
		
		splitPane_ = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPaneTable_, bottomPanel_);
		splitPane_.setContinuousLayout(false);
		splitPane_.setOneTouchExpandable(true);
		splitPane_.setResizeWeight(0.7);
		
		this.add ( splitPane_ );
		
		table_.autoResizeColWidth();
		
	}

	
	

	
	public void updateLayout() {
		
		table_.updateLayout();
	}
	

	
	public void selectRow(int idx) {
		
		bottomPanel_.removeAll();
		
		Vector<ScalarVariableReal> ScalarVarList = variableDataModel_.getVariables();
		
		ScalarVariableReal sVar = ScalarVarList.get(idx);
		ScalarValueReal sVal = variableDataModel_.getValueAt(idx);
		
		ScalarVariableRealPanel componentPanel = new ScalarVariableRealPanel( parentController_); // FlowLayout 
		componentPanel.setMetaData(sVar);
		

		if (sVal != null) {
			componentPanel.setValue(sVal);
		}
		
		if (title_.equals("Internal") || title_.equals("Ouput")) {
			
			componentPanel.setEnabled(false);
		}

		bottomPanel_.add(componentPanel);
		bottomPanel_.validate();
		bottomPanel_.repaint();
	}




	public JTableEx getTable() {
		return table_;
	}




	
	
	
	


}

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

import com.sri.straylight.client.controller.InputController;
import com.sri.straylight.client.model.InputDataModel;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.voManaged.BaseScalarValue;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;

// TODO: Auto-generated Javadoc
/**
 * The Class InputFormView.
 */
public class InputView extends BaseView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The table model_. */
	private DefaultTableModel tableModel_;
	
	/** The table_. */
	private  JTableEx table_;

	/** The input form controller_. */
	private InputController inputController_;
	
	/** The input form data model_. */
	private InputDataModel inputDataModel_;

	private JPanel bottomPanel_;
	
	private ScalarValueCollection latestInput_;

	protected JSplitPane splitPane_;

	private JScrollPane scrollPaneTable_;
	 
	private static final String TITLE = "Input";
	/**
	 * Instantiates a new input form view.
	 *
	 * @param inputFormController the input form controller
	 * @param inputFormDataModel the input form data model
	 */
	public InputView(InputController inputController, InputDataModel inputDataModel) {
		
		super(TITLE);
		
		inputDataModel_ = inputDataModel;
		inputController_ = inputController;
		
		
		Object[][] data = inputDataModel_.xmlParsed.getInputData();
		String[] columns = inputDataModel_.xmlParsed.getInputFormColumnNames();

		
		
		tableModel_ = new DefaultTableModel (	
				data,
				columns
				);

		
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
		
	}

	
	/**
	 * New result.
	 *
	 * @param resultInput the result input
	 */
	public void newResult(Vector<String> resultInput) {

		int len = resultInput.size();

		for (int i = 0; i < len; i++) {
			String str = resultInput.get(i);

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
	
	
	public void selectRow(int idx) {
		
		bottomPanel_.removeAll();
		
		ScalarVariableRealPanel componentPanel = new ScalarVariableRealPanel(this); // FlowLayout 
		Vector<ScalarVariableReal> ScalarVarList = inputDataModel_.xmlParsed.getInputVars();
		
		ScalarVariableReal sv = ScalarVarList.get(idx);
		
		componentPanel.setMetaData(sv);
		
		if (latestInput_ != null) {
			
			BaseScalarValue val =  latestInput_.get(idx);
			componentPanel.setValue((ScalarValueReal) val);
		}

		bottomPanel_.add(componentPanel);
		bottomPanel_.validate();
		bottomPanel_.repaint();
	}




	public JTableEx getTable() {
		return table_;
	}


	public void onDataModelUpdateRequest(ScalarValueChangeRequest event) {
		inputController_.onDataModelUpdateRequest(event);
	}



}

package com.sri.straylight.client.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import com.sri.straylight.client.controller.InputFormController;
import com.sri.straylight.client.event.ScalarValueChangeRequest;
import com.sri.straylight.client.model.InputFormDataModel;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class InputFormView.
 */
public class InputFormView extends JPanel {

	/** The table model_. */
	private DefaultTableModel tableModel_;
	
	/** The table_. */
	private  JTableEx table_;

	/** The result input ary_. */
	private Vector<String> resultInputAry_;

	/** The btn submit_. */
	private final JButton btnSubmit_ = new JButton("submit");

	/** The input form controller_. */
	private InputFormController inputFormController_;
	
	/** The input form data model_. */
	private InputFormDataModel inputFormDataModel_;


	/**
	 * Instantiates a new input form view.
	 *
	 * @param inputFormController the input form controller
	 * @param inputFormDataModel the input form data model
	 */
	public InputFormView(InputFormController inputFormController, InputFormDataModel inputFormDataModel) {

		inputFormDataModel_ = inputFormDataModel;
		inputFormController_ = inputFormController;


		tableModel_ = new DefaultTableModel (	
				inputFormDataModel.xmlParsed.getInputData(),
				inputFormDataModel.xmlParsed.getInputFormColumnNames()
				);


		JPanel panelButton = new JPanel();


		this.setPreferredSize(new Dimension(704, 300));
		this.setLayout(new GridLayout(2, 1, 0, 0));

		table_ = new JTableEx(tableModel_);
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPaneTable = new JScrollPane(table_);
		scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


		panelButton.add(btnSubmit_);

		this.add(scrollPaneTable);
		this.add(panelButton);

		bindActions_();
	}





	/**
	 * Reset.
	 *
	 * @param xmlParsed the xml parsed
	 */
	public void reset(XMLparsed xmlParsed) {

		inputFormDataModel_.xmlParsed = xmlParsed;

		tableModel_ = new DefaultTableModel (

				inputFormDataModel_.xmlParsed.getInputData(),
				inputFormDataModel_.xmlParsed.getInputFormColumnNames()
				);

		table_.setModel(tableModel_);

	}

	/**
	 * Bind actions_.
	 */
	private void bindActions_() {

		btnSubmit_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fireUpdateRequest();
			}
		}
				);

	}




	// call this method whenever you want to notify
	//the event listeners of the particular event
	/**
	 * Fire update request.
	 */
	private synchronized void fireUpdateRequest()	{


		Object[][] inData = inputFormDataModel_.xmlParsed.getInputData();

		ScalarVariableRealStruct[] inputVars = inputFormDataModel_.xmlParsed.getInputVars();

		Vector<ScalarValueRealStruct> scalarValueList = new Vector<ScalarValueRealStruct>();
		
		int len = inputVars.length;

		for (int i = 0; i < len; i++) {
			int idx = inputVars[i].idx;


			String theCell = (String) inData[i][1];
			String newValueStr = (String) tableModel_.getValueAt(i, 1);
			double newValueDouble = Double.valueOf(newValueStr);

			String currentValueDoubleStr = resultInputAry_.get(i);
			
			double currentValueDouble = Double.valueOf(currentValueDoubleStr);

			if (newValueDouble != currentValueDouble) {

				ScalarValueRealStruct scalarValue = new ScalarValueRealStruct();
				scalarValue.idx = idx;
				scalarValue.value = newValueDouble;
				
				scalarValueList.add(scalarValue);
				
			}

		}
		
		
		if (scalarValueList.size() > 0) {
			ScalarValueChangeRequest event = new ScalarValueChangeRequest(this, scalarValueList);
			inputFormController_.onDataModelUpdateRequest(event);
		}
		
		
	}


	/**
	 * New result.
	 *
	 * @param resultInput the result input
	 */
	public void newResult(Vector<String> resultInput) {

		resultInputAry_ = resultInput;
		int len = resultInput.size();


		//populate the 'value' column
		for (int i = 0; i < len; i++) {
			String str = resultInput.get(i);

			tableModel_.setValueAt(str, i, 1);
		}

		table_.updateLayout();
		
		

		
	}




}

package com.sri.straylight.client.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.sri.straylight.client.controller.InputDetailController;
import com.sri.straylight.client.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.voManaged.ResultOfStep;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStructBase;

// TODO: Auto-generated Javadoc
/**
 * The Class InputDetailView.
 */
public class InputDetailView extends JPanel  {

	 /** The xml parsed_. */
 	private XMLparsed xmlParsed_;
	 
 	/** The content panel_. */
 	private JPanel contentPanel_;

	 /** The scalar variable panel list_. */
 	private Vector<JPanel> scalarVariablePanelList_;
	 
 	/** The input detail controller_. */
 	private InputDetailController inputDetailController_;
	 
	/**
	 * Instantiates a new input detail view.
	 *
	 * @param inputDetailController the input detail controller
	 * @param xmlParsed the xml parsed
	 */
	public InputDetailView(InputDetailController inputDetailController, XMLparsed xmlParsed) {

		inputDetailController_ = inputDetailController;
		 
		this.setLayout(new GridLayout(1, 1, 0, 0));
		this.setAlignmentY(Component.LEFT_ALIGNMENT);
		
		contentPanel_ = new JPanel();
		contentPanel_.setLayout(new BoxLayout(contentPanel_, BoxLayout.Y_AXIS));
		contentPanel_.setAlignmentY(Component.LEFT_ALIGNMENT);
		    
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(contentPanel_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    this.add(scrollPaneTable);
	    setModel (xmlParsed);
	}
	
	
	/**
	 * Sets the model.
	 *
	 * @param xmlParsed the new model
	 */
	public void setModel(XMLparsed xmlParsed) {
		
		scalarVariablePanelList_ = new Vector<JPanel>();
		
		
		xmlParsed_ = xmlParsed;
		ScalarVariablesAll  svs = xmlParsed.getScalarVariablesAll();
		 
		ScalarVariableStructBase[] allValues = svs.input.allValues;
		int len = allValues.length;
		
		for (int i = 0; i < len; i++) {
			
			ScalarVariableStructBase base = allValues[i];
			
			if (base instanceof ScalarVariableRealStruct) {
				
				ScalarVariableRealStruct sv = (ScalarVariableRealStruct) base;
				showReal(sv);
			} else 	if (base instanceof ScalarVariableBooleanStruct) {
				
				ScalarVariableBooleanStruct sv = (ScalarVariableBooleanStruct) base;
				showBoolean(sv);
			}
			
		}

	}
	
	
	/**
	 * On data model update request.
	 *
	 * @param event the event
	 */
	public void onDataModelUpdateRequest(ScalarValueChangeRequest event) {

		inputDetailController_.onDataModelUpdateRequest(event);
	}
	
	
	/**
	 * Show real.
	 *
	 * @param sv the sv
	 */
	public void showReal(ScalarVariableRealStruct sv) {

		ScalarVariableRealPanel panel = new ScalarVariableRealPanel(this); // FlowLayout 
		panel.setMetaData(sv);

		scalarVariablePanelList_.add(panel);
		
		contentPanel_.add(leftJustify( panel ));
		
	}
	
	/**
	 * Show boolean.
	 *
	 * @param sv the sv
	 */
	public void showBoolean(ScalarVariableBooleanStruct sv) {
		
		ScalarVariableBooleanPanel panel = new ScalarVariableBooleanPanel(this); // FlowLayout 
		panel.setMetaData(sv);

		scalarVariablePanelList_.add(panel);
		
		contentPanel_.add(leftJustify( panel ));
	}
	
	
	/**
	 * Left justify.
	 *
	 * @param panel the panel
	 * @return the component
	 */
	private Component leftJustify( JPanel panel )  {
	    Box  b = Box.createHorizontalBox();
	    b.add( panel );
	    b.add( Box.createHorizontalGlue() );
	    // (Note that you could throw a lot more components
	    // and struts and glue in here.)
	    return b;
	}

	

	


	/**
	 * Sets the result.
	 *
	 * @param scalarValueResults the new result
	 */
	public void setResult(ScalarValueResults scalarValueResults) {

		
		Vector<String> valueList = scalarValueResults.input.getStringList();
		int len = valueList.size();
		
		for (int i = 0; i < len; i++) {
			
			String str = valueList.get(i);
			JPanel p = scalarVariablePanelList_.get(i);
			
			if (p instanceof ScalarVariableRealPanel) {
				
				ScalarVariableRealPanel realPanel = (ScalarVariableRealPanel) p;
				
				ScalarValueRealStruct sValue = new ScalarValueRealStruct();
				sValue.value = Double.valueOf(str);
				realPanel.setValue(sValue);

			} else if (p instanceof ScalarVariableBooleanPanel) {
				
				ScalarVariableBooleanPanel booleanPanel = (ScalarVariableBooleanPanel) p;
				
				ScalarValueBooleanStruct scalarValueBooleanStruct = new ScalarValueBooleanStruct();
				
				scalarValueBooleanStruct.value = Boolean.valueOf(str);
				booleanPanel.setValue(scalarValueBooleanStruct);
				
			}
			

			
		}
		
		
		
	}
	
	
}

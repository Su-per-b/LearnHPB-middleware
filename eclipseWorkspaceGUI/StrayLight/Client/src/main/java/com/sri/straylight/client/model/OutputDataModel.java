package com.sri.straylight.client.model;

import javax.swing.table.DefaultTableModel;

import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;


/**
 * The Class InputFormDataModel.
 */
public class OutputDataModel {

    /** The xml parsed. */
    private XMLparsedInfo xmlParsed_;

    
    
    
    
	public OutputDataModel(XMLparsedInfo xmlParsedArg) {
		xmlParsed_ = xmlParsedArg;
	}


	public DefaultTableModel getTableModel() {
		
		DefaultTableModel tableModel = new DefaultTableModel (
				xmlParsed_.getOutputFormData(),
				xmlParsed_.getOutputFormColumnNames()
		);
		
		return tableModel;
	}
    
    
    
}

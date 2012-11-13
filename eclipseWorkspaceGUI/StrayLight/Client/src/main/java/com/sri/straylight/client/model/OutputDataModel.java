package com.sri.straylight.client.model;

import javax.swing.table.DefaultTableModel;

import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;


/**
 * The Class InputFormDataModel.
 */
public class OutputDataModel {

    /** The xml parsed. */
    private XMLparsed xmlParsed_;

    
    
    
    
	public OutputDataModel(XMLparsed xmlParsedArg) {
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

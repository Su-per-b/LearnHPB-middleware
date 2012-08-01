package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

public class XMLparsedEvent extends EventObject {
	
	private static final long serialVersionUID = 1L;
	public ConfigStruct metaDataStruct;
	public XMLparsed xmlParsed;
	
    //here's the constructor
    public XMLparsedEvent(Object source, XMLparsed xmlParsedArg, ConfigStruct metaDataStruct_arg) {
        super(source);
        
        xmlParsed = xmlParsedArg;
        metaDataStruct = metaDataStruct_arg; 
    }


    

	
}

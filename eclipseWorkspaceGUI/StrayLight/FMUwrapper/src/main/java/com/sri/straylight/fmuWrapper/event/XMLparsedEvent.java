package com.sri.straylight.fmuWrapper.event;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import com.sri.straylight.fmuWrapper.FMUcontroller;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;

public class XMLparsedEvent extends EventObject {
	
	private static final long serialVersionUID = 1L;
	public ConfigStruct metaDataStruct;
	public XMLparsed xmlParsed;
	
    //here's the constructor
    public XMLparsedEvent(Object source, XMLparsed xmlParsedStruct, ConfigStruct metaDataStruct_arg) {
        super(source);
        
        xmlParsed = xmlParsedStruct;
        metaDataStruct = metaDataStruct_arg; 
    }


    

	
}

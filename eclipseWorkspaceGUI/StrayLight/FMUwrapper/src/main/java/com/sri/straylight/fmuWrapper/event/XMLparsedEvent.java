package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;

import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLparsedEvent.
 */
public class XMLparsedEvent extends EventObject {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The meta data struct. */
	public ConfigStruct metaDataStruct;
	
	/** The xml parsed. */
	public XMLparsed xmlParsed;
	
    //here's the constructor
    /**
     * Instantiates a new xM lparsed event.
     *
     * @param source the source
     * @param xmlParsedArg the xml parsed arg
     * @param metaDataStruct_arg the meta data struct_arg
     */
    public XMLparsedEvent(Object source, XMLparsed xmlParsedArg, ConfigStruct metaDataStruct_arg) {
        super(source);
        
        xmlParsed = xmlParsedArg;
        metaDataStruct = metaDataStruct_arg; 
    }

}

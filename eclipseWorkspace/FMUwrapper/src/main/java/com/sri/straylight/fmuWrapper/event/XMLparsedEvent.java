package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLparsedEvent.
 */
public class XMLparsedEvent extends BaseEvent<XMLparsedInfo> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    public XMLparsedEvent(Object source, XMLparsedInfo payload) {
        super(source, payload);
    }
    

    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        XMLparsedEvent typedObj = (XMLparsedEvent) obj;
        
        return new EqualsBuilder().
                append(this.payload_, typedObj.getPayload()).
                isEquals();

    }  
    
}

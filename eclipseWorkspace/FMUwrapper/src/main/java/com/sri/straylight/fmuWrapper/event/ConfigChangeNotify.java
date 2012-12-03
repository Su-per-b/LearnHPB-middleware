package com.sri.straylight.fmuWrapper.event;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigChangeNotify.
 */
public class ConfigChangeNotify extends BaseEvent<ConfigStruct> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigChangeNotify(Object source, ConfigStruct configStruct) {
        super(source, configStruct);
    }
	
    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        ResultEvent typedObj = (ResultEvent) obj;
        
        return new EqualsBuilder().
                append(this.payload_, typedObj.getPayload()).
                isEquals();

    }
    
}

package com.sri.straylight.fmuWrapper.voNative;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sun.jna.Structure;


// TODO: Auto-generated Javadoc
/**
 * The Class ConfigStruct.
 */
public class ConfigStruct  
	extends Structure
	implements Iserializable
 {
	
	/** The default experiment struct. */
	public DefaultExperimentStruct.ByReference defaultExperimentStruct;
	
	/** The step delta. */
	public double stepDelta;

	
	public String toJsonString() {
		return JsonController.getInstance().toJsonString(this);
	}
	
	
    @Override
    public boolean equals(Object obj) {
    	
    	

        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        ConfigStruct typedObj = (ConfigStruct) obj;

        return new EqualsBuilder().
            append(typedObj.stepDelta, typedObj.stepDelta).
            append(this.defaultExperimentStruct, typedObj.defaultExperimentStruct).

            isEquals();

    }
    
    
}

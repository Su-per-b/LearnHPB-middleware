package com.sri.straylight.fmuWrapper.voNative;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sun.jna.Structure;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct.ByReference;


// TODO: Auto-generated Javadoc
/**
 * The Class ConfigStruct.
 */
public class ConfigStruct  
	extends Structure
	implements JsonSerializable
 {
	
	/** The default experiment struct. */
	public DefaultExperimentStruct.ByReference defaultExperimentStruct;
	
	/** The step delta. */
	public double stepDelta;

	
	public String toJson() {
		return JsonController.getInstance().toJson(this);
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

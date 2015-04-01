package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sun.jna.Structure;


/**
 * The Class MessageStruct.
 */
public class DisplayUnitDefinitionStruct extends Structure implements Iserializable {

	public String displayUnit;

	public int displayUnitValueStatus;

	public double offset;

	public int offsetValueStatus;	

	public double gain;

	public int gainValueStatus;	
	
	private boolean serializeType_ = true;
	

	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}

	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(this.displayUnit).
            append(this.displayUnitValueStatus).
            append(this.offset).
            append(this.offsetValueStatus).
            append(this.gain).
            append(this.gainValueStatus).
            toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        DisplayUnitDefinitionStruct struct = (DisplayUnitDefinitionStruct) obj;
        
        return new EqualsBuilder().
            append(this.displayUnit, struct.displayUnit).
            append(this.displayUnitValueStatus, struct.displayUnitValueStatus).
            append(this.offset, struct.offset).
            append(this.offsetValueStatus, struct.offsetValueStatus).
            append(this.gain, struct.gain).
            append(this.gainValueStatus, struct.gainValueStatus).
            isEquals();
    }
    
    
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] { "displayUnit", "displayUnitValueStatus","offset", "offsetValueStatus","gain", "gainValueStatus" });
	}

	public boolean getSerializeType() {
		return serializeType_;
	}
	
	public void setSerializeType(boolean serializeType) {
		serializeType_ = serializeType;
	}
	
	
}

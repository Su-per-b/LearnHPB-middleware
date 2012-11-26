package com.sri.straylight.fmuWrapper.voManaged;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueBooleanStruct;

public class ScalarValueBoolean extends BaseScalarValue {
	
	
	/** The string. */
	private Boolean value_;
	
	
	public ScalarValueBoolean() {
	}

	/**
	 * Instantiates a new scalar value.
	 */
	public ScalarValueBoolean(int i, Boolean v) {
		super(i);
		value_ = v;
	}
	
	
	public ScalarValueBoolean(ScalarValueBooleanStruct boolStruct) {
		super(boolStruct.idx);
		value_ = boolStruct.value;
	}




	public String toString() {
		return Boolean.toString(value_);
	}

	public Boolean getValue() {
		return value_;
	}
	
	 @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers

            append(this.value_).
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

        ScalarValueBoolean typedObj = (ScalarValueBoolean) obj;
        
        return new EqualsBuilder().
            append(this.value_, typedObj.getValue()).
            append(this.idx_, typedObj.getIdx()).
            isEquals();
    }

}

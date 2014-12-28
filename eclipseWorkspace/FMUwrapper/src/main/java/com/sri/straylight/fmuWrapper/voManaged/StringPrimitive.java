package com.sri.straylight.fmuWrapper.voManaged;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;

public class StringPrimitive extends JsonSerializable {

	private String value_;
	
	
	public StringPrimitive(String  value) {
		this.value_ = value;	
	}
	
	public String getValue() {
	    return this.value_;
	}
	
	public String toString() {
		return this.value_;
	}
	
	
    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        StringPrimitive typedObj = (StringPrimitive) obj;
        
        return new EqualsBuilder().
            append(this.value_, typedObj.getValue()).
            isEquals();
    }
    
}

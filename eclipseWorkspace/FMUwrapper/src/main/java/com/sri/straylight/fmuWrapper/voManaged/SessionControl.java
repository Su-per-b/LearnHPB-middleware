package com.sri.straylight.fmuWrapper.voManaged;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;

public class SessionControl 
		implements JsonSerializable
{

	protected int idx_;
	protected String value_;
	


	
	//constructor 1
	public SessionControl(int idx, String value) {
		super();
		
		idx_ = idx;
		value_ = value;
	}

	
	public String getValue() {
		return value_;
	}
	
	public int getIdx() {
		return idx_;
	}
	

	@Override
	public String toJsonString() {
		return JsonController.getInstance().toJsonString(this);
	}
	
	
	 @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers

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

//        SessionControlEvent typedObj = (SessionControlEvent) obj;
        
        return new EqualsBuilder().
            isEquals();
    }





}

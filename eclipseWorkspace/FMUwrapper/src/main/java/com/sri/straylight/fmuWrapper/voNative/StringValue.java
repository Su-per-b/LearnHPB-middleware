package com.sri.straylight.fmuWrapper.voNative;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSpecReal.
 */
public class StringValue  
		extends Structure
		implements Iserializable

{
	
	/**
	 * The Class ByReference.
	 */
	public static class ByValue extends StringValue implements Structure.ByValue { }
	
	public String value;
	public int status;
	
	private boolean serializeType_ = false;
	
	
	public ValueStatus getStatusAsEnum() {

		ValueStatus theEnum = ValueStatus.valueDefined;
		theEnum = theEnum.getForValue(status);

		return theEnum;
	}
	
	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(this.value).
            append(this.status).
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

        StringValue typedObj = (StringValue) obj;

        return new EqualsBuilder().
        		
            append(this.value, typedObj.value).
            append(this.status, typedObj.status).
            isEquals();

    }
    
    
	public boolean getSerializeType() {
		return serializeType_;
	}
	
	public void setSerializeType(boolean serializeType) {
		serializeType_ = serializeType;
	}
	
	@Override
	protected List<String> getFieldOrder() {
	    return Arrays.asList(new String[] {  "value", "status"});
	}
}

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
public class TypeDefinitionBoolean  
		extends Structure
		implements Iserializable

{
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends TypeDefinitionBoolean implements Structure.ByReference { }
	
	public int idx;
	
	public StringValue.ByValue name;
	public StringValue.ByValue unit;
	public StringValue.ByValue quantity;
	public StringValue.ByValue displayUnit;
	
	public BooleanValue.ByValue start;
	public BooleanValue.ByValue fixed;
	
	private boolean serializeType_ = true;
	
	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	
	
	
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(this.idx).
            append(this.name).
            append(this.unit).
            append(this.quantity).
            append(this.displayUnit).
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

        TypeDefinitionBoolean typedObj = (TypeDefinitionBoolean) obj;

        return new EqualsBuilder().
        		
            append(this.idx, typedObj.idx).
            append(this.name, typedObj.name).
            append(this.unit, typedObj.unit).
            append(this.quantity, typedObj.quantity).
            append(this.displayUnit, typedObj.displayUnit).     
            append(this.start, typedObj.start).
            append(this.fixed, typedObj.fixed).
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
	    return Arrays.asList(new String[] {  "idx", "name", "unit", "quantity", "displayUnit","start", 
	    		"fixed"});
	}
}

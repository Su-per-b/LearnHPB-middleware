package com.sri.straylight.fmuWrapper.voNative;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSpecReal.
 */
public class TypeSpecReal  
		extends Structure
		implements Iserializable

{
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends TypeSpecReal implements Structure.ByReference { }
	
	public double start;
	public double nominal;
	public double min;
	public double max;
	public String unit;
	
	public int startValueStatus = 0;
	public int nominalValueStatus = 0;
	public int minValueStatus = 0;
	public int maxValueStatus = 0;
	public int unitValueStatus = 0;


	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(this.start).
            append(this.nominal).
            append(this.min).
            append(this.max).
            append(this.startValueStatus).
            append(this.nominalValueStatus).
            append(this.minValueStatus).
            append(this.maxValueStatus).
            append(this.unitValueStatus).
            append(this.unit).
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

        TypeSpecReal typedObj = (TypeSpecReal) obj;

        return new EqualsBuilder().
            append(this.start, typedObj.start).
            append(this.nominal, typedObj.nominal).
            append(this.min, typedObj.min).
            append(this.max, typedObj.max).
            append(this.startValueStatus, typedObj.startValueStatus).
            append(this.nominalValueStatus, typedObj.nominalValueStatus).
            append(this.minValueStatus, typedObj.minValueStatus).
            append(this.maxValueStatus, typedObj.maxValueStatus).
            append(this.unitValueStatus, typedObj.unitValueStatus).
            append(this.unit, typedObj.unit).
            isEquals();

    }
    
}

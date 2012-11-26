package com.sri.straylight.fmuWrapper.voNative;

import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueBoolean;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSpecReal.
 */
public class TypeSpecReal  
		extends Structure
		implements JsonSerializable

{
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends TypeSpecReal implements Structure.ByReference { }
	
	/** The start. */
	public double start;
	
	/** The nominal. */
	public double nominal;
	
	/** The min. */
	public double min;
	
	/** The max. */
	public double max;

	/** The start value status. */
	public int startValueStatus;
	
	/** The nominal value status. */
	public int nominalValueStatus;
	
	/** The min value status. */
	public int minValueStatus;
	
	/** The max value status. */
	public int maxValueStatus;

	
	public String toJson() {
		return JsonController.getInstance().toJson(this);
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
            isEquals();

    }
    
}

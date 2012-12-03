package com.sri.straylight.fmuWrapper.voManaged;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

public class ScalarValueReal extends BaseScalarValue {
	
	public static final int PRECISION = 3;
	
	/** The string. */
	private Double value_;
	
	
	
	/**
	 * Instantiates a new scalar value.
	 */
	public ScalarValueReal(int i, Double v) {
		super(i);
		value_ = v;
	}
	
	
	public ScalarValueReal(ScalarValueRealStruct realStruct) {
		super(realStruct.idx);
		value_ = realStruct.value;
	}


	
	public ScalarValueReal() {
		super();
	}


	public Double getValue() {
		return value_;
	}
	
	
	public String toString() {
		
		BigDecimal bd = new BigDecimal(value_).setScale(
				ScalarValueReal.PRECISION, RoundingMode.HALF_UP);
		
		return bd.toString();
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

        ScalarValueReal typedObj = (ScalarValueReal) obj;
        
        return new EqualsBuilder().
            append(this.value_, typedObj.getValue()).
            append(this.idx_, typedObj.getIdx()).
            isEquals();
    }
    
}

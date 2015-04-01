package com.sri.straylight.fmuWrapper.voNative;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarValueRealStruct.
 */
public class ScalarValueRealStruct extends Structure implements Iserializable {
	
	public static class ByReference extends ScalarValueRealStruct implements Structure.ByReference { }
	
	/** The idx. */
	public int idx;
	
	/** The value. */
	public double value;
	  
	private boolean serializeType_ = true;
	
	
	/**
	 * Gets the string.
	 *
	 * @return the string
	 */
	public String toString() {
		
		BigDecimal bd = new BigDecimal(value).setScale(3, RoundingMode.HALF_UP);
		
		return bd.toString();
		
	}
	
	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	
	
    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null)
            return false;
        
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        ScalarValueRealStruct typedObj = (ScalarValueRealStruct) obj;

        return new EqualsBuilder().
            append(typedObj.idx, typedObj.idx).
            append(typedObj.value, typedObj.value).

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
	    return Arrays.asList(new String[] { "idx", "value"});
	}
	
	
	
}

package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;

public class ScalarVariableReal 
		extends BaseScalarVariable 
		implements JsonSerializable
{


	private TypeSpecReal typeSpecReal_;
	
	/**
	 * @return the typeSpecReal_
	 */
	public TypeSpecReal getTypeSpecReal() {
		return typeSpecReal_;
	}

	
	public ScalarVariableReal(ScalarVariableRealStruct struct) {
		
		super(struct);
		typeSpecReal_ = struct.typeSpecReal;
	}

	public ScalarVariableReal(TypeSpecReal typeSpecReal2) {
		typeSpecReal_ = typeSpecReal2;
	}
	

	public ScalarVariableReal() {

	}


	public String[] toStringArray() {
		
		String[] ary  = {
				getName(),
				"{not set}",
				"Real",
				doubleToString(getTypeSpecReal().start),
				doubleToString(getTypeSpecReal().nominal),
				doubleToString(getTypeSpecReal().min),
				doubleToString(getTypeSpecReal().max),
				getCausalityEnum().toString(),
				getVariabilityEnum().toString(),
				getDescription()
		};
		
		return ary;
		
	}


	public static Vector<ScalarVariableReal> makeList (
			ScalarVariableRealStruct[] realVarStruct) {
			
		Vector<ScalarVariableReal> list = new Vector<ScalarVariableReal>();
		
		int len = realVarStruct.length;
		for (int i = 0; i < len; i++) {
			ScalarVariableRealStruct struct = realVarStruct[i];
			ScalarVariableReal scalarVariableReal = new ScalarVariableReal(struct);
			list.add(scalarVariableReal);
		}
		
		
		return list;
	}


	@Override
	public String toJson() {
		return JsonController.getInstance().toJson(this);
	}
	
	
	 @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers

            append(typeSpecReal_.hashCode()).
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

        ScalarVariableReal typedObj = (ScalarVariableReal) obj;
        
        return new EqualsBuilder().
            append(this.typeSpecReal_, typedObj.getTypeSpecReal()).
            isEquals();
    }


	public void setTypeSpecReal(TypeSpecReal typeSpecReal) {
		typeSpecReal_ = typeSpecReal;
	}


}

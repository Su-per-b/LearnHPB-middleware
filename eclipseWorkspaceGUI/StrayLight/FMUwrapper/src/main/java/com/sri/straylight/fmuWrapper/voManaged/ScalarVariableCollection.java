package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableCollection.
 */
public class ScalarVariableCollection implements JsonSerializable{

	private Vector<ScalarVariableReal> realVarList_;
	private Vector<ScalarVariableBoolean> booleanVarList_;
	
	/** The max array size. */
	public int maxArraySize = 0;
	
	/** The total size. */
	public int totalSize;
	


	/**
	 * @return the realValue
	 */
	public Vector<ScalarVariableReal> getRealVarList() {
		return realVarList_;
	}

	/**
	 * @param realValue the realValue to set
	*/
	public void setRealVarList(Vector<ScalarVariableReal> realVarList) {
		realVarList_ = realVarList;
	}
		 
	
	
	/**
	 * Instantiates a new scalar variable collection.
	 *
	 * @param struct the struct
	 * @param maxArraySize TODO
	 * @param maxArraySize TODO
	 */
	public ScalarVariableCollection(ScalarVariableCollectionStruct.ByReference struct, int maxArraySize) {
		init(struct,maxArraySize);
	}
	
	public ScalarVariableCollection(ScalarVariableCollectionStruct.ByReference struct) {
		init(struct,0);
	}
	

	public ScalarVariableCollection() {

	}
	

	private void init(ScalarVariableCollectionStruct.ByReference struct, int maxArraySize) {
		

		ScalarVariableRealStruct[] realAry = struct.getRealAsArray(maxArraySize);
		if (realAry != null) {
			totalSize += realAry.length;
		}

		realVarList_ = ScalarVariableReal.makeList(realAry);
		
		
		ScalarVariableBooleanStruct[] booleanAry = struct.getBooleanAsArray(maxArraySize);
		if (booleanVarList_ != null) {
			totalSize += booleanAry.length;
		}
		
		booleanVarList_ = ScalarVariableBoolean.makeList(booleanAry);
	}
	
	@Override
	public String toJson() {
		return JsonController.getInstance().toJson(this);
	}
	
	 @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers

            append(realVarList_.hashCode()).
            append(booleanVarList_.hashCode()).
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

        ScalarVariableCollection typedObj = (ScalarVariableCollection) obj;
        
        return new EqualsBuilder().
            append(this.realVarList_, typedObj.getRealVarList()).
            isEquals();
    }
    
}

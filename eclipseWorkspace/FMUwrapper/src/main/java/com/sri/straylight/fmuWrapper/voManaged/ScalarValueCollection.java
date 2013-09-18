package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Vector;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

public class ScalarValueCollection implements JsonSerializable {
	
	
	private Vector<ScalarValueReal> realList_;
	private Vector<ScalarValueBoolean> booleanList_;
	
	private Vector<BaseScalarValue> valueList_;
	
	

	public ScalarValueCollection() {
		valueList_ = new Vector<BaseScalarValue>();
	}
	
	/**
	 * Instantiates a new ScalarValueCollection from a struct.
	 *
	 * @param st1 the struct
	 */
	public ScalarValueCollection(ScalarValueCollectionStruct struct) {
		
		init_(struct);
	}
	

	
	
	public ScalarValueCollection(Vector<ScalarValueReal> realList, Vector<ScalarValueBoolean> booleanList) {
		valueList_ = new Vector<BaseScalarValue>();
		
		setRealList(realList);
		setBooleanList(booleanList);
		
	}
	
	
	
	
	@Override
	public String toJson() {
		return JsonController.getInstance().toJson(this);
	}
	
	private void init_(ScalarValueCollectionStruct struct) {
		valueList_ = new Vector<BaseScalarValue>();
		
		initReal_(struct);
		initBoolean_(struct);

	}
	

	

	
	public int size() {
		
		int size = valueList_.size();
		return size;
	}

	private void initBoolean_(ScalarValueCollectionStruct struct) {
		
		booleanList_ = new Vector<ScalarValueBoolean>();
		
		
		if (struct.booleanSize > 0) {
			
			ScalarValueBooleanStruct[] aryBoolean = struct.getBooleanAsArray(0);	
			int len = aryBoolean.length;
			
			if (struct.booleanSize != len) {
				try {
					throw new Exception("Error converting Array");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			
			for (int i = 0; i < len; i++) {
				ScalarValueBooleanStruct boolStruct = aryBoolean[i];
				
				ScalarValueBoolean sv = new ScalarValueBoolean(boolStruct);
				
				
				booleanList_.add(sv);
				valueList_.add(sv);
				
			}
			
		}

	}
	
	public void setRealList(Vector<ScalarValueReal> realList) {
		realList_ = realList;
		
		
		int len = realList_.size();
		for (int i = 0; i < len; i++) {
			ScalarValueReal sv = realList_.get(i);
			valueList_.add(sv);
		}
		
	}
	
	public Vector<ScalarValueReal> getRealList() {
		return realList_;
	}
	
	public void setBooleanList(Vector<ScalarValueBoolean> booleanList) {
		booleanList_ = booleanList;
		
		int len = booleanList_.size();
		
		for (int i = 0; i < len; i++) {
			ScalarValueBoolean sv = booleanList_.get(i);
			valueList_.add(sv);
		}
	}
	
	public Vector<ScalarValueBoolean> getBooleanList() {
		return booleanList_;
		
		

	}
	
	
	private void initReal_(ScalarValueCollectionStruct struct) {
		
		ScalarValueRealStruct[] aryReal = struct.getRealAsArray(0);	
		int len = aryReal.length;
		
		realList_ = new Vector<ScalarValueReal>();
		
		
		for (int i = 0; i < len; i++) {
			ScalarValueRealStruct realStruct = aryReal[i];
			
			ScalarValueReal sv = new ScalarValueReal(realStruct);
			
			realList_.add(sv);
			valueList_.add(sv);
		}
	}
	
	


	public BaseScalarValue get(int idx) {
		
		return valueList_.get(idx);
	}
	
	public String toString() {
		
		Vector<String> list = getStringList();
		String str = list.toString();
				
		return str;
	}



	public Vector<String> getStringList() {
		
		int len = realList_.size();

		Vector<String> vector = new Vector<String>();
		
		for (int i = 0; i < len; i++) {
			
			ScalarValueReal scalarValueReal = realList_.get(i);
			String str = scalarValueReal.toString();
			vector.add(str);
		}

		
		return vector;
		
	}
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers

            append(this.realList_).
            append(this.booleanList_).
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

        ScalarValueCollection typedObj = (ScalarValueCollection) obj;
        Vector<ScalarValueReal> realList2 = typedObj.getRealList();
        
        boolean isEqual = true;
        int len = realList_.size();
        for (int i = 0; i < len; i++) {
        	ScalarValueReal scalarValueReal1 = realList_.get(i);
        	ScalarValueReal scalarValueReal2 = realList2.get(i);
        	
        	isEqual = scalarValueReal1.equals(scalarValueReal2);
        	if (!isEqual) {
        		return false;
        	}
		}
        
        Vector<ScalarValueBoolean> booleanList2 = typedObj.getBooleanList();
        
        int len2 = booleanList_.size();
        for (int j = 0; j < len2; j++) {
        	
        	ScalarValueBoolean scalarValueBoolean1 = booleanList_.get(j);
        	ScalarValueBoolean scalarValueBoolean2 = booleanList2.get(j);
        	
        	isEqual = scalarValueBoolean1.equals(scalarValueBoolean2);
        	if (!isEqual) {
        		return false;
        	}
		}
        
        return true;

    }
	
}

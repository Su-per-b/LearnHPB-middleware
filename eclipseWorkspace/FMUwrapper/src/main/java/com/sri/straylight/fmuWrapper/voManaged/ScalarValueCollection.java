package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Vector;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

public class ScalarValueCollection extends JsonSerializable {
	
	
	private SerializableVector<ScalarValueReal> realList_;
	//private SerializableVector<ScalarValueBoolean> booleanList_;
	
	//private Vector<BaseScalarValue> valueList_;
	
	

	public ScalarValueCollection() {
		init2_();
	}
	

	
	/**
	 * Instantiates a new ScalarValueCollection from a struct.
	 *
	 * @param st1 the struct
	 */
	public ScalarValueCollection(ScalarValueCollectionStruct struct) {
		init2_();
		init_(struct);
	}
	
	
	public ScalarValueCollection(SerializableVector<ScalarValueReal> realList) {
		init2_();
		
		setRealList(realList);
//		setBooleanList(booleanList);
		
	}
	
	private void init2_() {
		realList_ = new SerializableVector<ScalarValueReal>("ScalarValueReal");
		
	}
	
	
	

	
	private void init_(ScalarValueCollectionStruct struct) {
		initReal_(struct);
	}
	

	

	
	public int size() {
		
		int size = realList_.size();
		return size;
	}

	
	public void setRealList(SerializableVector<ScalarValueReal> realList) {
		realList_ = realList;
	}
	
	public SerializableVector<ScalarValueReal> getRealList() {
		return realList_;
	}
	
	
	public ScalarValueRealStruct[] getRealStructAry() {
		
		
		int len = realList_.size();
		ScalarValueRealStruct[] ary = (ScalarValueRealStruct[]) new ScalarValueRealStruct().toArray(len);

		
		for (int i = 0; i < len; i++) {
			ScalarValueReal real = realList_.get(i);
			
			//ScalarValueRealStruct struct = real.toStruct();
			ary[i].idx = real.getIdx();
			ary[i].value = real.getValue();
		}
		
		
		return ary;
	}
	

	
	private void initReal_(ScalarValueCollectionStruct struct) {
		
		ScalarValueRealStruct[] aryReal = struct.getRealAsArray(0);	
		int len = aryReal.length;
		
		realList_ = new SerializableVector<ScalarValueReal>("ScalarValueReal");
		
		
		for (int i = 0; i < len; i++) {
			ScalarValueRealStruct realStruct = aryReal[i];
			
			ScalarValueReal sv = new ScalarValueReal(realStruct);
			
			realList_.add(sv);
		}
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
           // append(this.booleanList_).
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
        

        
        return true;

    }

	public Vector<ScalarValueRealStruct> getRealStructList() {
		
		Vector<ScalarValueRealStruct> stuctList = new Vector<ScalarValueRealStruct>();
		int len = realList_.size();
		
        for (int i = 0; i < len; i++) {
        	
        	ScalarValueReal real = realList_.get(i);
        	ScalarValueRealStruct realStruct = real.toStruct();
        	
        	stuctList.add(realStruct);

		}
        
        return stuctList;
		
	}
	
}

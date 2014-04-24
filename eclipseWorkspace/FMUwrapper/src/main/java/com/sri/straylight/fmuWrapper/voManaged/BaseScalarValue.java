package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;

public class BaseScalarValue extends JsonSerializable {
	

	/** The idx. */
	protected int idx_;
	
	public BaseScalarValue(int i) {
		setIdx(i);
	}

	
	public BaseScalarValue() {
		
	}


	public int getIdx() {
		return idx_;
	}

	public void setIdx(int idx_) {
		this.idx_ = idx_;
	}
	

	
}

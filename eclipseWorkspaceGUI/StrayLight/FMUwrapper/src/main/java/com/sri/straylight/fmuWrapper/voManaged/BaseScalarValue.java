package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;

public class BaseScalarValue implements JsonSerializable {
	

	
	
	/** The idx. */
	protected int idx_;
	
	public BaseScalarValue(int i) {
		setIdx(i);
	}

	
	public BaseScalarValue() {
		
	}


	@Override
	public String toJson() {
		return JsonController.getInstance().toJson(this);
	}

	public int getIdx() {
		return idx_;
	}

	public void setIdx(int idx_) {
		this.idx_ = idx_;
	}
	
	
}

package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Vector;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;

public class SerializableVector<T> extends Vector<T> implements JsonSerializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public String toJsonString() {
		return JsonController.getInstance().toJsonString(this);
	}
}

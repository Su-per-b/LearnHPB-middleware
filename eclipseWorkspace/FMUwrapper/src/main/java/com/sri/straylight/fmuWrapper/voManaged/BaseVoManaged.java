package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;

public class BaseVoManaged implements JsonSerializable {
	
	
	public String toJsonString() {
		return JsonController.getInstance().toJsonString(this);
	}
	
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
}

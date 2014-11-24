package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Vector;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;

public class SerializableVector<ITEM extends Iserializable> extends Vector<ITEM> implements Iserializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String itemTypeString_;
	
	public SerializableVector(String itemTypeString) {
		super();
		setItemTypeString(itemTypeString);
	}
	
	
	public void setItemTypeString(String itemTypeString) {
		
		itemTypeString_ = itemTypeString;
	}
	
	public String getItemTypeString() {
		
		return itemTypeString_;
	}


	@Override
	public String serialize() {
		return JsonController.getInstance().serialize(this);
	}
	
}

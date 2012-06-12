package com.sri.straylight.client.event.action;

import com.sri.straylight.fmuWrapper.voNative.MetaDataStruct;

public class MetaDataChangeAction {
	
	public MetaDataStruct payload;
	
	public MetaDataChangeAction(MetaDataStruct metaDataStruct_) {
		payload = metaDataStruct_;
	}

	
}

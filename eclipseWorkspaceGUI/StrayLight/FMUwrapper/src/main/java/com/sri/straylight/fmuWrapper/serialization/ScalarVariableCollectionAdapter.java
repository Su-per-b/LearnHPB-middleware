
package com.sri.straylight.fmuWrapper.serialization;


import java.lang.reflect.Type;
import java.util.Vector;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarVariableCollectionAdapter extends AdapterBase<ScalarVariableCollection> {

	final protected Type realListType_ = new TypeToken<Vector<ScalarVariableReal>>(){}.getType();
	
	public ScalarVariableCollectionAdapter() {
		super();
		super.setFieldNames(fieldNames_);
	}
	
	
	@Override
	public JsonElement serialize(ScalarVariableCollection src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		Vector<ScalarVariableReal> realVarList = src.getRealVarList();
		
		JsonElement element = serializationContext_.serialize(realVarList, realListType_);
		
		jsonObject_.add("realVarList_", element);
		return jsonObject_;
	}
	

	@Override
	public ScalarVariableCollection deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

		destObject_ = new ScalarVariableCollection();
		super.deserialize(jsonElement, typeOfT, context);
		
		JsonElement jsonElementReal = jsonObject_.get("realVarList_");
		Vector<ScalarVariableReal> realVarList = context.deserialize(jsonElementReal, realListType_);
		
		destObject_.setRealVarList(realVarList);
		
        
		return destObject_;

	}
	
	
	
	
	
	

}

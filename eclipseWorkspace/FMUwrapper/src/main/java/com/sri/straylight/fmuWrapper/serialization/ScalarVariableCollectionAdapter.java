
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
import com.sri.straylight.fmuWrapper.voManaged.SerializableVector;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarVariableCollectionAdapter extends AdapterBase<ScalarVariableCollection> {

	final protected Type realListType_ = new TypeToken<SerializableVector<ScalarVariableReal>>(){}.getType();
	
	public ScalarVariableCollectionAdapter() {
		
		super(ScalarVariableCollection.class);
		super.init(fieldNames_);
		

	}
	
	
	@Override
	public JsonElement serialize(ScalarVariableCollection src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		SerializableVector<ScalarVariableReal> realVarList = src.getRealVarList();
		
		JsonElement element = serializationContext_.serialize(realVarList);
		
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
		SerializableVector<ScalarVariableReal> realVarList = context.deserialize(jsonElementReal, realListType_);
		
		destObject_.setRealVarList(realVarList);
		
        
		return destObject_;

	}
	
	
	
	
	
	

}

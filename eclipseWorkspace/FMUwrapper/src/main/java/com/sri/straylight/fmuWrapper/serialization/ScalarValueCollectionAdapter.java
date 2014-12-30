
package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.SerializableVector;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarValueCollectionAdapter 
	extends AdapterBase<ScalarValueCollection> {

	final protected Type realListType_ = new TypeToken<SerializableVector<ScalarValueReal>>(){}.getType();

	
	public ScalarValueCollectionAdapter() {
		
		super(ScalarValueCollection.class);
	}
	
	@Override
	public JsonElement serialize(ScalarValueCollection src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		
		SerializableVector<ScalarValueReal> realList =  src.getRealList();
		JsonElement element1  = serializationContext_.serialize(realList);
		jsonObject_.add("realList", element1);
		
		return jsonObject_;
	}
	

	@Override
	public ScalarValueCollection deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {


		destObject_ = new ScalarValueCollection();
		super.deserialize(jsonElement, typeOfT, context);
		
		JsonElement jsonElementReal = jsonObject_.get("realList");
		SerializableVector<ScalarValueReal> realList = context.deserialize(jsonElementReal, realListType_);
		
		destObject_.setRealList(realList);
	

		return destObject_;

	}

}

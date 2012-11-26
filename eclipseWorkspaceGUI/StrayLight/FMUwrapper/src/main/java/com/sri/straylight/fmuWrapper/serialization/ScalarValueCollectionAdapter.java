
package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;
import java.util.Vector;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueBoolean;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarValueCollectionAdapter 
	extends AdapterBase<ScalarValueCollection> {


	public ScalarValueCollectionAdapter() {
		super();
	}
	
	@Override
	public JsonElement serialize(ScalarValueCollection src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		
		Type typeToken1 = new TypeToken<Vector<ScalarValueReal>>(){}.getType();
		Vector<ScalarValueReal> realList =  src.getRealList();
		JsonElement element1  = context_.serialize(realList, typeToken1);
		jsonObject_.add("realList", element1);
		
		Type typeToken2 = new TypeToken<Vector<ScalarValueBoolean>>(){}.getType();
		Vector<ScalarValueBoolean> booleanList =  src.getBooleanList();
		JsonElement element2 = context_.serialize(booleanList, typeToken2);
		jsonObject_.add("booleanList", element2);

		return jsonObject_;
	}
	

	@Override
	public ScalarValueCollection deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

		destObject_ = new ScalarValueCollection();
		super.deserialize(jsonElement, typeOfT, context);
		
		Type typeToken1 = new TypeToken<Vector<ScalarValueReal>>(){}.getType();
		JsonElement jsonElementReal = jsonObject_.get("realList");
		Vector<ScalarValueReal> realList = context.deserialize(jsonElementReal, typeToken1);
		destObject_.setRealList(realList);
		
		Type typeToken2 = new TypeToken<Vector<ScalarValueBoolean>>(){}.getType();
		JsonElement jsonElementBoolean = jsonObject_.get("booleanList");
		Vector<ScalarValueBoolean> booleanList = context.deserialize(jsonElementBoolean, typeToken2);
		destObject_.setBooleanList(booleanList);


		return destObject_;

	}

}


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

	final protected Type realListType_ = new TypeToken<Vector<ScalarValueReal>>(){}.getType();
	final protected Type booleanListType_ = new TypeToken<Vector<ScalarValueBoolean>>(){}.getType();
	
	public ScalarValueCollectionAdapter() {
		super();
	}
	
	@Override
	public JsonElement serialize(ScalarValueCollection src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		
		Vector<ScalarValueReal> realList =  src.getRealList();
		JsonElement element1  = serializationContext_.serialize(realList, realListType_);
		jsonObject_.add("realList", element1);
		
		Vector<ScalarValueBoolean> booleanList =  src.getBooleanList();
		JsonElement element2 = serializationContext_.serialize(booleanList, booleanListType_);
		jsonObject_.add("booleanList", element2);

		return jsonObject_;
	}
	

	@Override
	public ScalarValueCollection deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {


		super.deserialize(jsonElement, typeOfT, context);
		
		JsonElement jsonElementReal = jsonObject_.get("realList");
		Vector<ScalarValueReal> realList = context.deserialize(jsonElementReal, realListType_);
//		destObject_.setRealList(realList);
		
		JsonElement jsonElementBoolean = jsonObject_.get("booleanList");
		Vector<ScalarValueBoolean> booleanList = context.deserialize(jsonElementBoolean, booleanListType_);
//		destObject_.setBooleanList(booleanList);

		destObject_ = new ScalarValueCollection(realList, booleanList);
		
		
		return destObject_;

	}

}

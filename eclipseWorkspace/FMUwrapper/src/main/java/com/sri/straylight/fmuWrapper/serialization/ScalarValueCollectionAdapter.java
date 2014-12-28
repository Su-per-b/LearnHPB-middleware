
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
//	final protected Type booleanListType_ = new TypeToken<Vector<ScalarValueBoolean>>(){}.getType();
	


	
	public ScalarValueCollectionAdapter() {
		
		super(ScalarValueCollection.class);
		//super.init(fieldNames_, new ScalarValueCollection());
	}
	
	@Override
	public JsonElement serialize(ScalarValueCollection src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		
		SerializableVector<ScalarValueReal> realList =  src.getRealList();
		JsonElement element1  = serializationContext_.serialize(realList);
		jsonObject_.add("realList", element1);
		
//		Vector<ScalarValueBoolean> booleanList =  src.getBooleanList();
//		JsonElement element2 = serializationContext_.serialize(booleanList, booleanListType_);
//		jsonObject_.add("booleanList", element2);

		return jsonObject_;
	}
	

	@Override
	public ScalarValueCollection deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

//		super.deserialize(jsonElement, typeOfT, context);
//		
//		SerializableVector<ScalarValueReal> realList = new SerializableVector<ScalarValueReal>("ScalarValueReal");
//		
//		JsonElement realListElement = jsonObject_.getAsJsonArray("realList");
		
		
		
		
		destObject_ = new ScalarValueCollection();
		super.deserialize(jsonElement, typeOfT, context);
		
		JsonElement jsonElementReal = jsonObject_.get("realList");
		SerializableVector<ScalarValueReal> realList = context.deserialize(jsonElementReal, realListType_);
		
		destObject_.setRealList(realList);
	
		
//		
//		int len = jsonArray.size();
//		
//		for (int i = 0; i < len; i++) {
//			
// 			JsonElement jsonElementReal = jsonArray.get(i);
//			ScalarValueReal scalarValueReal = context.deserialize(jsonElementReal, ScalarValueReal.class);
//			realList.add(scalarValueReal);
//			
//		}
//		Vector<ScalarValueBoolean> booleanList = new Vector<ScalarValueBoolean>();
//		
//		JsonArray jsonArray2 = jsonObject_.getAsJsonArray("booleanList");
//		int len2 = jsonArray2.size();
//		for (int j = 0; j < len2; j++) {
//			
//			JsonElement jsonElementReal = jsonArray.get(j);
//			ScalarValueBoolean scalarValueBoolean = context.deserialize(jsonElementReal, ScalarValueBoolean.class);
//			booleanList.add(scalarValueBoolean);
//			
//		}
//		
//		destObject_.setBooleanList(booleanList);
		


		
		return destObject_;

	}

}

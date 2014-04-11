
package com.sri.straylight.fmuWrapper.serialization;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueBoolean;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarValueBooleanAdapter 
	extends AdapterBase<ScalarValueBoolean> {

	
	final protected String[][] fieldNamesEx_ = { 
			{ "idx_", "i" },
			{ "value_", "v" }
		};

	
	public ScalarValueBooleanAdapter() {
		super(ScalarValueBoolean.class);
		super.init(fieldNamesEx_);
		
	}
	
	
//	@Override
//	public JsonElement serialize(ScalarValueBoolean src, Type typeOfSrc,
//			JsonSerializationContext context) {
//
//		super.serialize(src, typeOfSrc, context);
//		
//		//JsonPrimitive primitive = new JsonPrimitive(src.getIdx());
//		//jsonObject_.add("idx_", primitive);
//		
//		return jsonObject_;
//	}
	

//	@Override
//	public ScalarValueBoolean deserialize(JsonElement jsonElement, Type typeOfT,
//			JsonDeserializationContext context)
//
//	throws JsonParseException {
//
//		destObject_ = new ScalarValueBoolean();
//		super.deserialize(jsonElement, typeOfT, context);
//		
//		
//		int idx = jsonObject_.get("idx_").getAsInt();
//		destObject_.setIdx(idx);
//		
//		
//		
//		return destObject_;

	//}

}

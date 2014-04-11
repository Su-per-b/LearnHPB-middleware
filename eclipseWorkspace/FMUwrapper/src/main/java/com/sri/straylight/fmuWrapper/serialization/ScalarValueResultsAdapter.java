package com.sri.straylight.fmuWrapper.serialization;



import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarValueResultsAdapter extends AdapterBase<ScalarValueResults> {

	final protected String[] fieldNames_ = { "time_" };

	
	public ScalarValueResultsAdapter() {
		
		super(ScalarValueResults.class);
		super.init(fieldNames_);
		

	}
	
	
	@Override
	public JsonElement serialize(ScalarValueResults src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		

		ScalarValueCollection input =  src.getInput();
		JsonElement element = serializationContext_.serialize(input, ScalarValueCollection.class);
		jsonObject_.add("input", element);
		
		ScalarValueCollection output =  src.getOutput();
		JsonElement element2 = serializationContext_.serialize(output, ScalarValueCollection.class);
		jsonObject_.add("output", element2);
		
		
		return jsonObject_;
	}
	

	@Override
	public ScalarValueResults deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

		destObject_ = new ScalarValueResults();
		
    	super.deserialize(jsonElement, typeOfT, context);
    	
        JsonElement jsonElementInput = jsonObject_.get("input");
        ScalarValueCollection input = context.deserialize(jsonElementInput, ScalarValueCollection.class);
        destObject_.setInput(input);
        
        JsonElement jsonElementOutput = jsonObject_.get("output");
        ScalarValueCollection output = context.deserialize(jsonElementOutput, ScalarValueCollection.class);
        destObject_.setOutput(output);
        
        
		return destObject_;

	}
	
	
	
	
	
	

}

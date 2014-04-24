
package com.sri.straylight.fmuWrapper.serialization;



import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarVariablesAllAdapter extends AdapterBase<ScalarVariablesAll> {


	public ScalarVariablesAllAdapter() {
		super(ScalarVariablesAll.class);
	}
	
	
	@Override
	public JsonElement serialize(ScalarVariablesAll src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);

		jsonObjectAdd_("input", src.getInput());
		jsonObjectAdd_("output", src.getOutput());
		
		
		ScalarVariableCollection internal = src.getInternal();
		if (null != internal) {
			jsonObjectAdd_("internal", internal);
		}


		return jsonObject_;
	}
	



	@Override
	public ScalarVariablesAll deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context)

	throws JsonParseException {

		destObject_ = new ScalarVariablesAll();
    	super.deserialize(jsonElement, typeOfT, context);
    	

    	
    	//input
        JsonElement jsonElement1 = jsonObject_.get("input");
        
        ScalarVariableCollection sVarColl = deserializationContext_.deserialize
        		(jsonElement1, ScalarVariableCollection.class);
        
        destObject_.setInput(sVarColl);
        
        
        //output
        JsonElement jsonElement2 = jsonObject_.get("output");
        
        ScalarVariableCollection sVarColl2 = deserializationContext_.deserialize
        		(jsonElement2, ScalarVariableCollection.class);
        
        destObject_.setOutput(sVarColl2);
        
        
        //internal
        JsonElement jsonElement3 = jsonObject_.get("internal");
        
        ScalarVariableCollection sVarColl3 = deserializationContext_.deserialize
        		(jsonElement3, ScalarVariableCollection.class);
        
        destObject_.setInternal(sVarColl3);

        
        
		return destObject_;

	}



	
	
	
	
	
	

}

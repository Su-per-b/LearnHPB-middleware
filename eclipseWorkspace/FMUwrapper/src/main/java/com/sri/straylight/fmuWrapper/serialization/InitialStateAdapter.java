package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.InitialState;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.SerializableVector;
import com.sri.straylight.fmuWrapper.voManaged.StringPrimitive;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;


/**
 * The Class MessageEventAdapter.
 */
public class InitialStateAdapter 
	extends AdapterBase<InitialState>
{

	
	public InitialStateAdapter() {
		super(InitialState.class);
	}
	
	@Override
	public JsonElement serialize(InitialState src, Type typeOfSrc,
			JsonSerializationContext context) {

		super.serialize(src, typeOfSrc, context);
		
		//parameters_
		ScalarValueCollection parameters = src.getParameters();
		JsonElement element_0 = serializationContext_.serialize(parameters, ScalarValueCollection.class);
		jsonObject_.add("parameters", element_0);
		
		//configStruct
		ConfigStruct configStruct = src.getConfigStruct();
		JsonElement element_1 = serializationContext_.serialize(configStruct, ConfigStruct.class);
		jsonObject_.add("configStruct", element_1);
		
		//outputVarList
		SerializableVector<StringPrimitive> outputVarList = src.getOutputVarList();		
		JsonElement element_2 = serializationContext_.serialize(outputVarList, SerializableVector.class);
		jsonObject_.add("outputVarList", element_2);
		

		return jsonObject_;
	}
	
    @Override
    public InitialState deserialize (
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
        throws JsonParseException {
    	
		destObject_ = new InitialState();
		super.deserialize(jsonElement, typeOfT, context);
		
		//parameters_
        JsonElement jsonElement1 = jsonObject_.get("parameters");
        
        ScalarValueCollection parameters = deserializationContext_.deserialize
        		(jsonElement1, ScalarValueCollection.class);
        
        
        destObject_.setParameters(parameters);

        //configStruct_
        JsonElement jsonElement2 = jsonObject_.get("configStruct");
        
        ConfigStruct configStruct = deserializationContext_.deserialize
        		(jsonElement2, ConfigStruct.class);
        
        destObject_.setConfigStruct(configStruct);
        
        //outputVarList_
        JsonElement jsonElement3 = jsonObject_.get("outputVarList");
        
        SerializableVector<StringPrimitive> outputVarList = deserializationContext_.deserialize
        		(jsonElement3, SerializableVector.class);
        
        destObject_.setOutputVarList(outputVarList);

        
        return destObject_;

        
    }
    
    
}

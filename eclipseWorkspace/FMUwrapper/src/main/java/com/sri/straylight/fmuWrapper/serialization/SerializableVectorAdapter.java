package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;
import java.util.Vector;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.sri.straylight.fmuWrapper.voManaged.SerializableVector;


/**
 * The Class MessageStructAdapter.
 */
public class SerializableVectorAdapter<ITEM extends JsonSerializable> 
	extends AdapterBase<SerializableVector<ITEM>> {
 

	public SerializableVectorAdapter() {
		
		super();
		typeString_ = "SerializableVector";
		
	}
	

	
	
	@Override
	public JsonElement serialize(SerializableVector<ITEM> src, Type typeOfSrc,
			JsonSerializationContext context) {

		//super.serialize(src, typeOfSrc, context);
		

		
		//Vector<ScalarValueReal> realList =  src.getRealList();
		
		int len = src.size();
		for (int i = 0; i < len; i++) {
			
			JsonSerializable itemObject = src.get(i);
			itemObject.serializeType = false;
			
		}
		
		
		init(src, typeOfSrc, context);
		
		jsonObject_.add("itemType", new JsonPrimitive(src.getItemTypeString()));
		JsonElement element1  = serializationContext_.serialize(src, Vector.class);
		jsonObject_.add("itemArray", element1);
		
		

		return jsonObject_;
	}
	
    public SerializableVector<ITEM> deserializeHelper () {
    	
    	SerializableVector<ITEM> destObject2  = new SerializableVector<ITEM>("");
   	 	return destObject2;
	}

    
    
    @Override
    public SerializableVector<ITEM> deserialize(
    		JsonElement jsonElement, 
    		Type typeOfT, 
    		JsonDeserializationContext context)
    
        throws JsonParseException {
    	
    	
		jsonElement_ = jsonElement;
		jsonObject_ = jsonElement_.getAsJsonObject();
		deserializationContext_ = context;
		

		
		JsonElement itemTypeElement = jsonObject_.get("itemType");
		String itemTypeString = itemTypeElement.getAsString();
		
   
        
	     Class<?> cl =  JsonController.getInstance().getClassForString(itemTypeString);
	     
    	 destObject_  = new SerializableVector<ITEM>(itemTypeString);

	     
        
    	//destObject_ = new SerializableVector<JsonSerializable>();
		JsonArray itemArray = jsonObject_.getAsJsonArray("itemArray");
		
		int len = itemArray.size();
		for (int i = 0; i < len; i++) {
			
			JsonElement el = itemArray.get(i);
			ITEM item = deserializationContext_.deserialize(el, cl);
				
			destObject_.add(item);
		}

      
        return destObject_;
    }
    
    

    
    
}

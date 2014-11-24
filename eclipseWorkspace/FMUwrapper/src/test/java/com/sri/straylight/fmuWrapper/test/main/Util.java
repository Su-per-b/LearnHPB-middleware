package com.sri.straylight.fmuWrapper.test.main;

import static org.junit.Assert.assertEquals;

import com.sri.straylight.fmuWrapper.serialization.Iserializable;
import com.sri.straylight.fmuWrapper.serialization.JsonController;

public class Util {
	

	
	
	public static Iserializable deserialize(String jsonString) {
		
		  JsonController gsonController = JsonController.getInstance();
		
		  Iserializable deserializedObject = gsonController.fromJson(jsonString);


		  return deserializedObject;

	}

	public static Iserializable deserializeOk(String jsonString, Class<? extends Iserializable> class1) {
		
		  JsonController gsonController = JsonController.getInstance();
		  Iserializable deserializedObject = gsonController.fromJson(jsonString);
		  
		  assertEquals(class1, deserializedObject.getClass());
			
		  
		return deserializedObject;
	}

	
	public static String serializeOk(Iserializable serializableObject, String expectedString) {

		  JsonController gsonController = JsonController.getInstance();
		  
		  String jsonString_0 = gsonController.serialize(serializableObject);
		  assertEquals(expectedString, jsonString_0);
		  
		  
		  return jsonString_0;
		  
	}
	


	
	
	
}

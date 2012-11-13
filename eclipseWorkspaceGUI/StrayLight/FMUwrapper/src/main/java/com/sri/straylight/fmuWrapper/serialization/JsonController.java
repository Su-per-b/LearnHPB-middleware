package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateWrapperNotify;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValue;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.serialization.SimStateNativeRequestAdapter;
/**
 * used for serialization
 */
public class JsonController {

	private static final JsonController _theInstance = new JsonController();

	/* this class really does all the work */
	private Gson gson_;

	private GsonBuilder gsonBuilder_;

	private HashMap<String, Type> registeredClasses_;

	/* Prevent direct access to the constructor */
	private JsonController() {
		super();
		init();
	}

	public static JsonController getInstance() {
		return _theInstance;
	}

	
	
	public String toJson(Object src) {

		if (src == null) {
			return toJson(JsonNull.INSTANCE);
		}

		String classString = src.getClass().getCanonicalName();
		boolean isRegisteredClass = registeredClasses_.containsKey(classString);



		return gson_.toJson(src, src.getClass());
	}
	
	
	
	public JsonSerializable fromJson(String jsonString) {

		SerializeableObject obj = gson_.fromJson(jsonString,
				SerializeableObject.class);
		
		String classString = obj.type;

        Class cl;
		try {
			
			cl = Class.forName(classString);
			JsonSerializable jsonSerializable = (JsonSerializable) gson_.fromJson(jsonString, cl);
			return jsonSerializable;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		return null;

	}
	
	

	/**
	 * Gets the gson.
	 * 
	 * @return the gson
	 */
	// public Gson getGson() {
	// return gson_;
	// }

	/**
	 * Inits the.
	 */
	private void init() {

		gsonBuilder_ = new GsonBuilder();

		registeredClasses_ = new HashMap<String, Type>();
		register_(SimStateWrapperNotify.class, new SimStateWrapperNotifyAdapter());
		
		register_(MessageStruct.class, new MessageStructAdapter());
		register_(MessageEvent.class, new MessageEventAdapter());

		register_(ScalarValue.class, new ScalarValueAdapter());

		register_(ResultEvent.class, new ResultEventAdapter());
		
		register_(XMLparsed.class, new InitializedStructAdapter());
		
		
		register_(SimStateNativeRequest.class, new SimStateNativeRequestAdapter());
		
		gson_ = gsonBuilder_.create();
	}
	
	

	private void register_(Type type, Object typeAdapter) {

		gsonBuilder_.registerTypeAdapter(type, typeAdapter);
		String classNameString = type.toString();

		registeredClasses_.put(classNameString, type);

	}


	
	



}

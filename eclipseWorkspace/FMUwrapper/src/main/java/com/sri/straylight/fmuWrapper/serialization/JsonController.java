package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueBoolean;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariablesAll;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.DefaultExperimentStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;


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

	
	public String toJson(Object src, Type typeOfSrc) {

		if (src == null) {
			return toJson(JsonNull.INSTANCE);
		}

		return gson_.toJson(src, typeOfSrc);
	}
	
	public String toJson(Object src) {

		if (src == null) {
			return toJson(JsonNull.INSTANCE);
		}

		return gson_.toJson(src, src.getClass());
	}
	
	public Object fromJsonGeneric(String jsonString) {

		Object obj = gson_.fromJson(jsonString,
				Object.class);
		

		return obj;

	}
	
	public Object fromJson(String jsonString, Type typeOfSrc) {

		Object obj = gson_.fromJson(jsonString,
				typeOfSrc);
		

		
		return obj;

	}
	
	
	public JsonSerializable fromJson(String jsonString) {
		
		//extract the class name as a string
		SerializeableObject obj = gson_.fromJson(jsonString,
				SerializeableObject.class);
		
		String classString = obj.type;

        Class<?> cl;
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
		
		register_(MessageStruct.class, new MessageStructAdapter());
		register_(MessageEvent.class, new MessageEventAdapter());
		
		register_(ScalarValueReal.class, new ScalarValueRealAdapter());
		register_(ScalarValueBoolean.class, new ScalarValueBooleanAdapter());
		
		register_(ScalarValueCollection.class, new ScalarValueCollectionAdapter());
		register_(ScalarValueResults.class, new ScalarValueResultsAdapter());
		register_(ResultEvent.class, new ResultEventAdapter());
		
		register_(SimStateNative.class, new SimStateNativeAdapter());
		register_(SimStateNativeRequest.class, new SimStateNativeRequestAdapter());
		register_(SimStateNativeNotify.class, new SimStateNativeNotifyAdapter());
		
		register_(TypeSpecReal.class, new TypeSpecRealAdapter());
		register_(ScalarVariableReal.class, new ScalarVariableRealAdapter());
		
		register_(ScalarVariableCollection.class, new ScalarVariableCollectionAdapter());
		register_(ScalarVariablesAll.class, new ScalarVariablesAllAdapter());
		
		register_(XMLparsedInfo.class, new XMLparsedInfoAdapter());
		register_(XMLparsedEvent.class, new XMLparsedEventAdapter());
		
		register_(ConfigStruct.class, new ConfigStructAdapter());
		register_(DefaultExperimentStruct.ByReference.class, new DefaultExperimentStructAdapter());
		register_(ConfigChangeNotify.class, new ConfigChangeNotifyAdapter());
		
		register_(ScalarValueChangeRequest.class, new ScalarValueChangeRequestAdapter());
		
		
		
		gson_ = gsonBuilder_.create();
	}
	
	

	private void register_(Type type, Object typeAdapter) {

		gsonBuilder_.registerTypeAdapter(type, typeAdapter);
		String classNameString = type.toString();

		registeredClasses_.put(classNameString, type);

	}


	
	



}

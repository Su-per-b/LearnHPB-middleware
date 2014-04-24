package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Assert;

import org.apache.commons.math.util.MathUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AdapterBase<T>
	implements JsonSerializer<T>, JsonDeserializer<T>

{

	protected JsonObject jsonObject_;
	protected JsonElement jsonElement_;

	protected T sourceObject_;
	protected T destObject_;
	
	protected Type typeOfT_;
	protected JsonSerializationContext serializationContext_;
	protected JsonDeserializationContext deserializationContext_;

	protected HashMap<Type, SerializeBase> serializeMap_ = new HashMap<Type, SerializeBase>();
	protected HashMap<Type, DeserializeBase> deserializeMap_ = new HashMap<Type, DeserializeBase>();
	
	

	
	
	protected String typeString_ = "";
	protected Class<T> clazz_;

	protected String[] fieldNames_ = null;
	private String[][] fieldNamesEx_ = null;
	

	
	//==main functions==//
	public AdapterBase() {
	}
	
	public AdapterBase(Class<T> clazz) {
		clazz_ = clazz;
		setClass_();
	}
	
	
	public void init(String[] fieldNames) {
		fieldNames_ = fieldNames;
		initMap_();
	}
	
	public void init(String[][] fieldNamesEx) {
		fieldNamesEx_ = fieldNamesEx;
		initMap_();
	}
	
	


	
	
	public String getTypeString() {
		return typeString_;
	}
	
	private void initMap_() {
		
		serializeMap_.put(String.class, new SerializeString());

		serializeMap_.put(int.class, new Serializeint());
		serializeMap_.put(Integer.class, new Serializeint());
		
		serializeMap_.put(Double.class, new SerializeDouble());
		serializeMap_.put(double.class, new SerializeDouble());
		
		serializeMap_.put(Boolean.class, new SerializeBoolean());
		serializeMap_.put(boolean.class, new SerializeBoolean());
				
		deserializeMap_.put(String.class, new DeserializeString());
		deserializeMap_.put(int.class, new Deserializeint());
		deserializeMap_.put(Integer.class, new Deserializeint());
		
		deserializeMap_.put(Double.class, new DeserializeDouble());
		deserializeMap_.put(double.class, new DeserializeDouble());
		
		deserializeMap_.put(Boolean.class, new DeserializeBoolean());
		deserializeMap_.put(boolean.class, new DeserializeBoolean());

	}
	
	
	protected JsonElement init(T src, Type typeOfSrc,
			JsonSerializationContext context) {

		sourceObject_ = src;
		typeOfT_ = typeOfSrc;

		serializationContext_ = context;
		jsonObject_ = new JsonObject();
		

		if ("" == typeString_) {
			jsonObject_.add("t", new JsonPrimitive(src.getClass()
					.getCanonicalName()));
		} else {
			jsonObject_.add("t", new JsonPrimitive(typeString_));
		}
		

		return jsonObject_;
	}

	private Field getField_(String javaFieldName) {
		
		Field field = null;
		
		try {
			//getDeclaredFields will give all fields (no matter the accessibility) but only of the current class
			field = clazz_.getDeclaredField(javaFieldName);
			
		} catch (NoSuchFieldException e1) {
			try {
				
				field = clazz_.getField(javaFieldName);
				
			} catch (NoSuchFieldException | SecurityException e) {
				
				
				try {
					
					Class<?> superKlass = clazz_.getSuperclass();
					field = superKlass.getDeclaredField(javaFieldName);
					
				} catch (NoSuchFieldException | SecurityException e2) {
					e2.printStackTrace();
				}
			}
		}
		
		return field;
		
	}
	
	//== serialize ==//
	public JsonElement serialize(T src, Type typeOfSrc,
			JsonSerializationContext context) {

		init(src, typeOfSrc, context);
		
		if(null != fieldNamesEx_ && fieldNamesEx_.length > 0) {
			serializeFieldsEx();
		}
		
		if(null != fieldNames_ && fieldNames_.length > 0) {
			serializeFields();
		}
		
		return jsonObject_;
	}
	
	protected void serializeFieldsEx() {

		int len = fieldNamesEx_.length;
		for (int i = 0; i < len; i++) {
			
			String javaFieldName = fieldNamesEx_[i][0];
			String jsonFieldName = fieldNamesEx_[i][1];
			
			
			serializeOneFieldEx_(javaFieldName, jsonFieldName);
		}
	}
	
	protected void serializeFields() {

		int len = fieldNames_.length;
		for (int i = 0; i < len; i++) {
			String fieldName = fieldNames_[i];
			serializeOneFieldEx_(fieldName, fieldName);
		}
	}
	

	private void serializeOneFieldEx_(String javaFieldName, String jsonFieldName) {
		
		
		Field field = getField_(javaFieldName);
		field.setAccessible(true);
		
		Type type = field.getGenericType();
		
		SerializeBase serializeObject = serializeMap_.get(type);
		
		

			
			
		serializeObject.setFieldEx(field, jsonFieldName);

		try {
			serializeObject.run();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		

		
	}
	
	

	
	protected void jsonObjectAdd_(String fieldName, Object obj) {
		
		JsonElement element = serializationContext_.serialize(obj, obj.getClass());
		jsonObject_.add(fieldName, element);
	}

	



	
	//== deserialize ==//
	
	public T deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context) {
		
		
		try {
			destObject_ = clazz_.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		
		deserializeHelper_(jsonElement, typeOfT, context);

		
		return destObject_;
	}
	
	
	public T deserializeHelper_(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context) {
		
		
		Assert.assertNotNull(destObject_);

		jsonElement_ = jsonElement;

		if (jsonElement_.isJsonObject()) {
			jsonObject_ = jsonElement_.getAsJsonObject();
		}

		deserializationContext_ = context;

		if(null != fieldNamesEx_ && fieldNamesEx_.length > 0) {
			deserializeFieldsEx_();
		}
		
		if(null != fieldNames_ && fieldNames_.length > 0) {
			deserializeFields_();
		}
		
		
		return destObject_;
	}
	
	
	private void deserializeFieldsEx_() {
		
		
		int len = fieldNamesEx_.length;
		for (int i = 0; i < len; i++) {
			
			String javaFieldName = fieldNamesEx_[i][0];
			String jsonFieldName = fieldNamesEx_[i][1];
			
			
			deserializeOneFieldEx_(javaFieldName, jsonFieldName);
		}
		

	}
	

	private void deserializeFields_() {
		int len = fieldNames_.length;
		for (int i = 0; i < len; i++) {
			String fieldName = fieldNames_[i];
			deserializeOneFieldEx_(fieldName, fieldName);
		}
	}
	
	
	private void deserializeOneFieldEx_(String javaFieldName, String jsonFieldName) {

		Field field = getField_(javaFieldName);
		field.setAccessible(true);
		
		Type type = field.getGenericType();
		
		DeserializeBase deserializeObject = deserializeMap_.get(type);
		deserializeObject.setFieldEx(field, jsonFieldName);
		
		
		try {
			deserializeObject.run();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}
	

//	private void deserializeOneField(String fieldName) {
//
//		try {
//			
//			Field field = klass_.getDeclaredField(fieldName);
//			field.setAccessible(true);
//			
//			Type type = field.getGenericType();
//			DeserializeBase deserializeObject = deserializeMap_.get(type);
//
//			deserializeObject.setField(field);
//			
//			try {
//				deserializeObject.run();
//			} catch (IllegalArgumentException | IllegalAccessException e) {
//				e.printStackTrace();
//			}
//
//		} catch (NoSuchFieldException | SecurityException e) {
//			e.printStackTrace();
//		}
//
//	}

	protected void deserializeStrings(String[] ary) {

		int len = ary.length;
		for (int i = 0; i < len; i++) {

			String fieldName = ary[i];
			deserializeOneString(fieldName);

		}
	}

	protected void deserializeOneString(String fieldName) {

		String fieldValue = jsonObject_.get(fieldName).getAsString();

		try {

			Field field = clazz_.getField(fieldName);
			field.setAccessible(true);

			try {
				field.set(destObject_, fieldValue);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

	}


	private class SerializeBase {
		protected Field javaField_;
		protected String jsonFieldName_;


		public void setFieldEx(Field field, String jsonFieldName) {
			javaField_ = field;
			jsonFieldName_ = jsonFieldName;
		}


		protected String getJsonFieldName_()  {
			if (null != jsonFieldName_) {
				return jsonFieldName_;
			} else {
				return javaField_.getName();
			}
		}
		
		
		
		public void run() throws IllegalArgumentException,
				IllegalAccessException {

		}
	}

	

	
	
	private class SerializeString extends SerializeBase {
		@Override
		public void run() throws IllegalArgumentException,
		IllegalAccessException {
			Object obj = javaField_.get(sourceObject_);
			JsonPrimitive primitive = new JsonPrimitive((String) obj);
			jsonObject_.add(getJsonFieldName_(), primitive);
		}
	}

	private class Serializeint extends SerializeBase {
		@Override
		public void run() throws IllegalArgumentException,
				IllegalAccessException {
			Object obj = javaField_.get(sourceObject_);
			JsonPrimitive primitive = new JsonPrimitive((int) obj);
			jsonObject_.add(getJsonFieldName_(), primitive);
		}
	}

	private class SerializeDouble extends SerializeBase {

		@Override
		public void run() throws IllegalArgumentException,
				IllegalAccessException {
 			Object obj = javaField_.get(sourceObject_);
			
			Double theDouble = (Double) obj;
			Double theDoubleRouded = MathUtils.round(theDouble, 0, BigDecimal.ROUND_HALF_DOWN);
			
			JsonPrimitive primitive;
			
			if( (double) theDouble == (double)theDoubleRouded) {
				int theInt = theDouble.intValue();
				primitive = new JsonPrimitive(theInt);
			} else {
				primitive = new JsonPrimitive(theDouble);
			}
			
			jsonObject_.add(getJsonFieldName_(), primitive);
		}

	}
	
	
	private class SerializeBoolean extends SerializeBase {
		
		@Override
		public void run() throws IllegalArgumentException,
		IllegalAccessException {
			Object obj = javaField_.get(sourceObject_);
			JsonPrimitive primitive = new JsonPrimitive((Boolean) obj);
			jsonObject_.add(getJsonFieldName_(), primitive);
		}
		
	}
	
	
	
	
	
	private class DeserializeBase {
		protected Field javaField_;
		protected String jsonFieldName_;
		
		
		public void setFieldEx(Field field, String jsonFieldName) {
			javaField_ = field;
			jsonFieldName_ = jsonFieldName;
		}
		

		
		public void run() throws IllegalArgumentException, IllegalAccessException {

		}
	}

	private class DeserializeString extends DeserializeBase  {

		@Override
		public void run() throws IllegalArgumentException, IllegalAccessException {

			JsonElement jsonElement = jsonObject_.get(jsonFieldName_);

			String fieldValue = jsonElement.getAsString();
			
			javaField_.setAccessible(true);
			javaField_.set(destObject_, fieldValue);

		}
	}
	
	
	private class Deserializeint extends DeserializeBase  {
		
		@Override
		public void run() throws IllegalArgumentException, IllegalAccessException {
			
			JsonElement jsonElement = jsonObject_.get(jsonFieldName_);
			
			int fieldValue = jsonElement.getAsInt();
			
			javaField_.setAccessible(true);
			javaField_.set(destObject_, fieldValue);
			
		}
	}
	
	private class DeserializeDouble extends DeserializeBase  {
		
		@Override
		public void run() throws IllegalArgumentException, IllegalAccessException {
			
			JsonElement jsonElement = jsonObject_.get(jsonFieldName_);
			
			Double fieldValue = jsonElement.getAsDouble();
			
			javaField_.setAccessible(true);
			javaField_.set(destObject_, fieldValue);
			
		}
	}
	
	private class DeserializeBoolean extends DeserializeBase  {
		
		@Override
		public void run() throws IllegalArgumentException, IllegalAccessException {
			
			JsonElement jsonElement = jsonObject_.get(jsonFieldName_);
			
			Boolean fieldValue = jsonElement.getAsBoolean();
			
			javaField_.setAccessible(true);
			javaField_.set(destObject_, fieldValue);
			
		}
	}



	protected void setClass_() {
		
		String classNameString = clazz_.toString();
		String[] parts = classNameString.split("\\.");
		int len = parts.length;
		
		String shortClassName = parts[len-1];
		
		String[] parts2 = shortClassName.split("\\$");
		shortClassName = parts2[0];
		
		
		
		typeString_ = shortClassName;

	}









}

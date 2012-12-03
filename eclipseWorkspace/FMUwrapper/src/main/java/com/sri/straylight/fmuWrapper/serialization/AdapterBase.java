package com.sri.straylight.fmuWrapper.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AdapterBase<T extends JsonSerializable>
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
	
	protected String[] fieldNames_ = { };
	

	public AdapterBase() {
	}
	
	public void setFieldNames(String[] fieldNames) {
		fieldNames_ = fieldNames;
		initMap_();
	}
	
	private void initMap_() {
		
		serializeMap_.put(String.class, new SerializeString());
		serializeMap_.put(int.class, new Serializeint());
		serializeMap_.put(Double.class, new SerializeDouble());
		serializeMap_.put(double.class, new SerializeDouble());
		serializeMap_.put(Boolean.class, new SerializeBoolean());
		
		deserializeMap_.put(String.class, new DeserializeString());
		deserializeMap_.put(int.class, new Deserializeint());
		deserializeMap_.put(Double.class, new DeserializeDouble());
		deserializeMap_.put(double.class, new DeserializeDouble());
		deserializeMap_.put(Boolean.class, new DeserializeBoolean());
		
	}
	
	protected JsonElement init(T src, Type typeOfSrc,
			JsonSerializationContext context) {

		sourceObject_ = src;
		typeOfT_ = typeOfSrc;

		serializationContext_ = context;
		jsonObject_ = new JsonObject();
		jsonObject_.add("type", new JsonPrimitive(src.getClass()
				.getCanonicalName()));

		return jsonObject_;
	}


	public JsonElement serialize(T src, Type typeOfSrc,
			JsonSerializationContext context) {

		init(src, typeOfSrc, context);
		
		if(fieldNames_.length > 0) {
			serializeFields(fieldNames_);
		}
		
		return jsonObject_;
	}
	
	
	protected void serializeFields(String[] fieldNames) {

		int len = fieldNames.length;
		for (int i = 0; i < len; i++) {
			String fieldName = fieldNames[i];
			serializeOneField_(fieldName);
		}
	}
	
	protected void serializeOneField_(String fieldName, Object obj) {
		
		JsonElement element = serializationContext_.serialize(obj, obj.getClass());
		jsonObject_.add(fieldName, element);
		
	}

	
	protected void serializeOneField_(String fieldName) {

		
		Field field = null;
		Class<?> cl = sourceObject_.getClass();
		
		try {
			
			field = cl.getDeclaredField(fieldName);
			
		} catch (NoSuchFieldException e1) {
			try {
				field = cl.getField(fieldName);
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		try {

			field.setAccessible(true);

			try {
				Type type = field.getGenericType();
				SerializeBase serializeObject = serializeMap_.get(type);
				serializeObject.setField(field);

				try {
					serializeObject.run();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

		} catch ( SecurityException e) {
			e.printStackTrace();
		}
	}
	
	
	public T deserialize(JsonElement jsonElement, Type typeOfT,
			JsonDeserializationContext context) {

		jsonElement_ = jsonElement;

		if (jsonElement_.isJsonObject()) {
			jsonObject_ = jsonElement_.getAsJsonObject();
		}

		typeOfT_ = typeOfT;
		deserializationContext_ = context;

		int len = fieldNames_.length;
		for (int i = 0; i < len; i++) {
			String fieldName = fieldNames_[i];
			deserializeOneField(fieldName);
		}
		
		return destObject_;
	}
	
	


	private void deserializeOneField(String fieldName) {

		try {
			
			Class<?> cl = destObject_.getClass();
			
			Field field = cl.getDeclaredField(fieldName);
			field.setAccessible(true);
			
			Type type = field.getGenericType();
			DeserializeBase deserializeObject = deserializeMap_.get(type);

			deserializeObject.setField(field);
			
			try {
				deserializeObject.run();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

	}

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

			Field field = destObject_.getClass().getField(fieldName);
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
		protected Field field_;

		public void setField(Field field) {
			field_ = field;
		}

		public void run() throws IllegalArgumentException,
				IllegalAccessException {

		}
	}

	
	private class SerializeString extends SerializeBase {
		@Override
		public void run() throws IllegalArgumentException,
				IllegalAccessException {
			Object obj = field_.get(sourceObject_);
			JsonPrimitive primitive = new JsonPrimitive((String) obj);
			jsonObject_.add(field_.getName(), primitive);
		}
	}

	private class Serializeint extends SerializeBase {
		@Override
		public void run() throws IllegalArgumentException,
				IllegalAccessException {
			Object obj = field_.get(sourceObject_);
			JsonPrimitive primitive = new JsonPrimitive((int) obj);
			jsonObject_.add(field_.getName(), primitive);
		}
	}

	private class SerializeDouble extends SerializeBase {

		@Override
		public void run() throws IllegalArgumentException,
				IllegalAccessException {
			Object obj = field_.get(sourceObject_);
			JsonPrimitive primitive = new JsonPrimitive((Double) obj);
			jsonObject_.add(field_.getName(), primitive);
		}

	}
	
	
	private class SerializeBoolean extends SerializeBase {
		
		@Override
		public void run() throws IllegalArgumentException,
		IllegalAccessException {
			Object obj = field_.get(sourceObject_);
			JsonPrimitive primitive = new JsonPrimitive((Boolean) obj);
			jsonObject_.add(field_.getName(), primitive);
		}
		
	}
	
	
	
	
	
	private class DeserializeBase {
		protected Field field_;

		public void setField(Field field) {
			field_ = field;
		}

		public void run() throws IllegalArgumentException, IllegalAccessException {

		}
	}

	private class DeserializeString extends DeserializeBase  {

		@Override
		public void run() throws IllegalArgumentException, IllegalAccessException {

			String fieldName = field_.getName();
			JsonElement jsonElement = jsonObject_.get(fieldName);

			String fieldValue = jsonElement.getAsString();
			
			field_.setAccessible(true);
			field_.set(destObject_, fieldValue);

		}
	}
	
	
	private class Deserializeint extends DeserializeBase  {
		
		@Override
		public void run() throws IllegalArgumentException, IllegalAccessException {
			
			String fieldName = field_.getName();
			JsonElement jsonElement = jsonObject_.get(fieldName);
			
			int fieldValue = jsonElement.getAsInt();
			
			field_.setAccessible(true);
			field_.set(destObject_, fieldValue);
			
		}
	}
	
	private class DeserializeDouble extends DeserializeBase  {
		
		@Override
		public void run() throws IllegalArgumentException, IllegalAccessException {
			
			String fieldName = field_.getName();
			JsonElement jsonElement = jsonObject_.get(fieldName);
			
			Double fieldValue = jsonElement.getAsDouble();
			
			field_.setAccessible(true);
			field_.set(destObject_, fieldValue);
			
		}
	}
	
	private class DeserializeBoolean extends DeserializeBase  {
		
		@Override
		public void run() throws IllegalArgumentException, IllegalAccessException {
			
			String fieldName = field_.getName();
			JsonElement jsonElement = jsonObject_.get(fieldName);
			
			Boolean fieldValue = jsonElement.getAsBoolean();
			
			field_.setAccessible(true);
			field_.set(destObject_, fieldValue);
			
		}
	}




}

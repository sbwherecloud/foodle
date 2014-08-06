package com.belanger.simon.foodle.datastructures;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class FValue<T> extends FObservable {

	private transient T			value;
	private transient boolean	silent;

	public FValue() {
		this(null);
	}

	public FValue(T value) {
		this.value = value;
	}

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
		if (!silent) {
			setChanged();
			notifyObservers(value);
		}
		silent = false;
	}

	public FValue<T> silently() {
		silent = true;
		return this;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (this.getClass().equals(o.getClass())) {
				@SuppressWarnings("rawtypes")
                FValue other = (FValue) o;
				return this.value.equals(other.value);
			}
		}
		return false;
	}

	/*
	 * JSON SERIALIZATION / DESERIALIZATION SUPPORT
	 */

	@SuppressWarnings("rawtypes")
	public static class FJsonSerializer implements JsonSerializer<FValue> {
		@Override
		public JsonElement serialize(FValue value, Type type, JsonSerializationContext context) {
			return context.serialize(value.get());
		}
	}

	public static class FJsonDeserializer<T> implements JsonDeserializer<FValue<T>> {
		private Class<?>	clazz;

		public FJsonDeserializer(Class<?> clazz) {
			this.clazz = clazz;
		}

		@Override
		public FValue<T> deserialize(JsonElement jsonElement, Type intoType, JsonDeserializationContext context)
				throws JsonParseException {
			FValue<T> value = new FValue<T>();
			T object = context.<T> deserialize(jsonElement, clazz);
			value.set(object);
			return value;
		}
	}
}
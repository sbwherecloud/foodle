package com.belanger.simon.foodle.datastructures;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FList<T> extends FObservable implements List<T> {

	private transient boolean		  silent;
	private transient ArrayList<T>	  list			= new ArrayList<T>();
	private transient FValue<Integer> focusingIndex	= new FValue<Integer>(-1);

	/*
	 * OBSERVER
	 */

	/**
	 * Prevents the next call from notifying observers. <br>
	 * 
	 * Ex. observableList.add(o); will notify <br>
	 * Ex. observableList.silently().add(o); will NOT notify <br>
	 * 
	 * @return Reference to self for method chaining.
	 */
	public FList<T> silently() {
		silent = true;
		return this;
	}

	private void notifyObserversIfNotSilent(Object data) {
		if (!silent) {
			notifyObservers(data);
		}
		silent = false;
	}

	private void notifyObserversIfNotSilent() {
		notifyObserversIfNotSilent(null);
	}

	/*
	 * FOCUSABILITY
	 */

	public FValue<Integer> getFocusIndex() {
		return focusingIndex;
	}

	public T getFocusedItem() {
		if (focusingIndex.get() >= 0 && focusingIndex.get() < list.size()) {
			return list.get(focusingIndex.get());
		} else {
			return null;
		}
	}

	/*
	 * LIST
	 */

	@Override
	public boolean add(T object) {
		boolean result = list.add(object);
		setChanged();
		notifyObserversIfNotSilent(object);
		return result;
	}

	@Override
	public void add(int location, T object) {
		list.add(location, object);
		setChanged();
		notifyObserversIfNotSilent(object);
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		if (collection == null) return false;
		boolean result = list.addAll(collection);
		if (result) {
			setChanged();
			notifyObserversIfNotSilent(collection);
		}
		return result;
	}

	@Override
	public boolean addAll(int location, Collection<? extends T> collection) {
		boolean result = list.addAll(location, collection);
		if (result) {
			setChanged();
			notifyObserversIfNotSilent(collection);
		}
		return result;
	}

	@Override
	public void clear() {
		list.clear();
		setChanged();
		notifyObserversIfNotSilent();
	}

	@Override
	public boolean contains(Object object) {
		return list.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return list.containsAll(collection);
	}

	@Override
	public T get(int location) {
		return list.get(location);
	}

	@Override
	public int indexOf(Object object) {
		return list.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return list.lastIndexOf(object);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int location) {
		return list.listIterator(location);
	}

	@Override
	public T remove(int location) {
		T removed = list.remove(location);
		setChanged();
		notifyObserversIfNotSilent(removed);
		return removed;
	}

	@Override
	public boolean remove(Object object) {
		boolean result = list.remove(object);
		if (result) {
			setChanged();
			notifyObserversIfNotSilent();
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean result = list.removeAll(collection);
		if (result) {
			setChanged();
			notifyObserversIfNotSilent();
		}
		return result;
	}

	public boolean replace(Collection<? extends T> collection) {
		silence();
		clear();
		boolean result = addAll(collection);
		unsilence();
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		boolean result = list.retainAll(collection);
		if (result) {
			setChanged();
			notifyObserversIfNotSilent();
		}
		return result;
	}

	@Override
	public T set(int location, T object) {
		T replaced = list.set(location, object);
		setChanged();
		notifyObserversIfNotSilent(replaced);
		return replaced;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<T> subList(int start, int end) {
		return list.subList(start, end);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <E> E[] toArray(E[] array) {
		return list.toArray(array);
	}

	/*
	 * JSON SERIALIZATION / DESERIALIZATION SUPPORT
	 */

	public static <E> Type getType() {
		Type type = new TypeToken<FList<E>>() {
		}.getType();
		return type;
	}

	@SuppressWarnings("rawtypes")
	public static class FJsonDeserializer<T> implements JsonDeserializer<FList<T>> {
		private Class	clazz;

		public FJsonDeserializer(Class clazz) {
			this.clazz = clazz;
		}

		@Override
		public FList<T> deserialize(JsonElement jsonElement, Type intoType, JsonDeserializationContext context)
				throws JsonParseException {
			FList<T> list = new FList<T>();
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			for (JsonElement jsonEntry : jsonArray) {
				T object = context.<T> deserialize(jsonEntry, clazz);
				list.add(object);
			}
			return list;
		}
	}
}

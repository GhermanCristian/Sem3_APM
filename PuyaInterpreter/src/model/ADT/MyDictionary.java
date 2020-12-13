package model.ADT;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public class MyDictionary<TKey, TValue> implements DictionaryInterface<TKey, TValue>{
	protected HashMap<TKey, TValue> dictionary;
	
	public MyDictionary() {
		this.dictionary = new HashMap<TKey, TValue>();
	}

	@Override
	public int size() {
		return this.dictionary.size();
	}

	@Override
	public boolean isDefined(TKey key) {
		return this.dictionary.containsKey(key);
	}

	@Override
	public boolean exists(TValue value) {
		return this.dictionary.containsValue(value);
	}

	@Override
	public boolean isEmpty() {
		return this.dictionary.isEmpty();
	}
	
	@Override
	public String toString() {
		String representation = "";
		
		Collection<TKey> allKeys = this.dictionary.keySet();
		for (TKey key : allKeys) {
			representation += (key.toString() + " -> " + this.dictionary.get(key).toString() + "\n");
		}
		
		return representation;
	}

	@Override
	public void update(TKey key, TValue newValue) {
		this.dictionary.replace(key, newValue);
	}

	@Override
	public void insert(TKey key, TValue newValue) {
		this.dictionary.put(key, newValue);
	}

	@Override
	public void clear() {
		this.dictionary.clear();
	}

	@Override
	public TValue getValue(TKey key) {
		return this.dictionary.get(key);
	}

	@Override
	public TValue remove(TKey key) {
		return this.dictionary.remove(key);
	}

	@Override
	public Collection<TValue> getAllValues() {
		return this.dictionary.values();
	}

	@Override
	public Collection<TKey> getAllKeys() {
		return this.dictionary.keySet();
	}

	@Override
	public HashMap<TKey, TValue> getAllPairs() {
		return this.dictionary;
	}

	@Override
	public void setContent(HashMap<TKey, TValue> newContent) {
		this.dictionary = newContent;
	}

	@Override
	public DictionaryInterface<TKey, TValue> clone() {
		DictionaryInterface<TKey, TValue> newDictionary = new MyDictionary<TKey, TValue>();
		// this will only work if the types are immutable
		// in this case, all Types and Values (and the java String) are immutable
		this.dictionary.entrySet().stream().forEach(pair -> newDictionary.insert(pair.getKey(), pair.getValue()));
		return newDictionary;
	}

	@Override
	public void forEachKey(Consumer<? super TKey> action) {
		for(TKey crtKey : this.dictionary.keySet()) {
			action.accept(crtKey);
		}
	}

	@Override
	public void forEachValue(Consumer<? super TValue> action) {
		for(TValue crtValue : this.dictionary.values()) {
			action.accept(crtValue);
		}
	}
}

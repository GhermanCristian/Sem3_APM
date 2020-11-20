package model.ADT;

import java.util.Collection;
import java.util.HashMap;

import model.value.ValueInterface;

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

	@SuppressWarnings("unchecked")
	@Override
	public void clone(HashMap<TKey, TValue> original) {
		// for now this only properly works with HashMaps that map to ValueInterface
		this.dictionary.clear();
		original.entrySet().stream().filter(pair -> pair.getValue() instanceof ValueInterface)
									.forEach(pair -> this.dictionary.put(pair.getKey(), (TValue)((ValueInterface)pair.getValue()).getDeepCopy()));
	}
}

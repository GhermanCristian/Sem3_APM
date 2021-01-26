package model.ADT;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class MyDictionary<TKey, TValue> implements DictionaryInterface<TKey, TValue>{
	protected HashMap<TKey, TValue> dictionary;
	private static Lock lock = new ReentrantLock();
	
	public MyDictionary() {
		this.dictionary = new HashMap<TKey, TValue>();
	}

	@Override
	public int size() {
		lock.lock();
		int returnValue = this.dictionary.size();
		lock.unlock();
		return returnValue;
	}

	@Override
	public boolean isDefined(TKey key) {
		lock.lock();
		boolean returnValue = this.dictionary.containsKey(key);
		lock.unlock();
		return returnValue;
	}

	@Override
	public boolean exists(TValue value) {
		lock.lock();
		boolean returnValue = this.dictionary.containsValue(value);
		lock.unlock();
		return returnValue;
	}

	@Override
	public boolean isEmpty() {
		lock.lock();
		boolean returnValue = this.dictionary.isEmpty();
		lock.unlock();
		return returnValue;
	}
	
	@Override
	public String toString() {
		String representation = "";
		
		lock.lock();
		Collection<TKey> allKeys = this.dictionary.keySet();
		for (TKey key : allKeys) {
			representation += (key.toString() + " -> " + this.dictionary.get(key).toString() + "\n");
		}
		lock.unlock();
		
		return representation;
	}

	@Override
	public void update(TKey key, TValue newValue) {
		lock.lock();
		this.dictionary.replace(key, newValue);
		lock.unlock();
	}

	@Override
	public void insert(TKey key, TValue newValue) {
		lock.lock();
		this.dictionary.put(key, newValue);
		lock.unlock();
	}

	@Override
	public void clear() {
		lock.lock();
		this.dictionary.clear();
		lock.unlock();
	}

	@Override
	public TValue getValue(TKey key) {
		lock.lock();
		TValue returnValue = this.dictionary.get(key);
		lock.unlock();
		return returnValue;
	}

	@Override
	public TValue remove(TKey key) {
		lock.lock();
		TValue returnValue = this.dictionary.remove(key);
		lock.unlock();
		return returnValue;
	}

	@Override
	public Collection<TValue> getAllValues() {
		lock.lock();
		Collection<TValue> returnValue = this.dictionary.values();
		lock.unlock();
		return returnValue;
	}

	@Override
	public Collection<TKey> getAllKeys() {
		lock.lock();
		Collection<TKey> returnValue = this.dictionary.keySet();
		lock.unlock();
		return returnValue;
	}

	@Override
	public HashMap<TKey, TValue> getAllPairs() {
		lock.lock();
		HashMap<TKey, TValue> returnValue = this.dictionary;
		lock.unlock();
		return returnValue;
	}

	@Override
	public void setContent(HashMap<TKey, TValue> newContent) {
		lock.lock();
		this.dictionary = newContent;
		lock.unlock();
	}

	@Override
	public DictionaryInterface<TKey, TValue> clone() {
		lock.lock();
		DictionaryInterface<TKey, TValue> newDictionary = new MyDictionary<TKey, TValue>();
		// this will only work if the types are immutable
		// in this case, all Types and Values (and the java String) are immutable
		this.dictionary.entrySet().stream().forEach(pair -> newDictionary.insert(pair.getKey(), pair.getValue()));
		lock.unlock();
		return newDictionary;
	}

	@Override
	public void forEachKey(Consumer<? super TKey> action) {
		lock.lock();
		for(TKey crtKey : this.dictionary.keySet()) {
			action.accept(crtKey);
		}
		lock.unlock();
	}

	@Override
	public void forEachValue(Consumer<? super TValue> action) {
		lock.lock();
		for(TValue crtValue : this.dictionary.values()) {
			action.accept(crtValue);
		}
		lock.unlock();
	}
}

package model.ADT;

import java.util.Collection;
import java.util.HashMap;

interface DictionaryInterface<TKey, TValue> {
	public int size();
	public boolean isDefined(TKey key);
	public boolean exists(TValue value);
	public boolean isEmpty();
	public String toString();
	public void update(TKey key, TValue newValue);
	public void insert(TKey key, TValue newValue);
	public void clear();
	public TValue getValue(TKey key);
	public TValue remove(TKey key);
	public Collection<TValue> getAllValues();
	public Collection<TKey> getAllKeys();
	public HashMap<TKey, TValue> getAllPairs();
}

package model.ADT;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public interface DictionaryInterface<TKey, TValue> {
	public int size();
	public String toString();
	public boolean isDefined(TKey key);
	public boolean exists(TValue value);
	public boolean isEmpty();
	public void setContent(HashMap<TKey, TValue> newContent);
	public void update(TKey key, TValue newValue);
	public void insert(TKey key, TValue newValue);
	public void clear();
	public void forEachKey(Consumer<? super TKey> action);
	public void forEachValue(Consumer<? super TValue> action);
	public TValue getValue(TKey key);
	public TValue remove(TKey key);
	public Collection<TValue> getAllValues();
	public Collection<TKey> getAllKeys();
	public HashMap<TKey, TValue> getAllPairs();
	public DictionaryInterface<TKey, TValue> clone();
}

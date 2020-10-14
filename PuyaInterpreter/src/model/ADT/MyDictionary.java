package model.ADT;

import java.util.HashMap;

class MyDictionary<TKey, TValue> implements DictionaryInterface<TKey, TValue>{
	private HashMap<TKey, TValue> dictionary;
	
	public MyDictionary() {
		this.dictionary = new HashMap<TKey, TValue>();
	}
}

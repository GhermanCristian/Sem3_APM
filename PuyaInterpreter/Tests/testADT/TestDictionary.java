package testADT;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import model.ADT.DictionaryInterface;
import model.ADT.MyDictionary;
import model.value.IntValue;
import model.value.ValueInterface;

public class TestDictionary {
	@Test
	// this test will work with ValueInterface as values
	public void Clone_EmptyOrigin_CurrentDictionaryBecomesEmpty() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		assertEquals(d1.size(), 1);
		d1.clone(d2.getAllPairs());
		assertEquals(d1.size(), 0);
	}
	
	@Test
	// this test will work with ValueInterface as values, but no ReferenceValues
	public void Clone_ValidOrigin_CorrectCopySize() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		d1.insert("a2", new IntValue(24));
		d1.insert("a3", new IntValue(25));
		d1.insert("a4", new IntValue(26));
		d1.insert("a5", new IntValue(27));
		assertEquals(d2.size(), 0);
		d2.clone(d1.getAllPairs());
		assertEquals(d2.size(), 5);
	}
	
	@Test
	// this test will work with ValueInterface as values, but no ReferenceValues
	public void Clone_ValidOrigin_CorrectCopyContent() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		d1.insert("a2", new IntValue(24));
		d1.insert("a3", new IntValue(25));
		d1.insert("a4", new IntValue(26));
		d1.insert("a5", new IntValue(27));
		d2.clone(d1.getAllPairs());
		
		assertEquals(d2.getValue("a1"), new IntValue(23));
		assertEquals(d2.getValue("a2"), new IntValue(24));
		assertEquals(d2.getValue("a3"), new IntValue(25));
		assertEquals(d2.getValue("a4"), new IntValue(26));
		assertEquals(d2.getValue("a5"), new IntValue(27));
	}
}

package testADT;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import model.ADT.DictionaryInterface;
import model.ADT.MyDictionary;
import model.type.BoolType;
import model.type.IntType;
import model.type.ReferenceType;
import model.type.StringType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.ReferenceValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestDictionary {
	@Test
	public void Clone_EmptyOriginAndValueIsValueInterface_CurrentDictionaryBecomesEmpty() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		assertEquals(d1.size(), 1);
		d1 = d2.clone();
		assertEquals(d1.size(), 0);
	}
	
	@Test
	public void Clone_EmptyOriginAndValueIsTypeInterface_CurrentDictionaryBecomesEmpty() {
		DictionaryInterface<String, TypeInterface> d1 = new MyDictionary<String, TypeInterface>();
		DictionaryInterface<String, TypeInterface> d2 = new MyDictionary<String, TypeInterface>();
		
		d1.insert("a1", new IntType());
		assertEquals(d1.size(), 1);
		d1 = d2.clone();
		assertEquals(d1.size(), 0);
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsValueInterfaceNoReference_CorrectCopySize() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		d1.insert("a2", new IntValue(24));
		d1.insert("a3", new BoolValue(true));
		d1.insert("a4", new IntValue(26));
		d1.insert("a5", new StringValue("cartof"));
		assertEquals(d2.size(), 0);
		d2 = d1.clone();
		assertEquals(d2.size(), 5);
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsValueInterfaceWithReference_CorrectCopySize() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		d1.insert("a2", new ReferenceValue(new ReferenceType(new BoolType())));
		d1.insert("a3", new BoolValue(true));
		d1.insert("a4", new ReferenceValue(new IntType()));
		d1.insert("a5", new StringValue("cartof"));
		assertEquals(d2.size(), 0);
		d2 = d1.clone();
		assertEquals(d2.size(), 5);
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsTypeInterfaceNoReference_CorrectCopySize() {
		DictionaryInterface<String, TypeInterface> d1 = new MyDictionary<String, TypeInterface>();
		DictionaryInterface<String, TypeInterface> d2 = new MyDictionary<String, TypeInterface>();
		
		d1.insert("a1", new IntType());
		d1.insert("a2", new IntType());
		d1.insert("a3", new BoolType());
		d1.insert("a4", new StringType());
		d1.insert("a5", new IntType());
		assertEquals(d2.size(), 0);
		d2 = d1.clone();
		assertEquals(d2.size(), 5);
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsTypeInterfaceWithReference_CorrectCopySize() {
		DictionaryInterface<String, TypeInterface> d1 = new MyDictionary<String, TypeInterface>();
		DictionaryInterface<String, TypeInterface> d2 = new MyDictionary<String, TypeInterface>();
		
		d1.insert("a1", new ReferenceType(new IntType()));
		d1.insert("a2", new IntType());
		d1.insert("a3", new BoolType());
		d1.insert("a4", new ReferenceType(new BoolType()));
		d1.insert("a5", new StringType());
		assertEquals(d2.size(), 0);
		d2 = d1.clone();
		assertEquals(d2.size(), 5);
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsValueInterfaceNoReference_CorrectCopyContent() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		d1.insert("a2", new IntValue(24));
		d1.insert("a3", new BoolValue(true));
		d1.insert("a4", new IntValue(26));
		d1.insert("a5", new StringValue("cartof"));
		d2 = d1.clone();
		
		assertEquals(d2.getValue("a1"), new IntValue(23));
		assertEquals(d2.getValue("a2"), new IntValue(24));
		assertEquals(d2.getValue("a3"), new BoolValue(true));
		assertEquals(d2.getValue("a4"), new IntValue(26));
		assertEquals(d2.getValue("a5"), new StringValue("cartof"));
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsValueInterfaceWithReference_CorrectCopyContent() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		d1.insert("a2", new ReferenceValue(new ReferenceType(new BoolType())));
		d1.insert("a3", new BoolValue(true));
		d1.insert("a4", new ReferenceValue(new IntType()));
		d1.insert("a5", new StringValue("cartof"));
		d2 = d1.clone();
		
		assertEquals(d2.getValue("a1"), new IntValue(23));
		assertEquals(d2.getValue("a2"), new ReferenceValue(new ReferenceType(new BoolType())));
		assertEquals(d2.getValue("a3"), new BoolValue(true));
		assertEquals(d2.getValue("a4"), new ReferenceValue(new IntType()));
		assertEquals(d2.getValue("a5"), new StringValue("cartof"));
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsTypeInterfaceNoReference_CorrectCopyContent() {
		DictionaryInterface<String, TypeInterface> d1 = new MyDictionary<String, TypeInterface>();
		DictionaryInterface<String, TypeInterface> d2 = new MyDictionary<String, TypeInterface>();
		
		d1.insert("a1", new IntType());
		d1.insert("a2", new BoolType());
		d1.insert("a3", new StringType());
		d1.insert("a4", new IntType());
		d1.insert("a5", new IntType());
		d2 = d1.clone();
		
		assertEquals(d2.getValue("a1"), new IntType());
		assertEquals(d2.getValue("a2"), new BoolType());
		assertEquals(d2.getValue("a3"), new StringType());
		assertEquals(d2.getValue("a4"), new IntType());
		assertEquals(d2.getValue("a5"), new IntType());
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsTypeInterfaceWithReference_CorrectCopyContent() {
		DictionaryInterface<String, TypeInterface> d1 = new MyDictionary<String, TypeInterface>();
		DictionaryInterface<String, TypeInterface> d2 = new MyDictionary<String, TypeInterface>();
		
		d1.insert("a1", new IntType());
		d1.insert("a2", new ReferenceType(new ReferenceType(new IntType())));
		d1.insert("a3", new BoolType());
		d1.insert("a4", new ReferenceType(new ReferenceType(new BoolType())));
		d1.insert("a5", new StringType());
		d2 = d1.clone();
		
		assertEquals(d2.getValue("a1"), new IntType());
		assertEquals(d2.getValue("a2"), new ReferenceType(new ReferenceType(new IntType())));
		assertEquals(d2.getValue("a3"), new BoolType());
		assertEquals(d2.getValue("a4"), new ReferenceType(new ReferenceType(new BoolType())));
		assertEquals(d2.getValue("a5"), new StringType());
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsValueInterface_ModifyingCloneDoesNotAffectOriginalSize() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		d1.insert("a3", new BoolValue(true));
		d1.insert("a5", new StringValue("cartof"));
		d1.insert("a7", new ReferenceValue(new ReferenceType(new BoolType())));
		d2 = d1.clone();
		
		d2.remove("a1");
		assertEquals(d1.size(), 4);
		d2.remove("a3");
		assertEquals(d1.size(), 4);
		d2.remove("a5");
		assertEquals(d1.size(), 4);
		d2.remove("a7");
		assertEquals(d1.size(), 4);
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsValueInterface_ModifyingCloneDoesNotAffectOriginalContent() {
		DictionaryInterface<String, ValueInterface> d1 = new MyDictionary<String, ValueInterface>();
		DictionaryInterface<String, ValueInterface> d2 = new MyDictionary<String, ValueInterface>();
		
		d1.insert("a1", new IntValue(23));
		d1.insert("a3", new BoolValue(true));
		d1.insert("a5", new StringValue("cartof"));
		d1.insert("a7", new ReferenceValue(new ReferenceType(new BoolType())));
		d2 = d1.clone();
		
		d2.update("a1", new IntValue(24));
		assertEquals(d1.getValue("a1"), new IntValue(23));
		d2.update("a3", new BoolValue(false));
		assertEquals(d1.getValue("a3"), new BoolValue(true));
		d2.update("a5", new StringValue("cartofff"));
		assertEquals(d1.getValue("a5"), new StringValue("cartof"));
		d2.update("a7", new ReferenceValue(new ReferenceType(new IntType())));
		assertEquals(d1.getValue("a7"), new ReferenceValue(new ReferenceType(new BoolType())));
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsTypeInterface_ModifyingCloneDoesNotAffectOriginalSize() {
		DictionaryInterface<String, TypeInterface> d1 = new MyDictionary<String, TypeInterface>();
		DictionaryInterface<String, TypeInterface> d2 = new MyDictionary<String, TypeInterface>();
		
		d1.insert("a1", new IntType());
		d1.insert("a3", new BoolType());
		d1.insert("a5", new StringType());
		d1.insert("a7", new ReferenceType(new ReferenceType(new BoolType())));
		d2 = d1.clone();
		
		d2.remove("a1");
		assertEquals(d1.size(), 4);
		d2.remove("a3");
		assertEquals(d1.size(), 4);
		d2.remove("a5");
		assertEquals(d1.size(), 4);
		d2.remove("a7");
		assertEquals(d1.size(), 4);
	}
	
	@Test
	public void Clone_ValidOriginAndValueIsTypeInterface_ModifyingCloneDoesNotAffectOriginalContent() {
		DictionaryInterface<String, TypeInterface> d1 = new MyDictionary<String, TypeInterface>();
		DictionaryInterface<String, TypeInterface> d2 = new MyDictionary<String, TypeInterface>();
		
		d1.insert("a1", new IntType());
		d1.insert("a3", new BoolType());
		d1.insert("a5", new StringType());
		d1.insert("a7", new ReferenceType(new ReferenceType(new BoolType())));
		d2 = d1.clone();
		
		d2.update("a1", new BoolType());
		assertEquals(d1.getValue("a1"), new IntType());
		d2.update("a3", new StringType());
		assertEquals(d1.getValue("a3"), new BoolType());
		d2.update("a5", new ReferenceType(new ReferenceType(new BoolType())));
		assertEquals(d1.getValue("a5"), new StringType());
		d2.update("a7", new IntType());
		assertEquals(d1.getValue("a7"), new ReferenceType(new ReferenceType(new BoolType())));
	}
}

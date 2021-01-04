package testStatement;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import org.junit.*;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyHeap;
import model.ADT.MyList;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
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

public class TestVariableDeclarationStatement{
	static StackInterface<StatementInterface> stack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static DictionaryInterface<StringValue, BufferedReader> fileTable;
	static DictionaryInterface<Integer, ValueInterface> heap;
	static DictionaryInterface<String, TypeInterface> typeEnvironment;
	static ProgramState crtState;
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
		typeEnvironment = new MyDictionary<String, TypeInterface>();
		crtState = new ProgramState(stack, symbolTable, output, fileTable, heap, null);
	}
	
	@After
	public void clearAndCloseData() {
		stack.clear();
		symbolTable.clear();
		output.clear();
		
		for (BufferedReader crtBuffer : fileTable.getAllValues()) {
			try {
				crtBuffer.close();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		fileTable.clear();
		heap.clear();
		typeEnvironment.clear();
	}
	
	@Test
	public void GetTypeEnvironment_VariableAlreadyDefined_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());

		try {
			s1.getTypeEnvironment(s1.getTypeEnvironment(typeEnvironment));
			fail("Variable 'abc' is already defined");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_NewVariable_VariableAddedToTypeEnvironment() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());

		assertTrue(typeEnvironment.isEmpty());
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("abc"), new IntType());
	}
	
	@Test
	public void Execute_NullType_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", null);
		
		try {
			s1.execute(crtState);
			fail("Type cannot be null");
		}
		catch (Exception e){
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_NonExistentInt_IntAdded() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());
		
		assertEquals(symbolTable.size(), 0);
		assertFalse(symbolTable.isDefined("abc"));
		assertTrue(symbolTable.isEmpty());
		// getvalue throws an error when undefined; don't know if I should keep it here or not
		//assertNotEquals(((IntValue)symbolTable.getValue("abc")).getValue(), VariableDeclarationStatement.DEFAULT_INT_VALUE);
		try {
			s1.execute(crtState);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(symbolTable.size(), 1);
		assertTrue(symbolTable.isDefined("abc"));
		assertFalse(symbolTable.isEmpty());
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), new IntValue().getValue()); // default value
	}
	
	@Test
	public void Execute_NonExistentInt_ReturnsNull() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());
		ProgramState result = null;
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertNull(result);
	}
	
	@Test
	public void Execute_ExistingVariable_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());
		StatementInterface s2 = new VariableDeclarationStatement("abc", new BoolType());
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		
		try {
			s2.execute(crtState);
			fail("Variable already exists");
		}
		catch (Exception e){
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_NonExistentBool_BoolAdded() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new BoolType());
		
		assertEquals(symbolTable.size(), 0);
		try {
			s1.execute(crtState);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(symbolTable.size(), 1);
		assertTrue(symbolTable.isDefined("abc"));
		assertFalse(symbolTable.isEmpty());
		assertEquals(((BoolValue)symbolTable.getValue("abc")).getValue(), new BoolValue().getValue()); // default value
	}

	@Test
	public void Execute_NonExistentString_StringAdded() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new StringType());
		
		assertEquals(symbolTable.size(), 0);
		try {
			s1.execute(crtState);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(symbolTable.size(), 1);
		assertTrue(symbolTable.isDefined("abc"));
		assertFalse(symbolTable.isEmpty());
		assertEquals(((StringValue)symbolTable.getValue("abc")).getValue(), new StringValue().getValue()); // default value
	}
	
	@Test
	public void Execute_NonExistentReference_ReferenceAdded() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new ReferenceType(new IntType()));
		
		assertEquals(symbolTable.size(), 0);
		try {
			s1.execute(crtState);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(symbolTable.size(), 1);
		assertTrue(symbolTable.isDefined("abc"));
		assertFalse(symbolTable.isEmpty());
		assertEquals(((ReferenceValue)symbolTable.getValue("abc")).getReferencedType(), new IntType());
	}
}

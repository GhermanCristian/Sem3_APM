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
import model.expression.ValueExpression;
import model.statement.HeapAllocationStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.type.ReferenceType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ReferenceValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestHeapAllocationStatement {
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
	public void GetTypeEnvironment_VariableNotAReference_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestHeapAllocationStatement: variable is not a reference");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ReferenceTypeNotMatching_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new BoolType()));
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestHeapAllocationStatement: the reference type does not match the expression type");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ValidOperandTypes_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s2.getTypeEnvironment(s1.getTypeEnvironment(typeEnvironment));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("a"), new ReferenceType(new IntType()));
	}
	
	@Test
	public void Execute_VariableUndefinedInSymbolTable_ThrowsException() {
		StatementInterface s1 = new HeapAllocationStatement("undefined baby", new ValueExpression(new IntValue()));
		try {
			s1.execute(crtState);
			fail("Variable is undefined in the symbol table");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ValidInput_CorrectSymbolTableUpdate() {
		TypeInterface t1 = new ReferenceType(new IntType());
		StatementInterface s1 = new VariableDeclarationStatement("a", t1);
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(symbolTable.getValue("a"), t1.getDefaultValue());
		
		try {
			s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(symbolTable.getValue("a"), new ReferenceValue(1, t1));
	}
	
	@Test
	public void Execute_ValidInput_CorrectHeapAllocation() {
		TypeInterface t1 = new ReferenceType(new IntType());
		StatementInterface s1 = new VariableDeclarationStatement("a", t1);
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(heap.isEmpty());
		
		try {
			s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(heap.size(), 1);
		assertEquals(((IntValue)heap.getValue(1)).getValue(), 23);
	}
	
	@Test
	public void Execute_ValidInput_ReturnsNull() {
		TypeInterface t1 = new ReferenceType(new IntType());
		StatementInterface s1 = new VariableDeclarationStatement("a", t1);
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		ProgramState result = null;
		
		try {
			s1.execute(crtState);
			result = s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertNull(result);
	}
}

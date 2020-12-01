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
import model.statement.HeapWritingStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.type.ReferenceType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestHeapWritingStatement {
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
	public void Execute_VariableUndefinedInSymbolTable_ThrowsException() {
		StatementInterface s1 = new HeapWritingStatement("undefined baby", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
			fail("Variable is undefined in the symbol table");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_VariableNotAReference_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.execute(crtState);
			fail("Variable is not a reference");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_VariableNotInHeap_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.execute(crtState);
			fail("Variable is not in the heap");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ExpressionTypeNotMatching_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new BoolType()));
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(23)));
		StatementInterface s3 = new HeapAllocationStatement("a", new ValueExpression(new BoolValue(true)));
		
		try {
			s1.execute(crtState);
			s3.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.execute(crtState);
			fail("The type of the variable does not match the expression");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ValidInput_HeapUpdated() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(24)));
		StatementInterface s3 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		assertTrue(heap.isEmpty());
		try {
			s1.execute(crtState);
			s3.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(heap.size(), 1);
		assertEquals(((IntValue)heap.getValue(1)).getValue(), 23);
		
		try {
			s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(heap.size(), 1);
		assertEquals(((IntValue)heap.getValue(1)).getValue(), 24);
	}
	
	@Test
	public void Execute_ValidInput_ReturnsNull() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s3 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(24)));
		ProgramState result = null;
		
		try {
			s1.execute(crtState);
			s3.execute(crtState);
			result = s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertNull(result);
	}
}

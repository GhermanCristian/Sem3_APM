package testStatement;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.util.ArrayList;

import org.junit.*;

import javafx.util.Pair;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyHeap;
import model.ADT.MyList;
import model.ADT.MyLockTable;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.expression.ArithmeticExpression;
import model.expression.LogicalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.AssignmentStatement;
import model.statement.PrintStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestPrintStatement {
	static StackInterface<StatementInterface> stack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static DictionaryInterface<StringValue, BufferedReader> fileTable;
	static DictionaryInterface<Integer, ValueInterface> heap;
	static DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable;
	static DictionaryInterface<String, TypeInterface> typeEnvironment;
	static ProgramState crtState;
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
		semaphoreTable = new MyLockTable<Integer, Pair<Integer, ArrayList<Integer>>>();
		typeEnvironment = new MyDictionary<String, TypeInterface>();
		crtState = new ProgramState(stack, symbolTable, output, fileTable, heap, semaphoreTable, null);
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
		semaphoreTable.clear();
		typeEnvironment.clear();
	}
	
	@Test
	// I won't do another one for InvalidExpression, because that has nothing to do with the print statement, but rather
	// with the underlying expression - however, I do need that sweet sweet code coverage
	public void GetTypeEnvironment_ValidExpression_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new AssignmentStatement("a", new ValueExpression(new IntValue(23)));
		StatementInterface s3 = new PrintStatement(new VariableExpression("a"));
		
		try {
			s2.getTypeEnvironment(s1.getTypeEnvironment(typeEnvironment));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("a"), new IntType());
		try {
			s3.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("a"), new IntType());
	}
	
	@Test
	public void Execute_ExistentVariable_OutputUpdated() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new AssignmentStatement("a", new ValueExpression(new IntValue(23)));
		StatementInterface s3 = new PrintStatement(new VariableExpression("a"));
		
		assertTrue(output.isEmpty());
		
		try {
			s1.execute(crtState); // int a;
			s2.execute(crtState); // a = 23;
			s3.execute(crtState); // print(a);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 1);
		
		try {
			assertEquals(output.getLast().getType(), new IntType());
			assertEquals(((IntValue)output.getLast()).getValue(), 23);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void Execute_NonExistentVariable_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new AssignmentStatement("a", new ValueExpression(new IntValue(23)));
		StatementInterface s3 = new PrintStatement(new VariableExpression("b"));
		
		assertTrue(output.isEmpty());
		
		try {
			s1.execute(crtState); // int a;
			s2.execute(crtState); // a = 23;
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s3.execute(crtState); // print(b);
			fail("Variable doesn't exist");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ValidValueExpression_OutputUpdated() {
		StatementInterface s1 = new PrintStatement(new ValueExpression(new IntValue(23))); // print(23);
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 1);
		try {
			assertEquals(output.getLast().getType(), new IntType());
			assertEquals(((IntValue)output.getLast()).getValue(), 23);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void Execute_ValidArithmeticExpression_OutputUpdated() {
		StatementInterface s1 = new PrintStatement(new ArithmeticExpression(
			new ValueExpression(new IntValue(23)), 
			new ValueExpression(new IntValue(24)), 
			"+")); // print(23 + 24);
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 1);
		try {
			assertEquals(output.getLast().getType(), new IntType());
			assertEquals(((IntValue)output.getLast()).getValue(), 47);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void Execute_ValidBooleanExpression_OutputUpdated() {
		StatementInterface s1 = new PrintStatement(new LogicalExpression(
			new ValueExpression(new BoolValue(true)), 
			new ValueExpression(new BoolValue(false)), 
			"||")); // print(true || false);
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 1);
		try {
			assertEquals(output.getLast().getType(), new BoolType());
			assertTrue(((BoolValue)output.getLast()).getValue());
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void Execute_ValidValueExpression_ReturnsNull() {
		StatementInterface s1 = new PrintStatement(new ValueExpression(new IntValue(23))); // print(23);
		ProgramState result = null;
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertNull(result);
	}
}

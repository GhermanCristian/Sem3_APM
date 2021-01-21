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
import model.statement.IncrementStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestIncrementStatement {
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
	public void GetTypeEnvironment_UndefinedVariable_ThrowsException() {
		StatementInterface s1 = new IncrementStatement("v", "+");
		
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("Variable v is undefined");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_VariableNotInteger_ThrowsException() {
		StatementInterface s0 = new VariableDeclarationStatement("v", new BoolType());
		StatementInterface s1 = new IncrementStatement("v", "+");
		
		try {
			s1.getTypeEnvironment(s0.getTypeEnvironment(typeEnvironment));
			fail("Variable v is undefined");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ValidVariable_TypeEnvironmentUnchanged() {
		StatementInterface s0 = new VariableDeclarationStatement("v", new IntType());
		StatementInterface s1 = new IncrementStatement("v", "+");
		
		try {
			typeEnvironment = s0.getTypeEnvironment(typeEnvironment);
			
			assertEquals(1, typeEnvironment.size());
			assertEquals(new IntType(), typeEnvironment.getValue("v"));
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
			assertEquals(1, typeEnvironment.size());
			assertEquals(new IntType(), typeEnvironment.getValue("v"));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void Execute_UndefinedVariable_ThrowsException() {
		StatementInterface s1 = new IncrementStatement("v", "+");
		
		try {
			s1.execute(crtState);
			fail("Variable v is undefined");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_InvalidOperator_ThrowsException() {
		StatementInterface s0 = new VariableDeclarationStatement("v", new IntType());
		StatementInterface s1 = new IncrementStatement("v", "cartof");
		
		try {
			s0.execute(crtState);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s1.execute(crtState);
			fail("'cartof' is not a valid operator");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ValidVariableAdditionDefaultIncrementValue_ValueUpdated() {
		StatementInterface s0 = new VariableDeclarationStatement("v", new IntType());
		StatementInterface s1 = new IncrementStatement("v", "+");
		
		try {
			s0.execute(crtState);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(new IntValue(0), symbolTable.getValue("v")); // the default value of an integer should be 0
		try {
			
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(new IntValue(1), symbolTable.getValue("v"));
	}
	
	@Test
	public void Execute_ValidVariableSubtractionDefaultIncrementValue_ValueUpdated() {
		StatementInterface s0 = new VariableDeclarationStatement("v", new IntType());
		StatementInterface s1 = new IncrementStatement("v", "-");
		
		try {
			s0.execute(crtState);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(new IntValue(0), symbolTable.getValue("v")); // the default value of an integer should be 0
		try {
			
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(new IntValue(-1), symbolTable.getValue("v"));
	}
	
	@Test
	public void Execute_ValidVariableAdditionNonDefaultIncrementValue_ValueUpdated() {
		StatementInterface s0 = new VariableDeclarationStatement("v", new IntType());
		StatementInterface s1 = new IncrementStatement("v", "+", 23);
		
		try {
			s0.execute(crtState);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(new IntValue(0), symbolTable.getValue("v")); // the default value of an integer should be 0
		try {
			
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(new IntValue(23), symbolTable.getValue("v"));
	}
	
	@Test
	public void Execute_ValidVariableSubtractionNonDefaultIncrementValue_ValueUpdated() {
		StatementInterface s0 = new VariableDeclarationStatement("v", new IntType());
		StatementInterface s1 = new IncrementStatement("v", "-", 23);
		
		try {
			s0.execute(crtState);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(new IntValue(0), symbolTable.getValue("v")); // the default value of an integer should be 0
		try {
			
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(new IntValue(-23), symbolTable.getValue("v"));
	}
}

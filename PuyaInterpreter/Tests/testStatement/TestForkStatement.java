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
import model.statement.AssignmentStatement;
import model.statement.CompoundStatement;
import model.statement.ForkStatement;
import model.statement.PrintStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestForkStatement {
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
	public void GetTypeEnvironment_TypeNotMatching_ThrowsException() {
		StatementInterface s1 = new ForkStatement(new CompoundStatement(
													new VariableDeclarationStatement("a", new BoolType()), 
													new AssignmentStatement("a", new ValueExpression(new IntValue(23)))));
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("TestForkStatement: variable 'a' is should be a bool type");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ValidInputTypes_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new ForkStatement(new CompoundStatement(
													new VariableDeclarationStatement("a", new IntType()), 
													new AssignmentStatement("a", new ValueExpression(new IntValue(23)))));
		assertTrue(typeEnvironment.isEmpty());
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(typeEnvironment.isEmpty());
	}
	
	@Test
	public void Execute_NoStatements_ReturnsNull() {
		StatementInterface s1 = new ForkStatement(null);
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertNull(result);
	}
	
	@Test
	public void Execute_ValidStatements_NewProgramStateCreated() {
		StatementInterface s1 = new ForkStatement(new PrintStatement(new ValueExpression(new IntValue(23))));
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}

		assertFalse(result == crtState);
		//assertNotEquals(result, crtState); // I want to compare their addresses using '==', not .equals()
	}
	
	@Test
	// I can't really test that the new threadID is equal to 2, 3, ... because that depends on the order in which the
	// tests are executed - so the new threadID can range between 2 and whatever
	public void Execute_ValidStatements_DifferentThreadID() {
		StatementInterface s1 = new ForkStatement(new PrintStatement(new ValueExpression(new IntValue(23))));
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}

		assertFalse(crtState.getThreadID() == result.getThreadID());
		//assertNotEquals(crtState.getThreadID(), result.getThreadID());
	}
	
	@Test
	public void Execute_ValidStatements_NewStack() {
		StatementInterface s1 = new ForkStatement(new PrintStatement(new ValueExpression(new IntValue(23))));
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}

		assertFalse(crtState.getExecutionStack() == result.getExecutionStack());
		//assertNotEquals(crtState.getExecutionStack(), result.getExecutionStack());
	}
	
	@Test
	public void Execute_ValidStatements_NewSymbolTable() {
		StatementInterface s1 = new ForkStatement(new PrintStatement(new ValueExpression(new IntValue(23))));
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}

		assertFalse(crtState.getSymbolTable() == result.getSymbolTable());
		//assertNotEquals(crtState.getSymbolTable(), result.getSymbolTable());
	}
	
	@Test
	public void Execute_ValidStatements_CorrectContentAndSizeInSymbolTable() {
		StatementInterface s1 = new ForkStatement(new PrintStatement(new ValueExpression(new IntValue(23))));
		StatementInterface s2 = new VariableDeclarationStatement("a", new IntType());
		ProgramState result = null;
		
		try {
			s2.execute(crtState); // this is executed in the main thread
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}

		assertEquals(crtState.getSymbolTable().size(), result.getSymbolTable().size());
		assertEquals(crtState.getSymbolTable().getValue("a"), result.getSymbolTable().getValue("a"));
	}
	
	@Test
	public void Execute_ValidStatements_SameOutput() {
		StatementInterface s1 = new ForkStatement(new PrintStatement(new ValueExpression(new IntValue(23))));
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}

		assertTrue(crtState.getOutput() == result.getOutput());
	}
	
	@Test
	public void Execute_ValidStatements_SameFileTable() {
		StatementInterface s1 = new ForkStatement(new PrintStatement(new ValueExpression(new IntValue(23))));
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}

		assertTrue(crtState.getFileTable() == result.getFileTable());
	}
	
	@Test
	public void Execute_ValidStatements_SameHeap() {
		StatementInterface s1 = new ForkStatement(new PrintStatement(new ValueExpression(new IntValue(23))));
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}

		assertTrue(crtState.getHeap() == result.getHeap());
	}
	
	@Test
	public void Execute_VariableDeclarationInNewThread_InitialSymbolTableNotModified() {
		StatementInterface s1 = new ForkStatement(new VariableDeclarationStatement("a", new IntType()));
		ProgramState result = null;
		
		assertTrue(crtState.getSymbolTable().isEmpty());
		try {
			result = s1.execute(crtState);
			result.oneStepExecution(); // we execute the vardecl statement
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(crtState.getSymbolTable().isEmpty());
	}
	
	@Test
	public void Execute_VariableDeclarationInNewThread_VariableDeclaredInsideNewThread() {
		StatementInterface s1 = new ForkStatement(new VariableDeclarationStatement("a", new IntType()));
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
			result.oneStepExecution(); // we execute the vardecl statement
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(result.getSymbolTable().isDefined("a"));
		assertEquals(result.getSymbolTable().getValue("a"), new IntValue()); 
		assertEquals(result.getSymbolTable().size(), 1);
		// when creating a new intType, the default value for IntValue is used, which is the same as IntValue() with no args
	}
}

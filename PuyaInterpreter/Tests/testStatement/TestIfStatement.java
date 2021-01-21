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
import model.expression.LogicalExpression;
import model.expression.ValueExpression;
import model.statement.IfStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestIfStatement {
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
	public void GetTypeEnvironment_NonBooleanCondition_ThrowsException() {
		StatementInterface s1 = new IfStatement(new ValueExpression(new IntValue(5)), 
			new VariableDeclarationStatement("v1", new IntType()), 
			new VariableDeclarationStatement("v2", new IntType())
		);
		
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("TestIfStatement: not a boolean condition");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ValidCondition_VariableNotDefinedOutsideScope() {
		StatementInterface s1 = new IfStatement(new ValueExpression(new BoolValue(true)), 
			new VariableDeclarationStatement("v1", new IntType()), 
			new VariableDeclarationStatement("v2", new IntType())
		);
		
		try {
			s1.getTypeEnvironment(typeEnvironment);
			s1.execute(crtState);
			crtState.getExecutionStack().pop().execute(crtState); 
			// after we execute "int v1", only the local typeEnv is changed, not the main one
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertFalse(typeEnvironment.isDefined("v1"));
	}

	@Test
	public void Execute_ValidValueCondition_GoToTrueBranch() {
		StatementInterface s1 = new IfStatement(new ValueExpression(new BoolValue(true)), 
			new VariableDeclarationStatement("v1", new IntType()), 
			new VariableDeclarationStatement("v2", new IntType())
		);
		
		try {
			// "if" will place the correct branch on the exe stack;
			s1.execute(crtState);
			crtState.getExecutionStack().pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(symbolTable.isDefined("v1"));
		assertFalse(symbolTable.isDefined("v2"));
	}
	
	@Test
	public void Execute_ValidValueCondition_GoToFalseBranch() {
		StatementInterface s1 = new IfStatement(new ValueExpression(new BoolValue(false)), 
			new VariableDeclarationStatement("v1", new IntType()), 
			new VariableDeclarationStatement("v2", new IntType())
		);
		
		try {
			// "if" will place the correct branch on the exe stack;
			s1.execute(crtState);
			crtState.getExecutionStack().pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertFalse(symbolTable.isDefined("v1"));
		assertTrue(symbolTable.isDefined("v2"));
	}
	
	@Test
	public void Execute_ValidLogicalCondition_GoToTrueBranch() {
		StatementInterface s1 = new IfStatement(
			new LogicalExpression(
					new ValueExpression(new BoolValue(true)), 
					new ValueExpression(new BoolValue(false)), 
					"||"), // true || false
			new VariableDeclarationStatement("v1", new IntType()), 
			new VariableDeclarationStatement("v2", new IntType())
		);
		
		try {
			// "if" will place the correct branch on the exe stack;
			s1.execute(crtState);
			crtState.getExecutionStack().pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(symbolTable.isDefined("v1"));
		assertFalse(symbolTable.isDefined("v2"));
	}
	
	@Test
	public void Execute_ValidValueCondition_ReturnsNull() {
		StatementInterface s1 = new IfStatement(new ValueExpression(new BoolValue(false)), 
			new VariableDeclarationStatement("v1", new IntType()), 
			new VariableDeclarationStatement("v2", new IntType())
		);
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertNull(result);
	}
}

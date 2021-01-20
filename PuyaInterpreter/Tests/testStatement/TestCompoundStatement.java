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
import model.ADT.MySemaphoreTable;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.expression.ValueExpression;
import model.statement.AssignmentStatement;
import model.statement.CompoundStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestCompoundStatement {
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
		semaphoreTable = new MySemaphoreTable<Integer, Pair<Integer, ArrayList<Integer>>>();
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
	public void GetTypeEnvironment_VariableDeclarationThenAssignment_VariableAddedToEnvironmentAndNoExceptionThrown() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
		);
		
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
	public void Execute_ValidInput_CorrectStackSize() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
		);
		
		assertEquals(stack.size(), 0);
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(stack.size(), 1); // the stack had 2 statements, the first of which was executed
	}
	
	@Test
	public void Execute_ValidInput_CorrectStatementOrder() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
		);
		
		assertFalse(symbolTable.isDefined("abc"));
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		
		assertTrue(symbolTable.isDefined("abc"));
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), 0); // the default initialization value is 0
		
		try { // execute the second statement (value assignment)
			stack.pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), 23);
		assertEquals(stack.size(), 0); // the stack is now empty
	}
	
	@Test
	public void Execute_ValidInput_ReturnsNull() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
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

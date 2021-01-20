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
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.ForStatement;
import model.statement.IncrementStatement;
import model.statement.PrintStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestForStatement {
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
	public void GetTypeEnvironment_NonBooleanCondition_ThrowsException() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(4)), 
									new ValueExpression(new IntValue(12)), 
									new IncrementStatement("v", "-"), 
									new PrintStatement(new VariableExpression("v"))
								);
		try {
			s1.getTypeEnvironment(s1.getTypeEnvironment(typeEnvironment));
			fail("Conditional expression is not boolean");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_InitialExpressionIsNotInteger_ThrowsException() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new BoolValue(true)), 
									new ValueExpression(new BoolValue(false)), 
									new IncrementStatement("v", "-"), 
									new PrintStatement(new VariableExpression("v"))
								);
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("Initial expression should be an integer");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	// technically this exception will be thrown by getTypeEnv from RelationalExpression
	// but it still is one of the possible behaviours of the for statement, so we test it here
	@Test
	public void GetTypeEnvironment_RelationalExpressionRightOperandNotInteger_ThrowsException() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(4)),
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new BoolValue(true)), 
											">"),  
									new IncrementStatement("v", "-"), 
									new PrintStatement(new VariableExpression("v"))
								);
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("Right operand of the relational expression should be an integer");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_BooleanCondition_VariableAddedToTypeEnvironment() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(4)), 
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(0)), 
											">"), 
									new IncrementStatement("v", "-"), 
									new PrintStatement(new VariableExpression("v"))
								);
		
		assertTrue(typeEnvironment.isEmpty());
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(1, typeEnvironment.size());
		assertEquals(new IntType(), typeEnvironment.getValue("v"));
	}
	
	@Test
	public void GetTypeEnvironment_1Iteration_VariableNotDefinedInTheOuterTypeEnvironment() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(1)), 
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(0)), 
											">"), 
									new IncrementStatement("v", "-"), 
									new VariableDeclarationStatement("new v", new IntType())
								);
		
		assertFalse(typeEnvironment.isDefined("new v"));
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
			s1.execute(crtState);
			while (crtState.getExecutionStack().size() > 0) {
				crtState.getExecutionStack().pop().execute(crtState);
			}
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertFalse(typeEnvironment.isDefined("new v"));
	}
	
	@Test
	public void Execute_ConditionalExpressionIsNotRelationalExpression_ThrowsException() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(1)), 
									new ValueExpression(new IntValue(1)),  
									new PrintStatement(new VariableExpression("v")), 
									new PrintStatement(new VariableExpression("v"))
								);
		try {
			s1.execute(crtState);
			fail("Conditional expression should be a relational expression");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_FinalStatementIsNotIncrement_ThrowsException() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(1)), 
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(0)), 
											">"),  
									new PrintStatement(new VariableExpression("v")), 
									new PrintStatement(new VariableExpression("v"))
								);
		
		try {
			s1.execute(crtState);
			fail("FinalStatement is not an IncrementStatement");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_FalseCondition_ExecutesOnlyInitialStatement() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(4)), 
									new RelationalExpression(
										new VariableExpression("v"), 
										new ValueExpression(new IntValue(0)), 
										"<"),
									new IncrementStatement("v", "-"), 
									new PrintStatement(new VariableExpression("v"))
								);
		
		assertNotEquals(new IntValue(4), symbolTable.getValue("v"));
		try {
			s1.execute(crtState);
			while (crtState.getExecutionStack().size() > 0) {
				crtState.getExecutionStack().pop().execute(crtState);
			}
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(new IntValue(4), symbolTable.getValue("v"));
		assertTrue(output.isEmpty()); // nothing gets printed
	}
	
	@Test
	public void Execute_FalseCondition_ReturnsNull() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(4)), 
									new RelationalExpression(
										new VariableExpression("v"), 
										new ValueExpression(new IntValue(0)), 
										"<"),
									new IncrementStatement("v", "-"), 
									new PrintStatement(new VariableExpression("v"))
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
	
	@Test
	public void Execute_5Iterations_CorrectOutputSize() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(5)), 
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(0)), 
											">"), 
									new IncrementStatement("v", "-"), 
									new PrintStatement(new VariableExpression("v"))
								);
		
		assertTrue(output.isEmpty());
		try {
			s1.execute(crtState);
			while (crtState.getExecutionStack().size() > 0) {
				crtState.getExecutionStack().pop().execute(crtState);
			}
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(output.size(), 5);
	}
	
	@Test
	public void Execute_5Iterations_CorrectOutputContent() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(5)), 
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(0)), 
											">"), 
									new IncrementStatement("v", "-"), 
									new PrintStatement(new VariableExpression("v"))
								);
		
		try {
			s1.execute(crtState);
			while (crtState.getExecutionStack().size() > 0) {
				crtState.getExecutionStack().pop().execute(crtState);
			}
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			assertEquals(new IntValue(5), output.get(0));
			assertEquals(new IntValue(4), output.get(1));
			assertEquals(new IntValue(3), output.get(2));
			assertEquals(new IntValue(2), output.get(3));
			assertEquals(new IntValue(1), output.get(4));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
}

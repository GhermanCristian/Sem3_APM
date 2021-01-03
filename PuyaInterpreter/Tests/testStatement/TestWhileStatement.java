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
import model.expression.ArithmeticExpression;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.AssignmentStatement;
import model.statement.CompoundStatement;
import model.statement.PrintStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.statement.WhileStatement;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestWhileStatement {
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
	public void GetTypeEnvironment_NonBooleanCondition_ThrowsException() {
		StatementInterface s1 = new WhileStatement(new ValueExpression(new IntValue(23)), 
												new VariableDeclarationStatement("a", new IntType()));
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("Conditional expression is not boolean");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_BooleanCondition_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new WhileStatement(new ValueExpression(new BoolValue(true)), 
												new VariableDeclarationStatement("a", new IntType()));
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
	public void GetTypeEnvironment_1IterationCompoundStatement_VariableNotDefinedInTheOuterTypeEnvironment() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new AssignmentStatement("a", new ValueExpression(new IntValue(1)));
		StatementInterface s3 = new WhileStatement(
									new RelationalExpression(
										new VariableExpression("a"), 
										new ValueExpression(new IntValue()), 
										">"), 
									new CompoundStatement(
											new VariableDeclarationStatement("new a", new IntType()), 
											new AssignmentStatement("a", new ArithmeticExpression(
																			new VariableExpression("a"), 
																			new ValueExpression(new IntValue(1)), 
																			"-"))));
		try {
			typeEnvironment = s3.getTypeEnvironment(s2.getTypeEnvironment(s1.getTypeEnvironment(typeEnvironment)));
			s1.execute(crtState);
			s2.execute(crtState);
			s3.execute(crtState);
			while (crtState.getExecutionStack().size() > 0) {
				crtState.getExecutionStack().pop().execute(crtState);
			}
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertFalse(typeEnvironment.isDefined("new a"));
	}
	
	@Test
	public void Execute_FalseConditionShouldBeTrue_DoesNothing() {
		StatementInterface s1 = new WhileStatement(
									new ValueExpression(new BoolValue(false)), 
									new VariableDeclarationStatement("a", new IntType()));
		assertTrue(symbolTable.isEmpty());
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(symbolTable.isEmpty());
	}
	
	@Test
	public void Execute_FalseConditionShouldBeTrue_ReturnsNull() {
		StatementInterface s1 = new WhileStatement(
									new ValueExpression(new BoolValue(false)), 
									new VariableDeclarationStatement("a", new IntType()));
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
	public void Execute_TrueConditionShouldBeFalse_DoesNothing() {
		StatementInterface s1 = new WhileStatement(
									new ValueExpression(new BoolValue(true)), 
									new VariableDeclarationStatement("a", new IntType()),
									false);
		assertTrue(symbolTable.isEmpty());
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(symbolTable.isEmpty());
	}
	
	@Test
	public void Execute_TrueConditionShouldBeFalse_ReturnsNull() {
		StatementInterface s1 = new WhileStatement(
									new ValueExpression(new BoolValue(true)), 
									new VariableDeclarationStatement("a", new IntType()),
									false);
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
	public void Execute_5IterationsCompoundStatement_CorrectOutputSize() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new AssignmentStatement("a", new ValueExpression(new IntValue(5)));
		StatementInterface s3 = new WhileStatement(
									new RelationalExpression(
										new VariableExpression("a"), 
										new ValueExpression(new IntValue()), 
										">"), 
									new CompoundStatement(
											new PrintStatement(new VariableExpression("a")), 
											new AssignmentStatement("a", new ArithmeticExpression(
																			new VariableExpression("a"), 
																			new ValueExpression(new IntValue(1)), 
																			"-"))));
		assertTrue(output.isEmpty());
		try {
			s1.execute(crtState);
			s2.execute(crtState);
			s3.execute(crtState);
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
	public void Execute_5IterationsCompoundStatement_CorrectOutputContent() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new AssignmentStatement("a", new ValueExpression(new IntValue(5)));
		StatementInterface s3 = new WhileStatement(
									new RelationalExpression(
										new VariableExpression("a"), 
										new ValueExpression(new IntValue()), 
										">"), 
									new CompoundStatement(
											new PrintStatement(new VariableExpression("a")), 
											new AssignmentStatement("a", new ArithmeticExpression(
																			new VariableExpression("a"), 
																			new ValueExpression(new IntValue(1)), 
																			"-"))));
		try {
			s1.execute(crtState);
			s2.execute(crtState);
			s3.execute(crtState);
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

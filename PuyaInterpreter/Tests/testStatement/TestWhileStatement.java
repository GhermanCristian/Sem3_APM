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
import model.expression.ExpressionInterface;
import model.expression.LogicalExpression;
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
	static ProgramState crtState;
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
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
	}
	
	@Test
	public void Execute_NonBooleanCondition_ThrowsException() {
		StatementInterface s1 = new WhileStatement(new ValueExpression(new IntValue(23)), 
												new VariableDeclarationStatement("a", new IntType()));
		try {
			s1.execute(crtState);
			fail("Conditional expression is not boolean");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_FalseCondition_DoesNothing() {
		StatementInterface s1 = new WhileStatement(new ValueExpression(new BoolValue(false)), 
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
	public void Execute_FalseCondition_ReturnsNull() {
		StatementInterface s1 = new WhileStatement(new ValueExpression(new BoolValue(false)), 
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
	public void Execute_5IterationsSingleStatement_CorrectOutputSize() {
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
	public void Execute_5IterationsSingleStatement_CorrectOutputContent() {
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
		
		int crtValue = 1;
		try {
			while (output.size() > 0) {
				assertEquals(output.pop(), new IntValue(crtValue));
				crtValue++;
			}
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
}

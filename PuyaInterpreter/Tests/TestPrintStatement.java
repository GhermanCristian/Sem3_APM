import static org.junit.jupiter.api.Assertions.*;

import org.junit.*;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyList;
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
import model.value.BoolValue;
import model.value.IntValue;
import model.value.ValueInterface;

public class TestPrintStatement {
	static StackInterface<StatementInterface> stack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static ProgramState crtState;
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		crtState = new ProgramState(stack, symbolTable, output, null);
	}
	
	@Before
	public void clearData() {
		stack.clear();
		symbolTable.clear();
		output.clear();
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
			0)); // print(23 + 24);
		
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
			1)); // print(true || false);
		
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

}
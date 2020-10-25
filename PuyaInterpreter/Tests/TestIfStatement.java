import static org.junit.jupiter.api.Assertions.*;

import org.junit.*;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyList;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.expression.LogicalExpression;
import model.expression.ValueExpression;
import model.statement.IfStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.ValueInterface;

public class TestIfStatement {
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
	public void Execute_NonBooleanCondition_ThrowsException() {
		StatementInterface s1 = new IfStatement(new ValueExpression(new IntValue(5)), 
			new VariableDeclarationStatement("v1", new IntType()), 
			new VariableDeclarationStatement("v2", new IntType())
		);
		
		try {
			s1.execute(crtState);
			fail("Not a boolean condition");
		}
		catch (Exception e) {
			assertTrue(true);
		}
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
					1), // true || false
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
}

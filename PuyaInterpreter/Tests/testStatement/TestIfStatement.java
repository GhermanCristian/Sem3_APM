package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.LogicalExpression;
import model.expression.ValueExpression;
import model.statement.IfStatement;
import model.statement.PrintStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;

public class TestIfStatement extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
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
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
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
				new PrintStatement(new ValueExpression(new IntValue(10))), 
				new PrintStatement(new ValueExpression(new IntValue(11)))
			);
			
		try {
			// "if" will place the correct branch on the exe stack;
			s1.execute(crtState);
			crtState.getExecutionStack().pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 1);
		try {
			assertEquals(output.get(0), new IntValue(10));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void Execute_ValidValueCondition_GoToFalseBranch() {
		StatementInterface s1 = new IfStatement(new ValueExpression(new BoolValue(false)), 
			new PrintStatement(new ValueExpression(new IntValue(10))), 
			new PrintStatement(new ValueExpression(new IntValue(11)))
		);
		
		try {
			// "if" will place the correct branch on the exe stack;
			s1.execute(crtState);
			crtState.getExecutionStack().pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 1);
		try {
			assertEquals(output.get(0), new IntValue(11));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void Execute_ValidValueConditionAndIsLastStatement_VariableDefinedOutsideScope() {
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
		
		assertTrue(symbolTable.isDefined("v2"));
	}
	
	@Test
	public void Execute_ValidValueConditionAndIsNotLastStatement_VariableNotDefinedOutsideScope() {
		StatementInterface s1 = new IfStatement(new ValueExpression(new BoolValue(false)), 
			new VariableDeclarationStatement("v1", new IntType()), 
			new VariableDeclarationStatement("v2", new IntType())
		);
		StatementInterface s2 = new PrintStatement(new ValueExpression(new StringValue("hello")));
		
		try {
			// we don't even have to execute this statement, as long as it is in the stack, the out of scope variables are removed
			crtState.getExecutionStack().push(s2); 
			// "if" will place the correct branch on the exe stack;
			s1.execute(crtState);
			crtState.getExecutionStack().pop().execute(crtState); // the if branch
			crtState.getExecutionStack().pop().execute(crtState); // the clear out of scope variable
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(symbolTable.isEmpty());
	}
	
	@Test
	public void Execute_ValidLogicalCondition_GoToTrueBranch() {
		StatementInterface s1 = new IfStatement(
			new LogicalExpression(
					new ValueExpression(new BoolValue(true)), 
					new ValueExpression(new BoolValue(false)), 
					"||"), // true || false
			new PrintStatement(new ValueExpression(new IntValue(10))), 
			new PrintStatement(new ValueExpression(new IntValue(11)))
		);
		
		try {
			// "if" will place the correct branch on the exe stack;
			s1.execute(crtState);
			crtState.getExecutionStack().pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 1);
		try {
			assertEquals(output.get(0), new IntValue(10));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
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

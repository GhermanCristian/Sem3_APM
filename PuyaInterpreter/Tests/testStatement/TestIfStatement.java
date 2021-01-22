package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.LogicalExpression;
import model.expression.ValueExpression;
import model.statement.IfStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;

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

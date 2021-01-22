package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.AssignmentStatement;
import model.statement.CompoundStatement;
import model.statement.IncrementStatement;
import model.statement.PrintStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.statement.WhileStatement;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;

public class TestWhileStatement extends BaseTest {
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
	public void GetTypeEnvironment_BooleanCondition_VariableNotDefinedInTheOuterTypeEnvironment() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new AssignmentStatement("a", new ValueExpression(new IntValue(1)));
		StatementInterface s3 = new WhileStatement(
									new RelationalExpression(
										new VariableExpression("a"), 
										new ValueExpression(new IntValue()), 
										">"), 
									new CompoundStatement(
											new VariableDeclarationStatement("new a", new IntType()), 
											new IncrementStatement("a", "-")));
		try {
			typeEnvironment = s3.getTypeEnvironment(s2.getTypeEnvironment(s1.getTypeEnvironment(typeEnvironment)));
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
											new IncrementStatement("a", "-")));
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
											new IncrementStatement("a", "-")));
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

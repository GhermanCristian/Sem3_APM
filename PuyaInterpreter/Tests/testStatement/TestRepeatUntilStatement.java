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
import model.statement.RepeatUntilStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;

public class TestRepeatUntilStatement extends BaseTest {
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
		StatementInterface s1 = new RepeatUntilStatement(
									new VariableDeclarationStatement("a", new IntType()),
									new ValueExpression(new IntValue(23)));
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
		StatementInterface s1 = new RepeatUntilStatement(
									new VariableDeclarationStatement("a", new IntType()),
									new ValueExpression(new BoolValue(true)));
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
		StatementInterface s3 = new RepeatUntilStatement(
									new CompoundStatement(
											new VariableDeclarationStatement("new a", new IntType()), 
											new IncrementStatement("a", "-")),
									new RelationalExpression(
											new VariableExpression("a"), 
											new ValueExpression(new IntValue()), 
											"<="));
		try {
			typeEnvironment = s3.getTypeEnvironment(s2.getTypeEnvironment(s1.getTypeEnvironment(typeEnvironment)));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertFalse(typeEnvironment.isDefined("new a"));
	}
	
	@Test
	public void Execute_TrueCondition_ExecutesOnlyOnce() {
		StatementInterface s1 = new RepeatUntilStatement(
									new PrintStatement(new ValueExpression(new IntValue(23))),
									new ValueExpression(new BoolValue(true)));
		assertTrue(output.isEmpty());
		try {
			s1.execute(crtState);
			stack.pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(1, output.size());
	}
	
	@Test
	public void Execute_TrueCondition_ReturnsNull() {
		StatementInterface s1 = new RepeatUntilStatement(
									new VariableDeclarationStatement("a", new IntType()),
									new ValueExpression(new BoolValue(true)));
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
		StatementInterface s3 = new RepeatUntilStatement(
									new CompoundStatement(
										new PrintStatement(new VariableExpression("a")), 
										new IncrementStatement("a", "-")),
									new RelationalExpression(
											new VariableExpression("a"), 
											new ValueExpression(new IntValue()), 
											"<="));
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
		StatementInterface s3 = new RepeatUntilStatement(
									new CompoundStatement(
										new PrintStatement(new VariableExpression("a")), 
										new IncrementStatement("a", "-")),
									new RelationalExpression(
											new VariableExpression("a"), 
											new ValueExpression(new IntValue()), 
											"<="));
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

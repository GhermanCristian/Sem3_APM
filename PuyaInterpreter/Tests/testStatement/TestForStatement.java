package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.AssignmentStatement;
import model.statement.EmptyStatement;
import model.statement.ForStatement;
import model.statement.IncrementStatement;
import model.statement.PrintStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;

public class TestForStatement extends BaseTest {
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
	public void GetTypeEnvironment_DeclaringVariableWithSameNameAsOutsideVariable_ThrowsException() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(5)), 
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(0)), 
											">"), 
									new IncrementStatement("v", "-"), 
									new VariableDeclarationStatement("x", new IntType())
								);
		try {
			typeEnvironment = new VariableDeclarationStatement("v", new IntType()).getTypeEnvironment(typeEnvironment);
			s1.getTypeEnvironment(typeEnvironment);
			fail("Variable v is already defined in the type environment");
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
	public void Execute_5Iterations_VariablesNotDefinedInOuterSymbolTable() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(5)), 
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(0)), 
											">"), 
									new IncrementStatement("v", "-"), 
									new VariableDeclarationStatement("x", new IntType())
								);
		try {
			crtState.getExecutionStack().push(new EmptyStatement()); // as long as there's something left in the stack, the out of scope variables will be removed
			s1.execute(crtState);
			while (crtState.getExecutionStack().size() > 0) {
				crtState.getExecutionStack().pop().execute(crtState);
			}
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertFalse(symbolTable.isDefined("v"));
		assertFalse(symbolTable.isDefined("x"));
	}
	
	@Test
	public void Execute_DeclaringVariableWithSameNameAsOutsideVariable_ThrowsException() {
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(5)), 
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(0)), 
											">"), 
									new IncrementStatement("v", "-"), 
									new VariableDeclarationStatement("x", new IntType())
								);
		try {
			new VariableDeclarationStatement("v", new IntType()).execute(crtState);
			crtState.getExecutionStack().push(new EmptyStatement()); // as long as there's something left in the stack, the out of scope variables will be removed
			s1.execute(crtState);
			while (crtState.getExecutionStack().size() > 0) {
				crtState.getExecutionStack().pop().execute(crtState);
			}
			fail("Variable v is already defined in the symbol table");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_5Iterations_OuterVariableModifiedInsideScopeAndChangesAreVisibleOutside() {
		StatementInterface s0 = new VariableDeclarationStatement("x", new IntType());
		StatementInterface s01 = new AssignmentStatement("x", new ValueExpression(new IntValue(10)));
		StatementInterface s1 = new ForStatement(
									"v",
									new ValueExpression(new IntValue(5)), 
									new RelationalExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(0)), 
											">"), 
									new IncrementStatement("v", "-"), 
									new IncrementStatement("x", "-")
								);
		try {
			crtState.getExecutionStack().push(new EmptyStatement()); // as long as there's something left in the stack, the out of scope variables will be removed
			s0.execute(crtState);
			s01.execute(crtState);
			s1.execute(crtState);
			while (crtState.getExecutionStack().size() > 0) {
				crtState.getExecutionStack().pop().execute(crtState);
			}
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(symbolTable.getValue("x"), new IntValue(5));
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

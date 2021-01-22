package testStatement;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.ValueExpression;
import model.statement.AssignmentStatement;
import model.statement.CompoundStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.value.IntValue;

public class TestCompoundStatement extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	public void GetTypeEnvironment_VariableDeclarationThenAssignment_VariableAddedToEnvironmentAndNoExceptionThrown() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
		);
		
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("abc"), new IntType());
	}
	
	@Test
	public void Execute_ValidInput_CorrectStackSize() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
		);
		
		assertEquals(stack.size(), 0);
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(stack.size(), 1); // the stack had 2 statements, the first of which was executed
	}
	
	@Test
	public void Execute_ValidInput_CorrectStatementOrder() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
		);
		
		assertFalse(symbolTable.isDefined("abc"));
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		
		assertTrue(symbolTable.isDefined("abc"));
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), 0); // the default initialization value is 0
		
		try { // execute the second statement (value assignment)
			stack.pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), 23);
		assertEquals(stack.size(), 0); // the stack is now empty
	}
	
	@Test
	public void Execute_ValidInput_ReturnsNull() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
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

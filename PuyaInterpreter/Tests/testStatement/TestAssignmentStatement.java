package testStatement;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.ValueExpression;
import model.statement.AssignmentStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.value.IntValue;

public class TestAssignmentStatement extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	public void Execute_UndefinedVariable_ThrowsException() {
		StatementInterface s1 = new AssignmentStatement("abc", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
			fail("Variable is undefined");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_UndefinedVariable_ThrowsException() {
		StatementInterface s2 = new AssignmentStatement("abc", new ValueExpression(new IntValue(23)));
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestAssignmentStatement: undefined variable");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_TypeNotMatching_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new BoolType());
		StatementInterface s2 = new AssignmentStatement("abc", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestAssignmentStatement: Type doesn't match");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ValidOperandTypes_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());
		StatementInterface s2 = new AssignmentStatement("abc", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("abc"), new IntType());
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("abc"), new IntType());
	}
	
	@Test
	public void Execute_ValidInput_VariableUpdated() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());
		StatementInterface s2 = new AssignmentStatement("abc", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState); // int abc = 0; (0 is the default initialization value)
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), 0);
		try {
			s2.execute(crtState); // abc = 23;
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), 23);
	}
	
	@Test
	public void Execute_ValidInput_ReturnsNull() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());
		StatementInterface s2 = new AssignmentStatement("abc", new ValueExpression(new IntValue(23)));
		ProgramState result = null;
		
		try {
			s1.execute(crtState); // int abc = 0; (0 is the default initialization value)
			result = s2.execute(crtState); // abc = 23;
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertNull(result);
	}
}

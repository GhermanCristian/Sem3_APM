package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
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

public class TestPrintStatement extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	// I won't do another one for InvalidExpression, because that has nothing to do with the print statement, but rather
	// with the underlying expression - however, I do need that sweet sweet code coverage
	public void GetTypeEnvironment_ValidExpression_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new AssignmentStatement("a", new ValueExpression(new IntValue(23)));
		StatementInterface s3 = new PrintStatement(new VariableExpression("a"));
		
		try {
			s2.getTypeEnvironment(s1.getTypeEnvironment(typeEnvironment));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("a"), new IntType());
		try {
			s3.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("a"), new IntType());
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
			"+")); // print(23 + 24);
		
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
			"||")); // print(true || false);
		
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

	@Test
	public void Execute_ValidValueExpression_ReturnsNull() {
		StatementInterface s1 = new PrintStatement(new ValueExpression(new IntValue(23))); // print(23);
		ProgramState result = null;
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertNull(result);
	}
}

package testExpression;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.expression.ExpressionInterface;
import model.expression.LogicalExpression;
import model.expression.ValueExpression;
import model.type.BoolType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.ValueInterface;

public class TestLogicalExpression extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	public void TypeCheck_FirstOperandNotBoolean_ThrowsException() {
		ExpressionInterface e1 = new LogicalExpression(
			new ValueExpression(new IntValue(12)), 
			new ValueExpression(new BoolValue(true)), 
			"||");
		
		try {
			e1.typeCheck(typeEnvironment);
			fail("First operand should be boolean");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void TypeCheck_SecondOperandNotBoolean_ThrowsException() {
		ExpressionInterface e1 = new LogicalExpression(
			new ValueExpression(new BoolValue(true)), 
			new ValueExpression(new IntValue(12)), 
			"||");
			
		try {
			e1.typeCheck(typeEnvironment);
			fail("Second operand should be boolean");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void TypeCheck_ValidOperands_ReturnsBoolType() {
		ExpressionInterface e1 = new LogicalExpression(
			new ValueExpression(new BoolValue(true)), 
			new ValueExpression(new BoolValue(true)), 
			"||");
			
		TypeInterface result = null;
		try {
			result = e1.typeCheck(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(result, new BoolType());
	}
	
	@Test
	public void Evaluate_InvalidOperator_ThrowsException() {
		ExpressionInterface e1 = new LogicalExpression(
			new ValueExpression(new BoolValue(true)), 
			new ValueExpression(new BoolValue(true)), 
			"invalidOperator");
		
		try {
			e1.evaluate(symbolTable, heap);
			fail("Invalid operator");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Evaluate_ValidAND_CorrectResult() {
		ExpressionInterface e1 = new LogicalExpression(
			new ValueExpression(new BoolValue(true)), 
			new ValueExpression(new BoolValue(false)), 
			"&&");
		
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertFalse(((BoolValue)res).getValue());
	}
	
	@Test
	public void Evaluate_ValidOR_CorrectResult() {
		ExpressionInterface e1 = new LogicalExpression(
			new ValueExpression(new BoolValue(true)), 
			new ValueExpression(new BoolValue(false)), 
			"||");
			
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(((BoolValue)res).getValue());
	}
}

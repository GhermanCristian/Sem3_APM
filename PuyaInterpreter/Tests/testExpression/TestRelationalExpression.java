package testExpression;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.expression.ExpressionInterface;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.type.BoolType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.ValueInterface;

public class TestRelationalExpression extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	public void TypeCheck_FirstOperandNotInteger_ThrowsException() {
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new BoolValue(true)), 
			new ValueExpression(new IntValue(0)), 
			"==");
		
		try {
			e1.typeCheck(typeEnvironment);
			fail("First operand is not an integer");
		}
		catch (Exception e) {
			assertTrue(true);
		}	
	}
	
	@Test
	public void TypeCheck_SecondOperandNotInteger_ThrowsException() {
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new IntValue(0)), 
			new ValueExpression(new BoolValue(true)), 
			"==");
		
		try {
			e1.typeCheck(typeEnvironment);
			fail("Second operand is not an integer");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void TypeCheck_ValidOperands_ReturnsBoolType() {
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new IntValue(0)), 
			new ValueExpression(new IntValue(0)), 
			"==");
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
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new IntValue(1)), 
			new ValueExpression(new IntValue(2)), 
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
	public void Evaluate_ValidLessThan_CorrectResult() {
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new IntValue(23)), 
			new ValueExpression(new IntValue(24)), 
			"<");
		
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(((BoolValue)res).getValue());
	}
	
	@Test
	public void Evaluate_ValidLessThanOrEqualTo_CorrectResult() {
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new IntValue(23)), 
			new ValueExpression(new IntValue(24)), 
			"<=");
		
		ValueInterface res1 = null;
		try {
			res1 = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(((BoolValue)res1).getValue());
	}
	
	@Test
	public void Evaluate_ValidLargerThan_CorrectResult() {
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new IntValue(24)), 
			new ValueExpression(new IntValue(23)), 
			">");
		
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(((BoolValue)res).getValue());
	}
	
	@Test
	public void Evaluate_ValidLargerThanOrEqualTo_CorrectResult() {
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new IntValue(24)), 
			new ValueExpression(new IntValue(23)), 
			">=");
		
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(((BoolValue)res).getValue());
	}
	
	@Test
	public void Evaluate_ValidEqualTo_CorrectResult() {
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new IntValue(23)), 
			new ValueExpression(new IntValue(23)), 
			"==");
		
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(((BoolValue)res).getValue());
	}
	
	@Test
	public void Evaluate_ValidDifferentFrom_CorrectResult() {
		ExpressionInterface e1 = new RelationalExpression(
			new ValueExpression(new IntValue(23)), 
			new ValueExpression(new IntValue(24)), 
			"!=");
		
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

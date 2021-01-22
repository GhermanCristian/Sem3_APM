package testExpression;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.expression.ArithmeticExpression;
import model.expression.ExpressionInterface;
import model.expression.ValueExpression;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.ValueInterface;

public class TestArithmeticExpression extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
	}
	
	@Test
	public void TypeCheck_FirstOperandNotInteger_ThrowsException() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new BoolValue(true)), 
			new ValueExpression(new IntValue(0)), 
			"+");
		
		try {
			e1.typeCheck(typeEnvironment);
			fail("First operand not an integer");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void TypeCheck_SecondOperandNotInteger_ThrowsException() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new IntValue(1)), 
			new ValueExpression(new BoolValue(false)), 
			"+");
		
		try {
			e1.typeCheck(typeEnvironment);
			fail("Second operand not an integer");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void TypeCheck_ValidOperands_ReturnsIntType() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new IntValue(1)), 
			new ValueExpression(new IntValue(1)), 
			"+");
		TypeInterface result = null;
	
		try {
			result = e1.typeCheck(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(result, new IntType());
	}
	
	@Test
	public void Evaluate_DivisionByZero_ThrowsException() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new IntValue(1)), 
			new ValueExpression(new IntValue(0)), 
			"/"); // e1 = 1 / 0
		
		try {
			e1.evaluate(symbolTable, heap);
			fail("Division by zero");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Evaluate_InvalidOperator_ThrowsException() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new IntValue(1)), 
			new ValueExpression(new IntValue(0)), 
			"invalidOperator"); // not an operator code
		
		try {
			e1.evaluate(symbolTable, heap);
			fail("Invalid operator");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Evaluate_ValidAddition_CorrectResult() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new IntValue(23)), 
			new ValueExpression(new IntValue(24)), 
			"+"); // 23 + 24
		
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(((IntValue)res).getValue(), 47);
	}
	
	@Test
	public void Evaluate_ValidSubtraction_CorrectResult() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new IntValue(23)), 
			new ValueExpression(new IntValue(24)), 
			"-"); // 23 - 24
		
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(((IntValue)res).getValue(), -1);
	}
	
	@Test
	public void Evaluate_ValidMultiplication_CorrectResult() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new IntValue(23)), 
			new ValueExpression(new IntValue(24)), 
			"*"); // 23 * 24
		
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(((IntValue)res).getValue(), 552);
	}
	
	@Test
	public void Evaluate_ValidDivision_CorrectResult() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new IntValue(24)), 
			new ValueExpression(new IntValue(23)), 
			"/"); // 24 / 23
		
		ValueInterface res = null;
		try {
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(((IntValue)res).getValue(), 1);
	}
}

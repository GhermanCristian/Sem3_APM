package testExpression;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;

import org.junit.*;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyHeap;
import model.ADT.MyList;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.expression.ArithmeticExpression;
import model.expression.ExpressionInterface;
import model.expression.ValueExpression;
import model.statement.StatementInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestArithmeticExpression {
	static StackInterface<StatementInterface> stack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static DictionaryInterface<StringValue, BufferedReader> fileTable;
	static DictionaryInterface<Integer, ValueInterface> heap;
	static ProgramState crtState;
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
		crtState = new ProgramState(stack, symbolTable, output, fileTable, heap, null);
	}
	
	@After
	public void clearAndCloseData() {
		stack.clear();
		symbolTable.clear();
		output.clear();
		
		for (BufferedReader crtBuffer : fileTable.getAllValues()) {
			try {
				crtBuffer.close();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		fileTable.clear();
		heap.clear();
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
	public void Evaluate_FirstOperandNotInteger_ThrowsException() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new BoolValue(true)), 
			new ValueExpression(new IntValue(0)), 
			"+");
		
		try {
			e1.evaluate(symbolTable, heap);
			fail("First operand not an integer");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Evaluate_SecondOperandNotInteger_ThrowsException() {
		ExpressionInterface e1 = new ArithmeticExpression(
			new ValueExpression(new IntValue(1)), 
			new ValueExpression(new BoolValue(false)), 
			"+");
		
		try {
			e1.evaluate(symbolTable, heap);
			fail("Second operand not an integer");
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

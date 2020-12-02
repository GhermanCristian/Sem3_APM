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
import model.expression.ExpressionInterface;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.statement.StatementInterface;
import model.type.BoolType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestRelationalExpression {
	static StackInterface<StatementInterface> stack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static DictionaryInterface<StringValue, BufferedReader> fileTable;
	static DictionaryInterface<Integer, ValueInterface> heap;
	static DictionaryInterface<String, TypeInterface> typeEnvironment;
	static ProgramState crtState;
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
		typeEnvironment = new MyDictionary<String, TypeInterface>();
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
		typeEnvironment.clear();
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

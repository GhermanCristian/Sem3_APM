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
import model.expression.HeapReadingExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.HeapAllocationStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.type.ReferenceType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ReferenceValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestHeapReadingExpression {
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
	public void TypeCheck_ExpressionNotReference_ThrowsException() {
		ExpressionInterface e1 = new HeapReadingExpression(new ValueExpression(new IntValue()));
		try {
			e1.typeCheck(typeEnvironment);
			fail("Expression is not a reference");
		}
		catch(Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void TypeCheck_ValidExpressionType_ReturnsInnerType() {
		ExpressionInterface e1 = new HeapReadingExpression(new ValueExpression(new ReferenceValue(new IntType())));
		TypeInterface result = null;
		try {
			result = e1.typeCheck(typeEnvironment);
		}
		catch(Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(result, new IntType());
	}
	
	@Test
	public void Evaluate_UndefinedVariable_ThrowsException() {
		ExpressionInterface e1 = new HeapReadingExpression(new ValueExpression(new ReferenceValue(new IntType())));
		try {
			e1.evaluate(symbolTable, heap);
			fail("Undefined variable");
		}
		catch(Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Evaluate_ValidInput_CorrectValue() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		ExpressionInterface e1 = new HeapReadingExpression(new VariableExpression("a"));
		
		ValueInterface res = null;
		try {
			s1.execute(crtState);
			s2.execute(crtState);
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(((IntValue)res).getValue(), 23);
	}
}

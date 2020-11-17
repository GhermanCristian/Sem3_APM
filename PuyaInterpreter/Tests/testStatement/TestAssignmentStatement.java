package testStatement;
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
import model.expression.ValueExpression;
import model.statement.AssignmentStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestAssignmentStatement {
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
	public void Execute_TypeNotMatching_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new BoolType());
		StatementInterface s2 = new AssignmentStatement("abc", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
			s2.execute(crtState);
			fail("Type doesn't match");
		}
		catch (Exception e) {
			assertTrue(true);
		}
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
}

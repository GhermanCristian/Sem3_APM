package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedReader;
import org.junit.*;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyList;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.statement.EmptyStatement;
import model.statement.StatementInterface;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestEmptyStatement {
	static StackInterface<StatementInterface> stack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static DictionaryInterface<StringValue, BufferedReader> fileTable;
	static ProgramState crtState;
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		crtState = new ProgramState(stack, symbolTable, output, fileTable, null);
	}
	
	@Before
	public void clearData() {
		stack.clear();
		symbolTable.clear();
		output.clear();
	}
	
	@Test
	public void Execute_ValidInput_NoEffect() {
		StatementInterface s1 = new EmptyStatement();
		
		assertEquals(stack.size(), 0);
		assertEquals(symbolTable.size(), 0);
		assertTrue(output.isEmpty());
		assertEquals(fileTable.size(), 0);
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(stack.size(), 0);
		assertEquals(symbolTable.size(), 0);
		assertTrue(output.isEmpty());
		assertEquals(fileTable.size(), 0);
	}

}

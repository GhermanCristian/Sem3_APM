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
import model.statement.CloseReadFileStatement;
import model.statement.OpenReadFileStatement;
import model.statement.StatementInterface;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestCloseReadFileStatement {
	private final String SRC_FOLDER_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter";
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
	public void Execute_FilePathNotAString_ThrowsException() {
		StatementInterface s1 = new CloseReadFileStatement(new ValueExpression(new IntValue()));
		
		try {
			s1.execute(crtState);
			fail("File path is not a string");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void Execute_UndefinedFile_ThrowsException() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new CloseReadFileStatement(new ValueExpression(path));
		
		try {
			s1.execute(crtState);
			fail("File is not defined in the file table");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ValidInput_FileRemovedFromTable() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(path));
		StatementInterface s2 = new CloseReadFileStatement(new ValueExpression(path));
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}		
		assertEquals(fileTable.size(), 1);
		assertTrue(fileTable.isDefined(path));
		
		try {
			s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(fileTable.size(), 0);
		assertFalse(fileTable.isDefined(path));
	}
	
	@Test
	public void Execute_ValidInput_ReturnsNull() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(path));
		StatementInterface s2 = new CloseReadFileStatement(new ValueExpression(path));	
		
		ProgramState result = null;
		try {
			s1.execute(crtState);
			result = s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertNull(result);
	}
}

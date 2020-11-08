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
import model.expression.ValueExpression;
import model.statement.OpenReadFileStatement;
import model.statement.ReadFileStatement;
import model.statement.StatementInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestReadFileStatement {
	private final String SRC_FOLDER_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter";
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
	}

	@Test
	public void Execute_UndefinedVariable_ThrowsException() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new ReadFileStatement(new ValueExpression(path), "nonExistentVariable");
		
		try {
			s1.execute(crtState);
			fail("Undefined variable");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_NonIntegerValue_ThrowsException() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new ReadFileStatement(new ValueExpression(path), "someVariable");
		
		symbolTable.insert("someVariable", new BoolValue());
		try {
			s1.execute(crtState);
			fail("Variable is not an integer");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}	
	
	@Test
	public void Execute_PathNotAString_ThrowsException() {
		StatementInterface s1 = new ReadFileStatement(new ValueExpression(new IntValue()), "someVariable");
		symbolTable.insert("someVariable", new IntValue());
		
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
		StatementInterface s1 = new ReadFileStatement(new ValueExpression(path), "someVariable");
		symbolTable.insert("someVariable", new IntValue());
		
		try {
			s1.execute(crtState);
			fail("File is not defined in the fileTable");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ValidEmptyInput_VariableUpdated() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\emptyFile.txt");
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(path));
		StatementInterface s2 = new ReadFileStatement(new ValueExpression(path), "someVariable");
		symbolTable.insert("someVariable", new IntValue(23));
		
		assertEquals(23, ((IntValue)symbolTable.getValue("someVariable")).getValue());
		try {
			s1.execute(crtState);
			s2.execute(crtState);	
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(0, ((IntValue)symbolTable.getValue("someVariable")).getValue());
	}
	
	@Test
	public void Execute_ValidNonEmptyInput_VariableUpdated() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(path));
		StatementInterface s2 = new ReadFileStatement(new ValueExpression(path), "someVariable");
		symbolTable.insert("someVariable", new IntValue(23));
		
		assertEquals(23, ((IntValue)symbolTable.getValue("someVariable")).getValue());
		try {
			s1.execute(crtState);
			s2.execute(crtState);	
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(24, ((IntValue)symbolTable.getValue("someVariable")).getValue());
	}
}



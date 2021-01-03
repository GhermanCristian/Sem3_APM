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
import model.statement.OpenReadFileStatement;
import model.statement.ReadFileStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class TestReadFileStatement {
	private final String SRC_FOLDER_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs";
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
	public void GetTypeEnvironment_NonIntegerValue_ThrowsException() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new ReadFileStatement(new ValueExpression(path), "someVariable");
		
		symbolTable.insert("someVariable", new BoolValue());
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("Variable is not an integer");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}	
	
	@Test
	public void GetTypeEnvironment_PathNotAString_ThrowsException() {
		StatementInterface s1 = new ReadFileStatement(new ValueExpression(new IntValue()), "someVariable");
		StatementInterface s2 = new VariableDeclarationStatement("someVariable", new IntType());
		
		try {
			typeEnvironment = s2.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("TestReadFileStatement: path should be a StringValue");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ValidInputTypes_TypeEnvironmentUnchanged() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new ReadFileStatement(new ValueExpression(path), "someVariable");
		StatementInterface s2 = new VariableDeclarationStatement("someVariable", new IntType());
		
		try {
			typeEnvironment = s2.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("someVariable"), new IntType());
		
		try {
			s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("someVariable"), new IntType());
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
	public void Execute_ValidEmptyInput_ReturnsNull() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\emptyFile.txt");
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(path));
		StatementInterface s2 = new ReadFileStatement(new ValueExpression(path), "someVariable");
		ProgramState result = null;
		symbolTable.insert("someVariable", new IntValue(23));

		try {
			s1.execute(crtState);
			result = s2.execute(crtState);	
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertNull(result);
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



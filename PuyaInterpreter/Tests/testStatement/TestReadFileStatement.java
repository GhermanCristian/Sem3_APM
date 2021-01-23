package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.ValueExpression;
import model.statement.OpenReadFileStatement;
import model.statement.ReadFileStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.value.IntValue;
import model.value.StringValue;

public class TestReadFileStatement extends BaseTest {
	private final String SRC_FOLDER_PATH = System.getProperty("user.dir") + File.separator + "logs";
	
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	public void GetTypeEnvironment_UndefinedVariable_ThrowsException() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new ReadFileStatement(new ValueExpression(path), "someVariable");
		
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("Variable is undefined");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}	
	
	@Test
	public void GetTypeEnvironment_NonIntegerValue_ThrowsException() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s0 = new VariableDeclarationStatement("someVariable", new BoolType());
		StatementInterface s1 = new ReadFileStatement(new ValueExpression(path), "someVariable");
		
		try {
			s1.getTypeEnvironment(s0.getTypeEnvironment(typeEnvironment));
			fail("Variable is undefined");
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



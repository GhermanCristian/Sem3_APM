package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.ValueExpression;
import model.statement.CloseReadFileStatement;
import model.statement.OpenReadFileStatement;
import model.statement.StatementInterface;
import model.value.IntValue;
import model.value.StringValue;

public class TestCloseReadFileStatement extends BaseTest {
	private final String SRC_FOLDER_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs";
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	public void GetTypeEnvironment_FilePathNotAString_ThrowsException() {
		StatementInterface s1 = new CloseReadFileStatement(new ValueExpression(new IntValue()));
		
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("File path is not a string");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_FilePathIsString_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new CloseReadFileStatement(new ValueExpression(new StringValue()));
		
		assertTrue(typeEnvironment.isEmpty());
		try {
			s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(typeEnvironment.isEmpty());
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

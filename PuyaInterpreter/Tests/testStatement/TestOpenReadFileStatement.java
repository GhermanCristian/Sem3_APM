package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.ValueExpression;
import model.statement.OpenReadFileStatement;
import model.statement.StatementInterface;
import model.value.IntValue;
import model.value.StringValue;

public class TestOpenReadFileStatement extends BaseTest {
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
	public void GetTypeEnvironment_PathNotAString_ThrowsException() {
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(new IntValue()));
		
		try {
			s1.getTypeEnvironment(typeEnvironment);
			fail("Path should be a StringValue");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_PathIsString_EnvironmentUnchanged() {
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(new StringValue()));
		
		assertTrue(typeEnvironment.isEmpty());
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(typeEnvironment.isEmpty());
	}
	
	@Test
	public void Execute_NonExistentFile_ThrowsException() {
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(new StringValue(this.SRC_FOLDER_PATH + "\\nonexistent.txt")));
		
		try {
			s1.execute(crtState);
			fail("File doesn't exist");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ValidFile_FileAddedToTable() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(path));
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(fileTable.size(), 1);
		assertTrue(fileTable.isDefined(path));
	}
	
	@Test
	public void Execute_ValidFile_ReturnsNull() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(path));
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertNull(result);
	}
	
	@Test
	public void Execute_FileAlreadyDefined_ThrowsException() {
		StringValue path = new StringValue(this.SRC_FOLDER_PATH + "\\logFile.txt");
		StatementInterface s1 = new OpenReadFileStatement(new ValueExpression(path));
		StatementInterface s2 = new OpenReadFileStatement(new ValueExpression(path));
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.execute(crtState);
			fail("File is already defined in the fileTable");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
}





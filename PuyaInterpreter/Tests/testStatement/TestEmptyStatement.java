package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.statement.EmptyStatement;
import model.statement.StatementInterface;

public class TestEmptyStatement extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	public void GetTypeEnvironment_ValidInput_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new EmptyStatement();
		assertTrue(typeEnvironment.isEmpty());
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
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

	@Test
	public void Execute_ValidInput_ReturnsNull() {
		StatementInterface s1 = new EmptyStatement();
		ProgramState result = null;
		
		try {
			result = s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertNull(result);
	}
}

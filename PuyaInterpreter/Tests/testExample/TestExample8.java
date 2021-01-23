package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.value.IntValue;
import view.AllExamples;

public class TestExample8 extends TestExample {
	private static final String REPOSITORY_PATH = TestExample.SRC_FOLDER_PATH + "log8.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample8();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	//"int v; v=4; while (v>0) {print(v); v--;} print(v);"
	
	@Test
	public void FullProgramExecution_Example8_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 5);
		try {
			assertEquals(output.get(0), new IntValue(4));
			assertEquals(output.get(1), new IntValue(3));
			assertEquals(output.get(2), new IntValue(2));
			assertEquals(output.get(3), new IntValue(1));
			assertEquals(output.get(4), new IntValue(0));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example8_CorrectHeapTable() {
		super.executeProgram();
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example8_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example8Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example8Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTable.size());
		assertEquals(symbolTable.getValue("v"), new IntValue(0));
	}

	@Test
	public void FullProgramExecution_Example8_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

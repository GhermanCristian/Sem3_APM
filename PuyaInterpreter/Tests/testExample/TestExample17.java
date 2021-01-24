package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.value.IntValue;
import view.AllExamples;

public class TestExample17 extends TestExample {
	private static final String REPOSITORY_PATH = TestExample.SRC_FOLDER_PATH + "log17.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample17();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	//"int v; for(v = 2; v > 0; v--) {fork(print(v + 23);} print(v);"
	
	@Test
	public void FullProgramExecution_Example17_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 2);
		try {
			assertEquals(output.get(0), new IntValue(25));
			assertEquals(output.get(1), new IntValue(24));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example17_CorrectHeapTable() {
		super.executeProgram();
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example17_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	// when I have multiple threads, the threadList becomes empty after the execution, so there's no way of checking each tread
	// crtState will always represent thread1, so we can at least check that
	@Test
	public void FullProgramExecution_Example17Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, crtState.getExecutionStack().size());
	}
	
	@Test
	public void FullProgramExecution_Example17Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTable.size());
		assertEquals(symbolTable.getValue("v"), new IntValue(0));
	}
	
	@Test
	public void FullProgramExecution_Example17_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

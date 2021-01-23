package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.value.IntValue;
import view.AllExamples;

public class TestExample2 extends TestExample {
	private static final String REPOSITORY_PATH = TestExample.SRC_FOLDER_PATH + "log2.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample2();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	//"int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);"
	
	@Test
	public void FullProgramExecution_Example2_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 1);
		try {
			assertEquals(output.get(0), new IntValue(18));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example2_CorrectHeapTable() {
		super.executeProgram();
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example2_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example2Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example2Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(2, symbolTable.size());
		assertEquals(new IntValue(17), symbolTable.getValue("a"));
		assertEquals(new IntValue(18), symbolTable.getValue("b"));
	}
	
	@Test
	public void FullProgramExecution_Example2_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

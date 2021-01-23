package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.value.IntValue;
import view.AllExamples;

public class TestExample1 extends TestExample {
	private static final String REPOSITORY_PATH = TestExample.SRC_FOLDER_PATH + "log1.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample1();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	//"int a; a = 23; print(a);"
	
	@Test
	public void FullProgramExecution_Example1_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 1);
		try {
			assertEquals(output.get(0), new IntValue(23));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example1_CorrectHeapTable() {
		super.executeProgram();
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example1_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example1Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example1Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTable.size());
		assertEquals(new IntValue(23), symbolTable.getValue("a"));
	}

	@Test
	public void FullProgramExecution_Example1_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

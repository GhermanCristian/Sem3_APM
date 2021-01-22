package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.value.IntValue;
import view.AllExamples;

public class TestExample18 extends TestExample {
	private static final String REPOSITORY_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs\\log18.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample18();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	//"int v; v = 0; repeat {fork(print(v); v--;); v++;} until (v == 3); int x; x = 1; print(v * 10);"
	
	@Test
	public void FullProgramExecution_Example18_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 4);
		try {
			assertEquals(output.get(0), new IntValue(0));
			assertEquals(output.get(1), new IntValue(1));
			assertEquals(output.get(2), new IntValue(2));
			assertEquals(output.get(3), new IntValue(30));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example18_CorrectHeapTable() {
		super.executeProgram();
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example18_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	// when I have multiple threads, the threadList becomes empty after the execution, so there's no way of checking each tread
	// crtState will always represent thread1, so we can at least check that
	@Test
	public void FullProgramExecution_Example18Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, crtState.getExecutionStack().size());
	}
	
	@Test
	public void FullProgramExecution_Example18Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(2, symbolTable.size());
		assertEquals(symbolTable.getValue("v"), new IntValue(3));
		assertEquals(symbolTable.getValue("x"), new IntValue(1));
	}
	
	@Test
	public void FullProgramExecution_Example18_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.value.IntValue;
import view.AllExamples;

public class TestExample30 extends TestExample {
	private static final String REPOSITORY_PATH = TestExample.SRC_FOLDER_PATH + "log30.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample30();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	
	@Test
	public void FullProgramExecution_Example30_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 5);
		try {
			assertEquals(output.get(0), new IntValue(5));
			assertEquals(output.get(1), new IntValue(4));
			assertEquals(output.get(2), new IntValue(3));
			assertEquals(output.get(3), new IntValue(2));
			assertEquals(output.get(4), new IntValue(1));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example30_CorrectHeapTable() {
		super.executeProgram();
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example30_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example30Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example30Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTableStack.size());
		assertTrue(symbolTable.isEmpty());
	}

	@Test
	public void FullProgramExecution_Example30_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example30_CorrectLockMechanisms() {
		super.executeProgram();
		
		assertTrue(semaphoreTable.isEmpty());
		assertTrue(latchTable.isEmpty());
		assertTrue(barrierTable.isEmpty());
		assertTrue(lockTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example30_CorrectProcedureTableSize() {
		super.executeProgram();
		
		assertEquals(1, procedureTable.size());
	}
}

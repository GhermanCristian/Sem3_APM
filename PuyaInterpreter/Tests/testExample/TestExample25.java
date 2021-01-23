package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.value.IntValue;
import view.AllExamples;

public class TestExample25 extends TestExample {
	private static final String REPOSITORY_PATH = TestExample.SRC_FOLDER_PATH + "log25.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample25();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	
	@Test
	public void FullProgramExecution_Example25_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 12);
		try {
			assertEquals(new IntValue(20), output.get(0));
			assertEquals(new IntValue(10), output.get(1));
			assertEquals(new IntValue(9), output.get(2));
			assertEquals(new IntValue(8), output.get(3));
			assertEquals(new IntValue(7), output.get(4));
			assertEquals(new IntValue(6), output.get(5));
			assertEquals(new IntValue(5), output.get(6));
			assertEquals(new IntValue(4), output.get(7));
			assertEquals(new IntValue(3), output.get(8));
			assertEquals(new IntValue(2), output.get(9));
			assertEquals(new IntValue(1), output.get(10));
			assertEquals(new IntValue(200), output.get(11));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example25_CorrectHeapTable() {
		super.executeProgram();
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example25_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example25Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example25Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTableStack.size());
		assertEquals(1, symbolTable.size());
		assertEquals(new IntValue(20), symbolTable.getValue("v"));
	}

	@Test
	public void FullProgramExecution_Example25_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example25_CorrectLockMechanisms() {
		super.executeProgram();
		
		assertTrue(semaphoreTable.isEmpty());
		assertTrue(latchTable.isEmpty());
		assertTrue(barrierTable.isEmpty());
		assertTrue(lockTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example25_CorrectProcedureTableSize() {
		super.executeProgram();
		
		assertTrue(procedureTable.isEmpty());
	}
}

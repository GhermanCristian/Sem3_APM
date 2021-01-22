package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.value.IntValue;
import view.AllExamples;

public class TestExample21 extends TestExample {
	private static final String REPOSITORY_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs\\log21.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample21();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	
	@Test
	public void FullProgramExecution_Example21_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 4);
		try {
			assertEquals(output.get(0), new IntValue(25));
			assertEquals(output.get(1), new IntValue(2));
			assertEquals(output.get(2), new IntValue(10));
			assertEquals(output.get(3), new IntValue(7));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example21_CorrectHeapTable() {
		super.executeProgram();
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example21_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example21Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example21Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTableStack.size());
		assertEquals(2, symbolTable.size());
		assertEquals(new IntValue(2), symbolTable.getValue("v"));
		assertEquals(new IntValue(5), symbolTable.getValue("w"));
	}

	@Test
	public void FullProgramExecution_Example21_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example21_CorrectLockMechanisms() {
		super.executeProgram();
		
		assertTrue(semaphoreTable.isEmpty());
		assertTrue(latchTable.isEmpty());
		assertTrue(barrierTable.isEmpty());
		assertTrue(lockTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example21_CorrectProcedureTableSize() {
		super.executeProgram();
		
		assertEquals(2, procedureTable.size());
	}
}

package testExample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.*;
import model.ProgramState;
import model.type.IntType;
import model.value.IntValue;
import model.value.ReferenceValue;
import view.AllExamples;

public class TestExample19 extends TestExample {
	private static final String REPOSITORY_PATH = TestExample.SRC_FOLDER_PATH + "log19.in";	
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample19();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	
	@Test
	public void FullProgramExecution_Example19_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 3);
		try {
			assertEquals(output.get(0), new IntValue(10));
			assertTrue((output.get(1).equals(new IntValue(200)) && output.get(2).equals(new IntValue(199)))
					|| (output.get(1).equals(new IntValue(9)) && output.get(2).equals(new IntValue(200))));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example19_CorrectHeapTable() {
		super.executeProgram();
		
		assertEquals(heap.size(), 1);
		try {
			assertEquals(heap.getValue(1), new IntValue(200));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example19_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example19Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example19Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTableStack.size());
		assertEquals(2, symbolTable.size());
		assertEquals(new IntValue(1), symbolTable.getValue("cnt"));
		assertEquals(new ReferenceValue(1, new IntType()), symbolTable.getValue("v1"));
	}

	@Test
	public void FullProgramExecution_Example19_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example19_CorrectLockMechanisms() {
		super.executeProgram();
		
		assertEquals(1, semaphoreTable.size());
		assertTrue(semaphoreTable.getValue(1).getValue().isEmpty());
		assertEquals(semaphoreTable.getValue(1).getKey(), 1);
		assertTrue(latchTable.isEmpty());
		assertTrue(barrierTable.isEmpty());
		assertTrue(lockTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example19_CorrectProcedureTableSize() {
		super.executeProgram();
		
		assertTrue(procedureTable.isEmpty());
	}
}

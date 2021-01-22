package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.type.IntType;
import model.value.IntValue;
import model.value.ReferenceValue;
import view.AllExamples;

public class TestExample22 extends TestExample {
	private static final String REPOSITORY_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs\\log22.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample22();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	
	@Test
	public void FullProgramExecution_Example22_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 3);
		try {
			assertEquals(output.get(0), new IntValue(4));
			assertEquals(output.get(1), new IntValue(20));
			assertEquals(output.get(2), new IntValue(300));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example22_CorrectHeapTable() {
		super.executeProgram();
		
		assertEquals(heap.size(), 3);
		try {
			assertEquals(heap.getValue(1), new IntValue(20));
			assertEquals(heap.getValue(2), new IntValue(300));
			assertEquals(heap.getValue(3), new IntValue(4));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example22_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example22Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example22Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTableStack.size());
		assertEquals(4, symbolTable.size());
		assertEquals(new IntValue(1), symbolTable.getValue("cnt"));
		assertEquals(new ReferenceValue(1, new IntType()), symbolTable.getValue("v1"));
		assertEquals(new ReferenceValue(2, new IntType()), symbolTable.getValue("v2"));
		assertEquals(new ReferenceValue(3, new IntType()), symbolTable.getValue("v3"));
	}

	@Test
	public void FullProgramExecution_Example22_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example22_CorrectLockMechanisms() {
		super.executeProgram();
		
		assertTrue(semaphoreTable.isEmpty());
		assertTrue(latchTable.isEmpty());
		assertEquals(1, barrierTable.size());
		assertEquals(3, barrierTable.getValue(1).getKey());
		assertEquals(2, barrierTable.getValue(1).getValue().get(0));
		assertEquals(1, barrierTable.getValue(1).getValue().get(1));
		assertEquals(3, barrierTable.getValue(1).getValue().get(2));
		assertTrue(lockTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example22_CorrectProcedureTableSize() {
		super.executeProgram();
		
		assertTrue(procedureTable.isEmpty());
	}
}

package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.type.IntType;
import model.value.IntValue;
import model.value.ReferenceValue;
import view.AllExamples;

public class TestExample23 extends TestExample {
	private static final String REPOSITORY_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs\\log23.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample23();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	
	@Test
	public void FullProgramExecution_Example23_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 2);
		try {
			assertTrue(output.get(0).equals(new IntValue(190)) || output.get(0).equals(new IntValue(199)));
			assertTrue(output.get(1).equals(new IntValue(350)) || output.get(1).equals(new IntValue(305)));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example23_CorrectHeapTable() {
		super.executeProgram();
		
		assertEquals(heap.size(), 2);
		try {
			assertTrue(heap.getValue(1).equals(new IntValue(190)) || heap.getValue(1).equals(new IntValue(199)));
			assertTrue(heap.getValue(2).equals(new IntValue(350)) || heap.getValue(2).equals(new IntValue(305)));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example23_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example23Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example23Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTableStack.size());
		assertEquals(4, symbolTable.size());
		assertEquals(new IntValue(1), symbolTable.getValue("x"));
		assertEquals(new IntValue(2), symbolTable.getValue("q"));
		assertEquals(new ReferenceValue(1, new IntType()), symbolTable.getValue("v1"));
		assertEquals(new ReferenceValue(2, new IntType()), symbolTable.getValue("v2"));
	}

	@Test
	public void FullProgramExecution_Example23_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example23_CorrectLockMechanisms() {
		super.executeProgram();
		
		assertTrue(semaphoreTable.isEmpty());
		assertTrue(latchTable.isEmpty());
		assertTrue(barrierTable.isEmpty());
		assertEquals(2, lockTable.size());
		lockTable.forEachValue(lockValue -> assertEquals(-1, lockValue));
	}
	
	@Test
	public void FullProgramExecution_Example23_CorrectProcedureTableSize() {
		super.executeProgram();
		
		assertTrue(procedureTable.isEmpty());
	}
}

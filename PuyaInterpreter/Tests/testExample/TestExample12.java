package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.type.IntType;
import model.value.IntValue;
import model.value.ReferenceValue;
import view.AllExamples;

public class TestExample12 extends TestExample {
	private static final String REPOSITORY_PATH = TestExample.SRC_FOLDER_PATH + "log12.in";	
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample12();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	//"int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(v=33; wH(a,24);); print(rH(a)); v=32; print(rH(a))); print(v); print(rH(a));"
	
	@Test
	public void FullProgramExecution_Example12_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 4);
		try {
			assertEquals(output.get(0), new IntValue(10));
			assertEquals(output.get(1), new IntValue(30));
			assertEquals(output.get(2), new IntValue(30));
			assertEquals(output.get(3), new IntValue(24));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example12_CorrectHeapTable() {
		super.executeProgram();
		
		assertEquals(heap.size(), 1);
		assertEquals(heap.getValue(1), new IntValue(24));
	}
	
	@Test
	public void FullProgramExecution_Example12_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	// when I have multiple threads, the threadList becomes empty after the execution, so there's no way of checking each tread
	// crtState will always represent thread1, so we can at least check that
	@Test
	public void FullProgramExecution_Example12Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, crtState.getExecutionStack().size());
	}
	
	@Test
	public void FullProgramExecution_Example12Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(2, symbolTable.size());
		assertEquals(symbolTable.getValue("v"), new IntValue(10));
		assertEquals(symbolTable.getValue("a"), new ReferenceValue(1, new IntType()));
	}
	
	@Test
	public void FullProgramExecution_Example12_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.type.IntType;
import model.value.IntValue;
import model.value.ReferenceValue;
import model.value.StringValue;
import view.AllExamples;

public class TestExample20 extends TestExample {
	private static final String REPOSITORY_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs\\log20.in";
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample20();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	
	@Test
	public void FullProgramExecution_Example20_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 9);
		try {
			assertEquals(output.get(0), new IntValue(20));
			assertEquals(output.get(1), new IntValue(30));
			assertEquals(output.get(2), new StringValue("(latch) 2"));
			assertEquals(output.get(3), new StringValue("(latch) 3"));
			assertEquals(output.get(4), new IntValue(40));
			assertEquals(output.get(5), new StringValue("(latch) 4"));
			assertEquals(output.get(6), new IntValue(100));
			assertEquals(output.get(7), new StringValue("(latch) 1"));
			assertEquals(output.get(8), new IntValue(100));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example20_CorrectHeapTable() {
		super.executeProgram();
		
		assertEquals(heap.size(), 3);
		try {
			assertEquals(heap.getValue(1), new IntValue(20));
			assertEquals(heap.getValue(2), new IntValue(30));
			assertEquals(heap.getValue(3), new IntValue(40));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example20_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example20Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example20Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(1, symbolTableStack.size());
		assertEquals(4, symbolTable.size());
		assertEquals(new IntValue(1), symbolTable.getValue("cnt"));
		assertEquals(new ReferenceValue(1, new IntType()), symbolTable.getValue("v1"));
		assertEquals(new ReferenceValue(2, new IntType()), symbolTable.getValue("v2"));
		assertEquals(new ReferenceValue(3, new IntType()), symbolTable.getValue("v3"));
	}

	@Test
	public void FullProgramExecution_Example20_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example20_CorrectLockMechanisms() {
		super.executeProgram();
		
		assertTrue(semaphoreTable.isEmpty());
		assertEquals(1, latchTable.size());
		assertEquals(0, latchTable.getValue(1));
		assertTrue(barrierTable.isEmpty());
		assertTrue(lockTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example20_CorrectProcedureTableSize() {
		super.executeProgram();
		
		assertTrue(procedureTable.isEmpty());
	}
}

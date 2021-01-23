package testExample;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import model.ProgramState;
import model.type.IntType;
import model.type.ReferenceType;
import model.value.IntValue;
import model.value.ReferenceValue;
import view.AllExamples;

public class TestExample5 extends TestExample {
	private static final String REPOSITORY_PATH = TestExample.SRC_FOLDER_PATH + "log5.in";	
	
	@BeforeClass
	public static void initialiseData() {
		TestExample.initialiseData();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample5();
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, example.getStatement());
		TestExample.initialiseExampleSpecificData(REPOSITORY_PATH);
	}
	
	@After
	public void clearAndCloseData() {
		super.clearAndCloseData();
		super.clearRepositoryFile(REPOSITORY_PATH);
	}
	//"Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(v); print(a);"
	
	@Test
	public void FullProgramExecution_Example5_CorrectOutput() {
		super.executeProgram();
		
		assertEquals(output.size(), 2);
		try {
			assertEquals(output.get(0), new ReferenceValue(1, new IntType()));
			assertEquals(output.get(1), new ReferenceValue(2, new ReferenceType(new IntType())));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example5_CorrectHeapTable() {
		super.executeProgram();
		
		assertEquals(heap.size(), 2);
		assertEquals(heap.getValue(1), new IntValue(23));
		assertEquals(heap.getValue(2), new ReferenceValue(1, new IntType()));
	}
	
	@Test
	public void FullProgramExecution_Example5_CorrectFileTable() {
		super.executeProgram();
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example5Thread1_EmptyStack() {
		super.executeProgram();
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example5Thread1_CorrectSymbolTable() {
		super.executeProgram();
		
		assertEquals(2, symbolTable.size());
		assertEquals(new ReferenceValue(1, new IntType()), symbolTable.getValue("v"));
		assertEquals(new ReferenceValue(2, new ReferenceType(new IntType())), symbolTable.getValue("a"));
	}

	@Test
	public void FullProgramExecution_Example5_EmptyThreadList() {
		super.executeProgram();
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

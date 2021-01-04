package testExample;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import org.junit.*;
import controller.Controller;
import controller.TextController;
import model.Example;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyHeap;
import model.ADT.MyList;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;
import repository.Repository;
import repository.RepositoryInterface;
import view.AllExamples;

public class TestExample18 {
	static StackInterface<StatementInterface> stack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static DictionaryInterface<StringValue, BufferedReader> fileTable;
	static DictionaryInterface<Integer, ValueInterface> heap;
	static DictionaryInterface<String, TypeInterface> typeEnvironment;
	static Example example;
	static ProgramState crtState;
	static RepositoryInterface repo;
	static Controller controller;
	
	private static final String REPOSITORY_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs\\log18.in";
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
		typeEnvironment = new MyDictionary<String, TypeInterface>();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample18();
		
		crtState = new ProgramState(stack, symbolTable, output, fileTable, heap, example.getStatement());
		repo = new Repository(REPOSITORY_PATH);
		controller = new TextController(repo);
		controller.addProgramState(crtState);
	}
	
	@After
	public void clearAndCloseData() {
		stack.clear();
		symbolTable.clear();
		output.clear();
		
		for (BufferedReader crtBuffer : fileTable.getAllValues()) {
			try {
				crtBuffer.close();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		fileTable.clear();
		heap.clear();
		typeEnvironment.clear();
		
		try {
			PrintWriter currentFileWriter = new PrintWriter(REPOSITORY_PATH);
			currentFileWriter.close(); // I do this to erase the content of the file
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		repo.getThreadList().clear();
		crtState.setStatement(example.getStatement());
		controller.addProgramState(crtState);
	}
	//"int v; v = 0; repeat {fork(print(v); v--;); v++;} until (v == 3); int x; x = 1; print(v * 10);"
	
	@Test
	public void FullProgramExecution_Example18_CorrectOutput() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
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
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example18_CorrectFileTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(fileTable.isEmpty());
	}
	
	// when I have multiple threads, the threadList becomes empty after the execution, so there's no way of checking each tread
	// crtState will always represent thread1, so we can at least check that
	@Test
	public void FullProgramExecution_Example18Thread1_EmptyStack() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(0, crtState.getExecutionStack().size());
	}
	
	@Test
	public void FullProgramExecution_Example18Thread1_CorrectSymbolTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(2, symbolTable.size());
		assertEquals(symbolTable.getValue("v"), new IntValue(3));
		assertEquals(symbolTable.getValue("x"), new IntValue(1));
	}
	
	@Test
	public void FullProgramExecution_Example18_EmptyThreadList() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

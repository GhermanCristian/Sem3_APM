package testExample;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.*;
import controller.Controller;
import controller.TextController;
import javafx.util.Pair;
import model.Example;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyHeap;
import model.ADT.MyList;
import model.ADT.MySemaphoreTable;
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

public class TestExample16 {
	static StackInterface<StatementInterface> stack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static DictionaryInterface<StringValue, BufferedReader> fileTable;
	static DictionaryInterface<Integer, ValueInterface> heap;
	static DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable;
	static DictionaryInterface<String, TypeInterface> typeEnvironment;
	static Example example;
	static ProgramState crtState;
	static RepositoryInterface repo;
	static Controller controller;
	
	private static final String REPOSITORY_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs\\log16.in";
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
		semaphoreTable = new MySemaphoreTable<Integer, Pair<Integer, ArrayList<Integer>>>();
		typeEnvironment = new MyDictionary<String, TypeInterface>();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample16();
		crtState = new ProgramState(stack, symbolTable, output, fileTable, heap, semaphoreTable, example.getStatement());
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
		semaphoreTable.clear();
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
	//"int v; for(v = 4; v > 0; v--) {print(v);} print(v);"
	
	@Test
	public void FullProgramExecution_Example16_CorrectOutput() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 5);
		try {
			assertEquals(output.get(0), new IntValue(4));
			assertEquals(output.get(1), new IntValue(3));
			assertEquals(output.get(2), new IntValue(2));
			assertEquals(output.get(3), new IntValue(1));
			assertEquals(output.get(4), new IntValue(0));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example16_CorrectHeapTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(heap.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example16_CorrectFileTable() {
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
	public void FullProgramExecution_Example16Thread1_EmptyStack() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(0, crtState.getExecutionStack().size());
	}
	
	@Test
	public void FullProgramExecution_Example16Thread1_CorrectSymbolTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(1, symbolTable.size());
		assertEquals(symbolTable.getValue("v"), new IntValue(0));
	}
	
	@Test
	public void FullProgramExecution_Example16_EmptyThreadList() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

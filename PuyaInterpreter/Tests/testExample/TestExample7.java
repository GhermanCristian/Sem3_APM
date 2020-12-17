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
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ReferenceValue;
import model.value.StringValue;
import model.value.ValueInterface;
import repository.Repository;
import repository.RepositoryInterface;
import view.AllExamples;

public class TestExample7 {
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
	
	private static final String REPOSITORY_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\log7.in";
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
		typeEnvironment = new MyDictionary<String, TypeInterface>();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample7();
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
		
		crtState.setStatement(example.getStatement());
	}
	//"Ref int v; new(v, 23); print(rH(v)); wH(v, 24); print(rH(v) + 5);"
	
	@Test
	public void FullProgramExecution_Example7_CorrectOutput() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 2);
		try {
			assertEquals(output.get(0), new IntValue(23));
			assertEquals(output.get(1), new IntValue(29));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example7_CorrectHeapTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(heap.size(), 1);
		assertEquals(heap.getValue(1), new IntValue(24));
	}
	
	@Test
	public void FullProgramExecution_Example7_CorrectFileTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example7Thread1_EmptyStack() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example7Thread1_CorrectSymbolTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(1, symbolTable.size());
		assertEquals(new ReferenceValue(1, new IntType()), symbolTable.getValue("v"));
	}

}

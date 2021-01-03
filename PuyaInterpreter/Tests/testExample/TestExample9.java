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
import model.type.ReferenceType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ReferenceValue;
import model.value.StringValue;
import model.value.ValueInterface;
import repository.Repository;
import repository.RepositoryInterface;
import view.AllExamples;

public class TestExample9 {
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
	
	private static final String REPOSITORY_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs\\log9.in";
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
		typeEnvironment = new MyDictionary<String, TypeInterface>();
		AllExamples allExamples = new AllExamples();
		example = allExamples.getExample9();
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
	//"Ref int v; new(v, 23); Ref Ref int a; new(a, v); new(v, 24); print(rH(rH(a)));"
	
	@Test
	public void FullProgramExecution_Example9_CorrectOutput() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(output.size(), 1);
		try {
			assertEquals(output.get(0), new IntValue(23));
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void FullProgramExecution_Example9_CorrectHeapTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(heap.size(), 3);
		assertEquals(heap.getValue(1), new IntValue(23));
		assertEquals(heap.getValue(2), new ReferenceValue(1, new IntType()));
		assertEquals(heap.getValue(3), new IntValue(24));
	}
	
	@Test
	public void FullProgramExecution_Example9_CorrectFileTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(fileTable.isEmpty());
	}
	
	@Test
	public void FullProgramExecution_Example9Thread1_EmptyStack() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(0, stack.size());
	}
	
	@Test
	public void FullProgramExecution_Example9Thread1_CorrectSymbolTable() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(2, symbolTable.size());
		assertEquals(new ReferenceValue(3, new IntType()), symbolTable.getValue("v"));
		assertEquals(new ReferenceValue(2, new ReferenceType(new IntType())), symbolTable.getValue("a"));
	}

	@Test
	public void FullProgramExecution_Example9_EmptyThreadList() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(repo.getThreadList().isEmpty());
	}
}

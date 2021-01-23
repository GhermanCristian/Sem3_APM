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
import model.Procedure;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyHeap;
import model.ADT.MyList;
import model.ADT.MyLockTable;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import model.type.TypeInterface;
import model.value.StringValue;
import model.value.ValueInterface;
import repository.Repository;
import repository.RepositoryInterface;

public class TestExample {
	static StackInterface<StatementInterface> stack;
	static StackInterface<DictionaryInterface<String, ValueInterface>> symbolTableStack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static DictionaryInterface<StringValue, BufferedReader> fileTable;
	static DictionaryInterface<Integer, ValueInterface> heap;
	static DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable;
	static DictionaryInterface<Integer, Integer> latchTable;
	static DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable;
	static DictionaryInterface<Integer, Integer> lockTable;
	static DictionaryInterface<String, Procedure> procedureTable;
	static DictionaryInterface<String, TypeInterface> typeEnvironment;
	static Example example;
	static ProgramState crtState;
	static RepositoryInterface repo;
	static Controller controller;

	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTableStack = new MyStack<DictionaryInterface<String,ValueInterface>>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		symbolTableStack.push(symbolTable);
		output = new MyList<ValueInterface>();
		fileTable = new MyDictionary<StringValue, BufferedReader>();
		heap = new MyHeap<Integer, ValueInterface>();
		semaphoreTable = new MyLockTable<Integer, Pair<Integer, ArrayList<Integer>>>();
		latchTable = new MyLockTable<Integer, Integer>();
		barrierTable = new MyLockTable<Integer, Pair<Integer,ArrayList<Integer>>>();
		lockTable = new MyLockTable<Integer, Integer>();
		procedureTable = new MyDictionary<String, Procedure>();
		typeEnvironment = new MyDictionary<String, TypeInterface>();
	}
	
	protected static void initialiseExampleSpecificData(String RepositoryPath) {
		repo = new Repository(RepositoryPath);
		controller = new TextController(repo);
		controller.addProgramState(crtState);
	}
	
	protected void clearRepositoryFile(String RepositoryPath) {
		try {
			PrintWriter currentFileWriter = new PrintWriter(RepositoryPath);
			currentFileWriter.close(); // I do this to erase the content of the file
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void executeProgram() {
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@After
	public void clearAndCloseData() {
		crtState.clear();
		symbolTable = new MyDictionary<String, ValueInterface>();
		symbolTableStack.push(symbolTable);
		typeEnvironment.clear();
		repo.getThreadList().clear();
		crtState.setStatement(example.getStatement());
		controller.addProgramState(crtState);
	}
}

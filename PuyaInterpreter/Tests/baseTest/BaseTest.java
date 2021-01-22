package baseTest;

import java.io.BufferedReader;
import java.util.ArrayList;
import org.junit.After;
import org.junit.BeforeClass;
import javafx.util.Pair;
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

public class BaseTest {
	protected static StackInterface<StatementInterface> stack;
	protected static StackInterface<DictionaryInterface<String, ValueInterface>> symbolTableStack;
	protected static DictionaryInterface<String, ValueInterface> symbolTable;
	protected static ListInterface<ValueInterface> output;
	protected static DictionaryInterface<StringValue, BufferedReader> fileTable;
	protected static DictionaryInterface<Integer, ValueInterface> heap;
	protected static DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable;
	protected static DictionaryInterface<Integer, Integer> latchTable;
	protected static DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable;
	protected static DictionaryInterface<Integer, Integer> lockTable;
	protected static DictionaryInterface<String, Procedure> procedureTable;
	protected static DictionaryInterface<String, TypeInterface> typeEnvironment;
	protected static ProgramState crtState;
	
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
		crtState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, null);
	}
	
	@After
	public void clearAndCloseData() {
		crtState.clear();
		symbolTable = new MyDictionary<String, ValueInterface>();
		symbolTableStack.push(symbolTable);
		typeEnvironment.clear();
	}
}

package view;

import java.io.BufferedReader;
import java.util.ArrayList;
import controller.TextController;
import javafx.util.Pair;
import controller.Controller;
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

public class RunExampleCommand extends Command {
	private final StatementInterface crtStatement;
	private final String repositoryLocation;
	
	public RunExampleCommand(String key, Example exampleToRun) {
		super(key, exampleToRun.getExampleDescription());
		this.crtStatement = exampleToRun.getStatement();
		this.repositoryLocation = exampleToRun.getRepositoryLocation();
	}
	
	@Override
	public void execute() throws Exception {
		StackInterface<StatementInterface> stack = new MyStack<StatementInterface>();
		StackInterface<DictionaryInterface<String, ValueInterface>> symbolTableStack = new MyStack<DictionaryInterface<String,ValueInterface>>();
		DictionaryInterface<String, ValueInterface> symbolTable = new MyDictionary<String, ValueInterface>();
		symbolTableStack.push(symbolTable);
		ListInterface<ValueInterface> output = new MyList<ValueInterface>();
		DictionaryInterface<StringValue, BufferedReader> fileTable = new MyDictionary<StringValue, BufferedReader>();
		DictionaryInterface<Integer, ValueInterface> heap = new MyHeap<Integer, ValueInterface>();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = new MyLockTable<Integer, Pair<Integer,ArrayList<Integer>>>();
		DictionaryInterface<Integer, Integer> latchTable = new MyLockTable<Integer, Integer>();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable = new MyLockTable<Integer, Pair<Integer,ArrayList<Integer>>>();
		DictionaryInterface<Integer, Integer> lockTable = new MyLockTable<Integer, Integer>();
		DictionaryInterface<String, Procedure> procedureTable = new MyDictionary<String, Procedure>();
		
		DictionaryInterface<String, TypeInterface> typeEnvironment = new MyDictionary<String, TypeInterface>();
		this.crtStatement.getTypeEnvironment(typeEnvironment);
		ProgramState crtProgramState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, barrierTable, lockTable, procedureTable, this.crtStatement);
		
		RepositoryInterface repo = new Repository(this.repositoryLocation);
		Controller controller = new TextController(repo);
		controller.addProgramState(crtProgramState);
		controller.fullProgramExecution();
	}
}

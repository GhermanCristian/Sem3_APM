package controller;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
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
import view.AllExamples;
import view.GUI.GUI;

public class GUIController extends Controller {
	private GUI currentGUI;
	
	public GUIController(GUI currentGUI) {
		this.currentGUI = currentGUI;
		AllExamples allExamples = new AllExamples();
		this.exampleList = allExamples.getAllExamples();
	}

	private ProgramState getProgramState(Example currentExample) throws Exception {
		StackInterface<StatementInterface> stack = new MyStack<StatementInterface>();
		StackInterface<DictionaryInterface<String, ValueInterface>> symbolTableStack = new MyStack<DictionaryInterface<String,ValueInterface>>();
		DictionaryInterface<String, ValueInterface> symbolTable = new MyDictionary<String, ValueInterface>();
		symbolTableStack.push(symbolTable);
		ListInterface<ValueInterface> output = new MyList<ValueInterface>();
		DictionaryInterface<StringValue, BufferedReader> fileTable = new MyDictionary<StringValue, BufferedReader>();
		DictionaryInterface<Integer, ValueInterface> heap = new MyHeap<Integer, ValueInterface>();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = new MyLockTable<Integer, Pair<Integer,ArrayList<Integer>>>();
		DictionaryInterface<Integer, Integer> latchTable = new MyLockTable<Integer, Integer>();
		DictionaryInterface<String, Procedure> procedureTable = new MyDictionary<String, Procedure>();
		
		DictionaryInterface<String, TypeInterface> typeEnvironment = new MyDictionary<String, TypeInterface>();
		currentExample.getStatement().getTypeEnvironment(typeEnvironment);
		ProgramState crtProgramState = new ProgramState(stack, symbolTableStack, output, fileTable, heap, semaphoreTable, latchTable, procedureTable, currentExample.getStatement());
		return crtProgramState;
	}
	
	@Override
	protected void beforeProgramExecution() {
		super.beforeProgramExecution();
		this.currentGUI.beforeProgramExecution();
	}
	
	public void loadProgramStateIntoRepository(Example currentExample) throws Exception {
		DictionaryInterface<String, TypeInterface> typeEnvironment = new MyDictionary<String, TypeInterface>();
		currentExample.getStatement().getTypeEnvironment(typeEnvironment);
		this.repository = new Repository(currentExample.getRepositoryLocation());
		this.addProgramState(this.getProgramState(currentExample));
		this.beforeProgramExecution();
	}
	
	public void advanceOneStepAllThreads() throws Exception {
		super.oneStepExecutionAllThreads(this.removeCompletedThreads(this.repository.getThreadList()));
		this.currentGUI.updateAllStructures();
		
		this.repository.setThreadList(this.removeCompletedThreads(this.repository.getThreadList()));
		if (this.repository.getThreadList().size() == 0) {
			this.afterProgramExecution();
		}
	}
	
	@Override
	protected void afterProgramExecution() {
		super.afterProgramExecution();
		this.currentGUI.afterProgramExecution();
	}
	
	@Override
	public void fullProgramExecution() throws Exception {
		if (this.repository == null) {
			// normally this shouldn't happen
			throw new Exception("GUIController: Repository not initialised before calling fullProgramExecution");
		}
		
		List<ProgramState> threadsStillInExecution = this.removeCompletedThreads(this.repository.getThreadList());
		while (threadsStillInExecution.size() > 0) {
			threadsStillInExecution.get(0).getHeap().setContent(this.getGarbageCollectedHeap(threadsStillInExecution));
			this.advanceOneStepAllThreads();
			threadsStillInExecution = this.removeCompletedThreads(this.repository.getThreadList());
		}
		
		// afterProgramExecution will be called even when we do a fullexec, because when advanceOneStep finds that it has no threads
		// left, it calls afterProgramExecution - this way, afterProgramExecution is called in both cases (when running one step or the entire program)
	}
}

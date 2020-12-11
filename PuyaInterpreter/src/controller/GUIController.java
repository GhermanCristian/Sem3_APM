package controller;

import java.io.BufferedReader;
import java.util.List;
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
import model.value.StringValue;
import model.value.ValueInterface;
import repository.Repository;
import view.AllExamples;
import view.GUI;

public class GUIController extends TextController {
	private GUI currentGUI;
	private MyList<Example> exampleList;
	
	public GUIController(GUI currentGUI) {
		super(null);
		this.currentGUI = currentGUI;
		AllExamples allExamples = new AllExamples();
		this.exampleList = allExamples.getAllExamples();
	}
	
	@Override
	public void fullProgramExecution() throws Exception {
		if (this.repository == null) {
			// normally this shouldn't happen
			throw new Exception("GUIController: Repository not initialised before calling fullProgramExecution");
		}
	}

	private ProgramState getProgramState(Example currentExample) throws Exception {
		StackInterface<StatementInterface> stack = new MyStack<StatementInterface>();
		DictionaryInterface<String, ValueInterface> symbolTable = new MyDictionary<String, ValueInterface>();
		ListInterface<ValueInterface> output = new MyList<ValueInterface>();
		DictionaryInterface<StringValue, BufferedReader> fileTable = new MyDictionary<StringValue, BufferedReader>();
		DictionaryInterface<Integer, ValueInterface> heap = new MyHeap<Integer, ValueInterface>();
		
		DictionaryInterface<String, TypeInterface> typeEnvironment = new MyDictionary<String, TypeInterface>();
		currentExample.getStatement().getTypeEnvironment(typeEnvironment);
		ProgramState crtProgramState = new ProgramState(stack, symbolTable, output, fileTable, heap, currentExample.getStatement());
		return crtProgramState;
	}
	
	public void loadProgramStateIntoRepository(Example currentExample) throws Exception {
		this.repository = new Repository(currentExample.getRepositoryLocation());
		this.addProgramState(this.getProgramState(currentExample));
		this.currentGUI.updateAllStructures();
		//System.out.println(this.repository.getThreadList().get(0).toString());
	}
	
	public void advanceOneStepAllThreads() throws Exception {
		super.oneStepExecutionAllThreads(this.removeCompletedThreads(this.repository.getThreadList()));
		this.currentGUI.updateAllStructures();
	}
	
	public MyList<Example> getAllExamples() {
		// I store the exampleList and don't just call AllExamples.getAllExamples() because that method requires actually
		// building the list and creating new examples each time, so this is faster, though it consumes a bit more memory
		return this.exampleList;
	}
	
	public List<ProgramState> getThreadList() {
		return this.repository.getThreadList();
	}

}
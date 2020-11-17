package view;

import java.io.BufferedReader;

import controller.Controller;
import controller.ControllerInterface;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyHeap;
import model.ADT.MyList;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import model.value.StringValue;
import model.value.ValueInterface;
import repository.Repository;
import repository.RepositoryInterface;

public class RunExampleCommand extends Command{
	private final StatementInterface crtStatement;
	private final String repositoryLocation;
	
	public RunExampleCommand(String key, String description, StatementInterface crtStatement, String repositoryLocation) {
		super(key, description);
		this.crtStatement = crtStatement;
		this.repositoryLocation = repositoryLocation;
	}
	
	@Override
	public void execute() {
		StackInterface<StatementInterface> stack = new MyStack<StatementInterface>();
		DictionaryInterface<String, ValueInterface> symbolTable = new MyDictionary<String, ValueInterface>();
		ListInterface<ValueInterface> output = new MyList<ValueInterface>();
		DictionaryInterface<StringValue, BufferedReader> fileTable = new MyDictionary<StringValue, BufferedReader>();
		DictionaryInterface<Integer, ValueInterface> heap = new MyHeap<Integer, ValueInterface>();
		ProgramState crtProgramState = new ProgramState(stack, symbolTable, output, fileTable, heap, this.crtStatement);
		RepositoryInterface repo = new Repository(this.repositoryLocation);
		ControllerInterface controller = new Controller(repo);
		controller.addProgramState(crtProgramState);
		
		try {
			controller.fullProgramExecution();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

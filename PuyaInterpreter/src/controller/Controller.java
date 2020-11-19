package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import exception.EmptyADTException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import model.value.ReferenceValue;
import model.value.ValueInterface;
import repository.RepositoryInterface;

public class Controller implements ControllerInterface{
	private RepositoryInterface repository;
	
	public Controller(RepositoryInterface repository) {
		this.repository = repository;
	}
	
	private HashMap<Integer, ValueInterface> getGarbageCollectedHeap(ProgramState crtProgramState) {
		DictionaryInterface<String, ValueInterface> symbolTable = crtProgramState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtProgramState.getHeap();
		
		List<Integer> symbolTableAddresses = symbolTable.getAllValues()
											.stream()
											.filter(elem -> elem instanceof ReferenceValue)
											.map(elem -> {ReferenceValue elem1 = (ReferenceValue)elem; return elem1.getHeapAddress();})
											.collect(Collectors.toList());
		List<Integer> heapReferencedAddresses = heap.getAllValues()
											.stream()
											.filter(elem -> elem instanceof ReferenceValue)
											.map(elem -> {ReferenceValue elem1 = (ReferenceValue)elem; return elem1.getHeapAddress();})
											.collect(Collectors.toList());
		return (HashMap<Integer, ValueInterface>)heap.getAllPairs().entrySet().stream()
											.filter(elem -> symbolTableAddresses.contains(elem.getKey()) || heapReferencedAddresses.contains(elem.getKey()))
											.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	@Override
	public ProgramState oneStepExecution(ProgramState crtProgramState) throws Exception{
		StackInterface<StatementInterface> executionStack = crtProgramState.getExecutionStack();
		if (executionStack.size() == 0) {
			throw new EmptyADTException("No program states available");
		}
		StatementInterface currentStatement = executionStack.pop();
		return currentStatement.execute(crtProgramState);
	}

	@Override
	public ProgramState fullProgramExecution() throws Exception{
		ProgramState crtProgramState = this.repository.getCurrentProgramState();
		StackInterface<StatementInterface> executionStack = crtProgramState.getExecutionStack();
		this.repository.logProgramExecution();
		while(executionStack.size() > 0) {
			// normally, oneStepExecution throws an exception if the exe stack is empty
			// but we are specifically checking if its size > 0 => that exception will never happen
			this.oneStepExecution(crtProgramState);
			this.repository.logProgramExecution();
			crtProgramState.getHeap().setContent(this.getGarbageCollectedHeap(crtProgramState));
		}
		
		this.repository.logProgramExecution();
		return crtProgramState;
	}

	@Override
	public void addProgramState(ProgramState newProgramState) {
		this.repository.addProgramState(newProgramState);
	}

}

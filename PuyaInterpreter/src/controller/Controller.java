package controller;

import exception.EmptyADTException;
import model.ProgramState;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import repository.Repository;
import repository.RepositoryInterface;

public class Controller implements ControllerInterface{
	private RepositoryInterface repository;
	
	public Controller() {
		this.repository = new Repository();
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
		while(executionStack.size() > 0) {
			// normally, oneStepExecution throws an exception if the exe stack is empty
			// but we are specifically checking if its size > 0 => that exception will never happen
			this.oneStepExecution(crtProgramState);
		}
		
		return crtProgramState;
	}

	@Override
	public void addProgramState(ProgramState newProgramState) {
		this.repository.addProgramState(newProgramState);
	}

}

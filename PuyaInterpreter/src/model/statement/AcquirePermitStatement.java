package model.statement;

import java.util.ArrayList;
import exception.InvalidTypeException;
import exception.LockAlreadyAcquiredException;
import exception.UndefinedVariableException;
import javafx.util.Pair;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class AcquirePermitStatement implements StatementInterface {
	private final String indexVariableName;
	
	public AcquirePermitStatement(String indexVariableName) {
		this.indexVariableName = indexVariableName;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = crtState.getSemaphoreTable();
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
		}
		
		int semaphoreIndexAsInteger = ((IntValue)(symbolTable.getValue(this.indexVariableName))).getValue();
		if (semaphoreTable.isDefined(semaphoreIndexAsInteger) == false) {
			throw new UndefinedVariableException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not a valid index in the semaphore table");
		}
		
		Pair<Integer, ArrayList<Integer>> semaphoreValue = semaphoreTable.getValue(semaphoreIndexAsInteger);
		Integer totalPermitCount = semaphoreValue.getKey();
		ArrayList<Integer> currentThreadsWithPermit = semaphoreValue.getValue();
		if (currentThreadsWithPermit.size() < totalPermitCount) {
			if (currentThreadsWithPermit.contains(crtState.getThreadID()) == true) {
				throw new LockAlreadyAcquiredException("AcquirePermitStatement: Thread " + crtState.getThreadID() + " already has a permit from semaphore " + this.indexVariableName);
			}
			currentThreadsWithPermit.add(crtState.getThreadID());
			semaphoreTable.update(semaphoreIndexAsInteger, new Pair<Integer, ArrayList<Integer>>(totalPermitCount, currentThreadsWithPermit));
		}
		else {
			stack.push(this);
		}
		
		return null;
	}

	@Override
	public String toString() {
		String representation = "";
		representation += ("acquire(" + this.indexVariableName + ");\n");
		return representation;
	}
	
	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
		}
		if (initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("AcquirePermitStatement: Variable " + this.indexVariableName + " is not an integer");
		}
		return initialTypeEnvironment;
	}

}

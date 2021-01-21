package model.statement;

import java.util.ArrayList;
import exception.InvalidTypeException;
import exception.LockNotAcquiredException;
import exception.UndefinedVariableException;
import javafx.util.Pair;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class ReleasePermitStatement implements StatementInterface {
	private final String indexVariableName;
	
	public ReleasePermitStatement(String indexVariableName) {
		this.indexVariableName = indexVariableName;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = crtState.getSemaphoreTable();
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("ReleasePermitStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
		}
		
		int semaphoreIndexAsInteger = ((IntValue)(symbolTable.getValue(this.indexVariableName))).getValue();
		if (semaphoreTable.isDefined(semaphoreIndexAsInteger) == false) {
			throw new UndefinedVariableException("ReleasePermitStatement: Variable " + this.indexVariableName + " is not a valid index in the semaphore table");
		}
		
		Pair<Integer, ArrayList<Integer>> semaphoreValue = semaphoreTable.getValue(semaphoreIndexAsInteger);
		Integer totalPermitCount = semaphoreValue.getKey();
		ArrayList<Integer> currentThreadsWithPermit = semaphoreValue.getValue();
		
		if (currentThreadsWithPermit.contains(crtState.getThreadID()) == false) {
			throw new LockNotAcquiredException("ReleasePermitStatement: Thread " + crtState.getThreadID() + " doesn't have a permit from semaphore " + this.indexVariableName);
		}
		// I need Integer.valueOf bc otherwise the threadID would've been considered an index in the list, and not a value
		// this way, we "enforce" it to be a value
		currentThreadsWithPermit.remove(Integer.valueOf(crtState.getThreadID()));
		semaphoreTable.update(semaphoreIndexAsInteger, new Pair<Integer, ArrayList<Integer>>(totalPermitCount, currentThreadsWithPermit));
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("release(" + this.indexVariableName + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("ReleasePermitStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
		}
		if (initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("ReleasePermitStatement: Variable " + this.indexVariableName + " is not an integer");
		}
		return initialTypeEnvironment;
	}
}

package model.statement;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class LockStatement implements StatementInterface {
	private final String indexVariableName;
	private static Lock lock = new ReentrantLock(); 
	
	public LockStatement(String indexVariableName) {
		this.indexVariableName = indexVariableName;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, Integer> lockTable = crtState.getLockTable();
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("LockStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
		}
		
		int lockIndexAsInteger = ((IntValue)(symbolTable.getValue(this.indexVariableName))).getValue();
		if (lockTable.isDefined(lockIndexAsInteger) == false) {
			throw new UndefinedVariableException("LockStatement: Variable " + this.indexVariableName + " is not a valid index in the lock table");
		}
		
		lock.lock();
		Integer lockThread = lockTable.getValue(lockIndexAsInteger);
		if (lockThread == -1) {
			lockTable.update(lockIndexAsInteger, crtState.getThreadID());
		}
		else {
			stack.push(this);
		}
		lock.unlock();
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("lock(" + this.indexVariableName + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("LockStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
		}
		if (initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("LockStatement: Variable " + this.indexVariableName + " is not an integer");
		}
		return initialTypeEnvironment;
	}	
}

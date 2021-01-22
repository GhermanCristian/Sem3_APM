package model.statement;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyLockTable;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class CreateLockStatement implements StatementInterface {
	private final String indexVariableName;
	private static Lock lock = new ReentrantLock(); 
	
	public CreateLockStatement(String indexVariableName) {
		this.indexVariableName = indexVariableName;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, Integer> lockTable = crtState.getLockTable();
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("CreateLockStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
		}
		
		lock.lock();
		int newPositionInLockTable = ((MyLockTable<Integer, Integer>)(lockTable)).getFirstAvailablePosition();
		lockTable.insert(newPositionInLockTable, -1);
		symbolTable.update(this.indexVariableName, new IntValue(newPositionInLockTable));
		lock.unlock();
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("createLock(" + this.indexVariableName + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("CreateLockStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
		}
		if (initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("CreateLockStatement: Variable " + this.indexVariableName + " is not an integer");
		}
		return initialTypeEnvironment;
	}
}

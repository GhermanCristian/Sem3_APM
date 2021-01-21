package model.statement;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;

public class CountDownLatchStatement implements StatementInterface {
	private final String indexVariableName;
	
	public CountDownLatchStatement(String indexVariableName) {
		this.indexVariableName = indexVariableName;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, Integer> latchTable = crtState.getLatchTable();
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("CountDownLatchStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
		}
		int latchIndexAsInteger = ((IntValue)(symbolTable.getValue(this.indexVariableName))).getValue();
		if (latchTable.isDefined(latchIndexAsInteger) == false) {
			throw new UndefinedVariableException("CountDownLatchStatement: Variable " + this.indexVariableName + " is not a valid index in the latch table");
		}
		
		Integer latchValue = latchTable.getValue(latchIndexAsInteger);
		if (latchValue > 0) {
			latchTable.update(latchIndexAsInteger, latchValue - 1);
		}
		crtState.getOutput().addLast(new StringValue("(latch) " + Integer.toString(crtState.getThreadID())));
		
		return null;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("CountDownLatchStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
		}
		if (initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("CountDownLatchStatement: Variable " + this.indexVariableName + " is not an integer");
		}
		return initialTypeEnvironment;
	}
}

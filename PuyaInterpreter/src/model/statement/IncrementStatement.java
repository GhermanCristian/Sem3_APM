package model.statement;

import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class IncrementStatement implements StatementInterface {
	private final int DEFAULT_INCREMENT_VALUE = 1;
	private final String variableName;
	private final String operator;
	private final int incrementValue;
	
	public IncrementStatement(String variableName, String operator) {
		this.variableName = variableName;
		this.operator = operator;
		this.incrementValue = this.DEFAULT_INCREMENT_VALUE;
	}
	
	public IncrementStatement(String variableName, String operator, int incrementValue) {
		this.variableName = variableName;
		this.operator = operator;
		this.incrementValue = incrementValue;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		if (symbolTable.isDefined(this.variableName) == false) {
			throw new UndefinedVariableException("IncrementStatement: variable " + this.variableName + " is undefined");
		}
		
		int previousValueAsInteger = ((IntValue)symbolTable.getValue(this.variableName)).getValue();
		if (this.operator == "+") {
			symbolTable.update(this.variableName, new IntValue(previousValueAsInteger + this.incrementValue));
		}
		else if(this.operator == "-") {
			symbolTable.update(this.variableName, new IntValue(previousValueAsInteger - this.incrementValue));
		}
		else {
			throw new InvalidOperatorException("IncrementStatement: operator should be either + or -, not " + this.operator);
		}
		
		return null;
	}
	
	public String toString() {
		String representation = "";
		representation += this.variableName;
		
		if (this.incrementValue == this.DEFAULT_INCREMENT_VALUE) {
			representation += (this.operator + this.operator + ";\n"); // beware that wrong operators will still be displayed here!
		}
		else {
			representation += (this.operator + "= " + Integer.toString(this.incrementValue) + ";\n");
		}

		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.variableName) == false) {
			throw new UndefinedVariableException("IncrementStatement: variable " + this.variableName + " is undefined");
		}
		if (initialTypeEnvironment.getValue(this.variableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("IncrementStatement: variable " + this.variableName + " should be an integer");
		}
		return initialTypeEnvironment;
	}

}

package model.expression;

import exception.UndefinedVariableException;
import model.ADT.DictionaryInterface;
import model.type.TypeInterface;
import model.value.ValueInterface;

public class VariableExpression implements ExpressionInterface{
	private final String variableName;
	
	public VariableExpression(String variableName) {
		this.variableName = variableName;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {
		if (symbolTable.isDefined(this.variableName) == false) {
			throw new UndefinedVariableException("VariableExpression: Variable " + this.variableName + " is not defined");
		}
		return symbolTable.getValue(this.variableName);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += (this.variableName);
		return representation;
	}

	@Override
	public TypeInterface typeCheck(DictionaryInterface<String, TypeInterface> typeEnvironment) throws Exception {
		return typeEnvironment.getValue(this.variableName);
	}
}

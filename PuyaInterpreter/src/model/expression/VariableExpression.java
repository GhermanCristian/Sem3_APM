package model.expression;

import model.ADT.DictionaryInterface;
import model.value.ValueInterface;

public class VariableExpression implements ExpressionInterface{
	private final String variableName;
	
	public VariableExpression(String variableName) {
		this.variableName = variableName;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable) throws Exception {
		return symbolTable.getValue(this.variableName);
	}
}

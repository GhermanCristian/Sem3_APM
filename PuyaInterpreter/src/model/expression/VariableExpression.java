package model.expression;

import model.ADT.MyDictionary;
import model.value.ValueInterface;

public class VariableExpression implements ExpressionInterface{
	private final String variableName;
	
	public VariableExpression(String variableName) {
		this.variableName = variableName;
	}

	@Override
	public ValueInterface evaluate(MyDictionary<String, ValueInterface> symbolTable) throws Exception {
		return symbolTable.getValue(this.variableName);
	}
}

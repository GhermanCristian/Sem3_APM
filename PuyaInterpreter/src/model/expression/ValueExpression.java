package model.expression;

import model.ADT.DictionaryInterface;
import model.value.ValueInterface;

public class ValueExpression implements ExpressionInterface {
	private final ValueInterface value;
	
	public ValueExpression(ValueInterface value) {
		this.value = value;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable) throws Exception {
		return this.value;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += (this.value.toString());
		return representation;
	}
}

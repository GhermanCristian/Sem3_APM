package model.expression;

import model.ADT.MyDictionary;
import model.value.ValueInterface;

public class ValueExpression implements ExpressionInterface {
	private final ValueInterface value;
	
	public ValueExpression(ValueInterface value) {
		this.value = value;
	}

	@Override
	public ValueInterface evaluate(MyDictionary<String, ValueInterface> symbolTable) throws Exception {
		return this.value;
	}
}

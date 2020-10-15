package model.expression;

import model.ADT.MyDictionary;
import model.value.ValueInterface;

public interface ExpressionInterface {
	ValueInterface evaluate(MyDictionary<String, ValueInterface> symbolTable)  throws Exception;
}

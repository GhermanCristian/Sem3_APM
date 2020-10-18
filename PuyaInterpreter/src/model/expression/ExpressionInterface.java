package model.expression;

import model.ADT.DictionaryInterface;
import model.value.ValueInterface;

public interface ExpressionInterface {
	ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable)  throws Exception;
}

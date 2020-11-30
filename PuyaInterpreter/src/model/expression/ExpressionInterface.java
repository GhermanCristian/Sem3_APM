package model.expression;

import model.ADT.DictionaryInterface;
import model.type.TypeInterface;
import model.value.ValueInterface;

public interface ExpressionInterface {
	public ValueInterface evaluate(
			DictionaryInterface<String, ValueInterface> symbolTable, 
			DictionaryInterface<Integer, ValueInterface> heap
			) throws Exception;
	public TypeInterface typeCheck(DictionaryInterface<String, TypeInterface> typeEnvironment) throws Exception;
	public String toString();
}

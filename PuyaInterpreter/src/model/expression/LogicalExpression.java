package model.expression;

import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import model.ADT.DictionaryInterface;
import model.type.BoolType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.ValueInterface;

public class LogicalExpression implements ExpressionInterface{
	private final ExpressionInterface firstExp;
	private final ExpressionInterface secondExp;
	private final String operator;
	
	public LogicalExpression(ExpressionInterface firstExp, ExpressionInterface secondExp, String operator){
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operator = operator;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {
		ValueInterface firstVal, secondVal;
		
		firstVal = this.firstExp.evaluate(symbolTable, heap);
		secondVal = this.secondExp.evaluate(symbolTable, heap);
		boolean firstBoolean = ((BoolValue)firstVal).getValue();
		boolean secondBoolean = ((BoolValue)secondVal).getValue();
		
		if (this.operator == "&&") {
			return new BoolValue(firstBoolean && secondBoolean);
		}
		if (this.operator == "||") {
			return new BoolValue(firstBoolean || secondBoolean);
		}
		else {
			throw new InvalidOperatorException("LogicalExpression: Invalid operator");
		}
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += (this.firstExp.toString());
		representation += (" " + this.operator + " ");
		representation += (this.secondExp.toString());
		return representation;
	}

	@Override
	public TypeInterface typeCheck(DictionaryInterface<String, TypeInterface> typeEnvironment) throws Exception {
		TypeInterface firstType, secondType, boolType;
		firstType = this.firstExp.typeCheck(typeEnvironment);
		secondType = this.secondExp.typeCheck(typeEnvironment);
		boolType = new BoolType();
		
		if (firstType.equals(boolType) == false) {
			throw new InvalidTypeException("LogicalExpression: First expression is not a boolean");
		}
		if (secondType.equals(boolType) == false) {
			throw new InvalidTypeException("LogicalExpression: Second expression is not a boolean");
		}
		
		return boolType;
	}
}
